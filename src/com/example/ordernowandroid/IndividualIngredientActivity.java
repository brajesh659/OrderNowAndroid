package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.aphidmobile.flip.FlipViewController;
import com.data.menu.Ingredient;
import com.example.ordernowandroid.adapter.IndividualIngredientsAdapter;
import com.example.ordernowandroid.adapter.IngredientListener;
import com.example.ordernowandroid.model.FoodIngredient;
import com.example.ordernowandroid.model.IngredientOptionView;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.util.OrderNowUtilities;
import com.util.Utilities;

public class IndividualIngredientActivity extends Activity implements
		IngredientListener {

	private FlipViewController flipView;
	private IndividualIngredientsAdapter adapter;
	ArrayList<FoodIngredient> ingList;
	public static final String OPTION_PAGE = "PageNumber";
	int page = 0;
	private String dishname;
	// Map<String,OptionView> selectedOptions = new
	// HashMap<String,OptionView>();
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
			ingList = (ArrayList<FoodIngredient>) b
					.getSerializable(IngredientsActivity.INGREDIENTS_LIST);
			Utilities.info("ing inside " + ingList.toString());
		}
		page++;

		selectedOptions = ApplicationState.getDishSelectedIngredientList(
				(ApplicationState) getApplicationContext(), dishname);
		if (selectedOptions == null) {
			selectedOptions = new ArrayList<IngredientOptionView>();
		}
		// adding dummay page in start and end to flip to previous activity
		ingList.add(new FoodIngredient(new Ingredient(dishname, null)));
		ingList.add(0, new FoodIngredient(new Ingredient(dishname, null)));

		setTitle(ingList.get(page).getTitle());

		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);

		adapter = new IndividualIngredientsAdapter(this, ingList, this);
		flipView.setAdapter(adapter);
		flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
		// flipView.setBackgroundColor(getResources().getColor(R.color.chartreuse));
		flipView.setSelection(page);
		flipView.setBackgroundColor(getResources().getColor(
				R.color.blanchedalmond));

		flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
			@Override
			public void onViewFlipped(View view, int position) {
				// dummy pages
				if ((position == adapter.getCount() - 1) || (position == 0)) {
					onBackPressed();
					return;
				} else {
					setTitle(ingList.get(position).getTitle());
					page = position;
				}

			}
		});

		setContentView(flipView);
		if (!OrderNowConstants.FALSE.equals(OrderNowUtilities
				.getKeyFromSharedPreferences(getApplicationContext(),
						OrderNowConstants.KEY_INGREDIENTS_SHOW_SWIPTE_TUT))) {
			OrderNowUtilities.showActivityOverlay(this,
					R.layout.overlay_activity);
			OrderNowUtilities.putKeyToSharedPreferences(
					getApplicationContext(),
					OrderNowConstants.KEY_INGREDIENTS_SHOW_SWIPTE_TUT,
					OrderNowConstants.FALSE);
		}
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
		if (selectedOptions.contains(optionView)) {
			return true;
		}
		return false;
	}

	@Override
	public void updateIngredient(IngredientOptionView optionView,
			boolean checked) {
		if (checked) {
			selectedOptions.add(optionView);
			ApplicationState.addDishSelectedIngredient(
					(ApplicationState) getApplicationContext(), dishname,
					optionView);
		} else {
			if (selectedOptions.contains(optionView)) {
				selectedOptions.remove(optionView);
			}
			ApplicationState.removeDishSelectedIngredient(
					(ApplicationState) getApplicationContext(), dishname,
					optionView);
		}

	}

}
