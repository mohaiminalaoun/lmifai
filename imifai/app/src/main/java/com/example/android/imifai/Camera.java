package com.example.android.imifai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Camera extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("CAMERA", extras.toString());
            File photo = null;

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            //createDirectoryAndSaveFile(imageBitmap, imageFileName);
            try {
                photo = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Exception", "imagefilecreationexception");
            }

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Boolean test = storageDir.isDirectory();
        Log.d("Directory exists?", test.toString());
        Log.d("Camera", "About to create file");
        Log.d("Camera storagDir", storageDir.toString());
        isExternalStorageWritable();

        int permissionCheck = this.checkSelfPermission
                (Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            this.requestPermissions(new String[]
                    {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);



        File image = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",          //suffix
                storageDir      //directory
        );
        Log.d("TEMP", "Temp file created");
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("CAMERA", "Photo Path :" +mCurrentPhotoPath);
        return image;
    }

    public void isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("Writeable", "Yes");
        } else {
            Log.d("Writeable", "False");
        }
    }

}