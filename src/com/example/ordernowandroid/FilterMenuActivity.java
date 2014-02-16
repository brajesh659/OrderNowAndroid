package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.filter.MenuPropertyKey;
import com.data.menu.filter.MenuPropertyValue;
import com.example.ordernowandroid.filter.MenuFilter;

public class FilterMenuActivity extends Activity {

    Map<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTitle("Filter");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(getResources().getColor(R.color.white));

        
        final MenuFilter menuFilter = ApplicationState.getMenuFilter((ApplicationState) getApplicationContext());
        selectedFilters = menuFilter.getFilterProperties();
        if (selectedFilters == null) {
            selectedFilters = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
        }
        Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters = menuFilter.getAvailableFilters();

        if (availableFilters != null && !availableFilters.isEmpty()) {
            for (final MenuPropertyKey filterKey : availableFilters.keySet()) {
                // Add Text
                TextView tv = new TextView(this);
                tv.setText(filterKey.toString());
                tv.setTextColor(getResources().getColor(R.color.purple));
                ll.addView(tv);
                // Add All checkbox for it
                List<MenuPropertyValue> filterKeyValues = availableFilters.get(filterKey);
                if (filterKeyValues != null && !filterKeyValues.isEmpty()) {
                    for (final MenuPropertyValue filterValue : filterKeyValues) {
                        final CheckBox ch = new CheckBox(this);
                        ch.setText(filterValue.toString());
                        ch.setTextColor(getResources().getColor(R.color.burlywood));
                        if (selectedFilters.get(filterKey) != null
                                && selectedFilters.get(filterKey).contains(filterValue)) {
                            ch.setChecked(true);
                        }
                        ch.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // ch.toggle();
                                if (ch.isChecked()) {

                                    List<MenuPropertyValue> filterValuesForFilterKey = selectedFilters.get(filterKey);
                                    if (filterValuesForFilterKey == null) {
                                        filterValuesForFilterKey = new ArrayList<MenuPropertyValue>();
                                    }
                                    filterValuesForFilterKey.add(filterValue);
                                    selectedFilters.put(filterKey, filterValuesForFilterKey);
                                } else {
                                    List<MenuPropertyValue> filterValuesForFilterKey = selectedFilters.get(filterKey);
                                    if (filterValuesForFilterKey != null) {
                                        filterValuesForFilterKey.remove(filterValue);
                                        if (filterValuesForFilterKey.isEmpty()) {
                                            selectedFilters.remove(filterKey);
                                        } else {
                                            selectedFilters.put(filterKey, filterValuesForFilterKey);
                                        }
                                    }
                                }

                            }
                        });
                        ll.addView(ch);
                    }
                }
            }
        }
        Button applyButton = new Button(this);

        applyButton.setText("Apply Filter to Menu");
        applyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuFilter.addFilter(selectedFilters);
                Toast.makeText(getApplicationContext(), "apply " + menuFilter.getFilterProperties().toString(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll.addView(applyButton);
        setContentView(ll);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
