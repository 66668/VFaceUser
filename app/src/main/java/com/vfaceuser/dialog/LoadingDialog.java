package com.vfaceuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vfaceuser.R;


public class LoadingDialog extends Dialog {

	private Context context;
	private int icon = 0;
	private Callback callback = null;
	private boolean cancelable = true;
	private AnimationDrawable animationDrawable ;

	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialog);
		this.context = context;
		init();
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		init();
	}

	public LoadingDialog(Context context, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;
		init();
	}

	public LoadingDialog(Context context, Callback callback, int theme, int icon) {
		super(context, theme);
		this.context = context;
		this.icon = icon;
		this.callback = callback;
		init();
	}

	void init() {
		LinearLayout contentView = new LinearLayout(context);
		contentView.setMinimumHeight(60);
		contentView.setGravity(Gravity.CENTER);
		contentView.setOrientation(LinearLayout.HORIZONTAL);
		ImageView image = null;

		if (icon == -1) {
			return;
		} else {
			image = new ImageView(context);
			image.setImageResource(R.drawable.loading);
//			image.setImageResource(R.anim.loading_anim);
//			animationDrawable = (AnimationDrawable) image.getDrawable(); 
//			image.post(new Runnable(){ public void run(){ animationDrawable.start(); } });//fixed for android 2.2 or old version
		}
		
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.rotate_repeat);
		LinearInterpolator lir = new LinearInterpolator();
		anim.setInterpolator(lir);
		image.setAnimation(anim);

		contentView.addView(image);
		setContentView(contentView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && cancelable) {
			if (null != callback)
				callback.update();
			this.dismiss();
		}
		return true;
	}

	public interface Callback {
		public void update();
	}
}