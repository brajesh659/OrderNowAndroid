package com.data.menu;

import java.util.List;


public class CategoryLevelFilter {
    private MenuPropertyKey filterName;
    private List<MenuPropertyValue> filterValue;

    public CategoryLevelFilter(MenuPropertyKey propertyName, List<MenuPropertyValue> propertyValue) {
        super();
        this.filterName = propertyName;
        this.filterValue = propertyValue;
    }

    public MenuPropertyKey getFilterName() {
        return filterName;
    }

    public List<MenuPropertyValue> getFilterValue() {
        return filterValue;
    }

}
