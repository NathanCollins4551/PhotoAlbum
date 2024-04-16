package com.example.photoalbum;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    String Name;
    ArrayList<Bitmap> Pictures;

    Album(){
        setName(null);
        setPictures(null);
    }

    public String getName(){
        return Name;
    }
    public void setName(String n){
        this.Name = n;
    }
    public ArrayList<Bitmap> getPictures(){
        return Pictures;
    }
    public void setPictures(ArrayList<Bitmap> p){
        this.Pictures = p;
    }
}
