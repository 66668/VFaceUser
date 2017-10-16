package com.vfaceuser.commen;

import android.content.Context;


public class Configuration {
	
	final static String DATABASE_NAME = "VFACE.db";
	
	public final static String getDBName(Context context){
		return DATABASE_NAME;
	}
	
	public final static int SPLASH_INTERVAL = 2000;
	public final static boolean PRINT_LOG = true;
	public final static boolean PRINT_DUBUG_LOG = true;
	public final static boolean PRINT_VIEW_LOG = true;
	public final static boolean PRINT_INFO_LOG = true;
	public final static boolean PRINT_WARN_LOG = true;
	public final static boolean PRINT_ERROR_LOG = true;
  
	public final static int GENDER_FEMALE = 2; 
	public final static int GENDER_MALE = 1; 
	public final static int PAGE_SIZE = 10;
	
}