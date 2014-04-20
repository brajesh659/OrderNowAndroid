package com.biznow.ordernow.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.biznow.ordernow.ApplicationState;
import com.biznow.ordernow.R;
import com.biznow.ordernow.adapter.FoodMenuItemAdapter;
import com.biznow.ordernow.filter.MenuFilter;
import com.biznow.ordernow.model.FoodMenuItem;
import com.google.gson.Gson;

public class IndividualMenuTabFragment extends Fragment {

	private String tabTitle;
	private ArrayList<FoodMenuItem> foodMenuItemList;
	private FoodMenuItemAdapter foodMenuItemAdapter;

	public interface numListener {
		
		public float getQuantity(FoodMenuItem foodMenuItem);
		
		public void incrementQuantity(FoodMenuItem foodMenuItem);
		
		public void decrementQuantity(FoodMenuItem foodMenuItem);
	}

	numListener numCallBack;	
	AddNoteListener addNoteListener;
	private MenuFilter menuFilterLocal = new MenuFilter();
//    private HashMap<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters;   

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try {
            numCallBack = (numListener) activity;
            addNoteListener = (AddNoteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Listeners!!");
        };
    };

	public static Fragment newInstance(String categoryName, ArrayList<FoodMenuItem> foodMenuItem, MenuFilter selectedFilters) {	
	    if(foodMenuItem == null||foodMenuItem.size()==0) {
	        return new NullDishesFragment();
	    }
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();		
		imt.tabTitle = categoryName;
		imt.foodMenuItemList = foodMenuItem;
		imt.menuFilterLocal = selectedFilters;
		return imt;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    Log.i("IndividualMenu view", "view "+tabTitle);
		View foodCategoryView = inflater.inflate(R.layout.category_page, null);	
        this.foodMenuItemAdapter = new FoodMenuItemAdapter(getActivity().getApplicationContext(), foodMenuItemList,
                numCallBack, addNoteListener);
        ListView lv = (ListView) foodCategoryView.findViewById(R.id.dish_list);
		lv.setAdapter(foodMenuItemAdapter);
		lv.setOnCreateContextMenuListener(getActivity());
		//check if filter is present
		
		MenuFilter menuFilter = ApplicationState.getMenuFilter((ApplicationState)getActivity().getApplicationContext());
		String filter = "";

		if (menuFilterLocal != null) {
            if (menuFilter != null && menuFilter.getFilterProperties() != null) {
                menuFilterLocal.addFilter(menuFilter.getFilterProperties());
            }
            Gson gs = new Gson();
            filter = gs.toJson(menuFilterLocal);
            Log.i("IndividualMenu","filterValue = " + filter);
        }

		if (filter != null && !filter.isEmpty()) {
            this.foodMenuItemAdapter.getFilter().filter(filter);
        }

		return foodCategoryView;
	}

}
