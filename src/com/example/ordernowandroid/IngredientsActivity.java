package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordernowandroid.adapter.StaggeredIngredientAdapter;
import com.example.ordernowandroid.fragments.StaggeredGridView;
import com.example.ordernowandroid.model.FoodIngredient;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.IngredientOptionView;
import com.example.ordernowandroid.model.MyOrderItem;

public class IngredientsActivity extends Activity {

	private FoodMenuItem foodItem;
	private ArrayList<FoodIngredient> ingList;
	private String dishName;
	private int itemQuantity = 1;
	public static final String DISH_NAME = "dishname";
	public static final String INGREDIENTS_LIST = "ingredientsList";
	public static final String FOOD_ITEM = "foodItem";
	private List<IngredientOptionView> selectedOptions;
	private StaggeredIngredientAdapter adapter;

	private String urls[] = {
			"http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
			"http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
			"http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
			"http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
			"http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
			"http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
			"http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
			"http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
			"http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
			"http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
			"http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
			"http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
			"http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg",
			"http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
			"http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
			"http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
			"http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
			"http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
			"http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
			"http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
			"http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
			"http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
			"http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
			"http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
			"http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
			"http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			dishName = b.getString(DISH_NAME);
			foodItem = (FoodMenuItem) b
					.getSerializable(IngredientsActivity.FOOD_ITEM);
			ingList = foodItem.getIngredients();
		}

		// if first time then selectedOptions will be null, populate it using
		// preselectedoptions if present
		if (selectedOptions == null
				&& foodItem.getCurrentSelectedIngredientOptions() != null
				&& !foodItem.getCurrentSelectedIngredientOptions().isEmpty()) {
			ApplicationState.setDishSelectedIngredientList(
					(ApplicationState) getApplicationContext(), dishName,
					foodItem.getCurrentSelectedIngredientOptions());
		}
		setTitle(dishName);

		// ingList = getFoodIngredientsLocaly();
		setContentView(R.layout.ingredeints_view);

		final ImageButton decrementQtyBtn = (ImageButton) findViewById(R.id.decrementQtyButton);
		final ImageButton incrementQtyBtn = (ImageButton) findViewById(R.id.incrementQtyButton);
		final TextView quantity = (TextView) findViewById(R.id.quantity);
		final Button addToCart = (Button) findViewById(R.id.addIngredientToCart);

		selectedOptions = ApplicationState.getDishSelectedIngredientList(
				(ApplicationState) getApplicationContext(), dishName);

		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState
				.getFoodMenuItemQuantityMap((ApplicationState) getApplicationContext());

		if (foodMenuItemQuantityMap.get(foodItem.getItemName()) != null) {

			itemQuantity = (int) foodMenuItemQuantityMap.get(
					foodItem.getItemName()).getQuantity();
			addToCart.setText(getResources().getString(R.string.modify));
		}
		if (selectedOptions != null) {
			for (FoodIngredient ing : ingList) {
				ing.prepareSelectedOptions(selectedOptions);
			}
		}

		StaggeredGridView gridView = (StaggeredGridView) this
				.findViewById(R.id.staggeredGridView1);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		gridView.setItemMargin(margin); // set the GridView margin

		gridView.setPadding(margin, 0, margin, 0); // have the margin on the
													// sides as well

		// StaggeredAdapter adapter = new
		// StaggeredAdapter(IngredientsActivity.this, R.id.scaleImageView,
		// urls);

		adapter = new StaggeredIngredientAdapter(IngredientsActivity.this,
				ingList);

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {

			@Override
			public void onItemClick(StaggeredGridView parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						IndividualIngredientActivity.class);
				intent.putExtra(IndividualIngredientActivity.OPTION_PAGE,
						position);
				intent.putExtra(IngredientsActivity.DISH_NAME, dishName);
				intent.putExtra(IngredientsActivity.INGREDIENTS_LIST, ingList);
				startActivity(intent);

			}
		});
		adapter.notifyDataSetChanged();

		quantity.setText(itemQuantity + "");

		decrementQtyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// HashMap<String, MyOrderItem> foodMenuItemQuantityMap =
				// ApplicationState
				// .getFoodMenuItemQuantityMap(applicationState);
				Float qty = Float.parseFloat((String) quantity.getText());

				if (qty > 1) {
					qty = qty - 1;
					quantity.setText(qty.intValue() + "");
					itemQuantity = qty.intValue();

					/*
					 * foodMenuItemQuantityMap.get(
					 * myOrderItemList.get(position).getFoodMenuItem()
					 * .getItemName()).setQuantity(qty - 1);
					 */

				} else if (qty == 1f) {
					// Show Dialog and Remove Item from ListView on Positive
					// Button Action
					// Show Dialog and Remove Item from ListView on Positive
					// Button Action
					AlertDialog.Builder builder = new AlertDialog.Builder(
							IngredientsActivity.this);
					builder.setTitle("Remove Item");
					builder.setMessage("Are you sure you want to remove all the selections for item ?");
					builder.setPositiveButton(R.string.ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ApplicationState
											.cleanDishSelectedIngredients(
													(ApplicationState) getApplicationContext(),
													dishName);
									finish();
								}
							});
					builder.setNegativeButton(R.string.cancel, null);
					AlertDialog alert = builder.create();
					alert.show();

				} else {
					Toast.makeText(getApplicationContext(),
							"Quantity cannnot be decreased below zero",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		incrementQtyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Float qty = Float.parseFloat((String) quantity.getText());
				qty = qty + 1;
				quantity.setText(qty.intValue() + "");
				itemQuantity = qty.intValue();
				// myOrderItemList.get(position).setQuantity(qty + 1);
			}
		});

		addToCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean isMinCriteriaSatisfied = true;
				for (FoodIngredient ing : ingList) {
					if (!ing.isMinOptionsSelected()) {
						isMinCriteriaSatisfied = false;
						break;
					}
				}
				if (isMinCriteriaSatisfied) {
					HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState
							.getFoodMenuItemQuantityMap((ApplicationState) getApplicationContext());
					if (foodMenuItemQuantityMap.get(foodItem.getItemName()) == null) {
						MyOrderItem myOrderItem = new MyOrderItem(foodItem,
								itemQuantity);
						foodMenuItemQuantityMap.put(foodItem.getItemName(),
								myOrderItem);
					} else {
						foodMenuItemQuantityMap.get(foodItem.getItemName())
								.setQuantity(itemQuantity);
					}
					finish();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							IngredientsActivity.this);
					builder.setTitle("Item not added");
					builder.setMessage("The criteria for minimum selection of options is not satisfied.");
					builder.setPositiveButton(R.string.ok, null);
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
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
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		selectedOptions = ApplicationState.getDishSelectedIngredientList(
				(ApplicationState) getApplicationContext(), dishName);
		if (selectedOptions != null) {
			for (FoodIngredient ing : ingList) {
				ing.prepareSelectedOptions(selectedOptions);
			}
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

}
