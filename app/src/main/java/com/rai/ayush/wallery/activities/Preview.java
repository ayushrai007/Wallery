package com.rai.ayush.wallery.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rai.ayush.wallery.BuildConfig;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.models.Wallpaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Preview extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    Wallpaper w;
    CheckBox checkBoxfav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ImageView imageView=findViewById(R.id.preim);
        Intent i=getIntent();
        ImageButton buttonShare,buttonDown,buttonSet;





            checkBoxfav=findViewById(R.id.check_fav);
            buttonShare=findViewById(R.id.button_share);
            buttonDown=findViewById(R.id.button_down);
            buttonSet=findViewById(R.id.button_gal);
            checkBoxfav.setOnCheckedChangeListener(this);
            buttonShare.setOnClickListener(this);
            buttonDown.setOnClickListener(this);
            buttonSet.setOnClickListener(this);
        if(i!=null){
             w= (Wallpaper) i.getParcelableExtra("wallpaper");
            Glide.with(this)
                    .load(w.url).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate()
                    .into(imageView);
            if(w.isFavourite==1){
                checkBoxfav.setChecked(true);
            }
            else {
                checkBoxfav.setChecked(false);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_share:
                shareWallpaper(w);
                break;
            case R.id.button_down:
                downloadWallpaper(w);

                break;
            case R.id.button_gal:
                Glide.with(this)
                        .asBitmap()
                        .load(w.url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Intent intent=new Intent(Intent.ACTION_ATTACH_DATA);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setDataAndType(getlocalBitmapUri(resource),"image/*");
                                intent.putExtra("mimeType","image/*");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(Intent.createChooser(intent,"Set As"));



                            }
                        });


                break;
        }
    }
    private void shareWallpaper(Wallpaper w){

        Glide.with(this)
                .asBitmap()
                .load(w.url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM,getlocalBitmapUri(resource));
                        startActivity(Intent.createChooser(intent,"Wallery"));
                    }
                });
    }

    private Uri getlocalBitmapUri(Bitmap b){
        Uri bUri=null;

        try {
            File file=new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Wallery_"+System.currentTimeMillis()+".png");
            FileOutputStream outputStream=new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG,65,outputStream);

            outputStream.close();
            bUri= FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bUri;
    }

    private void downloadWallpaper(final Wallpaper wallpaper){

        Glide.with(this)
                .asBitmap()
                .load(wallpaper.url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                        Intent intent=new Intent(Intent.ACTION_VIEW);

                        Uri uri=saveWallpaperndgetUri(resource,wallpaper.id);
                        if(uri!=null){
                            intent.setDataAndType(uri,"image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity(Intent.createChooser(intent,"Wallery"));
                        }

                    }
                });
        Toast.makeText(this,"Image succesfully saved in folder 'Wallery'",Toast.LENGTH_SHORT).show();
    }

    private Uri saveWallpaperndgetUri(Bitmap bitmap,String id){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat
                    .shouldShowRequestPermissionRationale((Activity) this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
            return null;
        }
        File folder=new File(Environment.getExternalStorageDirectory().toString()+"/Wallery");
        folder.mkdirs();

        File file=new File(folder,id+".png");
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
            outputStream.flush();
            outputStream.close();

            return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Toast.makeText(this,"Please Login first from Settings",Toast.LENGTH_SHORT).show();
            buttonView.setChecked(false);
            return;
        }



        DatabaseReference dbfavs= FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites")
                .child(w.category);

        if(isChecked){
            dbfavs.child(w.id).setValue(w);

        }
        else {
            dbfavs.child(w.id).setValue(null);
            w.isFavourite=0;
        }
    }


}
