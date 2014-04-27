package com.biznow.ordernow.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biznow.ordernow.AllCustomerHistoryActivity;
import com.biznow.ordernow.ApplicationState;
import com.biznow.ordernow.FoodMenuActivity;
import com.biznow.ordernow.QRCodeScannerActivity;
import com.biznow.ordernow.R;
import com.biznow.ordernow.model.OrderNowConstants;
import com.data.customers.Customer;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.parse.ParseInstallation;
import com.util.AsyncNetwork;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class LoginFragment extends Fragment {

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);
		LoginButton facebookAuthButton = (LoginButton) view.findViewById(R.id.facebook_auth_btn);
		facebookAuthButton.setFragment(this);
		return view;
	}

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        final ApplicationState applicationContext = (ApplicationState) getActivity().getApplicationContext();
        final Activity activityInstance = getActivity();
        if (state.isOpened()) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (session == Session.getActiveSession()) {
                        if (user != null) {
                            applicationContext.setUserName(user.getFirstName() + " " + user.getLastName());
                            applicationContext.setProfilePictureId(user.getId());
                            applicationContext.setFacebookId(user.getUsername());
                            applicationContext.setFirstName(user.getFirstName());
                            applicationContext.setLastName(user.getLastName());
                            applicationContext.setParseId(ParseInstallation.getCurrentInstallation().getObjectId().toString());
                            try {
                            	Gson gson = new Gson();
                            	Customer customerData = new Customer(null, user.getFirstName(), user.getLastName(), null, 
                            		ParseInstallation.getCurrentInstallation().getObjectId().toString(), user.getUsername());
                            
                            	String url = new URLBuilder().
                            		addPath(URLBuilder.Path.custInfo).
                            		addParam(URLBuilder.URLParam.custData, gson.toJson(customerData).toString())
                            		.build();
                            
                            	Utilities.info("URL: " + url);
                            	new AsyncNetwork().execute(url);
                            } catch(Exception e) {
                            	// Handle errors, will do so later.
                            }
                            
                            if (!OrderNowConstants.IS_DEBUG_MODE) {
                                Intent intent = new Intent(applicationContext, QRCodeScannerActivity.class);
                                activityInstance.startActivity(intent);
                                activityInstance.finish();
                            } else {
                                OrderNowUtilities.putKeyToSharedPreferences(applicationContext, OrderNowConstants.KEY_ACTIVE_TABLE_ID, "T1");
                                OrderNowUtilities.putKeyToSharedPreferences(applicationContext, OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID, "R1");
                                
                                Intent intent = new Intent(applicationContext, FoodMenuActivity.class);
                                activityInstance.startActivity(intent);
                                activityInstance.finish();
                            }
                        }
                    }
                    if (response.getError() != null) {
                        // Handle errors, will do so later.
                    }
                }
            });
            // Execute the Request for User Details
            request.executeAsync();

        } else if (state.isClosed()) {
            // Toast.makeText(getActivity(), "Logged out successfully",
            // Toast.LENGTH_SHORT).show();
        }
    }

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		//FIXME: Redirect to MainActivity if Result Code is Cancel
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

}
