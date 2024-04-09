package com.example.photoalbum;

import android.net.Credentials;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    ArrayList<Album> Albums;
    UserCredentials Credentials;

    UserData(){
        Albums = new ArrayList<Album>();
        Credentials = new UserCredentials();
    }

    public ArrayList<Album> getAlbums(){
        return Albums;
    }
    public void setAlbums(ArrayList<Album> a){
        this.Albums = a;
    }
    public UserCredentials getCredentials(){
        return Credentials;
    }
    public void setCredentials(UserCredentials userCredentials){
        this.Credentials = userCredentials;
    }
}
