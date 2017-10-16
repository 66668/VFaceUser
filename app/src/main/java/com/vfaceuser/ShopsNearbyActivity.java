package com.vfaceuser;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.vfaceuser.adapter.ShopNearbyPagerAdapter;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.dialog.LoadingDialog;
import com.vfaceuser.helper.ConfigHelper;
import com.vfaceuser.utils.AMapUtil;
import com.vfaceuser.utils.LogUtil;
import com.vfaceuser.utils.PageUtil;
import com.vfaceuser.widget.SlidingTabLayout;

import java.util.ArrayList;

public class ShopsNearbyActivity extends BaseActivity implements OnClickListener, AMapLocationListener, Runnable, ShopsNearbyAllTabMenu.MenuCallBack {

    private static final int GET_SHOP_TYPES_SUCCESS = 99;
	private boolean LOCATION_SUCCESS = false;
    private LocationManagerProxy aMapLocManager = null;
    private AMapLocation aMapLocation;// 用于判断定位超时
    private LoadingDialog loadingDialog;

    public double currentLng ,currentLat;
    private ShopsNearbyAllTabMenu tabMenu;
    
    private ArrayList<StoreBusinessTypeModel> shopTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shops_nearby_new);
		initTopBar();
		getShopTypes();
	}
	
	private void getShopTypes() {
		Loading.run(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					shopTypes = ConfigHelper.getStoreBusinessType(ShopsNearbyActivity.this);
					StoreBusinessTypeModel allTypeModel = new StoreBusinessTypeModel();
					allTypeModel.setKeyName("全部");
					allTypeModel.setValue("0");
					shopTypes.add(0, allTypeModel);
					sendMessage(GET_SHOP_TYPES_SUCCESS);
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
		case GET_SHOP_TYPES_SUCCESS:
			startLocation();
			break;

		default:
			break;
		}
	}

	private void startLocation(){
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        aMapLocManager = LocationManagerProxy.getInstance(this);
        aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);

        handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位
	}

	private ViewPager mPager;
    private void initMainView() {
    	
		LinearLayout tabContainer = (LinearLayout) findViewById(R.id.tab_container);
        FragmentPagerAdapter adapter = new ShopNearbyPagerAdapter(getSupportFragmentManager(),shopTypes);

        mPager = (ViewPager)findViewById(R.id.viewPager);
        mPager.setAdapter(adapter);

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.RED;
            }
        });

        tabMenu = new ShopsNearbyAllTabMenu(this,shopTypes,this);
        ImageView moreBtn = (ImageView) findViewById(R.id.more_btn);
        moreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAllTabWindow();
			}
		});
        tabContainer.setVisibility(View.VISIBLE);
    }

    protected void showAllTabWindow() {
    	tabMenu.showMenu(titleView);
	}

    private RelativeLayout titleView;
	private void initTopBar(){
		titleView = (RelativeLayout) findViewById(R.id.title);
		ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.near_by_shops));
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


    @Override
    public void run() {
        if (aMapLocation == null) {
            PageUtil.DisplayToast("定位失败");
            stopLocation();// 销毁掉定位
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocation();// 停止定位
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (aMapLocManager != null) {
            aMapLocManager.removeUpdates(this);
            aMapLocManager.destory();
        }
        aMapLocManager = null;
    }

    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * 混合定位回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            this.aMapLocation = location;// 判断超时机制
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String cityCode = "";
            String desc = "";
            Bundle locBundle = location.getExtras();
            if (locBundle != null) {
                cityCode = locBundle.getString("citycode");
                desc = locBundle.getString("desc");
            }

            currentLng = geoLng;
            currentLat = geoLat;
            loadingDialog.dismiss();

            String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
                    + "\n精    度    :" + location.getAccuracy() + "米"
                    + "\n定位方式:" + location.getProvider() + "\n定位时间:"
                    + AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
                    + cityCode + "\n位置描述:" + desc + "\n省:"
                    + location.getProvince() + "\n市:" + location.getCity()
                    + "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
                    .getAdCode());
            LogUtil.d(str);

            if(!LOCATION_SUCCESS){
                LOCATION_SUCCESS = true;
                initMainView();
            }

            stopLocation();


        }
    }

	@Override
	public void changeToItem(int position) {
		if(mPager != null){
			mPager.setCurrentItem(position);
		}
	}
}
