package com.vfaceuser.utils;

import android.content.Context;

import com.vfaceuser.R;


public class GenderUtil {
	/**
	 * 根据后台返回性别数字获取性别
	 * @param context
	 * @param gender
	 * @return
	 */
	public static String getGenderString(Context context,int gender){
		
		switch (gender) {
		case 1:
			return context.getString(R.string.male);
		case 2:
			return context.getString(R.string.female);
		default:
			return context.getString(R.string.unknow);
		}
		
	}
	
}
