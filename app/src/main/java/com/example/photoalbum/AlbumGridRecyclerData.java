package com.example.photoalbum;

import android.graphics.Bitmap;
import android.net.Uri;

public class AlbumGridRecyclerData {
    private String title;
    private Uri imgUri;
    private Album album;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public Album getAlbum(){return album;}

    public void setAlbum(Album al){this.album = al;}

    public AlbumGridRecyclerData(String title, Uri imgUri, Album al) {
        setTitle(title);
        setImgUri(imgUri);
        setAlbum(al);
    }
}
