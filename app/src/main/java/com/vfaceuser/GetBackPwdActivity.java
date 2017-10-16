package com.vfaceuser;


import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.GetPwdHelper;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.PageUtil;

/**
 * Created by HuBin on 15/4/26.
 */
public class GetBackPwdActivity extends BaseActivity implements GetPwdHelper.WaitResendObserver {

    @ViewInject(id = R.id.back_btn,click = "backImgClick")
    public ImageButton backBtn;

    @ViewInject(id = R.id.title_textview)
    public TextView titleView;

    @ViewInject(id = R.id.btnNext, click = "btnNextClick")
    public Button btnNext;

    @ViewInject(id = R.id.txtAccount)
    public EditText txtAccount;

    @ViewInject(id = R.id.txtValidCode)
    public EditText txtValidCode;

    @ViewInject(id = R.id.txtNewPwd)
    public EditText txtNewPwd;

    @ViewInject(id = R.id.txtNewPwd1)
    public EditText txtNewPwd1;

    @ViewInject(id = R.id.btnGetValidCode, click = "btnGetValidCodeClick")
    public Button btnGetValidCode;

    private static final int GET_BACK_PWD_SUCCESS = 9;

    private String resendString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_back_pwd);
        titleView.setText("找回密码");
        resendString = getString(R.string.resend_verification_code);
        GetPwdHelper.getInstance().registerObserver(this);
    }


    public void backImgClick(View v){
        finish();
    }

    public void btnNextClick(View v){

        final String phoneNumber = txtAccount.getText().toString();
        if(TextUtils.isEmpty(phoneNumber.trim())){
            PageUtil.DisplayToast("请输入手机号码");
            return;
        }

        final String validCode = txtValidCode.getText().toString();
        if(TextUtils.isEmpty(validCode.trim())){
            PageUtil.DisplayToast("请输入验证码");
            return;
        }

        final String newPwd = txtNewPwd.getText().toString();
        if(TextUtils.isEmpty(validCode.trim())){
            PageUtil.DisplayToast("请输入新密码");
            return;
        }


        final String newPwd1 = txtNewPwd1.getText().toString();
        if(!newPwd1.equals(newPwd)){
            PageUtil.DisplayToast("两次密码输入不一致");
            return;
        }

        Loading.run(this,new Runnable() {
            @Override
            public void run() {
                try {
                    UserHelper.regist(GetBackPwdActivity.this,phoneNumber,newPwd,validCode);
                    sendMessage(GET_BACK_PWD_SUCCESS);
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendToastMessage(e.getMessage());
                }
            }
        });

    }

    public void btnGetValidCodeClick(View v){

        final String phoneNumber = txtAccount.getText().toString();
        if(TextUtils.isEmpty(phoneNumber.trim())){
            PageUtil.DisplayToast("请输入手机号码");
            return;
        }

        Loading.run(this,new Runnable() {
            @Override
            public void run() {

                try {
                    UserHelper.sendAuthCodeForGetPsw(GetBackPwdActivity.this, phoneNumber);
                    sendToastMessage("发送成功");
                    GetPwdHelper.getInstance().startWaitForResend();
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendToastMessage(e.getMessage());
                }

            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what){
            case GET_BACK_PWD_SUCCESS:
                PageUtil.DisplayToast("设置成功，请用新密码登录");
                finish();
                break;
            default:
                break;
        }

        super.handleMessage(msg);
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
