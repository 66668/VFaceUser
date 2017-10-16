package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.adapter.ShopNearbyListAdapter;
import com.vfaceuser.bizmodel.ShopModel;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;
import com.vfaceuser.commen.BaseFragment;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ShopHelper;

import java.util.ArrayList;

/**
 * Created by HuBin on 15/4/12.
 */
public class ShopsNearbyListFragment extends BaseFragment implements CommonListAdapter.AdapterCallBack {

    private StoreBusinessTypeModel shopType;

    private ListView shopListView;
    private ShopNearbyListAdapter mAdapter;
    private EditText editText;
    private ImageView clearEditBtn;

    private final int MSG_GET_DATA_SUCCESS = -9;
    private final int MSG_GET_DATA_FAILED = -8;
    private final int MSG_LOADMORE_SUCCESS = -7;

    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean ifLoading;

    private String currentKey = "";

    public ShopsNearbyListFragment(){

    }

    public static ShopsNearbyListFragment newInstance(StoreBusinessTypeModel shopType){

        ShopsNearbyListFragment fragment = new ShopsNearbyListFragment();
        fragment.shopType = shopType;

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_shops_nearby,null);

        shopListView = (ListView) mainView.findViewById(R.id.shops_listView);
        mAdapter = new ShopNearbyListAdapter(getActivity(),this);
        shopListView.setAdapter(mAdapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ShopModel shopModel = (ShopModel) mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),ShopDetailActivity.class);
                intent.putExtra("shopModel",shopModel);
                intent.putExtra("userLatitude",((ShopsNearbyActivity)getActivity()).currentLat);
                intent.putExtra("userLongitude",((ShopsNearbyActivity)getActivity()).currentLng);

                startActivity(intent);
            }
        });

        editText = (EditText) mainView.findViewById(R.id.searchKeyEdit);
        clearEditBtn = (ImageView) mainView.findViewById(R.id.btn_clear_edit);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {//keyCode == 66 获取Enter键

                    String searchKey = editText.getText().toString();
                    getData(searchKey);
                    return true;
                }
                return false;
            }
        });
        clearEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        getData("");
        return mainView;
    }

    private void getData(final String storeName){

        if(ifLoading){
            return;
        }

        currentKey = storeName;
        pageIndex = 1;
        mAdapter.IsEnd = false;

        Loading.run(getActivity(),new Runnable() {
            @Override
            public void run() {

                ifLoading = true;

                try {

                    ArrayList<ShopModel> shopList = ShopHelper.getNearbyShopList(getActivity(),storeName,pageIndex,pageSize,
                            ((ShopsNearbyActivity)getActivity()).currentLat,
                            ((ShopsNearbyActivity)getActivity()).currentLng,shopType.getValue());
                    if(shopList.size() < pageSize){
                        mAdapter.IsEnd = true;
                    }
                    pageIndex++;
                    sendMessage(MSG_GET_DATA_SUCCESS, shopList);
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendToastMessage(e.getMessage());
                    sendMessage(MSG_GET_DATA_FAILED);
                }


            }
        });
    }

    @Override
    public void loadMore() {

        if(ifLoading){
            return;
        }

        Loading.run(getActivity(),new Runnable() {
            @Override
            public void run() {

                ifLoading = true;

                try {

                    ArrayList<ShopModel> shopList = ShopHelper.getNearbyShopList(getActivity(),currentKey,pageIndex,pageSize,
                            ((ShopsNearbyActivity)getActivity()).currentLat,
                            ((ShopsNearbyActivity)getActivity()).currentLng,shopType.getValue());
                    if(shopList.size() < pageSize){
                        mAdapter.IsEnd = true;
                    }
                    pageIndex++;
                    sendMessage(MSG_LOADMORE_SUCCESS, shopList);
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendToastMessage(e.getMessage());
                    sendMessage(MSG_GET_DATA_FAILED);
                }


            }
        });

    }

    @Override
    protected void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_GET_DATA_SUCCESS:
                mAdapter.setEntityList((ArrayList<ShopModel>) msg.obj);
                ifLoading = false;
                break;
            case MSG_LOADMORE_SUCCESS:
                mAdapter.addEntityList((ArrayList<ShopModel>) msg.obj);
                ifLoading = false;
                break;
            case MSG_GET_DATA_FAILED:
                ifLoading = false;
                break;
            default:
                break;
        }

        super.handleMessage(msg);
    }

}
