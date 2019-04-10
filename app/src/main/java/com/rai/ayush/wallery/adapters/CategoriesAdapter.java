package com.rai.ayush.wallery.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.activities.WallpapersActivity;
import com.rai.ayush.wallery.models.Category;
import com.rai.ayush.wallery.models.Wallpaper;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private Context mCtx;
    private List<Category> categoriesList;
    static int c=0;
    private InterstitialAd mInterstitialAd;

    public CategoriesAdapter(Context mCtx, List<Category> categoriesList) {
        this.mCtx = mCtx;
        this.categoriesList = categoriesList;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_categories,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
    Category c=categoriesList.get(position);
    holder.textView.setText(c.name);
       Glide.with(mCtx)
                .load(c.thumb).dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.textView.setBackground(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        //ImageView imageView;
    public CategoryViewHolder(View itemView) {
        super(itemView);

        textView=itemView.findViewById(R.id.textview_catname);
       // imageView=itemView.findViewById(R.id.imageview);

        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {


            int  p =getAdapterPosition();
             Category c=categoriesList.get(p);
            Intent intent=new Intent(mCtx, WallpapersActivity.class);
            intent.putExtra("category",c.name);
            mCtx.startActivity(intent);

        }
    }
}

