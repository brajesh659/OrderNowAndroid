package com.example.ordernowandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.data.menu.Category;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.TabsPagerAdapter;
import com.scroll.TabPageIndicator;

public class MenuFragment extends Fragment {

    private Category category;

    public MenuFragment() {
    }

    public static MenuFragment newInstance(Category category) {
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.category = category;
        return menuFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_tabs, container, false);        
        
        TabsPagerAdapter mTabPagerAdapter = new TabsPagerAdapter(getChildFragmentManager(), category);
        
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // http://stackoverflow.com/questions/10073214/viewpager-setoffscreenpagelimit0-doesnt-work-as-expected
        // mViewPager.setOffscreenPageLimit(0);

        mViewPager.setAdapter(mTabPagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        
        mTabPagerAdapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

       
    }
}