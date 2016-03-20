package com.example.android.imifai;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Shane on 3/19/2016.
 */
public class ImageRecord implements Serializable {
    String uri;
    List<String> tags;

    public ImageRecord(Uri uri, List<String> tags){
        this.uri=uri.toString();
        HashSet<String> dedup=new HashSet<>(tags);
        this.tags=new ArrayList<>(dedup);
    }

    public Uri getUri(){
        return Uri.parse(uri);
    }
    public List<String> getTags(){
        return tags;
    }
}
