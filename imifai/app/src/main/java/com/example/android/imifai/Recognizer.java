package com.example.android.imifai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Credentials;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 3/19/2016.
 */
public class Recognizer {

    private static final String TAG = Recognizer.class.getSimpleName();
    private static final double PROBABILITY_CUTOFF = 0.8;

    private final ClarifaiClient client = new ClarifaiClient(
            com.example.android.imifai.Credentials.CLIENT_ID,
            com.example.android.imifai.Credentials.CLIENT_SECRET);

    /** Loads a Bitmap from a content URI returned by the media picker. */
    private Bitmap loadBitmapFromUri(Uri uri, Context context) {
        try {
            // The image may be large. Load an image that is sized for display. This follows best
            // practices from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, opts);
            int sampleSize = 1;
            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, opts);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }

    /** Sends the given bitmap to Clarifai for recognition and returns the result. */
    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e(TAG, "Clarifai error", e);
            return null;
        }
    }

    public void recognizeImage(final Uri uri, Context context){
        Bitmap bitmap = loadBitmapFromUri(uri, context);
        if (bitmap != null) {
            // Run recognition on a background thread since it makes a network call.
            new AsyncTask<Bitmap, Void, RecognitionResult>() {
                @Override protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                    return recognizeBitmap(bitmaps[0]);
                }

                @Override protected void onPostExecute(RecognitionResult result) {
                    resultCallback(uri, result);
                }
            }.execute(bitmap);
        } else {
            Log.d(TAG,"Unable to load selected image.");
        }
    }

    private void resultCallback(Uri uri, RecognitionResult result) {
        List<Tag> tags = result.getTags();

        ArrayList<String> tagNames = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getProbability() > PROBABILITY_CUTOFF) {
                tagNames.add(tag.getName());
            }

            String listString="";
            for (String s : tagNames)
            {
                listString += s + "\t";
            }

            Log.d("Recognizer", listString);
            Database.getInstance().addImage(uri, tagNames);
        }
    }

}
