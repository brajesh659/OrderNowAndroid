package com.example.ordernowandroid.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
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
import com.example.ordernowandroid.filter.MenuPropertyKey;
import com.example.ordernowandroid.filter.MenuPropertyValue;
import com.example.ordernowandroid.model.FoodMenuItem;

public class IndividualMenuTabFragment extends Fragment implements TabListener  {

	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
    
	private String tabTitle;
	private ArrayList<FoodMenuItem> foodMenuItemList;
	private FoodMenuItemAdapter foodMenuItemAdapter;
	private LinkedHashMap<MenuPropertyValue, Integer> actionBarTagTitles;
	private boolean tabChange = false;

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
		super.onCreate(savedInstanceState);
		
        tabTitle = getArguments().getString(TAB_TITLE);
        foodMenuItemList = (ArrayList<FoodMenuItem>) getArguments().getSerializable(TAB_ITEM_LIST);
        ActionBar actionBar = getActivity().getActionBar();
        
        //delete all because this listener is this particular instance
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        actionBarTagTitles = new LinkedHashMap<MenuPropertyValue, Integer>();
        actionBarTagTitles.put(MenuPropertyValue.All, R.string.all);
        actionBarTagTitles.put(MenuPropertyValue.Veg, R.string.veg);
        actionBarTagTitles.put(MenuPropertyValue.NonVeg, R.string.nonveg);
        for (Entry<MenuPropertyValue, Integer> actionBarTagTitle : actionBarTagTitles.entrySet()) {
            actionBar.addTab(actionBar.newTab().setTag(actionBarTagTitle.getKey()).setText(actionBarTagTitle.getValue()).setTabListener(this));
        }

        MenuFilter menuFilter = ApplicationState.getMenuFilter((ApplicationState)getActivity().getApplicationContext());
        Map<MenuPropertyKey, List<MenuPropertyValue>> filterProperties = menuFilter.getFilterProperties();
        tabChange = true;
        if(filterProperties != null) {
        if(filterProperties.get(MenuPropertyKey.FoodType).contains(MenuPropertyValue.Veg))
            actionBar.selectTab(actionBar.getTabAt(1));
        else if (filterProperties.get(MenuPropertyKey.FoodType).contains(MenuPropertyValue.NonVeg))
            actionBar.selectTab(actionBar.getTabAt(2));
        else if (filterProperties.get(MenuPropertyKey.FoodType).contains(MenuPropertyValue.All))
            actionBar.selectTab(actionBar.getTabAt(0));
        } else {
            actionBar.selectTab(actionBar.getTabAt(0));
        }
        

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
        Map<MenuPropertyKey, List<MenuPropertyValue>> filterProperties = menuFilter.getFilterProperties();
        if (filterProperties != null && !filterProperties.isEmpty()) {
            this.foodMenuItemAdapter.getFilter().filter("");
        }

		return foodCategoryView;
	}
	

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction arg1) {
        // initial value of tab change is false and it won't display anything. It is expected that onCreate function will 
        // be invoked after this which will make this flag true and choose correct tab as per selected filters. 
        if (tabChange) {
            MenuFilter menuFilter = ApplicationState.getMenuFilter((ApplicationState) getActivity().getApplicationContext());
            List<MenuPropertyValue> value = new ArrayList<MenuPropertyValue>();
            MenuPropertyValue tabType = (MenuPropertyValue) tab.getTag();
            value.add(tabType);

            Map<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters = menuFilter.getFilterProperties();
            if (selectedFilters == null) {
                selectedFilters = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
            }
            selectedFilters.put(MenuPropertyKey.FoodType, value);

            menuFilter.addFilter(selectedFilters);

            if (foodMenuItemAdapter != null) {
                this.foodMenuItemAdapter.getFilter().filter("");
            }
        }

    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }


}
