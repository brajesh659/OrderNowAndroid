package com.example.ordernowandroid.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.TabsPagerAdapter;

public class MenuFragment extends Fragment {

	public MenuFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.viewpager, container,
				false);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		List<String> tabs = new ArrayList<String>(Arrays.asList("Heelo","World"));
		TabsPagerAdapter tpa = new TabsPagerAdapter(getChildFragmentManager(), tabs);
		pager.setAdapter(tpa);
	}
}