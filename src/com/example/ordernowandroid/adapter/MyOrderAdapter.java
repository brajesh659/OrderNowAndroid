package com.example.ordernowandroid.adapter;

import java.util.List;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.MyOrderItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//public class MyOrderFragment extends Fragment {
//	public MyOrderFragment() {
//	}
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(resource, root)
//		return super.onCreateView(inflater, container, savedInstanceState);
//	}
//}

public class MyOrderAdapter extends ArrayAdapter<MyOrderItem> {

	private List<MyOrderItem> orders;

	public MyOrderAdapter(Context context, List<MyOrderItem> orders) {
		super(context, R.layout.my_order, orders);
		this.orders = orders;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_order, null);
		}

		TextView item = (TextView) convertView.findViewById(R.id.item);

		TextView price = (TextView) convertView.findViewById(R.id.price);

		item.setText(orders.get(position).getItem());
		price.setText(Float.toString(orders.get(position).getQuantity()));
		return convertView;
	}
}
