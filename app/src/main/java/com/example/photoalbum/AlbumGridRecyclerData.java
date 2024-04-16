package com.example.photoalbum;

import android.graphics.Bitmap;
import android.net.Uri;

public class AlbumGridRecyclerData {
    private String title;
    private Bitmap imgBitmap;
    private Album album;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public Album getAlbum(){return album;}

    public void setAlbum(Album al){this.album = al;}

    public AlbumGridRecyclerData(String title, Bitmap imgBitmap, Album al) {
        setTitle(title);
        setImgBitmap(imgBitmap);
        setAlbum(al);
    }
}
