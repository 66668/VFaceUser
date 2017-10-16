package com.vfaceuser.utils;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.util.HashMap;

public class MyHttpGet extends HttpGet{
	public MyHttpGet(){
		super();
		initHeader();
	}
	
	public MyHttpGet(String url){
		super(url);
		initHeader();
	}
	
	public MyHttpGet(URI uri ){
		super(uri);
		initHeader();
	}
	
	public void initHeader(){
		HashMap<String, String> map =  HttpUtils.getInstance().getHeaders();
		for (String key : map.keySet()) {
			addHeader(key, map.get(key));
		} 
	}
 
}
