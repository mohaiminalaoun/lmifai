package com.example.android.imifai;

import android.net.Uri;

import com.orm.SugarRecord;

/**
 * Created by Shane on 3/19/2016.
 */
public class ImageTag extends SugarRecord {
    private String img;
    private String tag;

    public ImageTag(){

    }

    public ImageTag(Uri uri, String tag){
        this.img=uri.toString();
        this.tag=tag;
    }

    public Uri getUri(){
        return Uri.parse(img);
    }

    public String getTag(){
        return tag;
    }

}
