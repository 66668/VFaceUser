package com.vfaceuser.commen;

/**
 * Customized Exception
 * 
 * @author Don Fang
 * 
 */
public class MyException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int status;

	int resId;

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public MyException(int resId) {
		super();
		setResId(resId);
	}

	public MyException(String msg) {
		super(msg);
	}

	public MyException(String msg,int status){
		super(msg);
		this.status = status;
	}
	
	@Override
	public String getMessage() {
		if (resId > 0) {
			try {
				return MyApplication.getInstance().getString(resId);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return super.getMessage();
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
