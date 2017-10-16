package com.vfaceuser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.inject.ViewInject;

public class ChangePasswordActivity extends BaseActivity {

	@ViewInject(id = R.id.back_btn, click = "imgBackClick")
	View imgBack;

    @ViewInject(id = R.id.title_textview)
    TextView titleView;

	@ViewInject(id = R.id.btnSave, click = "btnSaveClick")
	View btnSave;

    @ViewInject(id = R.id.txtOldPwd)
    EditText txtOldPwd;

	@ViewInject(id = R.id.txtNewPwd)
	EditText txtNewPwd;

	@ViewInject(id = R.id.txtNewPwd1)
	EditText txtNewPwd1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
        titleView.setText("修改密码");
	}

	public void btnSaveClick(View v) {

        if(TextUtils.isEmpty(txtOldPwd.getText().toString().trim())){
            sendToastMessage("请输入旧密码");
            return;
        }

		if (!txtNewPwd.getText().toString()
				.equals(txtNewPwd1.getText().toString())) {
			sendToastMessage("两次输入的新密码不一致！");
			return;
		}
		Loading.run(this, new Runnable() {
			@Override
			public void run() {
				try {
                    String msg = UserHelper.changePassword(ChangePasswordActivity.this,
                            txtOldPwd.getText().toString(),
                            txtNewPwd.getText().toString());
					ChangePasswordActivity.this.sendToastMessage(msg);
					ChangePasswordActivity.this.finish();
				} catch (MyHttpException e) {
					e.printStackTrace();
					ChangePasswordActivity.this.sendToastMessage(e.getMessage());
				}
			}
		});
		// this.finish();
	}

	public void imgBackClick(View v) {
		this.finish();
	}

}
