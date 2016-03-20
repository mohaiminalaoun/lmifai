package com.example.android.imifai;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.lang.ref.WeakReference;

/**
 * Created by Vikingprime on 3/20/2016.
 */
public class ImageAdapter extends BaseAdapter{

    ImageRecord[] records;
    DisplayImageOptions options;

    /**
     *
     * The Context of the application.
     */
    private WeakReference<Context> mContext;
    /**
     * This static class holds the views
     * so they don't have to be found each time
     * getView is called
     */
    public static class ViewHolder {

        /**
         * Button used to play or pause the song.
         */
        public final ImageView mPicture;

        /**
         * Constructor initializes the fields.
         */
        public ViewHolder(View v) {
            mPicture = (ImageView)
                    v.findViewById(R.id.image);
        }
    }

    public ImageAdapter(ImageRecord[] imageRecords, DisplayImageOptions myoptions, Context context){
        records = imageRecords;
        options = myoptions;
        mContext = new WeakReference<Context>(context);
    }

    public void setAdapter(ImageRecord[] imageRecords){
        records = imageRecords;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return records.length;
    }

    @Override
    public Object getItem(int position) {
        return records[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        // If it's null, we make a new view.
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext.get());
            v = vi.inflate(R.layout.item_grid_image, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }
        String uri = records[position].getUri().toString();
        ImageLoader.getInstance().displayImage(uri,holder.mPicture,options);

        return v;
    }
}
