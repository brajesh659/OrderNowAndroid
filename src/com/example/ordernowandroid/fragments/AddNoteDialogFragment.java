package com.example.ordernowandroid.fragments;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;

public class AddNoteDialogFragment extends DialogFragment {

    private static final String TEXT_COMMENT = "TextComment";
    public static final String SPICE_LEVEL = "SpiceLevel";
    private static final String ITEM = "Item";
    private static final String ORDER_ITEM = "OrderItem";
    AddNoteListener numCallBack;
    private FoodMenuItem foodMenuItem;
    private static HashMap<String, Integer> spiceLevels = new HashMap<String, Integer>();
    {
        spiceLevels.put("Low", R.id.low);
        spiceLevels.put("Medium", R.id.meduim);
        spiceLevels.put("High", R.id.high);
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try {
            numCallBack = (AddNoteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Listeners!!");
        }
        ;
    };

    public static AddNoteDialogFragment newInstance(FoodMenuItem foodMenuItem, MyOrderItem myOrderItem) {
        AddNoteDialogFragment imt = new AddNoteDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(ITEM, foodMenuItem);
        b.putSerializable(ORDER_ITEM, myOrderItem);
        imt.setArguments(b);
        return imt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        foodMenuItem = (FoodMenuItem) getArguments().getSerializable(ITEM);
        MyOrderItem myOrderItem = (MyOrderItem) getArguments().getSerializable(ORDER_ITEM);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.add_note_dialog, null);
        builder.setView(view);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.spiceLevel);
        final EditText editText = (EditText) view.findViewById(R.id.notes);
        final HashMap<String, String> metaData = myOrderItem.getMetaData();
        if (metaData != null) {
            editText.setText(metaData.get(TEXT_COMMENT));
            RadioButton findViewById = (RadioButton) view.findViewById(spiceLevels.get(metaData.get(SPICE_LEVEL)));
            findViewById.setChecked(true);
        }
        // Add action buttons

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(checkedRadioButtonId);
                HashMap<String, String> metaData = new HashMap<String, String>();
                metaData.put(SPICE_LEVEL, radioButton.getText().toString());
                metaData.put(TEXT_COMMENT, editText.getText().toString());
                numCallBack.saveNote(foodMenuItem, metaData);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // LoginDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();

    }

}
