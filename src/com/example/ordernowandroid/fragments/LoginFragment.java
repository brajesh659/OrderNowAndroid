package com.example.ordernowandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.FoodMenuActivity;
import com.example.ordernowandroid.QRCodeScannerActivity;
import com.example.ordernowandroid.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {

	public static boolean IS_DEBUG_MODE = true;
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
		LoginButton authButton = (LoginButton) view.findViewById(R.id.facebook_auth_btn);
		authButton.setFragment(this);
		//authButton.setReadPermissions(Arrays.asList("email"));
		return view;
	}

	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		final ApplicationState applicationContext = (ApplicationState) getActivity().getApplicationContext();
		if (state.isOpened()) {
			Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
				// callback after Graph API response with user object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (session == Session.getActiveSession()) {
						if (user != null) {
							applicationContext.setUserName(user.getFirstName());
							applicationContext.setProfilePictureId(user.getId());

							if (!IS_DEBUG_MODE){
								Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class); //FIXME: Found NPE on a new login
								startActivity(intent);
								getActivity().finish();
							} else {
								ApplicationState.setTableId(applicationContext, "T1"); //All the Orders would default to Table 1 in Debug Mode
								Intent intent = new Intent(getActivity(), FoodMenuActivity.class);
								startActivity(intent);				
								getActivity().finish();
							}
						}
					}
					if (response.getError() != null) {
						// Handle errors, will do so later.
					}
				}
			});
			//Execute the Request for User Details
			request.executeAsync();

		} else if (state.isClosed()) {
			Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
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
