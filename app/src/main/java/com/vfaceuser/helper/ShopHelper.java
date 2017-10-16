package com.vfaceuser.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.ShopModel;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HuBin on 15/4/24.
 */
public class ShopHelper {

    /**
     * 商家详细信息
     * @param context
     * @return
     * @throws MyHttpException
     */
    public static ShopModel getShopDetailInfo(Context context,String storeId) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Shop.SHOP_DETAIL_INFO + storeId;

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.returnObject.toString(), ShopModel.class);

    }

    /**
     * 获取附近商家
     * @param context
     * @param pageIndex
     * @param pageSize
     * @param lng
     * @param lat
     * @return
     * @throws MyHttpException
     */
    public static ArrayList<ShopModel> getNearbyShopList(Context context,String storeName,int pageIndex,int pageSize,double lat,double lng,String businessType) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Shop.GET_NEARBY_STORE_URL;
        url += pageIndex+"/"+pageSize+"/"+lat+"/"+lng+"/?BusinessType="+businessType;
        url += "&storeName="+storeName;
//        if(!TextUtils.isEmpty(storeName)){
//        }

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.DataList.toString(),
                new TypeToken<List<ShopModel>>() {
                }.getType());

    }
    

}
