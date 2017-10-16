package com.vfaceuser;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.ConfigUtil;
import com.vfaceuser.utils.PageUtil;

public class LoginActivity extends BaseActivity {

	@ViewInject(id = R.id.btnLogin, click = "btnLoginClick")
	public Button btnLogin;

	@ViewInject(id = R.id.txtAccount)
	public EditText txtAccount;

	@ViewInject(id = R.id.txtPassword)
	public EditText txtPassword;

	@ViewInject(id = R.id.btnReg, click = "btnRegClick")
	public TextView btnReg;

    @ViewInject(id = R.id.btn_forget_pwd, click = "btnForgetPwdClick")
    public TextView btnForgetPwd;
	
	private final int LOGIN_SUCESS = 2001; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ConfigUtil configUtil = new ConfigUtil(this);
		txtAccount.setText(configUtil.getAccount());
		if (txtAccount.getText().toString().length()>0) { 
			txtPassword.setFocusable(true);
			txtPassword.requestFocus();
			txtPassword.setFocusableInTouchMode(true);
		}
	}

	public void btnRegClick(View v) {
		startActivity(RegActivity.class);
	} 

    public void btnForgetPwdClick(View v){
        startActivity(GetBackPwdActivity.class);
    }

	public void btnLoginClick(View v) {
		if (!checkInput()) {
			return;
		}
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				try {
					//UserHelper.loginOnline(LoginActivity.this, txtAccount.getText().toString(), txtPassword.getText().toString());
					UserHelper.loginOnline(LoginActivity.this, txtAccount.getText().toString(), txtPassword.getText().toString());
					MyApplication.getInstance().setIsLogin(true);
					sendMessage(LOGIN_SUCESS);
				} catch (MyHttpException e) {
					e.printStackTrace();
					if(TextUtils.isEmpty(e.getMessage())){
						sendToastMessage("用户名或密码错误!");
					}else {						
						sendToastMessage(e.getMessage());
					}
				}
			}
		});
		
	} 

	@Override
	protected void handleMessage(Message msg) { 
		super.handleMessage(msg);
		switch (msg.what) {
		case LOGIN_SUCESS: 
			this.finish();
			if(TextUtils.isEmpty(UserHelper.getCurrentUser().getFullName())){
				startActivity(CreateUserActivity.class); 
			}else{
				startActivity(MainActivity.class);
                PushManager.getInstance().turnOnPush(this);
			}
			break; 
		default:
			break;
		}
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(txtAccount.getText())) {
			PageUtil.DisplayToast(R.string.please_input_account);
			return false;
		}
		if (TextUtils.isEmpty(txtPassword.getText())) {
			PageUtil.DisplayToast(R.string.please_input_password);
			return false;
		}
		return true;
	}
}
