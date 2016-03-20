package com.example.android.imifai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

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

        Intent intent  = new Intent(this, Camera.class);

        startActivity(intent);
    }

    private void startGallery(){
        ArrayList<ImageRecord> records = Database.getInstance().getAllImages();
        Intent intent = new Intent(this, GalleryActivity.class);
        ImageRecord[] arr=new ImageRecord[records.size()];
        int i=0;
        for(ImageRecord rec: records){
            arr[i]=rec;
            i++;
        }
        intent.putExtra("images", new ImageRecordsWrapper(arr));
        startActivity(intent);
    }



};




