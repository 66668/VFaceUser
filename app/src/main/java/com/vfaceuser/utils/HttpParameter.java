package com.vfaceuser.utils;

import java.util.HashMap;

public class HttpParameter extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	
	public static HttpParameter create(){
		HttpParameter p = new HttpParameter();
		return p;
	}

	public HttpParameter add(String key, String value){
		this.put(key, value);
		return this;
	}
	
	public HttpParameter add(String key, int value){
		this.put(key, value+"");
		return this;
	}
}
