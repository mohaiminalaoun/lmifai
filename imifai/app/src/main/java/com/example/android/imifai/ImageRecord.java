package com.example.android.imifai;

import android.net.Uri;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Shane on 3/19/2016.
 */
public class ImageRecord implements Serializable {
    String uri;
    List<String> tags;

    public ImageRecord(Uri uri, List<String> tags){
        this.uri=uri.toString();
        this.tags=tags;
    }

    public Uri getUri(){
        return Uri.parse(uri);
    }
    public List<String> getTags(){
        return tags;
    }
}