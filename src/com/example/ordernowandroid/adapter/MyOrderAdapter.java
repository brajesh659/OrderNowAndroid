package com.example.ordernowandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.MyOrderItem;

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
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_order, null);
		}

		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
		//TextView itemUnitPrice = (TextView) convertView.findViewById(R.id.itemUnitPrice);
		TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
		TextView itemTotalPrice = (TextView) convertView.findViewById(R.id.itemTotalPrice);

		itemName.setText(orders.get(position).getFoodMenuItem().getItemName());
		//itemUnitPrice.setText(Integer.toString(orders.get(position).getFoodMenuItem().getItemPrice()));
		quantity.setText(Integer.toString(orders.get(position).getQuantity()));
		itemTotalPrice.setText(Integer.toString(orders.get(position).getFoodMenuItem().getItemPrice() * orders.get(position).getQuantity()));
		return convertView;
	}
}
