package com.vfaceuser.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.vfaceuser.bizmodel.VersionEntity;
import com.vfaceuser.dialog.UpdateAppDialog;

import java.io.File;

/**
 * 检查版本工具
 * @author Fangweidong
 *
 */
public class CheckUpdateUtil {
	
	private Context context;
	private Handler handler;

	
	public CheckUpdateUtil(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	 
	
	
	//显示更新版本对话框
	public void showUpdateAppDialog(final VersionEntity entity){
		
		boolean ifForce = false;
		if(entity.getIs_force() == 1){// 是否强制更新 1是 0否
			ifForce = true;
		}
		UpdateAppDialog dialog = new UpdateAppDialog(context,entity.getRelease_note(),ifForce,new UpdateAppDialog.UpdateAppDialogCallBack() {
			
			@Override
			public void confirm() {
				downloadApk(entity.getUrl());
			}
			
			@Override
			public void cancel() {
				
				
			}
		});
		dialog.show();
	}
	
	private void downloadApk(String apkUrl){
		 
		Uri uri = Uri.parse(apkUrl);
		Intent viewIntent = new Intent("android.intent.action.VIEW",uri);
		context.startActivity(viewIntent);
		
	}
	
	class CompleteReceiver extends BroadcastReceiver {
		
		public String savePath = null;
		
		CompleteReceiver(String savePath){
			this.savePath = savePath;
		}
		
		
		@Override
		public void onReceive(Context context, Intent intent) {
			downComplete(savePath);
		}
		
		private void downComplete(String filePath){
			File _file =  new File(filePath);
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");//向用户显示数据
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//以新压入栈
			intent.addCategory("android.intent.category.DEFAULT");
			Uri abc = Uri.fromFile(_file);
			intent.setDataAndType(abc,"application/vnd.android.package-archive");
			
			context.startActivity(intent);
		}

	};
	
}
