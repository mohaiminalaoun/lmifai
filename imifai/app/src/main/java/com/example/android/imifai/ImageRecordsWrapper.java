package com.example.android.imifai;

import java.io.Serializable;

/**
 * Created by sanji on 3/20/2016.
 */
public class ImageRecordsWrapper implements Serializable{
    public ImageRecord[] records;

    ImageRecordsWrapper(ImageRecord[] records){
        this.records=records;
    }
}
