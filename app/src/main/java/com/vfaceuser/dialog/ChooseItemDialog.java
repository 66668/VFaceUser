package com.vfaceuser.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.utils.Utils;


public class ChooseItemDialog extends Dialog implements View.OnClickListener{

	private Context context;
	private ChooseItemDialogCallBack callBack;
	private ImageView item1SelectedSign;
	private ImageView item2SelectedSign;
	private int whichItem;
	private boolean ifShowSign = true;
	
	public ChooseItemDialog(Context context,String item1Text,String item2Text,int whichItem,ChooseItemDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);
		this.context = context;
		this.callBack = callBack;
		this.whichItem = whichItem;
		init(item1Text,item2Text);
	}
	
	public ChooseItemDialog(Context context,String item1Text,String item2Text,int whichItem,boolean ifShowSign,ChooseItemDialogCallBack callBack) {
		super(context, R.style.LoadingDialog);
		this.context = context;
		this.callBack = callBack;
		this.whichItem = whichItem;
		this.ifShowSign = ifShowSign;
		init(item1Text,item2Text);
	}
	
	public interface ChooseItemDialogCallBack{
		
		public void confirm(int whichItem);
		
		public void cancel();
		
	}

	
	private void init(String item1Text,String item2Text) {
		
		View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_choose, null);
		
		RelativeLayout item1 = (RelativeLayout) dialogView.findViewById(R.id.item1);
		RelativeLayout item2 = (RelativeLayout) dialogView.findViewById(R.id.item2);
		
		TextView item1TextView = (TextView) dialogView.findViewById(R.id.txt_item1);
		TextView item2TextView = (TextView) dialogView.findViewById(R.id.txt_item2);
		TextView cancelTextView = (TextView) dialogView.findViewById(R.id.cancel_btn);
		item1SelectedSign = (ImageView) dialogView.findViewById(R.id.item1_selected_sign);
		item2SelectedSign = (ImageView) dialogView.findViewById(R.id.item2_selected_sign);
		
		item1TextView.setText(item1Text);
		item2TextView.setText(item2Text);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		
		initItemSign();
		
		cancelTextView.setOnClickListener(this);
		
		setContentView(dialogView);
		setCanceledOnTouchOutside(false);
		getWindow().setGravity(Gravity.BOTTOM);
	}
	
	private void initItemSign(){
		
		if(ifShowSign){
			if(whichItem == 0){
				item1SelectedSign.setVisibility(View.VISIBLE);
				item2SelectedSign.setVisibility(View.INVISIBLE);
			}else{
				item1SelectedSign.setVisibility(View.INVISIBLE);
				item2SelectedSign.setVisibility(View.VISIBLE);
			}
		}else{
			item1SelectedSign.setVisibility(View.INVISIBLE);
			item2SelectedSign.setVisibility(View.INVISIBLE);
		}
		
	}
	
	@Override
	public void show() {
		super.show();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		int screenWidth = (int)(Utils.getScreenWidth((Activity)context));
		lp.width = screenWidth - screenWidth * 60 / 640;
		lp.gravity = Gravity.BOTTOM;
		this.getWindow().setAttributes(lp);
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			callBack.cancel();
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item1:
			dismiss();
			whichItem = 0;
			initItemSign();
			callBack.confirm(0);
			break;
		case R.id.item2:
			dismiss();
			whichItem = 1;
			initItemSign();
			callBack.confirm(1);
			break;
		case R.id.cancel_btn:
			dismiss();
			callBack.cancel();
			break;
		default:
			break;
		}
	}
	
}
