package com.example.ordernowandroid.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.util.Utilities;

public class MenuFilter implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<MenuPropertyKey, List<MenuPropertyValue>> filterProperties;
    
    public MenuFilter() {
        filterProperties = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
    }

    public Map<MenuPropertyKey, List<MenuPropertyValue>> getFilterProperties() {
        return filterProperties;
    }

    public void addFilter(Map<MenuPropertyKey, List<MenuPropertyValue>> filterProp) {
        if (filterProperties == null) {
            filterProperties = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
        }
        filterProperties.putAll(filterProp);
    }

    public boolean isItemFiltered(FoodMenuItem item) {
        Utilities.info("isItemFiltered " + filterProperties + item);
        if (filterProperties == null || filterProperties.isEmpty()) {
            return true;
        }
        Map<MenuPropertyKey, MenuPropertyValue> dishProperties = item.getDishFilterProperties();
        for (MenuPropertyKey filterKey : filterProperties.keySet()) {
            MenuPropertyValue dishFilterValue = dishProperties.get(filterKey);
            if ((dishFilterValue != null && filterProperties.get(filterKey) != null
                    && !filterProperties.get(filterKey).isEmpty()
                    && !filterProperties.get(filterKey).contains(dishFilterValue))
                    && !filterProperties.get(filterKey).contains(MenuPropertyValue.All)) {
                return false;
            }
        }
        return true;
    }

}
