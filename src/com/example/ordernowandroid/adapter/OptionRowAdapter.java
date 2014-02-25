package com.example.ordernowandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.data.menu.RecommendationType;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.OptionView;

public class OptionRowAdapter extends ArrayAdapter<OptionView> {

	private List<OptionView> optionList;
	private IngredientListener ingredientListener;

	public OptionRowAdapter(Context context, List<OptionView> optionList,
			IngredientListener ingredientListener) {
		super(context, R.layout.option_row, optionList);
		this.optionList = optionList;
		this.ingredientListener = ingredientListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO ViewHolder Pattern as in FoodMenuItemAdapter
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.option_row, null);
		}

		final OptionView option = optionList.get(position);
		TextView optionName = (TextView) convertView
				.findViewById(R.id.optionName);
		optionName.setText(option.getOptionName());
		
		TextView optionDescription = (TextView) convertView
				.findViewById(R.id.optionDescription);
		optionDescription.setText(option.getDescription());
		
		TextView recommended = (TextView)convertView.findViewById(R.id.optionRecommend);
		String recommendationText = "";
		RecommendationType recommendationtype = option.getRecommendation();
		if(recommendationtype.equals(RecommendationType.None)) {
			 recommendationText = "";
		} else {
			recommendationText= recommendationtype.toString();
		}
		recommended.setText(recommendationText);
		
		ImageView helpView = (ImageView)convertView.findViewById(R.id.helpicon);
		if(recommendationText.equals("")) {
			helpView.setVisibility(View.INVISIBLE);
		} else {
			helpView.setVisibility(View.VISIBLE);
		}

		
		
		final CheckBox ch = (CheckBox) convertView.findViewById(R.id.optionCheck);
		ch.setChecked(ingredientListener.isSelected(option));
		ch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ingredientListener.updateIngredient(option, ch.isChecked());
				option.setSelected(ch.isChecked());
			}
		});
		return convertView;
	}

}
