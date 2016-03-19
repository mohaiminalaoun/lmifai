package com.example.android.imifai;

import android.net.Uri;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }

    public void addImage(Uri img, List<String> tags){
        //so hack, much denormalized
        for(String tag: tags){
            ImageTag imgTag=new ImageTag(img, tag);
            imgTag.save();
        }
    }

    public HashMap<Uri, List<String>> getImages(String tag){
        List<ImageTag> imageTags = ImageTag.find(ImageTag.class, "tag = ?", tag);

        HashMap<Uri, List<String>> images= new HashMap<>();
        for (ImageTag imgTag : imageTags){

            List<String> tags=null;
            if ((tags=images.get(imgTag.getUri()))!=null){
                tags.add(imgTag.getTag());
            }
            else{
                tags=new ArrayList<>();
                tags.add(imgTag.getTag());
                images.put(imgTag.getUri(),tags);
            }
        }
        return images;
    }

    public class ImageTag extends SugarRecord{
        Uri img;
        String tag;

        public ImageTag(){

        }

        public ImageTag(Uri img, String tag){
            this.img=img;
            this.tag=tag;
        }

        public Uri getUri(){
            return img;
        }

        public String getTag(){
            return tag;
        }

    }
}
