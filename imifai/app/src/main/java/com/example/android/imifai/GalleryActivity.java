package com.example.android.imifai;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;

public class GalleryActivity extends Activity implements Serializable {

    private ImageRecord[] mImages;
    private ImageAdapter mImageAdapter;
    private GridView mGridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mGridview=(GridView)findViewById(R.id.gridview);

        Log.d("TAG", "HI MY NAME IS VIKAS");

        ImageRecordsWrapper wrapper =(ImageRecordsWrapper) getIntent().getSerializableExtra("images");
        mImages = wrapper.records;


        Log.d("TAG", ""+(mImages!=null));
        configureImage();
        Log.d("TAG", ""+(mImages != null));

    }

    private void configureImage(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().init(config);

        //Create Adapter
        mImageAdapter = new ImageAdapter((ImageRecord[]) mImages,defaultOptions,this);
        mGridview.setAdapter(mImageAdapter);
    }
}
