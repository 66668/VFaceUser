package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.utils.ConfigUtil;


public class WelcomeActivity extends BaseActivity {
	 

	/**
	 * 是否登录
	 */
	boolean isLogin = false;
	/**
	 * 已经跳到别的页面
	 */
	boolean alreadyNext = false;
	/**
	 * 是否完成自动登录
	 */
	boolean autoLogined=false;
	
	boolean timerEnd=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				timerEnd = true;
				if(autoLogined){
					nextPage();
				}
			}
		}, 2000);//首页画面延迟

		new Thread(new Runnable() {

			@Override
			public void run() {
				autoLogin(); 
			}
		}).start();
	}
	

	private synchronized void autoLogin() {
//		try {
//			isLogin = UserHelper.autoLogin(WelcomeActivity.this);
//			 
//		} catch (MyException e) { 
//			e.printStackTrace();
//			isLogin = false;
//		}
		autoLogined=true;
		nextPage();
	}

	/**
	 * 跳到下一界面
	 */
	private synchronized void nextPage() {
		if (alreadyNext) {
			return;
		}
		if(!timerEnd){
			return;
		}
		alreadyNext = true;
		WelcomeActivity.this.finish();
		Intent intent = new Intent(WelcomeActivity.this, isLogin ? MainActivity.class
				: (isFirstUse() ? LoginActivity.class : LoginActivity.class) );
		intent.putExtra("isfirst", true);
		startActivity(intent);
	}
	
	/**
	 * 是否是第一次使用
	 * @return
	 */
	boolean isFirstUse(){
		ConfigUtil config = new ConfigUtil(this);
		return config.getIsFirstUse();
	}
	
}
