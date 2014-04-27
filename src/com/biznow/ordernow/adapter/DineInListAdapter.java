package com.biznow.ordernow.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.biznow.ordernow.QRCodeScannerActivity;
import com.biznow.ordernow.R;

public class DineInListAdapter extends BaseExpandableListAdapter implements OnClickListener {

	Context context;
	Resources res;
	private LayoutInflater inf;
	private static final int ZBAR_QR_SCANNER_REQUEST = 1;
	
	public DineInListAdapter(Context context){
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
		if(convertView == null){
			convertView = inf.inflate(R.layout.dine_in_list_child, parent, false);
		}
		
		Button qrCodeButton = (Button) convertView.findViewById(R.id.qrscan_btn);
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

		//add image for the dine in icon
		
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
		if(v.getId() == R.id.qrscan_btn){
			QRCodeScannerActivity qrCodeScannerActivity = new QRCodeScannerActivity();
			qrCodeScannerActivity.launchQRScanner(v);
		}
	}
	
}
