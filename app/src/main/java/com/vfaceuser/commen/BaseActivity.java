package com.vfaceuser.commen;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.vfaceuser.inject.ActivityInjectHelper;
import com.vfaceuser.inject.InjectHelper;
import com.vfaceuser.utils.LogUtil;
import com.vfaceuser.utils.PageUtil;


public abstract class BaseActivity extends FragmentActivity {
  
	public static final int MESSAGE_TOAST = 1001; 
	public static final int MESSAGE_CLOSE = 1002;
 
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initView();
	}

	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initView();
	}

	public void setContentView(View view) {
		super.setContentView(view);
		initView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	protected void onDestroy() { 
		super.onDestroy();
	}
	
   
	/**
	 * Activity
	 * 
	 * @param newClass
	 */
	public void startActivity(Class<?> newClass) {
		Intent intent = new Intent(this, newClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); 
	}

	public void startActivity(Class<?> newClass, Bundle extras) {
		Intent intent = new Intent(this, newClass);
		intent.putExtras(extras);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent); 
	}

	/**
	 * handler
	 */
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			BaseActivity.this.handleMessage(msg);
		}
	};

	/**
	 * 
	 * 
	 * @param msg
	 */
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_TOAST:
			if(msg.obj != null && msg.obj.toString().trim().length()>0){
				PageUtil.DisplayToast(msg.obj.toString());
			}
			break;
		case MESSAGE_CLOSE:
			try{
				this.finish();
			}catch (Exception e) {
				e.printStackTrace();
			}
			break; 
		default:
			break;
		}
	}

	protected void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}


	protected void sendMessage(int what) {
		handler.sendEmptyMessage(what);
	}

	public void sendMessage(int what, Object obj) {
		handler.sendMessage(handler.obtainMessage(what, obj));
	}

	public void sendToastMessage(int resId) {
		Message msg = new Message();
		msg.what = MESSAGE_TOAST;
		msg.obj = getString(resId);
		handler.sendMessage(msg);
	}

	public void sendCloseMessage() { 
		handler.sendEmptyMessage(MESSAGE_CLOSE);
	}
	
	public void sendToastMessage(String result) {
		Message msg = new Message();
		msg.what = MESSAGE_TOAST;
		msg.obj = result;
		handler.sendMessage(msg);
	}

	/**
	 * ViewInject
	 */
	void initView() {
		InjectHelper helper = new ActivityInjectHelper(this);
		helper.initView();
	}

	@Override
	protected void onResume() { 
		LogUtil.d(BaseActivity.this.getClass().toString() + "------------onResume");
		super.onResume(); 
	}

	@Override
	protected void onPause() {
		super.onPause(); 
	}

	public enum Method {
		Click, LongClick, ItemClick, itemLongClick
	}

	@Override
	public void finish() { 
		super.finish();
	}


}
