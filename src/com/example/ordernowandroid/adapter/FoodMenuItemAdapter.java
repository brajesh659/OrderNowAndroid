package com.example.ordernowandroid.adapter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.data.menu.FoodType;
import com.example.ordernowandroid.FoodMenuActivity;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.model.FoodMenuItem;

/**
 * 
 * @author Rohit Creating a CustomAdapter since the ArrayAdapter by default only
 *         displays the toString() implementation of the object for each list
 *         row
 */
public class FoodMenuItemAdapter extends ArrayAdapter<FoodMenuItem> implements Filterable {

    private ArrayList<FoodMenuItem> foodMenuItems;
    private ArrayList<FoodMenuItem> allfoodMenuItems;

    private numListener numCallBack;
	private ModelFilter filter;


    public FoodMenuItemAdapter(Context context, ArrayList<FoodMenuItem> foodMenuItems, numListener numCallBack) {
        super(context, R.layout.food_menu_item, foodMenuItems);
        allfoodMenuItems = new ArrayList<FoodMenuItem>();
        allfoodMenuItems.addAll(foodMenuItems);
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
            holder.txt_itemQuantity = (TextView) convertView.findViewById(R.id.dish_quantity);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.dish_photo);
            holder.addItem = (ImageButton) convertView.findViewById(R.id.addbutton);       
            holder.subItem = (ImageButton) convertView.findViewById(R.id.subbutton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.addItem.setTag(foodItem);
        holder.subItem.setTag(foodItem);
        holder.txt_itemName.setText(foodItem.getItemName());
        holder.txt_itemDescription.setText("item description comes here");
        holder.txt_itemPrice.setText(foodItem.getItemPrice().toString());
    
        URL abc = null;
        try {
            abc = new URL("http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(FoodMenuActivity.class.getName(),e.getLocalizedMessage()+" error ");
        }
        Bitmap bitmap = null;
        try {
            bitmap = new DownloadImageTask().execute(abc).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            holder.itemImage.setImageBitmap(bitmap);

        //holder.itemImage.setImageResource(R.drawable.bb1);
		if (numCallBack.getQuantity(foodItem) == 0) {
			holder.subItem.setVisibility(View.INVISIBLE);
			holder.txt_itemQuantity.setText("");
			holder.itemImage.setAlpha(1f);
		}
		else {
			holder.txt_itemQuantity.setText(numCallBack.getQuantity(foodItem).toString());
			holder.subItem.setVisibility(View.VISIBLE);
			holder.itemImage.setAlpha(0.3f);
		}

        holder.addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.incrementQuantity(foodItem);
                holder.subItem.setVisibility(View.VISIBLE);
                holder.txt_itemQuantity.setText(numCallBack.getQuantity(foodItem).toString());
                holder.itemImage.setAlpha(0.3f);                
            }
        });

        holder.subItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodMenuItem foodItem = (FoodMenuItem) v.getTag();
                numCallBack.decrementQuantity(foodItem);
                Integer quantity = numCallBack.getQuantity(foodItem);
                holder.txt_itemQuantity.setText(numCallBack.getQuantity(foodItem).toString());
                if (quantity == 0) {
                    holder.subItem.setVisibility(View.INVISIBLE);
                    holder.txt_itemQuantity.setText("");
                    holder.itemImage.setAlpha(1f);
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
        TextView txt_itemQuantity;
        ImageView itemImage;
        ImageButton addItem;
        ImageButton subItem;
    }
    
    
    @Override
    public Filter getFilter() {
    	if(filter == null) {
    		filter = new ModelFilter();
    	}
    	return filter;
    }
    
    private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            Bitmap bitmap= null;
            try {
                URL url = new URL("http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png");
                //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient
                        .execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                bitmap = BitmapFactory.decodeStream(input);

            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("FoodMenuItemAdapter", ex.getMessage()+"got exception");
            }
            return bitmap;
        }
        
    }
    
    private class ModelFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			List<FoodMenuItem> filteredItemList = new ArrayList<FoodMenuItem>();
			if(constraint.equals(FoodType.Veg.toString())) {
				for(FoodMenuItem foodItem : allfoodMenuItems) {
					if(foodItem.getFoodType().equals(FoodType.Veg)) {
						filteredItemList.add(foodItem);
					}
				}
			} else if(constraint.equals(FoodType.NonVeg.toString())) {
				for(FoodMenuItem foodItem : allfoodMenuItems) {
					if(foodItem.getFoodType().equals(FoodType.NonVeg)) {
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
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			List<FoodMenuItem> filteredItemList = (List<FoodMenuItem>) results.values;
			foodMenuItems.clear();
			foodMenuItems.addAll(filteredItemList);
			notifyDataSetChanged();
			
		}
    	
    }

}
