package com.example.photoalbum;


import android.graphics.Bitmap;
import android.net.Uri;

public class PhotoGridRecyclerData {

    private Bitmap imgBitmap;
    public Bitmap getImgBitmap(){
        return imgBitmap;
    }
    public void setImgBitmap(Bitmap bitmap){
        this.imgBitmap = bitmap;
    }
    public PhotoGridRecyclerData(Bitmap bitmap) {
        setImgBitmap(bitmap);
    }
}