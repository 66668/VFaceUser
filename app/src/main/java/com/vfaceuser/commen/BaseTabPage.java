package com.vfaceuser.commen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.vfaceuser.MainActivity;

public abstract class BaseTabPage {

	/**
	 * APP上下文
	 */
	public Context mContext;
	
	protected final static int REFRESH_DATA = 2001;

	/**
	 * 当前View
	 */
	View mView = null;

	/**
	 * 获取当前View
	 * 
	 * @return
	 */
	public View getView() {
		if (mView == null) {
			LayoutInflater mLi = LayoutInflater.from(mContext);
			mView = mLi.inflate(getLayout(), null);
		}
		return mView;
	}

	/**
	 * 获取Context
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 布局ID
	 * 
	 * @return
	 */
	protected abstract int getLayout();

	public BaseTabPage(Context context) {
		mContext = context;
	}

	public void onCreate(Bundle savedInstanceState) {
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void onDestroy() {
		mView = null;
	}

	public void onStart() {
	}

	public void onStop() {
	}

	protected View findViewById(int id) {
		return mView.findViewById(id);
	}
	
	/**
	 * 获取主Activity
	 * @return
	 */
	public MainActivity getMainActivity()
	{
		return (MainActivity)mContext;
	}
	
	/**
	 * 刷新本页面数据
	 */
	public abstract void refreshData();
}
