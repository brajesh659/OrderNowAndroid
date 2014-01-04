package com.example.ordernowandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class IndividualMenuTabFragment extends Fragment {
	
	private static final String TAB_TITLE = "tabTitle";
	private String tabTitle;
	
	public static Fragment newInstance(String string) {
		
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();
		Bundle b = new Bundle();
		b.putString(TAB_TITLE, string);
		imt.setArguments(b);
		return imt;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabTitle = getArguments().getString(TAB_TITLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		ListView lv = new ListView(getActivity());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"item1","item2","item3","item4","item5","item6"});
		lv.setAdapter(adapter );
		lv.setOnCreateContextMenuListener(getActivity());
		return lv;
	}
	
	

}
