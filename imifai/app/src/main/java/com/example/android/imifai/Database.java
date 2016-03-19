package com.example.android.imifai;

import android.net.Uri;
import java.util.ArrayList;
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

    public List<ImageRecord> getImages(String tag){
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
        ArrayList<ImageRecord> records=new ArrayList<>();
        for (HashMap.Entry<Uri, List<String>> entry : images.entrySet()) {
            records.add(new ImageRecord(entry.getKey(),entry.getValue()));
        }
        return records;
    }
}
