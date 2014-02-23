package com.example.ordernowandroid.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.FoodIngredient;

public class IndividualIngredientsAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<FoodIngredient> ingredientList ;
    
    private int repeatCount = 1;
     
    public IndividualIngredientsAdapter(Context context, ArrayList<FoodIngredient> ingredientList){
        this.context = context;
        this.ingredientList = ingredientList;
    }
 
    @Override
    public int getCount() {
        return ingredientList.size() * repeatCount;
    }
 
    @Override
    public Object getItem(int position) {       
        return ingredientList.get(position);
    }
 
	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        
        FoodIngredient ingredient = (FoodIngredient) getItem(position);
        TextView tv = new TextView(context);
        tv.setText(ingredient.getTitle());
        ll.addView(tv);
        for(String option : ingredient.getOptions()) {
        	// Add Text
        	CheckBox ch = new CheckBox(context);
            ch.setText(option);
            ch.setTextColor(context.getResources().getColor(R.color.burlywood));
            ll.addView(ch);
        }

        return ll;
    }
 
}