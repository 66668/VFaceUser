package com.vfaceuser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vfaceuser.bizmodel.ShopModel;
import com.vfaceuser.commen.BaseActivity;

/**
 * Created by HuBin on 15/4/12.
 */
public class ShopDetailActivity extends BaseActivity implements View.OnClickListener {

    private ShopModel shopModel;

    private double userLng ,userLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        shopModel = (ShopModel) getIntent().getSerializableExtra("shopModel");
        userLat = getIntent().getDoubleExtra("userLatitude",0);
        userLng = getIntent().getDoubleExtra("userLongitude",0);

        if(shopModel == null){
            finish();
            return;
        }
        initMainView();

    }

    private TextView shopNameView;
    private TextView shopPhoneView;
    private TextView shopAddressView;
    private ImageButton callBtn;
    private TextView checkMapBtn;
    private TextView shopInfoView;
    private TextView businessTypeNameView;

    private void initMainView() {
        initTopBar();
        shopNameView = (TextView) findViewById(R.id.shop_name);
        shopPhoneView = (TextView) findViewById(R.id.shop_phone);
        shopAddressView = (TextView) findViewById(R.id.shop_address);
        checkMapBtn = (TextView) findViewById(R.id.check_map_btn);
        shopInfoView = (TextView) findViewById(R.id.shop_info);
        callBtn = (ImageButton) findViewById(R.id.call_btn);
        businessTypeNameView = (TextView) findViewById(R.id.business_type_name);
        callBtn.setOnClickListener(this);
        checkMapBtn.setOnClickListener(this);

        shopNameView.setText(shopModel.getStorename());



        shopPhoneView.setText("热线："+getPhoneNum());
        shopAddressView.setText("地址："+shopModel.getAddress());
        shopInfoView.setText(shopModel.getDescription());
        businessTypeNameView.setText("行业："+shopModel.getBusinesstypename());

    }

    private String getPhoneNum(){

        if(!TextUtils.isEmpty(shopModel.getPhonenumber())){
            return shopModel.getPhonenumber();
        }

        if(!TextUtils.isEmpty(shopModel.getMobilephonenumber())){
            return shopModel.getMobilephonenumber();
        }
        return "";
    }

    private void initTopBar() {
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.shop_infos));
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.call_btn:
                callPhone();
                break;
            case R.id.check_map_btn:
                checkMap();
                break;
            default:
                break;
        }

    }

    private void checkMap() {

//        Intent intent = new Intent(this,NewShopMapActivity.class);
        Intent intent = new Intent(this,ShopMapActivity.class);
        intent.putExtra("shopLatitude",shopModel.getLatitude());
        intent.putExtra("shopLongitude",shopModel.getLongitude());
        intent.putExtra("shopName",shopModel.getStorename());
        intent.putExtra("shopAddress",shopModel.getAddress());
        intent.putExtra("userLatitude",userLat);
        intent.putExtra("userLongitude",userLng);
        startActivity(intent);

    }

    private void callPhone() {
        if(!TextUtils.isEmpty(getPhoneNum())){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getPhoneNum().replace("-","").replace("－","")));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}
