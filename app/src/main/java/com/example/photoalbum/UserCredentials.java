package com.example.photoalbum;

import java.util.List;

public class UserCredentials {

    //Credentials fields
    String Facebookusername;
    String Facebookpassword;
    String Instagramusername;
    String Instagrampassword;


    UserCredentials(){
        setInstagramusername(null);
        setInstagrampassword(null);
        setFacebookusername(null);
        setFacebookpassword(null);
    }


    //TODO add other credential fields

    //Getters and setters
    //Facebook
    public void setFacebookusername(String username){
        this.Facebookusername = username;
    }
    public String getFacebookUsername(){
        return Facebookusername;
    }
    public void setFacebookpassword(String password){
        this.Facebookpassword = password;
    }
    public String getFacebookPassword(){
        return Facebookpassword;
    }
    //Instagram
    public void setInstagramusername(String username){
        this.Instagramusername = username;
    }
    public String getInstagramUsername(){
        return Instagramusername;
    }
    public void setInstagrampassword(String password){
        this.Instagrampassword = password;
    }
    public String getInstagrampassword(){
        return Instagrampassword;
    }
    //etc.
    //TODO add other credential fields getters and setters


}
