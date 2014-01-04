package com.example.ordernowandroid.adapter;

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
		return IndividualMenuTabFragment.newInstance(TITLES.get(index));
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
