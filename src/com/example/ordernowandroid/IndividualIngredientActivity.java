package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.data.menu.Ingredient;
import com.example.ordernowandroid.adapter.IndividualIngredientsAdapter;
import com.example.ordernowandroid.model.FoodIngredient;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class IndividualIngredientActivity extends Activity {

	private FlipViewController flipView;
	ArrayList<FoodIngredient> ingList;
	public static final String OPTION_PAGE = "PageNumber";
	int page = 0;
	private String dishname;

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
		}
		ingList = (ArrayList<FoodIngredient>) getFoodIngredientsLocaly();

		setTitle(dishname);

		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);

		IndividualIngredientsAdapter adapter = new IndividualIngredientsAdapter(
				this, ingList);
		flipView.setAdapter(adapter);
		flipView.setSelection(page);
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

	private List<FoodIngredient> getFoodIngredientsLocaly() {
		List<FoodIngredient> ingList = new ArrayList<FoodIngredient>();
		String[] ingTitle = getResources().getStringArray(
				R.array.ingredients_title);

		Ingredient ing = new Ingredient(ingTitle[0],
				Arrays.asList(getResources().getStringArray(R.array.ing0)));
		FoodIngredient fi = new FoodIngredient(ing);
		ingList.add(fi);

		Ingredient ing1 = new Ingredient(ingTitle[1],
				Arrays.asList(getResources().getStringArray(R.array.ing1)));
		FoodIngredient fi1 = new FoodIngredient(ing1);
		ingList.add(fi1);

		Ingredient ing2 = new Ingredient(ingTitle[2],
				Arrays.asList(getResources().getStringArray(R.array.ing2)));
		FoodIngredient fi2 = new FoodIngredient(ing2);
		ingList.add(fi2);

		Ingredient ing3 = new Ingredient(ingTitle[3],
				Arrays.asList(getResources().getStringArray(R.array.ing3)));
		FoodIngredient fi3 = new FoodIngredient(ing3);
		ingList.add(fi3);
		return ingList;
	}

}
