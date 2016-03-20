package com.example.android.imifai;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class GalleryFragment extends Fragment {
    private Parcelable[] mImages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImages = getArguments().getParcelableArray("images");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        configureImage();
        // Create global configuration and initialize ImageLoader with this config
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    private void configureImage(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .build();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
                .cacheOnDisk(true)
        .build();
        ImageLoader.getInstance().init(config);
    }
    private void loadImage(){
        ImageLoader imageLoader = ImageLoader.getInstance();
        
    }



}
