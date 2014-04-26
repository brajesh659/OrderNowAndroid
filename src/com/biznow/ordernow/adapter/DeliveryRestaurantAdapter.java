package com.biznow.ordernow.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biznow.ordernow.R;
import com.data.menu.Restaurant;
import com.util.DownloadImageTask;

public class DeliveryRestaurantAdapter extends ArrayAdapter<Restaurant>{

    private List<Restaurant> restList;
    public DeliveryRestaurantAdapter(Context context, List<Restaurant> restList) {
        super(context, R.layout.rest_list_item, restList);
        this.restList = restList;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.rest_list_item, null);
        }
        Restaurant rest = restList.get(position);
        
        TextView restName = (TextView) convertView.findViewById(R.id.rest_name);
        TextView restDescription = (TextView) convertView.findViewById(R.id.rest_description);
        TextView restAddress = (TextView) convertView.findViewById(R.id.rest_address);
        ImageView restImage = (ImageView) convertView.findViewById(R.id.rest_photo);
        
        restName.setText(rest.getName());
        restDescription.setText("rest description comes here");
        restAddress.setText(rest.getAddress());
        Bitmap img = null;
        try {
            img = new DownloadImageTask().execute(rest.getImg()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(img == null) {
            restImage.setImageResource(R.drawable.default_food_icon);    
        } else {
            restImage.setImageBitmap(img);    
        }
          
        return convertView;
    }

}
