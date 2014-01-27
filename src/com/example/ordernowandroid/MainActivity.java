package com.example.ordernowandroid;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.data.database.CurrentOrdersHelpers;
import com.data.database.CustomDbAdapter;
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
		CustomDbAdapter dbManager = CustomDbAdapter
				.getInstance(getBaseContext());
		CurrentOrdersHelpers coh = new CurrentOrdersHelpers(dbManager);
		coh.getCurrentOrders();
		Parse.initialize(this, "vMFTELLhOo9RDRql9HpV9lKRot5xQTCCD63wkYdQ", "mdz7n8XUjy3u0MSQRnuwmogqXZrw3qJnRwmRxx0g"); 
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		Context context = getApplicationContext();
		CharSequence text = ParseInstallation.getCurrentInstallation().getObjectId();
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();     

		ParseAnalytics.trackAppOpened(getIntent());

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
		switch (requestCode) {
		case ZBAR_SCANNER_REQUEST:
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
				String tableId = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				
				Intent intent = new Intent(this, FoodMenuActivity.class);
				intent.putExtra(FoodMenuActivity.TABLE_ID, tableId);
				startActivity(intent);
				//finish this to disable back to this activity once scanned
				this.finish();				
			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
				Intent intent = new Intent(this, FoodMenuActivity.class);
				intent.putExtra(FoodMenuActivity.TABLE_ID, "T1");
				startActivity(intent);
				//finish this to disable back to this activity once scanned
				this.finish();
			}
			break;
		}
	}
}
