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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ViewAlbum extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<PhotoGridRecyclerData> recyclerDataArrayList;
    TextView textView;
    Button btnAddPhoto;
    Button btnDeleteAlbum;
    Album album;
    FileManager fileManager;

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

        fileManager = new FileManager(getApplicationContext(), "userdata.json");


        recyclerView=findViewById(R.id.idCourseRVPhoto);
        textView = findViewById(R.id.photoAlbumName);
        btnAddPhoto = findViewById(R.id.buttonAddPhoto);
        btnDeleteAlbum = findViewById(R.id.buttonDeleteAlbum);

        btnAddPhoto.setOnClickListener(this);
        btnDeleteAlbum.setOnClickListener(this);



        textView.setText(album.getName());

        updatePhotos();

    }

    @Override
    public void onClick(View view)
    {
        if(view.equals(btnDeleteAlbum)){
            if(verifyDelete()){
                deleteAlbum();
            }
        }
        else if(view.equals(btnAddPhoto)){
            addPhoto();
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
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        newPhotoBitmap(selectedImageBitmap);
                    }
                }
            });

    private void newPhotoBitmap(Bitmap selectedImageBitmap) {
        ArrayList<Bitmap> photosBitmap = new ArrayList<>();

        UserData userData = fileManager.readData();

        for(Album al : userData.getAlbums()){
            if(al.getName().equals(album.getName())){
                if(al.getPictures() != null){
                    photosBitmap = al.getPictures();
                }
                else{
                    photosBitmap = new ArrayList<>();
                }

                photosBitmap.add(selectedImageBitmap);

                al.setPictures(photosBitmap);

                fileManager.writeData(userData);

                break;
            }
        }

        updatePhotos();
    }


    private void deleteAlbum() {
    }

    private boolean verifyDelete() {

        return false;
    }


    public void updatePhotos(){
        // created new array list..
        recyclerDataArrayList=new ArrayList<>();

        // added data to array list

        UserData userData;
        userData = fileManager.readData();

        ArrayList<Bitmap> photos = new ArrayList<>();
        Optional<Album> al = userData.getAlbums()
                         .stream()
                         .filter(a -> a.getName().equals(album.getName()))
                         .findFirst();
        if(al.isPresent()){
            photos = al.get().getPictures();
        }

        if(photos != null){
            for(int i = 0; i < photos.size(); i++){
                recyclerDataArrayList.add(new PhotoGridRecyclerData(photos.get(i)));
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

        adapter.setOnClickListener((PhotoGridRecycleViewAdapter.OnClickListener) (position) -> {
            Toast.makeText(this,"clicked", Toast.LENGTH_LONG).show();
        });

    }

    /*
    private void getImagePath() {
        // in this method we are adding all our image paths
        // in our arraylist which we have created.
        // on below line we are checking if the device is having an sd card or not.
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {

            // if the sd card is present we are creating a new list in
            // which we are getting our images data with their ids.
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

            // on below line we are creating a new
            // string to order our images by string.
            final String orderBy = MediaStore.Images.Media._ID;

            // this method will stores all the images
            // from the gallery in Cursor
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            // below line is to get total number of images
            int count = cursor.getCount();

            // on below line we are running a loop to add
            // the image file path in our array list.
            for (int i = 0; i < count; i++) {

                // on below line we are moving our cursor position
                cursor.moveToPosition(i);

                // on below line we are getting image file path
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                // after that we are getting the image file path
                // and adding that path in our array list.
                imagePaths.add(cursor.getString(dataColumnIndex));
            }
            //imageRVAdapter.notifyDataSetChanged();
            // after adding the data to our
            // array list we are closing our cursor.
            cursor.close();
        }
    }

     */
}