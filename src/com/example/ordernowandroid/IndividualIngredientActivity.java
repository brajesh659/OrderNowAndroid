package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.aphidmobile.flip.FlipViewController;
import com.example.ordernowandroid.adapter.IndividualIngredientsAdapter;
import com.example.ordernowandroid.adapter.IngredientListener;
import com.example.ordernowandroid.model.FoodIngredient;
import com.example.ordernowandroid.model.IngredientOptionView;
import com.util.Utilities;

public class IndividualIngredientActivity extends Activity implements IngredientListener {

	private FlipViewController flipView;
	private IndividualIngredientsAdapter adapter;
	ArrayList<FoodIngredient> ingList;
	public static final String OPTION_PAGE = "PageNumber";
	int page = 0;
	private String dishname;
	//Map<String,OptionView> selectedOptions = new HashMap<String,OptionView>();
	private List<IngredientOptionView> selectedOptions;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			page = b.getInt(OPTION_PAGE);
			dishname = b.getString(IngredientsActivity.DISH_NAME);
			ingList = (ArrayList<FoodIngredient>) b.getSerializable(IngredientsActivity.INGREDIENTS_LIST);
			Utilities.info("ing inside " + ingList.toString());
		}

		selectedOptions = ApplicationState.getDishSelectedIngredientList((ApplicationState)getApplicationContext(),dishname);
		if(selectedOptions == null) {
			selectedOptions = new ArrayList<IngredientOptionView>();
		}
		setTitle(ingList.get(page).getTitle());

		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);

		adapter = new IndividualIngredientsAdapter(this, ingList, this);
		flipView.setAdapter(adapter);
		flipView.setSelection(page);

		flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
			@Override
			public void onViewFlipped(View view, int position) {
				setTitle(ingList.get(position).getTitle());
				page = position;

			}
		});

		setContentView(flipView);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
	}

	@Override
	public boolean isSelected(IngredientOptionView optionView) {
		if(selectedOptions.contains(optionView)) {
			return true;
		}
		return false;
	}
	

	@Override
	public void updateIngredient(IngredientOptionView optionView, boolean checked) {
		if(checked) {
			selectedOptions.add(optionView);
			ApplicationState.addDishSelectedIngredient((ApplicationState)getApplicationContext(), dishname, optionView);
		} else {
			if(selectedOptions.contains(optionView)) {
				selectedOptions.remove(optionView);
			}
			ApplicationState.removeDishSelectedIngredient((ApplicationState)getApplicationContext(), dishname, optionView);
		}
		
	}

}
