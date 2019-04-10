package com.rai.ayush.wallery.activities;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.rai.ayush.wallery.BuildConfig;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.fragments.FavouritesFragment;
import com.rai.ayush.wallery.fragments.HomeFragment;
import com.rai.ayush.wallery.fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private DrawerLayout drawer;
    BottomNavigationView bottomNavigationView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.draw);
        imageButton=findViewById(R.id.insta);
        NavigationView navigationView=findViewById(R.id.navview);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        displayFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(this);/*new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.follow:

                        insta();
                        break;
                    case R.id.share:
                        share();
                        break;
                    case R.id.rate:

                        break;

                    default:

                        break;
                }
                return true;
            }
        });*/
    imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            insta();
        }
    });
    }

    public void insta(){
        try {
            Uri uri = Uri.parse("https://www.instagram.com/quoty_love");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.setPackage("com.instagram.android");
            startActivity(i);
        }
        catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://www.instagram.com/quoty_love");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        }
    }

    public void share(){
        Intent s=new Intent(Intent.ACTION_SEND);
        s.setType("text/plain");
        s.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        s.putExtra(Intent.EXTRA_SUBJECT,"Wallery");
        s.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID+"\n");
        startActivity(Intent.createChooser(s,"Share App"));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            final AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
            View builderview =(View)getLayoutInflater().inflate(R.layout.aert,null);
            builder.setCancelable(false);
            Button b1=builderview.findViewById(R.id.rateus);
            Button b2=builderview.findViewById(R.id.exit);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=Uri.parse("market:/details?id="+getPackageName());
                    Intent i=new Intent(Intent.ACTION_VIEW,uri);
                    try {
                        startActivity(i);
                    }
                    catch (ActivityNotFoundException e){
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                    }
                }
            });
            //super.onBackPressed();
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setView(builderview);
            AlertDialog dialog=builder.create();

            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bck1);


        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.inst,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.inst_but:
                insta();
                break;
        }
        return true;
    }
*/
    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_area,fragment)
                .commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.follow:
                fragment=new HomeFragment();
                insta();
                break;
            case R.id.nav_pri:
                fragment=new HomeFragment();
                Uri uri = Uri.parse("https://quotylovee.wixsite.com/wallery");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
                break;
            case R.id.share:
                share();
                fragment=new HomeFragment();
                break;
            case R.id.rate:
                Uri urig=Uri.parse("market:/details?id="+getPackageName());
                Intent in=new Intent(Intent.ACTION_VIEW,urig);
                try {
                    startActivity(in);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
                }
                fragment=new HomeFragment();
                break;
            case R.id.nav_home:
                fragment=new HomeFragment();
                break;
            case R.id.nav_fav:
                fragment=new FavouritesFragment();
                break;
            case R.id.nav_set:
                fragment=new SettingsFragment();
                break;

                default:
                    fragment=new HomeFragment();
                    break;
        }
        displayFragment(fragment);
        return true;
    }
}