package com.vfaceuser.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.utils.Utils;


public class MyAlertDialog extends Dialog implements View.OnClickListener{

	private Activity context;
	private MyAlertDialogCallBack callBack;
	
	public MyAlertDialog(Activity context,String title,MyAlertDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);
		this.context = context;
		this.callBack = callBack;
		init(title);
	}
	
	public MyAlertDialog(Activity context,String title,String confirmText,MyAlertDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);
		this.context = context;
		this.callBack = callBack;
		init(title,confirmText);
	}
	
	public interface MyAlertDialogCallBack{
		
		public void confirm();
		
	}

	private void init(String title) {
		String confirmText = context.getResources().getString(R.string.confirm);
		init(title,confirmText);
	}
	
	private void init(String title,String confirmText) {
		
		this.setCancelable(false);
		
		View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_without_cancel, null);
		
		TextView titleView = (TextView) dialogView.findViewById(R.id.dialog_title);
		TextView confirmTextView = (TextView) dialogView.findViewById(R.id.confirm_btn);
		
		if(TextUtils.isEmpty(title)){
			titleView.setVisibility(View.GONE);
		}else{
			titleView.setText(title);
		}
		confirmTextView.setText(confirmText);
		
		confirmTextView.setOnClickListener(this);
		setContentView(dialogView);
		
	}
	
	
	
	@Override
	public void show() {
		if (!context.isFinishing()){
			super.show();
			WindowManager.LayoutParams lp = this.getWindow().getAttributes();
			int screenWidth = (int)(Utils.getScreenWidth((Activity)context));
			lp.width = screenWidth - screenWidth * 60 / 640;
			lp.gravity = Gravity.CENTER;
			this.getWindow().setAttributes(lp);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_btn:
			dismiss();
			callBack.confirm();
			break;
		default:
			break;
		}
	}
	
}
