package com.example.android.imifai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton mCameraButton;
    private Button mGalleryButton;
    private final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton =(ImageButton) findViewById(R.id.imageButton);

        mGalleryButton =(Button) findViewById(R.id.galleryButton);

        //anonymous inner class as parameter
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startCamera();
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startGallery();
            }
        });


    }

    private void startCamera(){

        Intent intent  = new Intent(this, Search.class);

        startActivity(intent);
    }

    private void startGallery(){

        Intent intent = new Intent(this, GalleryFragment.class);

        startActivity(intent);
    }



};




