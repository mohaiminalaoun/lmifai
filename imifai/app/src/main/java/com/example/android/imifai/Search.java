package com.example.android.imifai;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.os.Handler;

import java.util.Queue;
import java.util.logging.LogRecord;

public class Search extends AppCompatActivity implements Serializable {
    private MultiAutoCompleteTextView textView;
    private String[] tags = new String[0];
    private Handler mHandler;
    private final int COMPLETION_THRESHOLD = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = (MultiAutoCompleteTextView) findViewById(R.id.SearchBar);
        textView.setThreshold(COMPLETION_THRESHOLD);

        //tags=["indoors", "hospital", "adult", "table", "paper", "inside", "relaxation", "business", "sit", "laptop"];
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, tags);
        textView.setAdapter(adapter);

        getTags(this);



        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
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

    private static final String[] COUNTRIES = new String[] {
            "Belgium","Bermingham", "France", "Italy", "Germany", "Spain"
    };
}
