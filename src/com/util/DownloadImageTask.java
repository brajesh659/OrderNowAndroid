package com.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.biznow.ordernow.adapter.ImageService;

public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        // "http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png"
        return ImageService.getInstance().getImageWithCache(params[0]);
    }

}
