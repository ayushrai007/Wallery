package com.rai.ayush.wallery.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Wallpaper implements Parcelable{
    @Exclude
    public String id;
    public String title,desc,url;
    @Exclude
    public  String category;
    @Exclude
    public int isFavourite=0;
    public static final Parcelable.Creator creator=new Parcelable.Creator(){
        public Wallpaper createFromParcel(Parcel in){
            return new Wallpaper(in);
        }
        public Wallpaper[] newArray(int size){
            return new Wallpaper[size];
        }

    };

    public Wallpaper(String id, String title, String desc, String url,String category) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.category=category;
    }
    public Wallpaper(Parcel in){
        this.id=in.readString();
        this.title=in.readString();
        this.desc=in.readString();
        this.url=in.readString();
        this.category=in.readString();
        this.isFavourite=in.readInt();
    }

    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel in) {
            return new Wallpaper(in);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.url);
        dest.writeString(this.category);
        dest.writeInt(this.isFavourite);
    }


}
