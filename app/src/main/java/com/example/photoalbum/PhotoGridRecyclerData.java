package com.example.photoalbum;


import android.graphics.Bitmap;
import android.net.Uri;

public class PhotoGridRecyclerData {

    private Uri imgUri;
    public Uri getImgUri(){
        return imgUri;
    }
    public void setImgUri(Uri uri){
        this.imgUri = uri;
    }
    public PhotoGridRecyclerData(Uri uri) {
        setImgUri(uri);
    }
}