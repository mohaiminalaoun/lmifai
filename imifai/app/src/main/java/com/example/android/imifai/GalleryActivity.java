package com.example.android.imifai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Queue;

public class GalleryActivity extends Activity implements Serializable,AdapterView.OnItemClickListener {

    private ImageRecord[] mImages;
    private ImageAdapter mImageAdapter;
    private GridView mGridview;

    private MultiAutoCompleteTextView textView;
    private String[] tags = new String[0];
    private final int COMPLETION_THRESHOLD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mGridview=(GridView)findViewById(R.id.gridview);

        textView = (MultiAutoCompleteTextView) findViewById(R.id.SearchBar);
        textView.setThreshold(COMPLETION_THRESHOLD);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, tags);
        textView.setAdapter(adapter);
        getTags(this);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Log.d("TAG", "HI MY NAME IS VIKAS");

        ImageRecordsWrapper wrapper =(ImageRecordsWrapper) getIntent().getSerializableExtra("images");
        mImages = wrapper.records;


        Log.d("TAG", ""+(mImages!=null));
        configureImage();
        Log.d("TAG", "" + (mImages != null));

        mGridview.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Camera.class);
        intent.putExtra("imageRecord",(ImageRecord) mImageAdapter.getItem(position));
        startActivity(intent);
    }

    public void getTags(final Context context){
        Runnable runnable = new Runnable(){
            public void run(){
                tags = Database.getInstance().getAllTags();
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line, tags);
                Log.d("Tags", Arrays.toString(tags));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setAdapter(adapter);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    public void searchForTags(View view){
        String [] tags = textView.getText().toString().split(",");
        Queue<ImageRecord> records = Database.getInstance().getImages(Arrays.asList(tags));

        if(records.size()==0){
            Toast.makeText(this, "No results were found", Toast.LENGTH_LONG);
            return;
        }

        Intent intent = new Intent(this, GalleryActivity.class);
        ImageRecord[] arr=new ImageRecord[records.size()];
        int i=0;
        for(ImageRecord rec: records){
            arr[i]=rec;
            i++;
        }
        intent.putExtra("images", new ImageRecordsWrapper(arr));
        startActivity(intent);
        Log.d("All images", records.poll().getUri().toString());

    }
}
