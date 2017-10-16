package com.vfaceuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.utils.AMapUtil;
import com.vfaceuser.utils.PageUtil;

/**
 * Created by HuBin on 15/4/12.
 */
public class ShopMapActivity extends BaseActivity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private ProgressDialog progDialog = null;
    private String addressName;
    private GeocodeSearch geocoderSearch;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint shopPoint;
    private Marker geoMarker;
    private Marker regeoMarker;
    private String shopName;
    private String shopAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map);

        double shopLongitude = getIntent().getDoubleExtra("shopLongitude", 0);
        double shopLatitude = getIntent().getDoubleExtra("shopLatitude", 0);
        shopName = getIntent().getStringExtra("shopName");
        shopAddress = getIntent().getStringExtra("shopAddress");
        shopPoint = new LatLonPoint(shopLatitude, shopLongitude);

        findViewById(R.id.back_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title_textview)).setText("商家位置");

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        getAddress(shopPoint);
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .title(shopName)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .title(shopName)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            regeoMarker.showInfoWindow();
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
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
    public void DisplayToastDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
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
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        DisplayToastDialog();
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        DisplayToastDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 0) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                        + address.getFormatAddress();
//                PageUtil.DisplayToast(addressName);
            } else {
                PageUtil.DisplayToast(R.string.no_result);
            }

        } else if (rCode == 27) {
            PageUtil.DisplayToast(R.string.error_network);
        } else if (rCode == 32) {
            PageUtil.DisplayToast(R.string.error_key);
        } else {
            PageUtil.DisplayToast(
                    getString(R.string.error_other) + rCode);
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(shopPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(shopPoint));
//                PageUtil.DisplayToast(addressName);
            } else {
                PageUtil.DisplayToast(R.string.no_result);
            }
        } else if (rCode == 27) {
            PageUtil.DisplayToast(R.string.error_network);
        } else if (rCode == 32) {
            PageUtil.DisplayToast(R.string.error_key);
        } else {
            PageUtil.DisplayToast(
                    getString(R.string.error_other) + rCode);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        openSystemMap(marker.getPosition().latitude,marker.getPosition().longitude,marker.getTitle());
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.activity_shop_map_custom_info_window, null);
        TextView shopNameView = (TextView) infoWindow.findViewById(R.id.shopName);
        TextView shopAddressView = (TextView) infoWindow.findViewById(R.id.shop_address);
        shopNameView.setText(shopName);
        shopAddressView.setText(shopAddress);
        return infoWindow;

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    private void openSystemMap(double shopLatitude,double shopLongitude,String name){
        Uri mUri = Uri.parse("geo:"+shopLatitude+","+shopLongitude+"?q="+name);
        Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
        startActivity(mIntent);
    }
}
