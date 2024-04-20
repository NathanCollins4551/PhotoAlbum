package com.example.photoalbum;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AlbumGridRecyclerData> recyclerDataArrayList;
    final String CREATE_NEW = "Create New";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.idCourseRV);

        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");

        if(!checkPermission()){
            requestPermission();
        }

        UserData userData = fileManager.readData();


        try {
            updateAlbums(userData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onResume(){
        super.onResume();

        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");
        UserData userData = fileManager.readData();
        try {
            updateAlbums(userData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    public void updateAlbums(UserData userData) throws FileNotFoundException {
        // created new array list..
        recyclerDataArrayList=new ArrayList<>();


        Uri defaultAlbumUri = getUriToDrawable(getApplicationContext(),android.R.drawable.ic_menu_gallery);

        // added data to array list
        for(int i = 0; i < userData.getAlbums().size(); i++){
            if(userData.getAlbums().get(i).getPictures() != null && !userData.getAlbums().get(i).getPictures().isEmpty()){
                Uri uri = Uri.parse(userData.getAlbums().get(i).getPictures().get(0));
                recyclerDataArrayList.add(new AlbumGridRecyclerData(userData.getAlbums().get(i).getName(),uri, userData.getAlbums().get(i)));
            }
            else{
                recyclerDataArrayList.add(new AlbumGridRecyclerData(userData.getAlbums().get(i).getName(),defaultAlbumUri, userData.getAlbums().get(i)));
            }
        }

        //add new album option

        Album tmp = new Album();
        tmp.setName(CREATE_NEW);
        recyclerDataArrayList.add(new AlbumGridRecyclerData(tmp.getName(), defaultAlbumUri, tmp));

        // added data from arraylist to adapter class.
        AlbumGridRecycleViewAdapter adapter = new AlbumGridRecycleViewAdapter(recyclerDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((AlbumGridRecycleViewAdapter.OnClickListener) (position, al) -> {
            if(Objects.equals(al.getName(), CREATE_NEW)){
                getAlbumNameAlertDialog();
            }
            else{
                loadAlbum(al);
            }
        });

    }
    void loadAlbum(Album album){

        Intent intent = new Intent(this, ViewAlbum.class);

        intent.putExtra("album",album);

        startActivity(intent);
    }
    void createAlbum(String name){
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");
        Album newAlbum = new Album();
        newAlbum.setName(name);

        UserData userData = fileManager.readData();
        userData.getAlbums().add(newAlbum);

        fileManager.writeData(userData);

        loadAlbum(newAlbum);
    }

    public void getAlbumNameAlertDialog() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.get_new_album_name, null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            EditText editText = customLayout.findViewById(R.id.editText);
            createAlbum(editText.getText().toString());
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private boolean checkPermission() {
        // in this method we are checking if the permissions are granted or not and returning the result.

        // int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        //on below line we are requesting the read external storage permissions.
        ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called after permissions has been granted.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // we are checking the permission code.
        if (requestCode == PERMISSION_REQUEST_CODE) {// in this case we are checking if the permissions are accepted or not.
            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!storageAccepted) {
                    Toast.makeText(this, "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}