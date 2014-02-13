package com.example.ordernowandroid.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ordernowandroid.model.FoodMenuItem;
import com.util.Utilities;

public class MenuFilter {

    private Map<MenuPropertyKey, List<MenuPropertyValue>> filterProperties;

    private Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters;

    public void setAvailableFilters(Map<MenuPropertyKey, List<MenuPropertyValue>> availablefilterProperties) {
        this.availableFilters = availablefilterProperties;
    }

    public Map<MenuPropertyKey, List<MenuPropertyValue>> getFilterProperties() {
        return filterProperties;
    }

    public Map<MenuPropertyKey, List<MenuPropertyValue>> getAvailableFilters() {
        if (availableFilters == null) {
            Map<MenuPropertyKey, List<MenuPropertyValue>> dishProperties = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
            dishProperties
                    .put(MenuPropertyKey.FoodType, Arrays.asList(MenuPropertyValue.Veg, MenuPropertyValue.NonVeg));

            dishProperties.put(MenuPropertyKey.CousineType,
                    Arrays.asList(MenuPropertyValue.NorthIndian, MenuPropertyValue.SouthIndian));
            return dishProperties;
        }
        return availableFilters;

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
            if (dishFilterValue != null && filterProperties.get(filterKey) != null
                    && !filterProperties.get(filterKey).isEmpty()
                    && !filterProperties.get(filterKey).contains(dishFilterValue)) {
                return false;
            }
        }
        return true;
    }

}
