package com.example.ordernowandroid.filter;

import java.util.List;
import java.util.Map;

public class AvailableMenuFilter {
    public MenuFilter availableFilters;

    public AvailableMenuFilter(Map<MenuPropertyKey, List<MenuPropertyValue>> availablefilterProperties) {
        availableFilters = new MenuFilter();
        this.availableFilters.addFilter(availablefilterProperties);
    }
  
    public Map<MenuPropertyKey, List<MenuPropertyValue>> getAvailableFilters() {
        
        return availableFilters.getFilterProperties();

    }
}
