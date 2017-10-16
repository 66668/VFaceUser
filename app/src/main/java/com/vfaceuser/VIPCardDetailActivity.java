package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.commen.BaseActivity;

/**
 * Created by HuBin on 15/5/6.
 */
public class VIPCardDetailActivity extends BaseActivity implements View.OnClickListener {

    private CardListModel cardListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_card_detail);
        cardListModel = (CardListModel) getIntent().getSerializableExtra("model");
        if(cardListModel == null){
            finish();
            return;
        }
        initMainView();
    }

    private void initMainView() {

        initTopBar();
        TextView cardNumber = (TextView) findViewById(R.id.card_number);
        TextView shopName = (TextView) findViewById(R.id.shop_name);
        TextView leverView = (TextView) findViewById(R.id.level);
        TextView moneyView = (TextView) findViewById(R.id.money);
        TextView pointsView = (TextView) findViewById(R.id.points);
        TextView consumeDetailView = (TextView) findViewById(R.id.consume_detail);
        TextView availableServiceView = (TextView) findViewById(R.id.available_service);
        RelativeLayout consumeDetailBar = (RelativeLayout) findViewById(R.id.consume_detail_bar);
        RelativeLayout availableServiceBar = (RelativeLayout) findViewById(R.id.available_service_bar);

        cardNumber.setText(cardListModel.getMemberCardNumber()+"");
        shopName.setText(cardListModel.getStoreName()+"");
        leverView.setText(cardListModel.getGradeName());
        moneyView.setText(cardListModel.getTotalMoney()+"元");
        pointsView.setText(cardListModel.getTotalPoints());
//        consumeDetailView.setText(cardListModel.getStoreMemberId()); TODO
//        availableServiceView.setText();
        consumeDetailBar.setOnClickListener(this);
        availableServiceBar.setOnClickListener(this);
    }

    private void initTopBar() {

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.vip_card_detail));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.consume_detail_bar:
                Intent detailIntent = new Intent(VIPCardDetailActivity.this,VipCardConsumeHistoryActivity.class);
                detailIntent.putExtra("model",cardListModel);
                startActivity(detailIntent);
                break;
            case R.id.available_service_bar:
                Intent serviceIntent = new Intent();
                serviceIntent.setClass(VIPCardDetailActivity.this,AvailableServiceActivity.class);
                serviceIntent.putExtra("model",cardListModel);
                startActivity(serviceIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleMessage(Message msg) {

        switch (msg.what){
        
        }

        super.handleMessage(msg);
    }
}
