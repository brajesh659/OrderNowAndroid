package com.example.ordernowandroid.fragments;

import java.util.HashMap;

import com.example.ordernowandroid.model.FoodMenuItem;

public interface AddNoteListener {

    public abstract void showNote(FoodMenuItem foodMenuItem);

    public abstract void saveNote(FoodMenuItem foodMenuItem, HashMap<String, String> metaData);

}