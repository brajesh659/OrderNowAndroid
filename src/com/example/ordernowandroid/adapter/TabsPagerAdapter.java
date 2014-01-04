package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

	private final List<String> TITLES;
	public TabsPagerAdapter(FragmentManager fm, List<String> tabs) {
		super(fm);
		TITLES = tabs;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int index) {
		return IndividualMenuTabFragment.newInstance(TITLES.get(index), new ArrayList<String>(Arrays.asList("item1","item2","item3","item4","item5","item6")));
		//return null;
		
	}

	@Override
	public int getCount() {
		if(TITLES !=  null ) {
			return TITLES.size();
		}
		return 0;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return TITLES.get(position);
	}

}
