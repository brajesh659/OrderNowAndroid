package com.example.ordernowandroid.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.FoodMenuItemAdapter;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.model.FoodMenuItem;

public class IndividualMenuTabFragment extends Fragment  {

	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
    
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
    private MenuFilter menuFilter;   

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

	public static Fragment newInstance(String categoryName, ArrayList<FoodMenuItem> foodMenuItem) {	
	    if(foodMenuItem == null||foodMenuItem.size()==0) {
	        return new NullDishesFragment();
	    }
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();		
		Bundle b = new Bundle();
		b.putString(TAB_TITLE, categoryName);
		b.putSerializable(TAB_ITEM_LIST, foodMenuItem);		
		imt.setArguments(b);		
		return imt;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setHasOptionsMenu(true);
		//getActivity().invalidateOptionsMenu();
		
        tabTitle = getArguments().getString(TAB_TITLE);
        foodMenuItemList = (ArrayList<FoodMenuItem>) getArguments().getSerializable(TAB_ITEM_LIST);

	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View foodCategoryView = inflater.inflate(R.layout.category_page, null);	
        this.foodMenuItemAdapter = new FoodMenuItemAdapter(getActivity().getApplicationContext(), foodMenuItemList,
                numCallBack, addNoteListener);
        ListView lv = (ListView) foodCategoryView.findViewById(R.id.dish_list);
		lv.setAdapter(foodMenuItemAdapter);
		lv.setOnCreateContextMenuListener(getActivity());
		//check if filter is present
		this.menuFilter = ApplicationState.getMenuFilter((ApplicationState)getActivity().getApplicationContext());
        if (menuFilter.getFilterProperties() != null && !menuFilter.getFilterProperties().isEmpty()) {
            this.foodMenuItemAdapter.getFilter().filter("");
        }
		return foodCategoryView;
	}


}
