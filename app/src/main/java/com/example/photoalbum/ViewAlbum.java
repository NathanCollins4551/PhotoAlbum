package com.example.photoalbum;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
    Button deleteButton;
    Button selectButton;
    private ArrayList<PhotoGridRecyclerData> recyclerDataArrayList;
    TextView textView;
    Button btnBack;
    Album album;
    final String ADD_PHOTO = "Add Photo";
    final String CANCEL = "Cancel";
    final String SELECT = "Select";
    ArrayList<Uri> selectPhotos;

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
        btnBack = findViewById(R.id.buttonBack);
        selectButton = findViewById(R.id.buttonSelectPhoto);
        deleteButton = findViewById(R.id.buttonDeleteSelectedPhotos);

        btnBack.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);



        textView.setText(album.getName());

        updatePhotos(false);

    }
    @Override
    public void onResume(){
        super.onResume();

        updatePhotos(false);
    }

    @Override
    public void onClick(View view)
    {

        if(view.equals(btnBack)){
            finish();
        }
        else if(view.equals(selectButton)){
            if(!selectButton.getText().equals(CANCEL)){
                selectButton.setText(CANCEL);
                updatePhotos(true);
                deleteButton.setVisibility(View.VISIBLE);
            }
            else{
                selectButton.setText(SELECT);
                updatePhotos(false);
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }
        else if(view.equals(deleteButton)){
            verifyDeleteAlertDialog();
        }

    }

    public void verifyDeleteAlertDialog() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Do you want to remove the selected photos from this album?");
        builder.setCancelable(false);

        // add a button
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            deletePhotos();
        });

        builder.setNegativeButton("No",(dialog, which) -> {
            dialog.cancel();
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePhotos() {
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");

        UserData userData = fileManager.readData();

        for(int i = 0; i < userData.getAlbums().size();i++){
            if(userData.getAlbums().get(i).getName().equalsIgnoreCase(album.getName())){

                while(!selectPhotos.isEmpty()){

                    for(int j = 0; j < userData.getAlbums().get(i).getPictures().size(); j++){
                        if (userData.getAlbums().get(i).getPictures().get(j).equalsIgnoreCase(selectPhotos.get(0).toString())) {
                            userData.getAlbums().get(i).getPictures().remove(j);
                            selectPhotos.remove(0);
                            break;
                        }
                    }
                }
                break;
            }

        }

        fileManager.writeData(userData);

        updatePhotos(false);

        selectButton.setText(SELECT);
        deleteButton.setVisibility(View.INVISIBLE);
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
        String stringUri = selectedImageUri.toString();
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

                for(String x : photosUri){
                    if(x.equalsIgnoreCase(stringUri)){
                        Toast.makeText(this,"Photo is already in album!",Toast.LENGTH_LONG).show();
                       return;
                    }
                }
                photosUri.add(stringUri);

                al.setPictures(photosUri);
                fileManager.writeData(userData);
                break;
            }
        }
        updatePhotos(false);
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }


    public void updatePhotos(boolean selected){
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

        Uri defaultPhotoUri = getUriToDrawable(getApplicationContext(),R.drawable.new_image);

        if(!selected){
            Album tmp = new Album();
            tmp.setName(ADD_PHOTO);
            recyclerDataArrayList.add(new PhotoGridRecyclerData(defaultPhotoUri));
        }

        // added data from arraylist to adapter class.
        PhotoGridRecycleViewAdapter adapter = new PhotoGridRecycleViewAdapter(recyclerDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((PhotoGridRecycleViewAdapter.OnClickListener) (view, position, uri) -> {
            if(selected){
                if(selectPhotos == null){
                    selectPhotos = new ArrayList<>();
                }
                if(!selectPhotos.contains(uri)){
                    selectPhotos.add(uri);
                    Drawable foreground = AppCompatResources.getDrawable(getApplicationContext(),R.drawable.border_red);
                    view.setForeground(foreground);
                }
                else{
                    selectPhotos.remove(uri);
                    Drawable foreground = AppCompatResources.getDrawable(getApplicationContext(),R.drawable.background_border);
                    view.setForeground(foreground);
                }
            }
            else if(uri.equals(defaultPhotoUri)){
                addPhoto();
            }
            else{
                loadPhoto(uri);
            }
        });

    }

    private void loadPhoto(Uri uri) {
        Intent intent = new Intent(this, SharePhotoToFacebook.class);

        String uriString = uri.toString();
        intent.putExtra("photo",uriString);

        startActivity(intent);
    }

}