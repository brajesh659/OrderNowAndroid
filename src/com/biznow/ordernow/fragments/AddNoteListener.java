package com.biznow.ordernow.fragments;

import java.util.HashMap;

import com.biznow.ordernow.model.FoodMenuItem;

public interface AddNoteListener {

    public abstract void showNote(FoodMenuItem foodMenuItem);

    public abstract void saveNote(FoodMenuItem foodMenuItem, HashMap<String, String> metaData);

}