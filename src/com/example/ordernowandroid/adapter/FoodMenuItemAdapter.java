package com.example.ordernowandroid.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.model.FoodMenuItem;

/**
 * 
 * @author Rohit Creating a CustomAdapter since the ArrayAdapter by default only
 *         displays the toString() implementation of the object for each list
 *         row
 */
public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> {

    private ArrayList<FoodMenuItem> foodMenuItems;

    private numListener numCallBack;


    public FoodMenuItemAdapter(Context context, ArrayList<FoodMenuItem> foodMenuItems, numListener numCallBack) {
        super(context, R.layout.food_menu_item, foodMenuItems);
        this.foodMenuItems = foodMenuItems;
        this.numCallBack = numCallBack;
    }

    /*
     * @Override public View getView(int position, View convertView, ViewGroup
     * parent) { LayoutInflater inflater = (LayoutInflater)
     * getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); View
     * rowView = inflater.inflate(R.layout.food_menu_item, parent, false);
     * 
     * TextView itemName = (TextView) rowView.findViewById(R.id.dish_name);
     * TextView itemPrice = (TextView) rowView.findViewById(R.id.dish_price);
     * ImageView itemImage = (ImageView) rowView.findViewById(id)
     * 
     * itemName.setText(foodMenuItems.get(position).getItemName());
     * itemPrice.setText
     * (Integer.toString(foodMenuItems.get(position).getItemPrice())); return
     * rowView; }
     */

    public View getView(int position, View convertView, ViewGroup parent) {
        FoodMenuItem foodItem = foodMenuItems.get(position);
        LayoutInflater l_Inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.food_menu_item, null);
            holder = new ViewHolder();
            holder.txt_itemName = (TextView) convertView.findViewById(R.id.dish_name);
            holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.dish_description);
            holder.txt_itemPrice = (TextView) convertView.findViewById(R.id.dish_price);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.dish_photo);
            holder.addItem = (Button) convertView.findViewById(R.id.addbutton);
            holder.addItem.setTag(foodItem);
            
            holder.subItem = (ImageButton) convertView.findViewById(R.id.subbutton);
            holder.subItem.setTag(foodItem);
            if (numCallBack.getQuantity(foodItem) == 0)
                holder.subItem.setVisibility(View.INVISIBLE);
            else {
                holder.addItem.setText(numCallBack.getQuantity(foodItem).toString());
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_itemName.setText(foodItem.getItemName());
        holder.txt_itemDescription.setText("item description comes here");
        holder.txt_itemPrice.setText(foodItem.getItemPrice().toString());
        holder.itemImage.setImageResource(R.drawable.bb1);

        holder.addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.incrementQuantity(foodItem);
                holder.addItem.setText(numCallBack.getQuantity(foodItem).toString());
                holder.subItem.setVisibility(View.VISIBLE);
            }
        });

        holder.subItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.decrementQuantity(foodItem);
                Integer quantity = numCallBack.getQuantity(foodItem);
                holder.addItem.setText(quantity.toString());
                if (quantity == 0) {
                    holder.subItem.setVisibility(View.INVISIBLE);
                }
            }

        });

        // imageLoader.DisplayImage("http://192.168.1.28:8082/ANDROID/images/BEVE.jpeg",
        // holder.itemImage);

        return convertView;
    }

    static class ViewHolder {
        TextView txt_itemName;
        TextView txt_itemDescription;
        TextView txt_itemPrice;
        ImageView itemImage;
        Button addItem;
        ImageButton subItem;
    }

}
