package com.vfaceuser;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ConfigHelper;
import com.vfaceuser.helper.UserHelper;
import com.vfaceuser.utils.PageUtil;

/**
 * Created by HuBin on 15/4/13.
 */
public class WelcomeWordConfigActivity extends BaseActivity implements View.OnClickListener {


    private static final int SET_WELCOME_VOICE_SUCCESS = 9;
    private CheckBox ifEnableCheck;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_words_config);
        initTopBar();
        ifEnableCheck = (CheckBox) findViewById(R.id.voice_welcome_check_box);
        editText = (EditText) findViewById(R.id.welcome_edit);
        String customWelcomeStirng = UserHelper.getCurrentUser().getCustomWelcome();
        boolean ifEnableWelcome = UserHelper.getCurrentUser().isEnableWelcome();

        if(customWelcomeStirng != null){
            editText.setText(customWelcomeStirng+"");
        }
        ifEnableCheck.setChecked(ifEnableWelcome);
    }

    private void initTopBar() {
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText("欢迎语设置");
    }


    public void save(View v){

        final String welcomeString = editText.getText().toString();

        Loading.run(this,new Runnable() {
            @Override
            public void run() {


                try {
                    boolean ifEnable = ifEnableCheck.isChecked();
                    ConfigHelper.setWelcomeVoice(WelcomeWordConfigActivity.this, welcomeString, ifEnable);
                    UserHelper.getCurrentUser().setEnableWelcome(ifEnable);
                    UserHelper.getCurrentUser().setCustomWelcome(welcomeString);
                    sendMessage(SET_WELCOME_VOICE_SUCCESS);
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
        switch (msg.what){
            case SET_WELCOME_VOICE_SUCCESS:
                PageUtil.DisplayToast("设置成功");
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }
}
