package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.ShopModel;

/**
 * Created by HuBin on 15/4/12.
 */
public class ShopNearbyListAdapter extends CommonListAdapter {


    public ShopNearbyListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);

    }

    @Override
    protected View inflateConvertView() {

        return inflater.inflate(R.layout.fragment_shops_nearby_list_item,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

        TextView shopNameView = (TextView) convertView.findViewById(R.id.shop_name);
        TextView shopLocationView = (TextView) convertView.findViewById(R.id.shop_location);
        TextView shopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
        TextView businessTypeNameView = (TextView) convertView.findViewById(R.id.businesstype_name);
        ShopModel shopModel = (ShopModel) entityList.get(position);

        shopNameView.setText(shopModel.getStorename());
        shopLocationView.setText(shopModel.getAddress());
        shopDistanceView.setText(shopModel.getFriendlyDistance());
        businessTypeNameView.setText("行业："+shopModel.getBusinesstypename());

    }
}
