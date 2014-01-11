package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.FoodMenuItem;

/**
 * 
 * @author Rohit
 * Creating a CustomAdapter since the ArrayAdapter by default only displays the toString() implementation of the object for each list row
 */
public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> {

	private ArrayList<FoodMenuItem> foodMenuItems;

	public FoodMenuItemAdapter(Context context, ArrayList<FoodMenuItem> foodMenuItems) {
		super(context, R.layout.food_menu_item, foodMenuItems);
		this.foodMenuItems = foodMenuItems;
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.food_menu_item, parent, false);

		TextView itemName = (TextView) rowView.findViewById(R.id.itemName);
		TextView itemPrice = (TextView) rowView.findViewById(R.id.itemPrice);
		Log.d("foodMenuItems.get(position).getItemPrice()", Integer.toString(foodMenuItems.get(position).getItemPrice()));		
		
		itemName.setText(foodMenuItems.get(position).getItemName());
		itemPrice.setText(Integer.toString(foodMenuItems.get(position).getItemPrice()));
		return rowView;
	}
}
