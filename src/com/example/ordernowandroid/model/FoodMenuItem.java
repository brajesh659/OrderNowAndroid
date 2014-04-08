package com.example.ordernowandroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.data.menu.Dish;
import com.data.menu.Ingredient;
import com.data.menu.IngredientOption;
import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;
import com.example.ordernowandroid.adapter.ImageService;

/**
 * 
 * @author Rohit
 * 
 */

public class FoodMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Dish dish;
	private String category = "";
	private ArrayList<FoodIngredient> ingredients;

	public FoodMenuItem(Dish dish) {
		this.dish = dish;
		populateImageCache(); // To populate image cache.

		// prepare ingredients if any
		if (isItemCustomizable()) {
			ingredients = new ArrayList<FoodIngredient>();
			ArrayList<Ingredient> dishIngredients = dish.getDishIngredients()
					.getIngredients();
			if (dishIngredients != null && !dishIngredients.isEmpty()) {
				for (Ingredient ing : dishIngredients) {
					FoodIngredient fi = new FoodIngredient(ing,
							dish.getSelectedIngredientOptions());
					ingredients.add(fi);
				}
			}
		}
	}
	
	/*
	public ArrayList<IngredientOptionView> getPreSelectedIngredientOptionsView() {
		ArrayList<IngredientOptionView> preSelectedOptions = new ArrayList<IngredientOptionView>();
		ArrayList<IngredientOption> preOptions =  dish.getSelectedIngredientOptions();
		if(preOptions != null) {
			for(IngredientOption option : preOptions) {
				IngredientOptionView optionView = new IngredientOptionView(option);
				preSelectedOptions.add(optionView);
			}
		}
		return preSelectedOptions;
	}
	*/
	
	public ArrayList<IngredientOptionView> getCurrentSelectedIngredientOptions() {
		ArrayList<IngredientOptionView> currentSelectedOptions = new ArrayList<IngredientOptionView>();
		for(FoodIngredient foodIngredient : getIngredients()) {
			ArrayList<IngredientOptionView> options = foodIngredient.getSelectedOptions();
			if(options != null && !options.isEmpty()) {
				currentSelectedOptions.addAll(options);
			}
		}
		return currentSelectedOptions;
	}

    public ArrayList<IngredientOption> getCurrentSelectedIngredients() {
        ArrayList<IngredientOption> currentSelectedOptions = new ArrayList<IngredientOption>();
        for (FoodIngredient foodIngredient : getIngredients()) {
            ArrayList<IngredientOptionView> options = foodIngredient.getSelectedOptions();
            if (options != null && !options.isEmpty()) {
                for (IngredientOptionView option : options) {
                    currentSelectedOptions.add(option.getOption());
                }
            }
        }
        return currentSelectedOptions;
    }

	public String getItemName() {
		return dish.getName();
	}

	public Float getItemPrice() {
		return dish.getPrice();
	}

	public String getDescription() {
		return dish.getDescription();
	}

	public ArrayList<FoodIngredient> getIngredients() {
		if (ingredients == null) {
			ingredients = new ArrayList<FoodIngredient>();
			ArrayList<Ingredient> dishIngredients = dish.getDishIngredients()
					.getIngredients();
			for (Ingredient ing : dishIngredients) {
				FoodIngredient fi = new FoodIngredient(ing);
				ingredients.add(fi);
			}
		}
		return ingredients;
	}

	public boolean isItemCustomizable() {
		return dish.isIngredientCustomizable();
	}

	@Override
	public String toString() {
		return dish.toString() + " category " + category;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FoodMenuItem) {
			FoodMenuItem fmi = (FoodMenuItem) o;
			return this.dish.equals(fmi.getDish());
		}
		return false;
	}

	private Dish getDish() {
		return this.dish;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	public String getDishId() {
		return dish.getDishId();
	}

	public Bitmap getImage() {

		String image = dish.getImg();
		if (image == null || image.equals("")) {
			return null;
		}

		Bitmap bitmap = null;

		try {
			bitmap = new DownloadImageTask().execute(image).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public void populateImageCache() {

		String image = dish.getImg();
		if (image == null || image.equals("")) {
			return;
		}
		// not keeping the asynctask else it wont be serializable
		// also the underlying class ImageService is singleton so no problem
		new DownloadImageTask().execute(image);

	}

	private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			// "http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png"
			return ImageService.getInstance().getImageWithCache(params[0]);
		}

	}

	public Map<MenuPropertyKey, MenuPropertyValue> getDishFilterProperties() {
		return dish.getDishProperties();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
