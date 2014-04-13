package com.biznow.ordernow.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.data.menu.IngredientOption;
import com.biznow.ordernow.R;
import com.biznow.ordernow.model.FoodIngredient;
import com.biznow.ordernow.model.IngredientOptionView;
import com.util.Utilities;

public class IndividualIngredientsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<FoodIngredient> ingredientList;
	private IngredientListener ingredientListener;

	private int repeatCount = 1;

	public IndividualIngredientsAdapter(Context context,
			ArrayList<FoodIngredient> ingredientList,
			IngredientListener ingredientListener) {
		this.context = context;
		this.ingredientList = ingredientList;
		this.ingredientListener = ingredientListener;
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
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.option_list, null);
		}
		ListView optionsDisplayList = (ListView) convertView
				.findViewById(R.id.optionsList);
		FoodIngredient ingredient = (FoodIngredient) getItem(position);
		List<IngredientOptionView> optionsList = ingredient.getIngredientOptions();
		Utilities.info("optionList " + optionsList);
		OptionRowAdapter adapter = new OptionRowAdapter(context, optionsList, ingredientListener);
		optionsDisplayList.setAdapter(adapter);

		return convertView;
	}

}