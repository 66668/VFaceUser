package com.vfaceuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class CommonListAdapter extends BaseAdapter{

	public Context context;
	public LayoutInflater inflater;
	public AdapterCallBack callBack; 
	public ArrayList entityList = new ArrayList();
	public boolean IsEnd=false;
	
	public CommonListAdapter(Context context,AdapterCallBack callBack){
		this.context = context;
		this.callBack = callBack;
		inflater = LayoutInflater.from(context); 
	}
	
	public interface AdapterCallBack{
		void loadMore();
	}
	
	@Override
	public int getCount() {
		if(entityList != null){
			return entityList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return entityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} 
	
	public ArrayList getEntityList() {
		return entityList;
	}

	public void setEntityList(ArrayList entityList) {
//		this.entityList = entityList;
		this.entityList.clear();
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}
	
	public void addEntityList(ArrayList entityList) { 
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}
	
	protected abstract View inflateConvertView();
	
	protected abstract void initViewData(int position, View convertView); 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		if(convertView == null){
			convertView = inflateConvertView();
		} 
		initViewData(position, convertView);
		if(position == getCount() - 1  && !IsEnd){
			callBack.loadMore();
		} 
		return convertView;
	}

}
