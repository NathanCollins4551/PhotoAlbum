package com.example.photoalbum;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    ObjectMapper objectMapper;
    Gson gson;
    StringBuilder stringBuilder;
    String FileName;
    Context ApplicationContext;

    FileManager(Context c, String fileName){
        objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        gson = new Gson();
        stringBuilder = new StringBuilder();
        FileName = fileName;
        ApplicationContext = c;
    }

    public UserData readData(){
        File file = new File(ApplicationContext.getFilesDir(), FileName);
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            writeData(new UserData());
            return readData();
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = null;

        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (line != null) {
            stringBuilder.append(line).append("\n");
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String response = stringBuilder.toString();

        try {
            JSONObject jsonObjectRead = new JSONObject(response);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        UserData data;
        try {
            data = objectMapper.readValue(response, UserData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public void writeData(UserData data){

        String jsonString = null;
        jsonString = gson.toJson(data);

        jsonString = jsonString.toLowerCase();

        File file = new File(ApplicationContext.getFilesDir(), FileName);

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            bufferedWriter.flush();
            bufferedWriter.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
