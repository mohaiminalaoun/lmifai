package com.example.android.imifai;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.clarify.api.ClarifaiClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Camera extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private ImageView view;
    private Recognizer recognizer = new Recognizer();
    private TextView mTagTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        view =(ImageView) findViewById(R.id.imageView);

        ImageRecord record = (ImageRecord) getIntent().getSerializableExtra("imageRecord");
        if(record!=null) {
            view.setImageURI(record.getUri());
            gotTagsBack(record.getTags());
        }
        else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                Log.d("DispactPic","creating image from file");
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Log.d("DispatchTakePicture", "taking pic");
      /*          takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));*/
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("Camera onResult", "Pic taken");
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            String path = MediaStore.Images.Media.insertImage(getContentResolver()
                    , imageBitmap, imageFileName, "");
            Uri imageUri = Uri.parse(path);
            view.setImageURI(imageUri);

            recognizer.recognizeImage(imageUri, this);

        }
    }

    public void gotTagsBack(List<String> tags){
        Log.d("LALALA", "GOT mah tags bacckk");
        Toast.makeText(this, tags.get(0), Toast.LENGTH_LONG);
        GridView gridview = (GridView) findViewById(R.id.gridViewButtons);
        gridview.setAdapter(new ButtonAdapter(this, tags));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void galleryAddPic() {
        Log.d("galleryAdd","Adding to gallery : " +  mCurrentPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Log.d("file paht", f.getAbsolutePath());
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

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
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("CAMERA", "Photo Path :" +mCurrentPhotoPath);
        return image;
    }


    //writing code to make textView and add tags that are returned.

/*    public void setTagTextView(String tags){
        mTagTextView=(TextView)findViewById(R.id.tagTextView);
        mTagTextView.setText(tags);
    }*/


    public class ButtonAdapter extends BaseAdapter {
        private Context mContext;
        private String[] mtags;

        public ButtonAdapter(Context c, List<String> tags) {
            Log.d("camera", "In imageadapter creator");
            mContext = c;
            mtags = tags.toArray(new String[tags.size()]);
            Log.d("tags", Arrays.toString(mtags));
        }

        public int getCount() {
            return mtags.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Button button;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                button = new Button(mContext);
                button.setLayoutParams(new GridView.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setLayoutParams(new ActionBar.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                button.setBackgroundColor(Color.parseColor("#21B684"));
                button.setTextColor(Color.parseColor("#64ffffff"));
                button.setPadding(1, 1, 1, 1);
                button.setText(mtags[position]);
            } else {
                button = (Button) convertView;
            }

            return button;
        }

    }
}
/*
<?xml version="1.0" encoding="utf-8"?>
<Button xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_centerVertical="true"
android:layout_centerHorizontal="true"
android:layout_marginLeft="30dp"
android:layout_marginRight="30dp"
android:background="#21B684"
android:textStyle="bold"
android:textColor="#64ffffff"
android:singleLine="false"/>

 */