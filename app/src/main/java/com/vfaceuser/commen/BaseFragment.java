package com.vfaceuser.commen;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.vfaceuser.utils.PageUtil;


public abstract class BaseFragment extends Fragment {
	
	public static final int MESSAGE_TOAST = 1001;
	 
	public void onResume() {
		super.onResume(); 
	}

	public void onPause() {
		super.onPause(); 
	}
	
	/**
	 * handler
	 */
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			BaseFragment.this.handleMessage(msg);
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
			PageUtil.DisplayToast(msg.obj.toString());
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

	public void sendToastMessage(String result) {
		Message msg = new Message();
		msg.what = MESSAGE_TOAST;
		msg.obj = result;
		handler.sendMessage(msg);
	}
	
}
