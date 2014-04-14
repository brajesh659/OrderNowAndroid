package com.data.menu;

public class RestaurantWrapper {
    Restaurant restaurant;
    boolean isUpdated;
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public boolean isUpdated() {
        return isUpdated;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void setUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
}
