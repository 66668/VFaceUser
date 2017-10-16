package com.vfaceuser.commen;

/**
 * Customized Exception
 * 
 * @author Don Fang
 * 
 */
public class MyHttpDataException extends MyHttpException {
	
	public MyHttpDataException(int resId) {
		super(resId); 
	}
	
	public MyHttpDataException(String msg) {
		super( msg); 
	}
	
	public MyHttpDataException(String msg,int status) {
		super(msg,status); 
	}
	
	private static final long serialVersionUID = 1L;
	
}
