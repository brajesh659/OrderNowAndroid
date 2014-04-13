package com.biznow.ordernow.model;

import com.data.menu.Category;

public class CategoryNavDrawerItem {
    
    private Category category;
    // boolean to set visiblity of the counter
     
    public CategoryNavDrawerItem(Category title){
        this.category = title;
    }
     
    public String getTitle(){
        return this.category.getName();
    }

}