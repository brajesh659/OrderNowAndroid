package com.example.ordernowandroid.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.CategoryNavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<CategoryNavDrawerItem> navDrawerItems;
     
    public NavDrawerListAdapter(Context context, ArrayList<CategoryNavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        ImageView imgView = (ImageView) convertView.findViewById(R.id.icon_parent);
        imgView.setVisibility(View.VISIBLE);
        
        return convertView;
    }
 
}