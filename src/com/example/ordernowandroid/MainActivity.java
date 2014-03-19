package com.example.ordernowandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.ordernowandroid.fragments.LoginFragment;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends FragmentActivity {

	private LoginFragment loginFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			loginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, loginFragment).commit();
		} else {
			loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}

		Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(getIntent());
	}

}
