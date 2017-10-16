package com.vfaceuser;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vfaceuser.bizmodel.MemberModel;
import com.vfaceuser.bizmodel.UpgradeModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.ImageLoadingConfig;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.dialog.UpdateAppDialog;
import com.vfaceuser.helper.AppHelper;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.DeleteFileUtil;
import com.vfaceuser.utils.IntentUtil;
import com.vfaceuser.utils.Utils;

public class MainActivity extends BaseActivity {

	private boolean ifClearing = false;// 是否正在清除缓存

	@ViewInject(id = R.id.btnInfo, click = "btnCreateUserClick")
	View btnCreateUser;
	//
	// @ViewInject(id = R.id.btnPath, click = "btnPathClick")
	// View btnPath;
	//
	@ViewInject(id = R.id.btnChangePwd, click = "btnChangePwdClick")
	View btnChangePwd;

	@ViewInject(id = R.id.btnUpgrade, click = "btnUpgradeClick")
	View btnUpgrade;

	// @ViewInject(id = R.id.btnClear, click = "btnClearClick")
	// View btnClear;

	@ViewInject(id = R.id.btnExit, click = "btnExitClick")
	View btnExit;

    @ViewInject(id = R.id.btnWelcome,click = "btnWelcomeConfig")
    View btnWelcomeConfig;

	@ViewInject(id = R.id.imgHeader)
	ImageView imgHeader;

	@ViewInject(id = R.id.txtUserName)
	TextView txtUserName;

    @ViewInject(id = R.id.txt_balances)
    TextView txtBalances;

    @ViewInject(id = R.id.txt_points)
    TextView txtPoints;

    @ViewInject(id = R.id.txt_monetary)
    TextView txtMonetary;

//	@ViewInject(id = R.id.txtCompany)
//	TextView txtCompany;

	final int UPGRADE = 2001;
	final int LOGOUT = 2002;

	private ImageLoader imgLoader;
	@SuppressWarnings("unused")
	private DisplayImageOptions imgOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (!MyApplication.getInstance().isLogin()) {
			this.finish();
			startActivity(WelcomeActivity.class);
			return;
		}
		imgLoader = ImageLoader.getInstance();
		imgLoader.init(ImageLoaderConfiguration.createDefault(this));
		imgOptions = ImageLoadingConfig
				.generateDisplayImageOptions(R.drawable.photo);
        PushManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		initUserView();
        if(MyApplication.getInstance().ClickMember!=null){
            startActivity(ElectronTicketActivity.class);
            getUserInfo();
        }
	}
	
	private void initUserView(){
		MemberModel memberModel = UserHelper.getCurrentUser();
        if(memberModel != null){

            if(memberModel.getFullName() != null){
                if(TextUtils.isEmpty(memberModel.getNickName())){
                    txtUserName.setText(memberModel.getFullName());
                }else {
                    txtUserName.setText(memberModel.getFullName()+"("+memberModel.getNickName()+")");
                }
            }
//		txtCompany.setText(UserHelper.getCurrentUser().get());
            imgLoader.displayImage(memberModel.getPicPath(),
                    imgHeader, imgOptions);

            txtBalances.setText("余额："+memberModel.getTotalBalance()+"元");
            txtPoints.setText("积分："+memberModel.getTotalPoints());
            txtMonetary.setText("消费金额："+memberModel.getTotalConsumeAmount()+"元");

        }
	}

	/**
	 * 个人信息
	 * 
	 * @param v
	 */
	public void btnCreateUserClick(View v) {
		startActivity(CreateUserActivity.class);
	}


	/**
	 * 修改密码
	 * 
	 * @param v
	 */
	public void btnChangePwdClick(View v) {
		startActivity(ChangePasswordActivity.class);
	}

	/**
	 * 升级版本
	 * 
	 * @param v
	 */
	public void btnUpgradeClick(View v) {
		Loading.run(this, new Runnable() {

			@Override
			public void run() {
				try {
					UpgradeModel upgradeModel = AppHelper.getUpgradeInfo(
                            com.vfaceuser.MainActivity.this,
                            Utils.getVersionCode(com.vfaceuser.MainActivity.this));
					if (upgradeModel.isIsexistsnewversion()) {
						sendMessage(UPGRADE, upgradeModel);
					} else {
						sendToastMessage(R.string.last_version_already);
					}
				} catch (MyHttpException e) {
					e.printStackTrace();
					sendToastMessage(e.getMessage());
				}
			}
		});
	}

	@Override
	protected void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case LOGOUT:
			PushManager.getInstance().turnOffPush(this);
			this.finish();
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(getPackageName());
			break;
		case UPGRADE:
			final UpgradeModel upgradeModel = (UpgradeModel) msg.obj;
			new UpdateAppDialog(this, upgradeModel.getMessage(), false,
					new UpdateAppDialog.UpdateAppDialogCallBack() {

						@Override
						public void confirm() {
							Intent intent = IntentUtil.getBrowserIntent(upgradeModel.getPackageUrl());
							startActivity(intent);
						}

						@Override
						public void cancel() {

						}
					}).show();
			break;

		default:
			break;
		}
	}

	/**
	 * 清除缓存
	 * 
	 * @param v
	 */
	public void btnClearClick(View v) {
		if (!ifClearing) {
			Toast.makeText(this, R.string.clearing_cache, Toast.LENGTH_SHORT)
					.show();
			ifClearing = true;
			new ClearCacheThread().start();
		}
	}

	/**
	 * 退出
	 * 
	 * @param v
	 */
	public void btnExitClick(View v) {
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				try {
					UserHelper.logout(com.vfaceuser.MainActivity.this);
					sendMessage(LOGOUT);
				} catch (MyHttpException e) {
					e.printStackTrace();
					if (TextUtils.isEmpty(e.getMessage())) {
						sendToastMessage("注销失败!");
					} else {
						sendToastMessage(e.getMessage());
					}
				}
			}
		});
	}

    public void btnGoShopNearBy(View v){
        Intent intent = new Intent();
        intent.setClass(this,ShopsNearbyActivity.class);
        startActivity(intent);
    }

    public void btnGoVIPCardManage(View v){
        Intent intent = new Intent();
        intent.setClass(this,VIPCardManageActivity.class);
        startActivity(intent);
    }

    public void btnConsumeHistory(View v){
        Intent intent = new Intent();
        intent.setClass(this,ConsumeHistoryActivity.class);
        startActivity(intent);
    }

    public void btnWelcomeConfig(View v){
        startActivity(WelcomeWordConfigActivity.class);
    }

	class ClearCacheThread extends Thread {

		@Override
		public void run() {
			DeleteFileUtil.deleteFolder(MyApplication
					.getPicCachePath(com.vfaceuser.MainActivity.this));
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(com.vfaceuser.MainActivity.this,
							R.string.clear_cache_success, Toast.LENGTH_SHORT)
							.show();
					ifClearing = false;
				}
			});
		}

	}
	
	private void getUserInfo(){
		
		new Thread(){

			@Override
			public void run() {
				try {
					UserHelper.getMemberBalance(com.vfaceuser.MainActivity.this);
				} catch (MyHttpException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
	}
}
