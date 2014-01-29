package com.example.ordernowandroid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.ordernowandroid.MyOrderActivity;
import com.example.ordernowandroid.R;

//Refactor to use it at Multiple Places later. One could pass the title, message, view and create a custom dialog fragment 
public class ConfirmOrderDialogFragment extends DialogFragment {

	private static final String ALERT_DIALOG_TITLE = "Confirm Order";
	private static final String ALERT_DIALOG_MESSAGE = "Are you sure you want to confirm the order ?";

	/*public static ConfirmOrderDialogFragment newInstance() {
		ConfirmOrderDialogFragment frag = new ConfirmOrderDialogFragment();
		Bundle b = new Bundle();
		frag.setArguments(b);
		return frag;
	}
	 */

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.confirm_order_dialog, null);		
		final EditText confirmOrderEditText = (EditText) view.findViewById(R.id.confirmOrderEditText);  

		return new AlertDialog.Builder(getActivity())
		.setTitle(ALERT_DIALOG_TITLE)
		.setMessage(ALERT_DIALOG_MESSAGE)
		.setView(view)
		.setPositiveButton(R.string.ok, new OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String orderNote = confirmOrderEditText.getText().toString();
				((MyOrderActivity)getActivity()).doPositiveClick(orderNote);
			}
		}
				)
				.setNegativeButton(R.string.cancel, new OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((MyOrderActivity)getActivity()).doNegativeClick();
					}
				}
						)
						.create();
	}

}

