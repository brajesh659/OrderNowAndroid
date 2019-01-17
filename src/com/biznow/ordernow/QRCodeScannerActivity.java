package com.biznow.ordernow;

import net.sourceforge.zbar.Symbol;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biznow.ordernow.model.OrderNowConstants;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.facebook.Session;
import com.util.OrderNowUtilities;
import com.util.Utilities;

public class QRCodeScannerActivity extends Activity implements OnClickListener{

    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	private String activeTableId;
	private String activeRestId;
	private String activeRestName;
	Button deliveryButton;
	Button historyButton;
	RelativeLayout qrScanLayout;
	Button dineInButton; 
	Context context;
	Button qrCodeButton;
	TextView welcomeText;
	
	DisplayMetrics metrics;
	int width;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_scanner);
		
		context = this;
		
		final ImageView backgroundImageView = (ImageView) findViewById(R.id.background);
		
		final Handler handler = new Handler();
		
		 handler.postDelayed(new Runnable(){
			 int i = 1;
	            @Override
	            public void run() {
	            	if(i == 0){
						backgroundImageView.setBackgroundResource(R.drawable.food_img5);
						i++;
						handler.postDelayed(this, 15*1000);
					} else if(i == 1){
						backgroundImageView.setBackgroundResource(R.drawable.food_img4);
						i++;
						handler.postDelayed(this, 15*1000);
					} else if(i == 2){
						backgroundImageView.setBackgroundResource(R.drawable.food_img2);
						i++;
						handler.postDelayed(this, 15*1000);
					} else if(i == 3){
						backgroundImageView.setBackgroundResource(R.drawable.food_img3);
						i = 0;
						handler.postDelayed(this, 15*1000);
					}
	            }
		 }, 15*1000);
		
		deliveryButton = (Button) findViewById(R.id.deliveryButton);
		historyButton = (Button) findViewById(R.id.history_button);
		
		dineInButton = (Button) findViewById(R.id.dine_in_button);
		qrScanLayout = (RelativeLayout) findViewById(R.id.qrscan_layout);
		
		qrCodeButton = (Button) findViewById(R.id.qrscan_btn);
		welcomeText = (TextView) findViewById(R.id.welcome_text);
		
		dineInButton.setOnClickListener(this);
		qrCodeButton.setOnClickListener(this);
		
		checkForActiveSessionAndUpdateUI();
	}

	private void checkForActiveSessionAndUpdateUI() {
		if(activeSessionPresent()) {
			
			/*welcome.setText("You are currently logged in " + activeRestName + " restaurant.");
			qrCodeButton.setText(getResources().getString(R.string.open_res_menu));
			openRestMenuButton.setVisibility(View.GONE);
			qrCodeButton.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    openRestaurantMenu(v);
                    
                }
            });
			openRestMenuButton.setVisibility(View.GONE);
			orText.setVisibility(View.GONE);*/
			
			dineStatusOpenRest(activeRestName, activeRestId, activeTableId);
			
		} else {
			/*welcome.setText(welcome.getText());
			qrCodeButton.setVisibility(View.VISIBLE);
			qrCodeButton.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    launchQRScanner(v); 
                }
            });*/
			
			dineStatusQRCode();
			
			/*if (OrderNowConstants.IS_PRODUCTION_SAMPLE_MODE) {
				openRestMenuButton.setVisibility(View.VISIBLE);
				orText.setVisibility(View.VISIBLE);
            } else {
            	openRestMenuButton.setVisibility(View.GONE);
            	orText.setVisibility(View.GONE);
            }*/
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.qr_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.signOut :
			Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				session.closeAndClearTokenInformation();
			}
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			finish();
			Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.sendAppFeedback :
			Intent Email = new Intent(Intent.ACTION_SEND);
	        Email.setType("text/email");
	        Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "ordernowinfo@gmail.com" });
	        Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback For OrderNow Android");
	        Email.putExtra(Intent.EXTRA_TEXT, "Hi ,\n" + "");
	        startActivity(Intent.createChooser(Email, "Choose an Email Client "));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private boolean activeSessionPresent() {
		String activeSession = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SESSION);
		if(activeSession != null && activeSession.trim() != "" && (Boolean.valueOf(activeSession) == true)) {
		    activeTableId = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID);
			activeRestId = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
			activeRestName = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_NAME);
			return true;
		}
		return false;
	}

	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openHistory(View v){
		Intent historyintent = new Intent(getApplicationContext(), AllCustomerHistoryActivity.class);
		startActivity(historyintent);
	}
	
	public void openDelivery(View v){
		Intent intent = new Intent(this, DeliveryRestaurantActivity.class);
        startActivity(intent);
	}

    public void openRestaurantMenu(View v) {
        if (activeTableId != null && activeTableId.trim() != "") {
            ApplicationState applicationContext = (ApplicationState) getApplicationContext();
            ApplicationState.setOpenCategoryDrawer(applicationContext, true);
            Toast.makeText(this, "Session Table Id = " + activeTableId + " Rest Id = " + activeRestId,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, FoodMenuActivity.class);
            startActivity(intent);
        }
    }
	
    public void openSampleRestaurantMenu(View v) {
        //Load all the restaurant for delivery
        Intent intent = new Intent(this, DeliveryRestaurantActivity.class);
        startActivity(intent);
        //loadRestaurantTable((ApplicationState) getApplicationContext(), SAMPLE_TABLE_ID, SAMPLE_REST_ID,true);
    }
    
    public void dineStatusOpenRest(String activeRestName, String activeRestId, String activeTableId) {
		this.activeRestId = activeRestId;
		this.activeRestName = activeRestName;
		this.activeTableId = activeTableId;
			welcomeText.setText("You are logged in " + activeRestName
					+ " restaurant.");
			qrCodeButton.setText(getResources().getString(R.string.open_res_menu));
	}

	public void dineStatusQRCode() {
		if (welcomeText != null && qrCodeButton != null) {
			welcomeText.setText(getResources().getString(R.string.welcome_text));
			qrCodeButton.setText(getResources().getString(R.string.scan_qr_codes));
		}
	}
	
	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);    
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		switch (requestCode) {
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "To be implemented", Toast.LENGTH_SHORT).show();
					String tableId_restID = data.getStringExtra(ZBarConstants.SCAN_RESULT);
					String[] scanResultStrings = tableId_restID.split(" ");
					String tableId = scanResultStrings[0];
					String restId = (scanResultStrings.length > 1)? scanResultStrings[1] : "";
					//Toast.makeText(this, "Table Id = " + tableId + " Rest Id = " + restId, Toast.LENGTH_SHORT).show();
					loadRestaurantTable(applicationContext, tableId, restId,false);
				

			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				Utilities.error("Error Message: " + error);
				Toast.makeText(this, "To be implemented", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

    private void loadRestaurantTable(ApplicationState applicationContext, String tableId, String restId, boolean sampleMenu) {

        //clean order stuff if present
        ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);
        //clean any previous session if present and start fresh
        OrderNowUtilities.sessionClean(applicationContext);

        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID, tableId);
        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID, restId);
        boolean activeSession = !sampleMenu;
        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SESSION,Boolean.toString(activeSession));
        
        ApplicationState.setOpenCategoryDrawer(applicationContext, true);

        Intent intent = new Intent(this, FoodMenuActivity.class);
        startActivity(intent);
    }

	@Override
	protected void onResume() {
		checkForActiveSessionAndUpdateUI();	
		if(qrScanLayout.getVisibility() == View.GONE){
			//System.out.println("*** active rest View is GONE");
			Drawable rightImage = context.getResources().getDrawable(R.drawable.arrow_icon_down);
			Drawable leftImage = context.getResources().getDrawable(R.drawable.ic_action_edit);
			dineInButton.setCompoundDrawablesWithIntrinsicBounds(leftImage, null, rightImage, null );
		} else if(qrScanLayout.getVisibility() == View.VISIBLE){
			//System.out.println("*** active rest View is VISIBLE");
			Drawable rightImage = context.getResources().getDrawable(R.drawable.arrow_icon_up);
			Drawable leftImage = context.getResources().getDrawable(R.drawable.ic_action_edit);
			dineInButton.setCompoundDrawables(leftImage, null, rightImage, null );
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dine_in_button:
			if(qrScanLayout.getVisibility() == View.GONE){
				Drawable rightImage = context.getResources().getDrawable(R.drawable.arrow_icon_up);
				Drawable leftImage = context.getResources().getDrawable(R.drawable.ic_action_edit);
				dineInButton.setCompoundDrawablesWithIntrinsicBounds(leftImage, null, rightImage, null);
				qrScanLayout.setVisibility(View.VISIBLE);
			} else if(qrScanLayout.getVisibility() == View.VISIBLE){
				Drawable image = context.getResources().getDrawable(R.drawable.arrow_icon_down);
				Drawable leftImage = context.getResources().getDrawable(R.drawable.ic_action_edit);
				dineInButton.setCompoundDrawablesWithIntrinsicBounds( leftImage, null, image, null );
				qrScanLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.qrscan_btn:
			if(qrCodeButton.getText().toString().equals(getResources().getString(R.string.scan_qr_codes))){
				launchQRScanner(v);
			}else if(qrCodeButton.getText().toString().equals(getResources().getString(R.string.open_res_menu))){
				openRestaurantMenu(v);
			}
			break;
		}
	}
}