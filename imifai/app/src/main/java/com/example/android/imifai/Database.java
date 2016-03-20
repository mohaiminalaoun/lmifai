package com.example.android.imifai;

import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


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

    public Queue<ImageRecord> getImages(String tag){
        List<ImageTag> imageTags = ImageTag.find(ImageTag.class, "tag = ?", tag);
        if (imageTags==null || imageTags.size()==0){
            return new PriorityQueue<>();
        }
        HashMap<Uri, List<String>> images= new HashMap<>();

        populateHashMap(imageTags, images);
        return orderResults(images);
    }

    public Queue<ImageRecord> getImages(List<String> tags){
        HashMap<Uri, List<String>> images= new HashMap<>();
        for(String tag: tags){
            List<ImageTag> imageTags = ImageTag.find(ImageTag.class, "tag = ?", tag);
            if(imageTags!=null && imageTags.size()>0) {
                populateHashMap(imageTags, images);
            }
        }
        return orderResults(images);
    }

    public String[] getAllTags(){
        List<ImageTag> tags = ImageTag.listAll(ImageTag.class);
        HashSet<String> tagset=new HashSet<>();
        for(ImageTag tag: tags){
            tagset.add(tag.getTag());
        }
        Log.d("Database", Integer.toString(tagset.size()));
        return tagset.toArray(new String[tagset.size()]);
    }

    @NonNull
    private PriorityQueue<ImageRecord> orderResults(HashMap<Uri, List<String>> images) {
        PriorityQueue<ImageRecord> records=new PriorityQueue<ImageRecord>(10,new RecordComparator());
        if(images!=null && images.size()>0){
            for (HashMap.Entry<Uri, List<String>> entry : images.entrySet()) {
                records.add(new ImageRecord(entry.getKey(), entry.getValue()));
            }
        }
        return records;
    }

    public void populateHashMap(List<ImageTag> imageTags, HashMap<Uri, List<String>> images){
        if(images==null){
            images=new HashMap<>();
        }
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
    }

    //Ranks from highest number of tags to lowest
    public class RecordComparator implements Comparator<ImageRecord>{

        public RecordComparator(){

        }
        @Override
        public int compare(ImageRecord lhs, ImageRecord rhs) {
            if (lhs==null || rhs==null){
                return 0;
            }
            else if (lhs.getTags().size()>rhs.getTags().size()){
                return 1;
            }
            else if (lhs.getTags().size()<rhs.getTags().size()){
                return -1;
            }
            return 0;
        }
    }
}
