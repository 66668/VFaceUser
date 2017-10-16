package com.vfaceuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.utils.PageUtil;

/**
 * Created by HuBin on 15/4/12.
 */
public class NewShopMapActivity extends BaseActivity implements View.OnClickListener, RouteSearch.OnRouteSearchListener {

    private ProgressDialog progDialog = null;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint shopPoint;
    private LatLonPoint userPoint;
    private RouteSearch routeSearch;
    private DriveRouteResult driveRouteResult;// 驾车模式查询结果
    private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);

        double shopLongitude = getIntent().getDoubleExtra("shopLongitude", 0);
        double shopLatitude = getIntent().getDoubleExtra("shopLatitude", 0);
        double userLongitude = getIntent().getDoubleExtra("userLongitude", 0);
        double userLatitude = getIntent().getDoubleExtra("userLatitude", 0);
        String shopName = getIntent().getStringExtra("shopName");

        shopPoint = new LatLonPoint(shopLatitude, shopLongitude);
        userPoint = new LatLonPoint(userLatitude, userLongitude);

        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_textview)).setText("商家位置");

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        searchRouteResult(userPoint,shopPoint);
        openSystemMap(shopLatitude,shopLongitude,shopName);
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        progDialog = new ProgressDialog(this);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 显示进度条对话框
     */
    public void showProgressDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {

//        startPoint = new LatLonPoint(31.321473,120.756701);
//        endPoint = new LatLonPoint(31.310336,120.775709);

        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode,
                null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }


    /**
     * 公交路线查询回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        dissmissProgressDialog();
    }

    /**
     * 驾车结果回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                driveRouteResult = result;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this, aMap, drivePath, driveRouteResult.getStartPos(),
                        driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                PageUtil.DisplayToast( R.string.no_result);
            }
        } else if (rCode == 27) {
            PageUtil.DisplayToast( R.string.error_network);
        } else if (rCode == 32) {
            PageUtil.DisplayToast( R.string.error_key);
        } else {
            PageUtil.DisplayToast( getString(R.string.error_other)
                    + rCode);
        }
    }

    /**
     * 步行路线结果回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        dissmissProgressDialog();
    }

    private void openSystemMap(double shopLatitude,double shopLongitude,String name){
        Uri mUri = Uri.parse("geo:"+shopLatitude+","+shopLongitude+"?q="+name);
        Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
        startActivity(mIntent);
    }
}
