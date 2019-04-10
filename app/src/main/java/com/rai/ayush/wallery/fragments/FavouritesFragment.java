package com.rai.ayush.wallery.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class FavouritesFragment extends Fragment {
    List<Wallpaper> favWalls;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    WallpapersAdapter adapter;
    DatabaseReference dbfavs;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


      /*  if(){
             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_area,new SettingsFragment()).commit();

            return;
        }*/
        if(FirebaseAuth.getInstance().getCurrentUser()==null )
            return;

        favWalls=new ArrayList<>();
        final ImageView imageView=view.findViewById(R.id.nomedia);
        final TextView textView=view.findViewById(R.id.notxt);

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));



        progressBar=view.findViewById(R.id.progressbar);

        adapter=new WallpapersAdapter(getActivity(),favWalls);

        recyclerView.setAdapter(adapter);

        dbfavs= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");




        progressBar.setVisibility(View.VISIBLE);

        dbfavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                for(DataSnapshot category: dataSnapshot.getChildren()){
                    for(DataSnapshot wallpaperSnapshot:category.getChildren()){
                        String id=wallpaperSnapshot.getKey();
                        String title=wallpaperSnapshot.child("title").getValue(String.class);
                        String desc=wallpaperSnapshot.child("desc").getValue(String.class);
                        String url=wallpaperSnapshot.child("url").getValue(String.class);
                        Wallpaper w = new Wallpaper(id,title,desc,url,category.getKey());
                        w.isFavourite=1;
                        favWalls.add(w);

                    }
                }
                adapter.notifyDataSetChanged();
                if(favWalls.size()==0){
                    Toast.makeText(getActivity(),"No Favourites Selected",Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_area,new SettingsFragment()).commit();

            return inflater.inflate(R.layout.frag_fav_deff,container,false);
        }
        return inflater.inflate(R.layout.fragment_fav,container,false);
    }
}
