package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.CategoryNavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewNavDrawerListAdapter extends BaseExpandableListAdapter {

    private Context applicationContext;
    private ArrayList<CategoryNavDrawerItem> navDrawerItems;
    private Map<String, ArrayList<CategoryNavDrawerItem>> childDrawerItems;
    private LayoutInflater minflater;
    private Activity activity;

    public NewNavDrawerListAdapter(Context applicationContext, ArrayList<CategoryNavDrawerItem> navDrawerItems, Map<String, ArrayList<CategoryNavDrawerItem>> childDrawerItems) {
        this.applicationContext = applicationContext;
        // TODO Auto-generated constructor stub
        this.navDrawerItems = navDrawerItems;
        this.childDrawerItems = childDrawerItems;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
    return this.childDrawerItems.get(navDrawerItems.get(groupPosition).getTitle()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosititon) {
        return childPosititon;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            minflater = (LayoutInflater) applicationContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.child_drawer_item, null);
        }

        ArrayList<CategoryNavDrawerItem> tempChild = childDrawerItems.get(navDrawerItems.get(groupPosition).getTitle());

        TextView txtTitle = (TextView) convertView.findViewById(R.id.lblListItem);
        txtTitle.setText(tempChild.get(childPosition).getTitle());
        
//        TextView text = null;
//        text = (TextView) convertView;
        // convertView.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // Toast.makeText(activity, tempChild.get(childPosition),
        // Toast.LENGTH_SHORT).show();
        // }
        // });
        convertView.setTag(tempChild.get(childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childDrawerItems.get(navDrawerItems.get(groupPosition).getTitle()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return this.navDrawerItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return navDrawerItems.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int position, boolean arg1, View convertView, ViewGroup parent) {
        if (convertView == null) {
            minflater = (LayoutInflater) applicationContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        ImageView imgView = (ImageView) convertView.findViewById(R.id.icon);
        imgView.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}
