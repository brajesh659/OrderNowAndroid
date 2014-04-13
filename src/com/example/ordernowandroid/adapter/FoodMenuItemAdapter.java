package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;
import com.example.ordernowandroid.IngredientsActivity;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.fragments.AddNoteListener;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.google.gson.Gson;
import com.util.Utilities;

public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> implements Filterable {

    private ArrayList<FoodMenuItem> foodMenuItems;
    private ArrayList<FoodMenuItem> allfoodMenuItems;

    private numListener numCallBack;
    private AddNoteListener addNoteListener;
    private ModelFilter filter;
    
    private Context context;

    public FoodMenuItemAdapter(Context context, ArrayList<FoodMenuItem> foodMenuItems, numListener numCallBack, AddNoteListener addNoteListener) {
        super(context, R.layout.food_menu_item, foodMenuItems);
        this.context = context;
        this.addNoteListener = addNoteListener;
        allfoodMenuItems = new ArrayList<FoodMenuItem>();
        allfoodMenuItems.addAll(foodMenuItems);
        this.foodMenuItems = foodMenuItems;
        this.numCallBack = numCallBack;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final FoodMenuItem foodItem = foodMenuItems.get(position);
        LayoutInflater l_Inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.food_menu_item, null);
            holder = new ViewHolder();
            holder.txt_itemName = (TextView) convertView.findViewById(R.id.dish_name);
            holder.txt_itemCategory = (TextView) convertView.findViewById(R.id.dish_category);
            holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.dish_description);
            holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.dish_price);
            holder.txt_itemQuantity = (TextView) convertView.findViewById(R.id.dish_quantity);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.dish_photo);
            holder.catImage = (ImageView) convertView.findViewById(R.id.dish_cat_image);
            holder.addItem = (ImageButton) convertView.findViewById(R.id.addbutton);
            holder.subItem = (ImageButton) convertView.findViewById(R.id.subbutton);
            holder.addNote = (ImageButton) convertView.findViewById(R.id.addnote);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.addItem.setTag(foodItem);
        holder.subItem.setTag(foodItem);
        holder.addNote.setTag(foodItem);
        holder.txt_itemName.setTag(foodItem);
        holder.txt_itemDescription.setTag(foodItem);
        holder.txt_itemPrice.setTag(foodItem);
        holder.itemImage.setTag(foodItem);
        
        holder.txt_itemName.setText(foodItem.getItemName());
        holder.txt_itemDescription.setText(foodItem.getDescription());
        holder.txt_itemPrice.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + foodItem.getItemPrice().toString());

        if(foodItem.getCategory()!= null && !foodItem.getCategory().isEmpty()) {
        	holder.txt_itemCategory.setText(foodItem.getCategory());
        	holder.txt_itemCategory.setVisibility(View.VISIBLE);
        } else {
        	holder.txt_itemCategory.setVisibility(View.INVISIBLE);
        }
        
        Utilities.info("FoodMenuItemAdapter" + foodItem.getItemName());
        MenuPropertyValue foodTypeValue = foodItem.getDishFilterProperties().get(MenuPropertyKey.FoodType);
        if (foodTypeValue != null && foodTypeValue.equals(MenuPropertyValue.Veg)) {
            holder.catImage.setVisibility(View.VISIBLE);
            holder.catImage.setImageResource(R.drawable.veg);
        } else if (foodTypeValue != null && foodTypeValue.equals(MenuPropertyValue.NonVeg)) {
            holder.catImage.setVisibility(View.VISIBLE);
            holder.catImage.setImageResource(R.drawable.non_veg);
        } else {
            holder.catImage.setVisibility(View.INVISIBLE);
        }
        Bitmap bitmap = foodItem.getImage();
        if (bitmap == null) {
            holder.itemImage.setImageResource(R.drawable.default_food_icon);
        } else {
            holder.itemImage.setImageBitmap(bitmap);
        }

        View.OnClickListener addOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.incrementQuantity(foodItem);
                holder.subItem.setVisibility(View.VISIBLE);
                holder.addNote.setVisibility(View.VISIBLE);
                final Float quantity = numCallBack.getQuantity(foodItem);
                if ((quantity - quantity.intValue()) == 0)
                    holder.txt_itemQuantity.setText(quantity.intValue() + "");
                else
                    holder.txt_itemQuantity.setText(quantity + "");
                holder.itemImage.setAlpha(0.3f);
            }
        };
        holder.addItem.setOnClickListener(addOnClickListener);
        holder.itemImage.setOnClickListener(addOnClickListener);
        holder.txt_itemDescription.setOnClickListener(addOnClickListener);
        holder.txt_itemName.setOnClickListener(addOnClickListener);
        holder.txt_itemPrice.setOnClickListener(addOnClickListener);
        
        holder.subItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.decrementQuantity(foodItem);
                Float quantity = numCallBack.getQuantity(foodItem);
                if (quantity - quantity.intValue() == 0)
                    holder.txt_itemQuantity.setText(quantity.intValue() + "");
                else
                    holder.txt_itemQuantity.setText(quantity + "");
                if (quantity == 0) {
                    holder.subItem.setVisibility(View.INVISIBLE);
                    holder.addNote.setVisibility(View.INVISIBLE);
                    holder.txt_itemQuantity.setText("");
                    holder.itemImage.setAlpha(1f);
                }
            }

        });
       
        holder.addNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                addNoteListener.showNote(foodItem);
            }
        });


        if(!foodItem.isAvailable()) {
            holder.itemImage.setImageResource(R.drawable.not_available);
            hideButtonsFromFoodItem(holder);
            View.OnClickListener ingredientOnClickListener = new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    Toast.makeText(context.getApplicationContext(), "This item is currently unavailable", Toast.LENGTH_SHORT).show();             
                }
            };
            holder.itemImage.setOnClickListener(ingredientOnClickListener);
            holder.txt_itemDescription.setOnClickListener(ingredientOnClickListener);
            holder.txt_itemDescription.setText("Currently Unavailable");
            holder.txt_itemName.setOnClickListener(ingredientOnClickListener);
            holder.txt_itemPrice.setOnClickListener(ingredientOnClickListener);
            
            holder.txt_itemName.setTextColor(context.getResources().getColor(R.color.gray));
            holder.txt_itemPrice.setTextColor(context.getResources().getColor(R.color.gray));
        } else {
            //TODO without this first item view is screwed, look is it the right approach
            holder.addItem.setVisibility(View.VISIBLE);
            holder.txt_itemName.setTextColor(context.getResources().getColor(R.color.itemNameColor));
            holder.txt_itemPrice.setTextColor(context.getResources().getColor(R.color.itemPriceColor));
        }
        

        // imageLoader.DisplayImage("http://192.168.1.28:8082/ANDROID/images/BEVE.jpeg",
        // holder.itemImage);

        if(foodItem.isItemCustomizable()) {
        	holder.itemImage.setImageResource(R.drawable.ic_category);
        	hideButtonsFromFoodItem(holder);
        	View.OnClickListener ingredientOnClickListener = new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context, IngredientsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(IngredientsActivity.DISH_NAME, holder.txt_itemName.getText());
					intent.putExtra(IngredientsActivity.FOOD_ITEM, foodItem);
					context.startActivity(intent);				
					
				}
			};
			holder.itemImage.setOnClickListener(ingredientOnClickListener);
			holder.txt_itemDescription.setOnClickListener(ingredientOnClickListener);
			holder.txt_itemName.setOnClickListener(ingredientOnClickListener);
			holder.txt_itemPrice.setOnClickListener(ingredientOnClickListener);
			
        }
        
        if (numCallBack.getQuantity(foodItem) == 0) {
            holder.subItem.setVisibility(View.INVISIBLE);
            holder.addNote.setVisibility(View.INVISIBLE);
            holder.txt_itemQuantity.setText("");
            holder.itemImage.setAlpha(1f);
        } else {
            final Float quantity = numCallBack.getQuantity(foodItem);
            if (quantity - quantity.intValue() == 0)
                holder.txt_itemQuantity.setText(quantity.intValue() + "");
            else
                holder.txt_itemQuantity.setText(quantity + "");

            holder.subItem.setVisibility(View.VISIBLE);
            holder.addNote.setVisibility(View.VISIBLE);
            holder.itemImage.setAlpha(0.3f);
        }
        
        return convertView;
    }

    private void hideButtonsFromFoodItem(final ViewHolder holder) {
        holder.addItem.setVisibility(View.INVISIBLE);
        holder.subItem.setVisibility(View.INVISIBLE);
        holder.addNote.setVisibility(View.INVISIBLE);
    }
    
    static class ViewHolder {
        TextView txt_itemName;
        TextView txt_itemCategory;
        TextView txt_itemDescription;
        TextView txt_itemPrice;
        TextView txt_itemQuantity;
        ImageView itemImage;
        ImageView catImage;
        ImageButton addItem;
        ImageButton subItem;
        ImageButton addNote;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    private class ModelFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<FoodMenuItem> filteredItemList = new ArrayList<FoodMenuItem>();
            MenuFilter filter = null ;
            if (constraint != null && !constraint.equals("")) {
                Gson gs = new Gson();
                filter = gs.fromJson(constraint.toString(), MenuFilter.class);
            }
            
            if(filter!=null && filter.getFilterProperties() !=null && !filter.getFilterProperties().isEmpty()) {
                for (FoodMenuItem foodItem : allfoodMenuItems) {
                    if(filter.isItemFiltered(foodItem)) {
                        filteredItemList.add(foodItem);
                    }
                }
            } else {
                filteredItemList.addAll(allfoodMenuItems);
            }

            filterResults.count = filteredItemList.size();
            filterResults.values = filteredItemList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<FoodMenuItem> filteredItemList = (List<FoodMenuItem>) results.values;
            foodMenuItems.clear();
            foodMenuItems.addAll(filteredItemList);
            notifyDataSetChanged();

        }

    }

}
