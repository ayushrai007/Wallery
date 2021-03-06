package com.rai.ayush.wallery.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.adapters.WallpapersAdapter;
import com.rai.ayush.wallery.models.Wallpaper;

import java.util.ArrayList;
import java.util.List;

public class WallpapersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    WallpapersAdapter adapter;
    List<Wallpaper> wallpaperList;
    List<Wallpaper> favList;
    DatabaseReference dbWallpapers,dbFavs;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers);

        Intent intent=getIntent();
        final String category=intent.getStringExtra("category");

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);

        favList=new ArrayList<>();
        wallpaperList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,6);
        adapter=new WallpapersAdapter(this,wallpaperList);

        recyclerView.setAdapter(adapter);
        progressBar=findViewById(R.id.progressbar);


        dbWallpapers= FirebaseDatabase.getInstance().getReference("images")
                .child(category);


        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            dbFavs=FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
                    .child(category);
            fetchFavWallpapers(category);
        }
        else {
            fetchWallpapers(category);
        }
    }
    private void fetchFavWallpapers(final String category){
        progressBar.setVisibility(View.VISIBLE);
        dbFavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists()){
                    for(DataSnapshot wallpaperSnapshot: dataSnapshot.getChildren()){
                        String id=wallpaperSnapshot.getKey();
                        String title=wallpaperSnapshot.child("title").getValue(String.class);
                        String desc=wallpaperSnapshot.child("desc").getValue(String.class);
                        String url=wallpaperSnapshot.child("url").getValue(String.class);
                        Wallpaper w = new Wallpaper(id,title,desc,url,category);
                        favList.add(w);
                    }
                }
                fetchWallpapers(category);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void fetchWallpapers(final String category){
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists()){
                    for(DataSnapshot wallpaperSnapshot: dataSnapshot.getChildren()){
                        String id=wallpaperSnapshot.getKey();
                        String title=wallpaperSnapshot.child("title").getValue(String.class);
                        String desc=wallpaperSnapshot.child("desc").getValue(String.class);
                        String url=wallpaperSnapshot.child("url").getValue(String.class);
                        Wallpaper w = new Wallpaper(id,title,desc,url,category);
                        if(isFavourite(w)){
                            w.isFavourite=1;
                        }

                        wallpaperList.add(w);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean isFavourite(Wallpaper w){
        for(Wallpaper f:favList){
            if(f.id.equals(w.id))
                return true;
        }
        return false;
    }
}
