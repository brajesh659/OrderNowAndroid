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
	private static final String PARSE_APP_ID = "vMFTELLhOo9RDRql9HpV9lKRot5xQTCCD63wkYdQ";
	private static final String PARSE_CLIENT_KEY = "mdz7n8XUjy3u0MSQRnuwmogqXZrw3qJnRwmRxx0g";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			loginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, loginFragment).commit();
		} else {
			loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
		}

		Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY);
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(getIntent());
	}

}
