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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.cameraIcon);
       // fab.setImageResource(R.drawable.ic_camera_black_48dp);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_black_48dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startCamera();
            }
        });
    }

    private void startCamera(){
        //here we start new activity
        Intent intent  = new Intent(this, Camera.class);

        startActivity(intent);
    }



};




