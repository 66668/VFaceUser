package com.vfaceuser.dialog;

import android.content.Context;

import org.json.JSONObject;

public class Loading {

	public static LoadingDialog run(Context context, boolean  cancelable, final Runnable runable) {
		final LoadingDialog loadingDialog = new LoadingDialog(context);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(cancelable);
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runable.run();
				} finally {
					loadingDialog.dismiss();
				}
			}
		}).start();
		return loadingDialog;
	} 
	
	public static LoadingDialog run(Context context, final Runnable runable) {
		 return run(context, true, runable);
	} 

	public interface HttpResult {
		void onSuccess(JSONObject result); 
		void onError(Exception e);
	}
}
