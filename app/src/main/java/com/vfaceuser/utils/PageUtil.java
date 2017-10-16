package com.vfaceuser.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.dialog.MyAlertDialog;

import java.util.List;

public class PageUtil {

	public static void DisplayLongToast(String showMsg) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), showMsg,
				Toast.LENGTH_LONG);
		toast.show();
	}

	public static void DisplayToast(String showMsg) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), showMsg,
				Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void DisplayToast(String showMsg, boolean inCenter) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), showMsg,
				Toast.LENGTH_SHORT);
		if (inCenter) {
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.show();
	}

	public static void DisplayToast(int showMsgResId, boolean inCenter) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), MyApplication
				.getInstance().getApplicationContext().getString(showMsgResId),
				Toast.LENGTH_SHORT);
		if (inCenter) {
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.show();
	}

	public static void DisplayToast(int showMsgResId) {
		PageUtil.DisplayToast(MyApplication.getInstance()
				.getApplicationContext().getString(showMsgResId));
	}

	/**
	 * Set spinner select value for update page
	 *
	 */
	public static void setSpinner(Spinner spinner, Context ctx, int resource,
			int position) {
		setSpinner(spinner, ctx, resource);
		spinner.setSelection(position);
	}

	public static void setSpinner(Spinner spinner, Context ctx, int resource) {
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				ctx, resource, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter2);
	}

	public static void setSpinner(Spinner spinner, String[] names) {
		setSpinner(spinner, names, null);
	}

	public static void setSpinner(Spinner spinner, String[] names, String select) {
		ArrayAdapter<String> adapterLevel = new ArrayAdapter<String>(
				MyApplication.getInstance().getApplicationContext(),
				android.R.layout.simple_spinner_item, names);
		adapterLevel
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapterLevel);
		if (null != select)
			setSpinnerWithBlank(spinner, names, select);
	}

	public static void selectSpinner(Spinner sp, String[] vals, String val) {
		sp.setSelection(getPosInArray(vals, val) + 1);
	}

	public static void selectSpinner(Spinner sp, int[] vals, int val) {
		sp.setSelection(getSelectPositionSpinner(vals, val) + 1);
	}

	public static void setSpinnerWithBlank(Spinner sp, String[] vals, String val) {
		sp.setSelection(getPosInArray(vals, val));
	}

	public static boolean isEmpty(String e) {
		boolean isbool = false;
		if (null != e && e.length() > 0) {
			isbool = false;
		} else {
			isbool = true;
		}
		return isbool;
	}

	public static int dip2px(Context ctx, float dpValue) {
		final float scale = ctx.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * Default value is 0
	 * 
	 * @param vals
	 * @param val
	 * @return
	 */
	public static int getPosInStrs(String[] vals, String val) {
		int res = 0;
		int len = 0;
		if (null != val && null != vals) {
			len = vals.length;
			for (int i = 0; i < len; i++) {
				if (val.equals(vals[i])) {
					res = i;
					break;
				}
			}
		}
		return res;
	}

	/***
	 * default is -1
	 * 
	 * @param vals
	 * @param val
	 * @return
	 */
	public static int getPosInArray(String[] vals, String val) {
		int res = -1;
		int len = 0;
		if (null != val && null != vals) {
			len = vals.length;
			for (int i = 0; i < len; i++) {
				if (val.equals(vals[i])) {
					res = i;
					break;
				}
			}
		}
		return res;
	}

	public static int getPosInList(List<String> vals, String val) {
		int res = -1;
		int len = 0;
		if (null != val && null != vals) {
			len = vals.size();
			for (int i = 0; i < len; i++) {
				if (val.equals(vals.get(i))) {
					res = i;
					break;
				}
			}
		}
		return res;
	}

	public static int getSelectPositionSpinner(int[] vals, int val) {
		int res = -1;
		int len = 0;
		if (null != vals) {
			len = vals.length;
			for (int i = 0; i < len; i++) {
				if (val == vals[i]) {
					res = i;
					break;
				}
			}
		}
		return res;
	}

	/**
	 * 弹出错误提示
	 * 
	 * @param context
	 * @param title
	 * @param message
	 */
//	public static void alertError(Context context, String title, String message) {
//		new AlertDialog.Builder(context)
//				.setIcon(
//						context.getResources().getDrawable(
//								R.drawable.login_error_icon)).setTitle(title)
//				.setMessage(message).create().show();
//	}
	
	/**
	 * 弹出错误提示
	 * @param context
	 * @param message
	 */
	public static void alertError(Activity context, String message) {
//		new AlertDialog.Builder(context)
//				.setIcon(
//						context.getResources().getDrawable(
//								R.drawable.login_error_icon))
//				.setTitle(context.getString(R.string.title_error))
//				.setMessage(message).create().show();
		MyAlertDialog dialog = new MyAlertDialog(context, message, new MyAlertDialog.MyAlertDialogCallBack(){

			@Override
			public void confirm() {
				// TODO Auto-generated method stub
				
			}
			
		});
		dialog.show();
	}

	/**
	 * 弹出错误提示
	 * @param context
	 * @param resId
	 */
	public static void alertError(Activity context, int resId) {
//		new AlertDialog.Builder(context)
//				.setIcon(
//						context.getResources().getDrawable(
//								R.drawable.login_error_icon))
//				.setTitle(context.getString(R.string.title_error))
//				.setMessage(context.getString(resId)).create().show();
		
		MyAlertDialog dialog = new MyAlertDialog(context, context.getString(resId), new MyAlertDialog.MyAlertDialogCallBack(){

			@Override
			public void confirm() {
				// TODO Auto-generated method stub
				
			}
			
		});
		dialog.show();
	}

}
