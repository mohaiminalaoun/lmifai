package com.example.android.imifai;

import android.net.Uri;

import java.util.List;

/**
 * Created by Shane on 3/19/2016.
 */
public class ImageRecord{
    Uri uri;
    List<String> tags;

    public ImageRecord(Uri uri, List<String> tags){
        this.uri=uri;
        this.tags=tags;
    }
}