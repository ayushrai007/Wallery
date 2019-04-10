package com.rai.ayush.wallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rai.ayush.wallery.R;
import com.rai.ayush.wallery.models.Wallpaper;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    private List<Wallpaper> wallpaperList;
    LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, List<Wallpaper> wallpaperList) {
        this.activity = activity;
        this.wallpaperList = wallpaperList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater=(LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.viewitem,container,false);
        ImageView imageView;
        imageView=view.findViewById(R.id.preim);
        DisplayMetrics dis=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height=dis.heightPixels;
        int wiidth=dis.widthPixels;
        imageView.setMinimumHeight(height);
        imageView.setMinimumWidth(wiidth);

        try {
            Glide.with(activity.getApplicationContext())
                    .load(wallpaperList.get(position).url)
                    .into(imageView);
        }
        catch (Exception ex){

        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return wallpaperList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
