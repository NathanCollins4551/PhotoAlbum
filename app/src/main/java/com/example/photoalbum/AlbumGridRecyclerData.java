package com.example.photoalbum;

public class AlbumGridRecyclerData {
    private String title;
    private int imgID;
    private Album album;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public Album getAlbum(){return album;}

    public void setAlbum(Album al){this.album = al;}

    public AlbumGridRecyclerData(String title, int imgID, Album al) {
        setTitle(title);
        setImgID(imgID);
        setAlbum(al);
    }
}
