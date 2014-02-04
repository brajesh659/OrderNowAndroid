package com.example.ordernowandroid;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends Activity {

	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Parse.initialize(this, "vMFTELLhOo9RDRql9HpV9lKRot5xQTCCD63wkYdQ", "mdz7n8XUjy3u0MSQRnuwmogqXZrw3qJnRwmRxx0g");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(getIntent());

		//Toast.makeText(this, ParseInstallation.getCurrentInstallation().getObjectId(), Toast.LENGTH_SHORT).show();
		setContentView(R.layout.activity_main);
	}

	public void launchScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
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

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);    
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
			    String tableId = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Toast.makeText(this, "Table Id = " + tableId, Toast.LENGTH_SHORT).show();

				//FIXME: Validate QR Code with Backend Server before calling FoodMenuActivity Class
				if (tableId.equalsIgnoreCase("T1")) {
				    applicationContext.setTableId(tableId);
					Intent intent = new Intent(this, FoodMenuActivity.class);
					//TODO delete below line
					intent.putExtra("TableId1", tableId);
					startActivity(intent);				
					this.finish(); //finish this to disable back to this activity once scanned	
				} else {
					Toast.makeText(this, "This QR Code is not a valid code as per our database. Please contact the Restaurant Staff if the issue persists.", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(this, MainActivity.class);
					startActivity(intent);				
					this.finish();				
				}
			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
				applicationContext.setTableId("T1");
				Intent intent = new Intent(this, FoodMenuActivity.class);
				//TODO delete below line
				intent.putExtra("TableId1", "T1");
				startActivity(intent);				
				this.finish(); //finish this to disable back to this activity once scanned
			}
			break;
		}
	}
}
