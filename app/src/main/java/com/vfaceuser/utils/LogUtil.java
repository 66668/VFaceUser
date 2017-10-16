package com.vfaceuser.utils;

import android.util.Log;

import com.vfaceuser.commen.Configuration;

import java.util.ArrayList;

public class LogUtil {

	static String TAG = "MYTAG"; 
	public static boolean D;
	public static boolean E;
	public static boolean I;
	public static boolean V;
	public static boolean W;
	private static boolean printLog;

	static {
		printLog = Configuration.PRINT_LOG;
		if (printLog) {
			D = Configuration.PRINT_DUBUG_LOG;
			V = Configuration.PRINT_VIEW_LOG;
			I = Configuration.PRINT_INFO_LOG;
			W = Configuration.PRINT_WARN_LOG;
			E = Configuration.PRINT_ERROR_LOG;
		}
	}

	public static void v(String paramString2) {
		v(null, paramString2);
	}

	public static void v(String tag, String paramString2) {
		if (V) {
			Log.v(tag, paramString2);
		}
	}

	public static void d(String paramString) {
		d(TAG, paramString);
	}

	public static void d(String tag, String paramString) {
		if (D)
			Log.d(tag, paramString);
	}

	public static void d(String tag, String paramString,
			ArrayList<String> parameters) {
		if (D) {
			for (Object obj : parameters) {
				paramString += ("," + obj.toString());
			}
			Log.d(tag, paramString);
		} 
	}
	
	public static void d(String tag, String paramString,
			Object ... parameters) {
		if (D) {
			for (Object obj : parameters) {
				if(obj!=null)
				paramString += ("," + obj.toString());
			}
			Log.d(tag, paramString);
		} 
	}

	public static void d(String tag, Throwable paramThrowable) {
		if (D)
			Log.d(tag, "", paramThrowable);
	}

	public static void e(Throwable paramThrowable) {
		e("", paramThrowable);
	}

	public static void e(String tag, Throwable paramThrowable) {
		try {
			if (E)
				Log.e(tag, "", paramThrowable);
		} catch (Exception localException) {
			Log.e(TAG, "Failed to e: " + localException.getMessage());
		}
	}

	public static void i(String tag, String paramString) {
		if (I)
			Log.i(tag, paramString);
	}

	public static void i(String paramString) {
		if (I)
			i(TAG, paramString);
	}

}
