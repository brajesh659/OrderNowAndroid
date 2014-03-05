package com.example.ordernowandroid.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.FoodMenuItemAdapter;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.filter.MenuPropertyKey;
import com.example.ordernowandroid.filter.MenuPropertyValue;
import com.example.ordernowandroid.model.FoodMenuItem;

public class IndividualMenuTabFragment extends Fragment {

	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
    private static final String FILTER_CONTENT = "filterContent";
    
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
    private HashMap<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters;   

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

	public static Fragment newInstance(String categoryName, ArrayList<FoodMenuItem> foodMenuItem, HashMap<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters) {	
	    if(foodMenuItem == null||foodMenuItem.size()==0) {
	        return new NullDishesFragment();
	    }
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();		
		imt.tabTitle = categoryName;
		imt.foodMenuItemList = foodMenuItem;
		imt.selectedFilters = selectedFilters;
		return imt;
	}
	
		
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FILTER_CONTENT)) {
                selectedFilters = (HashMap<MenuPropertyKey, List<MenuPropertyValue>>) savedInstanceState.getSerializable(FILTER_CONTENT);
            }
            if (savedInstanceState.containsKey(TAB_TITLE)) {
                tabTitle = savedInstanceState.getString(TAB_TITLE);
            }
            if (savedInstanceState.containsKey(TAB_ITEM_LIST)) {
                foodMenuItemList = (ArrayList<FoodMenuItem>) savedInstanceState.getSerializable(TAB_ITEM_LIST);
            }
        }

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
		
		// TODO govind: Need refactoring to avoid race condition. 
        // PageAdapter automatically loads next page to current selected page and as filters are defined globally
        // so there is race condition when in ALL filter, instead of displaying ALL items it applies Veg 
        // filter of some of the dishes. 
		
		MenuFilter menuFilter = ApplicationState.getMenuFilter((ApplicationState)getActivity().getApplicationContext());
        if (selectedFilters != null) {
            menuFilter.addFilter(selectedFilters);
        }
        Map<MenuPropertyKey, List<MenuPropertyValue>> filterProperties = menuFilter.getFilterProperties();
        if (filterProperties != null && !filterProperties.isEmpty()) {
            this.foodMenuItemAdapter.getFilter().filter("");
        }

		return foodCategoryView;
	}

	
	 @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putSerializable(FILTER_CONTENT, selectedFilters);
	        outState.putString(TAB_TITLE, tabTitle);
	        outState.putSerializable(TAB_ITEM_LIST, foodMenuItemList);
	    }

}
