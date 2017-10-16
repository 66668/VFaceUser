package com.vfaceuser;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.vfaceuser.R;
import com.vfaceuser.adapter.ShopNearByGridAdapter;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;

import java.util.ArrayList;

public class ShopsNearbyAllTabMenu {
	
	private Context context;
	private ArrayList<StoreBusinessTypeModel> tabStrings;
	private PopupWindow popupWindow;
	private View mainView;
	private MenuCallBack callBack;
	
	public ShopsNearbyAllTabMenu(final Context context,ArrayList<StoreBusinessTypeModel> tabStrings,MenuCallBack callBack) {
		
		this.context = context;
		this.tabStrings = tabStrings;
		this.callBack = callBack;
	}
	
	public interface MenuCallBack{
		void changeToItem(int position);
	}
	
	private void initView() {
		mainView = View.inflate(context, R.layout.activity_shops_nearby_all_tabs_menu, null);
		ImageView closeMenuBtn = (ImageView) mainView.findViewById(R.id.close_menu_btn);
		closeMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(popupWindow != null && popupWindow.isShowing()){
					popupWindow.dismiss();
				}
			}
		});
		
		GridView gridView = (GridView) mainView.findViewById(R.id.gridView);
		ShopNearByGridAdapter adapter = new ShopNearByGridAdapter(context);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				callBack.changeToItem(position);
				popupWindow.dismiss();
			}
		});
		gridView.setAdapter(adapter);
		adapter.setTabStrings(tabStrings);		
	}
	
	public void showMenu(View parentView) {
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}else{
			initView();
			showAsDropDown(parentView);
		}
	}
	
//	public void showMenu(View parent,int x,int y){
//		dismiss();
//		initView();
//		popupWindow = new PopupWindow(mainView);
//
////		popupWindow.setBackgroundDrawable(new BitmapDrawable());
////		System.out.println("x : " + x + " y : " + y);
//		popupWindow = new PopupWindow(mainView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y+parent.getMeasuredHeight());
//		popupWindow.update();
//	}


	public void showAsDropDown(View parentView) {
		popupWindow = new PopupWindow(mainView,LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		
//		popupWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 
//				context.getResources().getDimensionPixelSize(
//				R.dimen.popmenu_yoff));
//		popupWindow.setAnimationStyle(R.style.AnimationPopMenu);
//		popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		popupWindow.showAsDropDown(parentView);
		mainView.setFocusableInTouchMode(true);
		mainView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_MENU)
						&& (popupWindow.isShowing())) {
					popupWindow.dismiss();
					return true;
				}

				return false;
			}
		});
	}
	
	public void dismiss() {
		if(popupWindow != null && popupWindow.isShowing())
			popupWindow.dismiss();
	}
	
}
