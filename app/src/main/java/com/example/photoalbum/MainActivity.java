package com.example.photoalbum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AlbumGridRecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.idCourseRV);

        FileManager fileManager = new FileManager(getApplicationContext(), "userdata.json");



/*
        UserData test = new UserData();
        test.getCredentials().setInstagramusername("instaUser");
        test.getAlbums().add(new Album());
        test.getAlbums().get(0).setName("empty");

        fileManager.writeData(test);
 */

        UserData userData = fileManager.readData();

        for(int i = 0; i < userData.Albums.size(); i++){
            Log.i("hi",userData.getAlbums().get(i).getName());
        }

        if(userData.getCredentials().getInstagrampassword() == null){
            Toast.makeText(this, "obj is null", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, userData.getCredentials().getInstagrampassword(), Toast.LENGTH_LONG).show();
        }

        updateAlbumsView(userData);

    }

    public void updateAlbumsView(UserData userData){
        // created new array list..
        recyclerDataArrayList=new ArrayList<>();

        // added data to array list
        for(int i = 0; i < userData.getAlbums().size(); i++){
            recyclerDataArrayList.add(new AlbumGridRecyclerData(userData.getAlbums().get(i).getName(),R.drawable.parrot, userData.getAlbums().get(i)));
        }

        //add new album option

        recyclerDataArrayList.add(new AlbumGridRecyclerData("Create New", R.drawable.parrot, new Album()));

        // added data from arraylist to adapter class.
        AlbumGridRecycleViewAdapter adapter = new AlbumGridRecycleViewAdapter(recyclerDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((AlbumGridRecycleViewAdapter.OnClickListener) (position, al) -> {
            Toast.makeText(this,""+position, Toast.LENGTH_LONG).show();
        });

    }
}