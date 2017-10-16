package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.adapter.VipCardListAdapter;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ConfigHelper;
import com.vfaceuser.helper.VipCardHelper;

import java.util.ArrayList;

/**
 * Created by HuBin on 15/4/12.
 */
public class VIPCardManageActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

    private ListView listView;
    private VipCardListAdapter mAdapter;

    private int currentBusinessType = 0;
    private String currentStoreName = "";
    
    private boolean ifLoading;
    private final int MSG_GET_DATA_SUCCESS = -9;
    private final int MSG_GET_DATA_FAILED = -8;
    private final int GET_BUSINESS_TYPE_SUCCESS = -7;
    
    private  ArrayList<StoreBusinessTypeModel> businessTypeModels = new ArrayList<StoreBusinessTypeModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_card_manage);
        initMainView();
        getBusinessType();
        getData();
    }
    
    private void getBusinessType(){
    	Loading.run(VIPCardManageActivity.this, new Runnable() {
			
			@Override
			public void run() {
				try {
					businessTypeModels = ConfigHelper.getStoreBusinessType(VIPCardManageActivity.this);
					if(businessTypeModels != null){
						StoreBusinessTypeModel model = new StoreBusinessTypeModel();
						model.setKeyName("全部");
						model.setValue(0+"");
						businessTypeModels.add(0,model);
					}
					sendMessage(GET_BUSINESS_TYPE_SUCCESS);
				} catch (MyHttpException e) {
					e.printStackTrace();
				}
			}
		});
    }


    private EditText storeNameEdit;
    private Spinner shopTypeSpinner;
    private TextView searchBtn;
    private void initMainView() {

        initTopBar();

        storeNameEdit = (EditText) findViewById(R.id.store_name_edit);
        shopTypeSpinner = (Spinner) findViewById(R.id.shop_type_spinner);
        searchBtn = (TextView) findViewById(R.id.search_btn);

        shopTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	if(position > 0){
            		int index = position - 1;
            		currentBusinessType = Integer.parseInt(businessTypeModels.get(index).getValue());
            	}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchBtn.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new VipCardListAdapter(this,this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardListModel model = (CardListModel) mAdapter.getEntityList().get(position);
                Intent intent = new Intent(VIPCardManageActivity.this,VIPCardDetailActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);

            }
        });

    }

    private void initTopBar() {

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.vip_card_manage));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_btn:
            	currentStoreName = storeNameEdit.getText().toString();
            	getData();
                break;
            default:
                break;
        }

    }


    private void getData() {

        if(ifLoading){
            return;
        }
        Loading.run(this,new Runnable() {
            @Override
            public void run() {
                ifLoading = true;
                try {
                    ArrayList<CardListModel> cardListModels = VipCardHelper.getCardList(VIPCardManageActivity.this,currentBusinessType,currentStoreName);
                    if(cardListModels != null){
                        mAdapter.IsEnd = true;
                        sendMessage(MSG_GET_DATA_SUCCESS,cardListModels);
                    }else{
                        sendMessage(MSG_GET_DATA_FAILED);
                    }
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendMessage(MSG_GET_DATA_FAILED);
                }



            }
        });

    }

    @Override
    public void loadMore() {
    }

    @Override
    protected void handleMessage(Message msg) {

        switch (msg.what){
            case MSG_GET_DATA_SUCCESS:
                mAdapter.setEntityList((ArrayList) msg.obj);
                ifLoading = false;
                break;
            case MSG_GET_DATA_FAILED:
                ifLoading = false;
                break;
            case GET_BUSINESS_TYPE_SUCCESS:
            	if(businessTypeModels != null && businessTypeModels.size() > 0){
            		
            		ArrayList<String> data = new ArrayList<String>();
            		data.add("请选择行业");
            		for (StoreBusinessTypeModel model : businessTypeModels) {
            			data.add(model.getKeyName());
					}
            		
            		ArrayAdapter<String> spinnerAdapter = 
            				new ArrayAdapter<String>(this, 
            						R.layout.spinner_my,data);
            		shopTypeSpinner.setAdapter(spinnerAdapter);
            	}
            	break;
        }

        super.handleMessage(msg);
    }
}
