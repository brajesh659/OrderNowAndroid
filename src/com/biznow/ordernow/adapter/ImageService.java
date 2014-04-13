package com.biznow.ordernow.adapter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.util.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageService {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
    private LruCache<String, Bitmap> lruBitmap = new LruCache<String, Bitmap>(cacheSize);
    private static ImageService imageService = new ImageService();
    public static ImageService getInstance (){
        return imageService;
    }
    
    private ImageService() {
    }

    public Bitmap getImageWithCache(String param) {
        String urlEncodedParam = null; 
        try {
            urlEncodedParam = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (lruBitmap.get(urlEncodedParam) == null) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(param);
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                bitmap = BitmapFactory.decodeStream(input);
                Utilities.info("Image cached successfullly : "+ urlEncodedParam);
                lruBitmap.put(urlEncodedParam, bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("ImageService", ex.getMessage() + " got exception");
            }
        }
        
        return lruBitmap.get(urlEncodedParam);
    }
}
