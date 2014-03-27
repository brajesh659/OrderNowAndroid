package com.example.ordernowandroid.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.AllHistoryViewItem;
import com.util.Utilities;

public class AllHistoryAdapter extends BaseExpandableListAdapter {

	private Context applicationContext;
	private ArrayList<AllHistoryViewItem> allHistoryItems;

	private static final int[] EMPTY_STATE_SET = {};
	private static final int[] GROUP_EXPANDED_STATE_SET = { android.R.attr.state_expanded };
	private static final int[][] GROUP_STATE_SETS = { EMPTY_STATE_SET, // 0
			GROUP_EXPANDED_STATE_SET // 1
	};

	public AllHistoryAdapter(Context applicationContext,
			ArrayList<AllHistoryViewItem> allHistoryItems) {
		super();
		this.applicationContext = applicationContext;
		this.allHistoryItems = allHistoryItems;

	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.allHistoryItems.get(groupPosition).getDishnames()
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosititon) {
		return childPosititon;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater minflater = (LayoutInflater) applicationContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = minflater.inflate(R.layout.history_child_view, null);
		}

		String dishName = this.allHistoryItems.get(groupPosition)
				.getDishnames().get(childPosition);
		TextView dishNameView = (TextView) convertView
				.findViewById(R.id.history_dish_name);
		dishNameView.setText(dishName);
		return convertView;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.allHistoryItems.get(groupPosition).getDishnames().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.allHistoryItems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.allHistoryItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int position, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater minflater = (LayoutInflater) applicationContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = minflater.inflate(R.layout.history_parent_view, null);
		}
		ImageView ind = (ImageView) convertView
				.findViewById(R.id.explist_indicator);
		if (getChildrenCount(position) != 0) {
			ind.setVisibility(View.VISIBLE);
			int stateSetIndex = (isExpanded ? 1 : 0);
			Drawable drawable = ind.getDrawable();
			drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
		} else {
			ind.setVisibility(View.INVISIBLE);
		}
		TextView restNameView = (TextView) convertView
				.findViewById(R.id.history_rest_name);
		TextView orderIdView = (TextView) convertView
				.findViewById(R.id.history_order_id);
		TextView orderDateView = (TextView) convertView
				.findViewById(R.id.history_order_date);

		AllHistoryViewItem item = this.allHistoryItems.get(position);
		restNameView.setText(item.getRestName());
		orderIdView.setText("OrderID " + item.getOrderId());
		String orderDate = Utilities.defaultDateFormat(item.getOrderDate());
		orderDateView.setText(orderDate);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
