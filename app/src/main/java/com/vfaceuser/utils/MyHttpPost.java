package com.vfaceuser.utils;

import org.apache.http.client.methods.HttpPost;

import java.net.URI;
import java.util.HashMap;

public class MyHttpPost extends HttpPost{
	public MyHttpPost(){
		super();
		initHeader();
	}
	
	public MyHttpPost(String url){
		super(url);
		initHeader();
	}
	
	public MyHttpPost(URI uri ){
		super(uri);
		initHeader();
	}
	
	public void initHeader(){
		HashMap<String, String> map =  HttpUtils.getInstance().getHeaders();
		for (String key : map.keySet()) {
			//addHeader(key, map.get(key));
		} 
		addHeader("Accept-Encoding", "gzip,deflate"); 
		addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
	}
 
}
