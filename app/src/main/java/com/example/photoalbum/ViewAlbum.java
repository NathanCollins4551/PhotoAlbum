package com.example.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ViewAlbum extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<PhotoGridRecyclerData> recyclerDataArrayList;
    TextView textView;
    Button btnAddPhoto;
    Button btnDeleteAlbum;
    Button btnBack;
    Album album;
    //FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_album);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        album = (Album) getIntent().getSerializableExtra("album");

        //fileManager = new FileManager(getApplicationContext(), "userdata.json");


        recyclerView=findViewById(R.id.idCourseRVPhoto);
        textView = findViewById(R.id.photoAlbumName);
        btnAddPhoto = findViewById(R.id.buttonAddPhoto);
        btnDeleteAlbum = findViewById(R.id.buttonDeleteAlbum);
        btnBack = findViewById(R.id.buttonBack);

        btnAddPhoto.setOnClickListener(this);
        btnDeleteAlbum.setOnClickListener(this);
        btnBack.setOnClickListener(this);



        textView.setText(album.getName());

        updatePhotos();

    }
    @Override
    public void onResume(){
        super.onResume();

        updatePhotos();
    }

    @Override
    public void onClick(View view)
    {
        if(view.equals(btnDeleteAlbum)){
            updatePhotos();
            verifyDeleteAlertDialog();
        }
        else if(view.equals(btnAddPhoto)){
            addPhoto();
        }
        else if(view.equals(btnBack)){
            finish();
        }

    }

    private void addPhoto() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                        }
                        newPhotoUri(selectedImageUri);
                    }
                }
            });

    private void newPhotoUri(Uri selectedImageUri) {
        ArrayList<String> photosUri;
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");

        UserData userData = fileManager.readData();

        for(Album al : userData.getAlbums()){
            if(al.getName().equalsIgnoreCase(album.getName())){
                if(al.getPictures() != null){
                    photosUri = al.getPictures();
                }
                else{
                    photosUri = new ArrayList<>();
                }

                photosUri.add(selectedImageUri.toString());

                al.setPictures(photosUri);
                fileManager.writeData(userData);
                break;
            }
        }
        updatePhotos();
    }


    private void deleteAlbum() {
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");

        UserData userData = fileManager.readData();

        for(int i = 0; i < userData.getAlbums().size();i++){
            if(userData.getAlbums().get(i).getName().equalsIgnoreCase(album.getName())){
                userData.getAlbums().remove(i);
                fileManager.writeData(userData);
                break;
            }
        }

        finish();
    }

    public void verifyDeleteAlertDialog() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Do you want to delete this album?");
        builder.setCancelable(false);

        // add a button
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
           deleteAlbum();
        });

        builder.setNegativeButton("No",(dialog, which) -> {
            dialog.cancel();
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void updatePhotos(){
        // created new array list..
        recyclerDataArrayList=new ArrayList<>();
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");


        // added data to array list

        UserData userData;
        userData = fileManager.readData();

        ArrayList<String> photos = new ArrayList<>();
        Optional<Album> al = userData.getAlbums()
                         .stream()
                         .filter(a -> a.getName().equalsIgnoreCase(album.getName()))
                         .findFirst();
        if(al.isPresent()){
            photos = al.get().getPictures();
        }

        if(photos != null){
            for(int i = 0; i < photos.size(); i++){
                recyclerDataArrayList.add(new PhotoGridRecyclerData(Uri.parse(photos.get(i))));
            }
        }

        // added data from arraylist to adapter class.
        PhotoGridRecycleViewAdapter adapter = new PhotoGridRecycleViewAdapter(recyclerDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((PhotoGridRecycleViewAdapter.OnClickListener) (position, uri) -> {
            loadPhoto(uri);
        });

    }

    private void loadPhoto(Uri uri) {
        Intent intent = new Intent(this, ViewPhoto.class);

        String uriString = uri.toString();
        intent.putExtra("photo",uriString);
        intent.putExtra("album",album);

        startActivity(intent);
    }

}