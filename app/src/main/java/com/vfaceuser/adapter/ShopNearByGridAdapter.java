package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;

import java.util.ArrayList;

public class ShopNearByGridAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<StoreBusinessTypeModel> tabStrings = new ArrayList<StoreBusinessTypeModel>();
	
	public ShopNearByGridAdapter(Context context){
		this.context = context;
	}

	public void setTabStrings(ArrayList<StoreBusinessTypeModel> tabStrings) {
		this.tabStrings.clear();
		this.tabStrings.addAll(tabStrings);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tabStrings.size();
	}

	@Override
	public Object getItem(int position) {
		return tabStrings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.activity_shops_nearby_all_tabs_menu_item, null);
		}
		TextView txtItem = (TextView) convertView.findViewById(R.id.item_txt);
		txtItem.setText(tabStrings.get(position).getKeyName());
		return convertView;
	}

}
