package com.vfaceuser;

import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.GetPwdHelper;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.PageUtil;


public class RegActivity extends BaseActivity implements GetPwdHelper.WaitResendObserver {

	@ViewInject(id = R.id.btnReg, click = "btnRegClick")
	public Button btnLogin;

	@ViewInject(id = R.id.txtAccount)
	public EditText txtAccount;

	@ViewInject(id = R.id.txtPassword)
	public EditText txtPassword;

	@ViewInject(id = R.id.txtValidCode)
	public EditText txtValidCode;

	@ViewInject(id = R.id.btnGetValidCode, click = "btnGetValidCodeClick")
	public Button btnGetValidCode;

    @ViewInject(id = R.id.provision_text)
    public TextView txtProvision;

    @ViewInject(id = R.id.ifAgreeProvisionCheckBox)
    public CheckBox ifAgreeProvisionCheckBox;

	private final int REG_SUCESS = 2001;

    private String resendString;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
        initProvisionTextView();
        resendString = getString(R.string.resend_verification_code);
        GetPwdHelper.getInstance().registerObserver(this);
	}

    private void initProvisionTextView() {
        ifAgreeProvisionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnLogin.setEnabled(isChecked);
            }
        });
        String provisionText = "我已阅读并同意<font color=\"#ffffff\"><a href = 'http://protocols.pss100.com/user.html'>此协议</a></font>";
        txtProvision.setText(Html.fromHtml(provisionText));
        txtProvision.setMovementMethod(LinkMovementMethod.getInstance());
    }

	public void btnGetValidCodeClick(View v) {
		if (txtAccount.getText().length() != 11) {
			sendToastMessage("请输入正确的手机号码！");
			return;
		}
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				try {
					UserHelper.sendAuthCode(RegActivity.this, txtAccount
                            .getText().toString());
					sendToastMessage("验证码发送成功!");
                    GetPwdHelper.getInstance().startWaitForResend();
				} catch (MyHttpException e) {
					e.printStackTrace();
					if (TextUtils.isEmpty(e.getMessage())) {
						sendToastMessage("验证码发送失败!");
					} else {
						sendToastMessage(e.getMessage());
					}
				}
			}
		});
	}

	public void btnRegClick(View v) {
		if (!checkInput()) {
			return;
		}
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				try {
                    UserHelper.regist(RegActivity.this,txtAccount.getText().toString(), txtPassword.getText()
							.toString(),
							txtValidCode.getText().toString());
					sendMessage(REG_SUCESS);
				} catch (MyHttpException e) {
					e.printStackTrace();
					if (TextUtils.isEmpty(e.getMessage())) {
						sendToastMessage("注册失败!");
					} else {
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
		case REG_SUCESS:
			PageUtil.DisplayToast("注册成功！");
			this.finish();
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
		if (TextUtils.isEmpty(txtValidCode.getText())) {
			PageUtil.DisplayToast(R.string.please_input_validcode);
			return false;
		}
		return true;
	}

    //设置发送短信按钮状态
    private void setSendSmsBtnState(final int waitResendTime){

        boolean ifEnabled = true;

        if(waitResendTime > 0){
            ifEnabled = false;
        }
        if(btnGetValidCode != null){
            btnGetValidCode.setEnabled(ifEnabled);
            if(ifEnabled){
                btnGetValidCode.setText(resendString);
            }else{
                btnGetValidCode.setText(resendString+"("+waitResendTime+")");
            }
        }

    }

    @Override
    public void updateResendVCodeTime(int resendVerificationCodeTime) {
        setSendSmsBtnState(resendVerificationCodeTime);
    }

    @Override
    protected void onDestroy() {
        GetPwdHelper.getInstance().removeObserver(this);
        super.onDestroy();
    }
}
