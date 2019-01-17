package com.biznow.ordernow.adapter;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.biznow.ordernow.ApplicationState;
import com.biznow.ordernow.FoodMenuActivity;
import com.biznow.ordernow.R;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class DineInListAdapter extends BaseExpandableListAdapter implements
		OnClickListener {

	Context context;
	Resources res;
	private LayoutInflater inf;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	private String activeTableId;
	private String activeRestId;
	private String activeRestName;

	TextView welcomeText;

	Button qrCodeButton;

	public DineInListAdapter(Context context) {
		this.context = context;
		res = context.getResources();
		this.inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inf.inflate(R.layout.dine_in_list_child, parent,
					false);
		}

		welcomeText = (TextView) convertView.findViewById(R.id.welcome_text);

		qrCodeButton = (Button) convertView.findViewById(R.id.qrscan_btn);
		qrCodeButton.setOnClickListener(this);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return res.getString(R.string.dine_in);
	}

	@Override
	public int getGroupCount() {
		return 1;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String value = res.getString(R.string.dine_in);
		if (convertView == null) {
			convertView = inf.inflate(R.layout.dine_in_list_parent, parent,
					false);
		}

		// add image for the dine in icon

		TextView tv = (TextView) convertView.findViewById(R.id.dine_in_text);
		tv.setText(value);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.qrscan_btn) {
			if(qrCodeButton.getText().toString().equals(res.getString(R.string.scan_qr_codes))){
				launchQRScanner(v);	
			}else {
				openRestaurantMenu(v);
			}
		}
	}

	public void launchQRScanner(View v) {
		if (isCameraAvailable()) {
			Intent intent = new Intent(context, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES,
					new int[] { Symbol.QRCODE });
			((Activity) context).startActivityForResult(intent,
					ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(context, "Rear Facing Camera Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public void dineStatusOpenRest(String activeRestName, String activeRestId, String activeTableId) {
		this.activeRestId = activeRestId;
		this.activeRestName = activeRestName;
		this.activeTableId = activeTableId;
		if (welcomeText != null && this.activeRestName != null
				&& qrCodeButton != null) {
			welcomeText.setText("You are logged in " + activeRestName
					+ " restaurant.");
			qrCodeButton.setText(res.getString(R.string.open_res_menu));
		}
	}

	public void dineStatusQRCode() {
		if (welcomeText != null && qrCodeButton != null) {
			welcomeText.setText(res.getString(R.string.welcome_text));
			qrCodeButton.setText(res.getString(R.string.scan_qr_codes));
		}
	}
	
	public void openRestaurantMenu(View v) {
        if (activeTableId != null && activeTableId.trim() != "") {
            ApplicationState applicationContext = (ApplicationState) context.getApplicationContext();
            ApplicationState.setOpenCategoryDrawer(applicationContext, true);
            Toast.makeText(context, "Session Table Id = " + activeTableId + " Rest Id = " + activeRestId,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, FoodMenuActivity.class);
            context.startActivity(intent);
        }
    }
	
	

}
