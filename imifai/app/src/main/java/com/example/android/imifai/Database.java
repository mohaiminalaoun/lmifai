package com.example.android.imifai;

import android.net.Uri;

import com.orm.SugarRecord;


public class Database {
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }

    public class Image extends SugarRecord{
        Uri uri;

        public Image(){
        }

        public Image(Uri uri){
            this.uri=uri;
        }
    }

    public class Tag extends SugarRecord{
        String name;

        public Tag(){

        }

        public Tag(String name){
            this.name=name;
        }
    }

    public class ImageTag extends SugarRecord{
        Image img;
        Tag tag;

        public ImageTag(){

        }

        public ImageTag(Image img, Tag tag){
            this.img=img;
            this.tag=tag;
        }
    }
}
