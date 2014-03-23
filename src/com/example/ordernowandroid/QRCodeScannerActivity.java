package com.example.ordernowandroid;

import java.util.ArrayList;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.CustomerOrderWrapper;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.facebook.widget.ProfilePictureView;
import com.util.OrderNowUtilities;
import com.util.Utilities;

public class QRCodeScannerActivity extends Activity {

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	private ProfilePictureView profilePictureView;
	private String activeTableId;
	private String activeRestId;
	private String activeRestName;
	private ArrayList<CustomerOrderWrapper> activeSubOrderList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_scanner);

		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		TextView welcome = (TextView) findViewById(R.id.welcome_text);
		TextView custName = (TextView) findViewById(R.id.selection_profile_name);
		Button qrCodeButton = (Button) findViewById(R.id.qrscan_btn);
		Button openRestMenuButton = (Button) findViewById(R.id.open_res_menu);
		if(applicationContext.getUserName() != null && applicationContext.getUserName().trim() != "") {
			custName.setText(applicationContext.getUserName());
		}
		if(activeSessionPresent()) {
			welcome.setText(" You are currently logged in " + activeRestName + " restaurant.");
			qrCodeButton.setVisibility(View.GONE);
			openRestMenuButton.setVisibility(View.VISIBLE);
		} else {
			welcome.setText( welcome.getText());
			qrCodeButton.setVisibility(View.VISIBLE);
			openRestMenuButton.setVisibility(View.GONE);
		}
		
		if(applicationContext.getProfilePictureId() != null) {
			profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
			profilePictureView.setProfileId(applicationContext.getProfilePictureId());
		}
		
	}

	private boolean activeSessionPresent() {
		activeTableId = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID);
		if(activeTableId != null && activeTableId.trim() != "") {
			activeRestId = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
			activeRestName = OrderNowUtilities.getKeyFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_NAME);
			activeSubOrderList = OrderNowUtilities.getObjectFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
			return true;
		}
		return false;
	}

	public void launchQRScanner(View v) {        
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}    	        
	}
	
	public void openRestaurantMenu(View v) {
		if(activeTableId != null && activeTableId.trim() != "") {
			ApplicationState applicationContext = (ApplicationState) getApplicationContext();
			ApplicationState.setTableId(applicationContext, activeTableId);
			ApplicationState.setRestaurantId(applicationContext, activeRestId);
			ApplicationState.setSubOrderList(applicationContext, activeSubOrderList);
			ApplicationState.setOpenCategoryDrawer(applicationContext, true);
			Toast.makeText(this, "Session Table Id = " + activeTableId + " Rest Id = " + activeRestId, Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(this, FoodMenuActivity.class);
			startActivity(intent);				
			finish();				
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
				String tableId_restID = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				String[] scanResultStrings = tableId_restID.split(" ");
				String tableId = scanResultStrings[0];
				String restId = (scanResultStrings.length > 1)? scanResultStrings[1] : "";
				Toast.makeText(this, "Table Id = " + tableId + " Rest Id = " + restId, Toast.LENGTH_SHORT).show();

				ApplicationState.setTableId(applicationContext, tableId);
				ApplicationState.setRestaurantId(applicationContext, restId);
				ApplicationState.setOpenCategoryDrawer(applicationContext, true);
				
				//clean order stuff if present
				ApplicationState.cleanSubOrderList(applicationContext);
				ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);				

				Intent intent = new Intent(this, FoodMenuActivity.class);
				startActivity(intent);				
				finish();	
			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				Utilities.error("Error Message: " + error);
				Toast.makeText(this, "To be implemented", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
}
