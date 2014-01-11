package com.example.ordernowandroid.fragments;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.ordernowandroid.R;

public class IndividualMenuTabFragment extends Fragment {
	
	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
	private String tabTitle;
	private List<String> itemList;
	
	public interface numListener {
        public void onQtyChange(String itemName, int quantity);
    }
	
	numListener numCallBack;
	
	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try {
			numCallBack = (numListener)activity;
		} catch(ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listeners!!");
        };
	};
	
	
	public static Fragment newInstance(String string, ArrayList<String> itemList) {
		
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();
		Bundle b = new Bundle();
		b.putString(TAB_TITLE, string);
		b.putStringArrayList(TAB_ITEM_LIST, itemList);
		imt.setArguments(b);
		return imt;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabTitle = getArguments().getString(TAB_TITLE);
		itemList = getArguments().getStringArrayList(TAB_ITEM_LIST);
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		ListView lv = new ListView(getActivity());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemList);
		lv.setAdapter(adapter );
		lv.setOnCreateContextMenuListener(getActivity());
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(), item,Toast.LENGTH_SHORT).show();
				//Below is needed for inflater from activity
				//LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
				
				View layout = inflater.inflate(R.layout.qty_selector, null);
				
				final NumberPicker np = (NumberPicker)layout.findViewById(R.id.numberPicker1);
				np.setMinValue(1);
				np.setMaxValue(10);
				np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
				 
				 
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setView(layout);
				builder.setCancelable(true);
				builder.setTitle("Select Number of items.");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
	            	  @Override
	            	  public void onClick(DialogInterface dialog, int which) {
	            		  numCallBack.onQtyChange(item, np.getValue());
	            		  Toast.makeText(getActivity(), item + " qty:" + np.getValue(), Toast.LENGTH_SHORT).show();
	            	  }
	            	});
				
				AlertDialog alert = builder.create();
            	alert.show();
			}
		});
		return lv;
	}
	
	

}
