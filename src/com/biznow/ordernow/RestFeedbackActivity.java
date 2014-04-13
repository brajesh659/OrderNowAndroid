package com.biznow.ordernow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biznow.ordernow.R;
import com.data.menu.RestaurantFeedback;
import com.google.gson.Gson;
import com.util.AsyncNetwork;
import com.util.URLBuilder;
import com.util.URLBuilder.Path;
import com.util.URLBuilder.URLAction;
import com.util.URLBuilder.URLParam;

public class RestFeedbackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Restaurant Feedback");
		setContentView(R.layout.restaurant_rate_page);
		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		final String orderId = applicationContext.getActiveOrderId();
		final String restaurantName = applicationContext.getRestaurantName();

		TextView resName = (TextView) findViewById(R.id.restName);
		final RatingBar ratingBar = (RatingBar) findViewById(R.id.restRatingBar);
		final EditText feedbackText = (EditText) findViewById(R.id.restFeedbackEditText);
		Button feedbackSubmit = (Button) findViewById(R.id.restFeedbackSubmit);

		resName.setText(restaurantName);
		feedbackSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String feedText = feedbackText.getText().toString();
				float rating = ratingBar.getRating();

				RestaurantFeedback restFeedback = new RestaurantFeedback(
						rating, feedText);
				Gson gs = new Gson();
				String feedback = gs.toJson(restFeedback);
				String url = new URLBuilder().addPath(Path.serveTable)
						.addAction(URLAction.feedbackSubmit)
						.addParam(URLParam.orderId, orderId)
						.addParam(URLParam.feedback, feedback).build();

				new AsyncNetwork().execute(url);
				Toast.makeText(getApplicationContext(),
						"Thank you for visting " + restaurantName + "!", Toast.LENGTH_LONG).show();

				Intent intent = new Intent(getApplicationContext(), QRCodeScannerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});

	}
	
	@Override
	public void onBackPressed() {
		//Back Button Disabled on Feedback Page 
	}

}
