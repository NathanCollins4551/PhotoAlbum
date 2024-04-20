package com.example.photoalbum;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewPhoto extends AppCompatActivity {

    ImageView ivPhoto;
    Button btnBack;
    Button btnShare;
    Button btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_photo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String photoUriString = (String) getIntent().getSerializableExtra("photo");
        Album album = (Album) getIntent().getSerializableExtra("album");


        Uri photoUri = Uri.parse(photoUriString);

        ivPhoto = findViewById(R.id.ivPhoto);
        btnBack = findViewById(R.id.buttonBack2);
        btnDelete = findViewById(R.id.buttonDeletePhoto);
        btnShare = findViewById(R.id.buttonSharePhoto);

        ivPhoto.setImageURI(photoUri);

        btnShare.setOnClickListener(l -> {
            Toast.makeText(this,"sharing",Toast.LENGTH_LONG).show();
        });
        btnBack.setOnClickListener(l -> {
            finish();
        });
        btnDelete.setOnClickListener(l -> {
            deletePhoto(photoUriString, album);
            finish();
        });
    }

    private void deletePhoto(String photoUriString, Album album) {
        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");

        UserData userData = fileManager.readData();

        for(int i = 0; i < userData.getAlbums().size();i++){
            if(userData.getAlbums().get(i).getName().equalsIgnoreCase(album.getName())){
                for(int j = 0; j < userData.getAlbums().get(i).getPictures().size(); j++){
                    if (userData.getAlbums().get(i).getPictures().get(j).equalsIgnoreCase(photoUriString)) {
                        userData.getAlbums().get(i).getPictures().remove(j);
                        fileManager.writeData(userData);
                        break;
                    }
                }
                break;
            }
        }
    }
}