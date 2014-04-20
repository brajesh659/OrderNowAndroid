package com.biznow.ordernow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.biznow.ordernow.R;
import com.biznow.ordernow.fragments.LoginFragment;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

public class MainActivity extends FragmentActivity {

	private LoginFragment loginFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Check for Internet Connectivity, Block Application Access if the Internet Connection is not available
		if (!isNetworkAvailable()) {
			openFailedInternetConnectionAlertDialog(savedInstanceState);
		} else {
			initiateLoginFragment(savedInstanceState);
		}

	}

	private void initiateLoginFragment(final Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			loginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, loginFragment).commit();
		} else {
			loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}
		
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	    ParseAnalytics.trackAppOpened(getIntent());
		
	}

	private boolean isNetworkAvailable() { 
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void openFailedInternetConnectionAlertDialog(final Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);            
		builder.setTitle("Internet Connection Unavailable!");
		builder.setMessage("Please check your connection and try again");
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.retry, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!isNetworkAvailable()){
					openFailedInternetConnectionAlertDialog(savedInstanceState);
				} else {
					initiateLoginFragment(savedInstanceState);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    dialog.dismiss();
                }
                return true;
            }
        });
		alert.show();
	}

}
