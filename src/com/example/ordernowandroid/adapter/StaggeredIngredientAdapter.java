package com.example.ordernowandroid.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.fragments.ScaleImageView;
import com.example.ordernowandroid.model.FoodIngredient;
import com.example.ordernowandroid.views.AutoResizeTextView;
import com.util.ImageLoader;
import com.util.Utilities;


public class StaggeredIngredientAdapter extends ArrayAdapter<FoodIngredient> {

	private ImageLoader mLoader;
	private Context context;

	public StaggeredIngredientAdapter(Context context, ArrayList<FoodIngredient> ingredientList) {
		super(context, R.layout.row_staggered_demo, ingredientList);
		this.context = context;
		mLoader = new ImageLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,
					null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView .findViewById(R.id.scaleImageView);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		
		
		//AutoResizeTextView tv = new AutoResizeTextView(context);
    	//tv.setText(getItem(position).getBitMapText());
		FoodIngredient ingredient = getItem(position);
		holder.textView.setText(ingredient.getBitMapText());
		
		if(ingredient.isMinOptionsSelected()) {
			holder.textView.setBackgroundColor(context.getResources().getColor(
					R.color.greenyellow));
		} else if(ingredient.getSelectedOptions().size()!=0) {
			holder.textView.setBackgroundColor(context.getResources().getColor(
					R.color.rosybrown));
		} else {
			holder.textView.setBackgroundColor(context.getResources().getColor(
					R.color.mintcream));
		}
    	
		//Removing AutoResize because it is not working in other phone
    	//holder.textView.setHeight(100);
    	//holder.textView.resizeText();
    	/*
    	tv.setTextSize(20);
    	tv.setDrawingCacheEnabled(true);
    	
		tv.measure(MeasureSpec.makeMeasureSpec(200, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(300, MeasureSpec.UNSPECIFIED));
    	tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight()); 
    	tv.buildDrawingCache(true);
  
    	//tv.measure(100, 100);
    	//tv.layout(0, 0, 100, 100);
    	//tv.setTextColor(context.getResources().getColor(R.color.blueviolet));
    	//tv.setBackgroundColor(context.getResources().getColor(R.color.chartreuse));
    	tv.setDrawingCacheEnabled(true);
    	tv.buildDrawingCache(true);
    	//Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    	Bitmap bm = Bitmap.createBitmap(tv.getDrawingCache());//,0,0,10,10);
    	tv.setDrawingCacheEnabled(false);
    	//Canvas canvas = new Canvas(bm);
		//tv.draw(canvas);
        if(bm!=null) {
        	Utilities.info("bitmap drawin cache in ");
        	holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        	holder.imageView.setImageBitmap(bm);
        }
		
		//mLoader.DisplayBitMapImage(getItem(position).getBitMapText(), holder.imageView);
		*/
		return convertView;
	}

	static class ViewHolder {
		//ScaleImageView imageView;
		TextView textView;
	}
}
