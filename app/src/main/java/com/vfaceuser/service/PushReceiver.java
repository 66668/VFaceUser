package com.vfaceuser.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.helper.AppHelper;
import com.vfaceuser.utils.ConfigUtil;

public class PushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传（payload）数据
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				Log.d("GetuiSdkDemo", "Got Payload:" + data);
				try{
                    MyApplication.getInstance().ClickMember = (new Gson()).fromJson(data, ExpenseListModel.class);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			final String cid = bundle.getString("clientid");
			Log.d("GetuiSdkDemo", "Got ClientID:" + cid);
			ConfigUtil configUtil = new ConfigUtil(context);
			configUtil.setPushId(cid);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						AppHelper.setPushKey(context, cid);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			 
			break;
		default:
			break;
		}
	}
}