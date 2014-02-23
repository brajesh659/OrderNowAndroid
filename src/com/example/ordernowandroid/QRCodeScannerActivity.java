package com.example.ordernowandroid;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.facebook.widget.ProfilePictureView;

public class QRCodeScannerActivity extends Activity {

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	private ProfilePictureView profilePictureView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_scanner);

		ApplicationState applicationContext = (ApplicationState) getApplicationContext();

		if(applicationContext.getUserName() != null || applicationContext.getUserName().trim() != "") {		
			TextView welcome = (TextView) findViewById(R.id.welcome_text);
			welcome.setText("Hello " + applicationContext.getUserName() + "! " + welcome.getText());
		}

		if(applicationContext.getProfilePictureId() != null) {
			profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
			profilePictureView.setProfileId(applicationContext.getProfilePictureId());
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
		case ZBAR_QR_SCANNER_REQUEST:
			if (resultCode == RESULT_OK) {
				String tableId = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Toast.makeText(this, "Table Id = " + tableId, Toast.LENGTH_SHORT).show();

				//FIXME: Validate QR Code with Backend Server before calling FoodMenuActivity Class
				if (tableId.equalsIgnoreCase("T1")) {
					applicationContext.setTableId(tableId);
					Intent intent = new Intent(this, FoodMenuActivity.class);
					startActivity(intent);				
					finish();	
				} else {
					Toast.makeText(this, "This QR Code is not a valid code as per our database. Please contact the Restaurant Staff if the issue persists.", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(this, QRCodeScannerActivity.class);
					startActivity(intent);				
					finish();				
				}
			} else if(resultCode == RESULT_CANCELED && data != null) {
				String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
				if(!TextUtils.isEmpty(error)) {
					Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
				}
				applicationContext.setTableId("T1");
				Intent intent = new Intent(this, FoodMenuActivity.class);
				startActivity(intent);				
				finish();
			}
			break;
		}
	}

}
