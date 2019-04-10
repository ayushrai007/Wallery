package com.rai.ayush.wallery.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.adapters.CategoriesAdapter;
import com.rai.ayush.wallery.models.Category;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ProgressBar progressBar;
    private List<Category> categorieslist;
    private DatabaseReference dbCategories;
    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        categorieslist=new ArrayList<>();
        adapter=new CategoriesAdapter(getActivity(),categorieslist);
        recyclerView.setAdapter(adapter);

        dbCategories= FirebaseDatabase.getInstance().getReference("categories");
        dbCategories.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey();
                        String desc = ds.child("desc").getValue(String.class);
                        String thumb = ds.child("thumbnail").getValue(String.class);

                        Category category = new Category(name, desc, thumb);
                        categorieslist.add(category);
                    }
                    adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
