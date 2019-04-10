package com.rai.ayush.wallery.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rai.ayush.wallery.BuildConfig;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.activities.Preview;
import com.rai.ayush.wallery.activities.WallpapersActivity;
import com.rai.ayush.wallery.models.Category;
import com.rai.ayush.wallery.models.Wallpaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder> {

    private Context mCtx;
    private List<Wallpaper> wallpaperList;


    public WallpapersAdapter(Context mCtx, List<Wallpaper> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;

    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpapers,parent,false);

        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WallpaperViewHolder holder, int position) {
    Wallpaper w= wallpaperList.get(getItemCount()-1-position);
   // holder.textView.setText(w.title);
        Glide.with(mCtx)

                .load(w.url)
               .dontAnimate().placeholder(R.drawable.plll).dontTransform().into(holder.imageView);
                //.into(holder.imageView);


    }



    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        ImageView imageView;
       // TextView textView;

    public WallpaperViewHolder(View itemView) {
        super(itemView);

       // textView=itemView.findViewById(R.id.textview_title);
        imageView=itemView.findViewById(R.id.imageview);

        imageView.setOnClickListener(this);

    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                    case R.id.imageview:
                        Intent intent=new Intent(mCtx, Preview.class);
                        intent.putExtra("wallpaper",wallpaperList.get(getItemCount()-1-getAdapterPosition()));

                        mCtx.startActivity(intent);

                        break;

            }
        }


    }
}

