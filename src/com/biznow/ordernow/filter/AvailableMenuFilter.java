package com.biznow.ordernow.filter;

import java.util.List;
import java.util.Map;

import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;

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
