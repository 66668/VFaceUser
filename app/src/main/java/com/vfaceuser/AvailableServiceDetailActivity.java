package com.vfaceuser;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vfaceuser.adapter.AvailableServiceDetailListAdapter;
import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.bizmodel.AvailableServiceModel;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.bizmodel.ServiceExpenseHistoryModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.VipCardHelper;

import java.util.ArrayList;

/**
 * 可用服务使用详情
 * Created by HuBin on 15/5/21.
 */
public class AvailableServiceDetailActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

	private static final int MSG_GET_SERVICE_EXPENSE_HISTORY_SUCCESS = -9;
	private AvailableServiceModel availableServiceModel;
	private CardListModel cardListModel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_service_detail);
        initIntentData();
        initTopBar();
        initMainView();
        getData();

    }

    private void initIntentData() {
    	availableServiceModel = (AvailableServiceModel) getIntent().getSerializableExtra("AvailableServiceModel");
    	cardListModel = (CardListModel) getIntent().getSerializableExtra("CardListModel");
	}

	private void initTopBar() {
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.service_detail));
    }

    AvailableServiceDetailListAdapter mAdapter;
    private void initMainView() {

    	TextView countsView = (TextView) findViewById(R.id.counts);
        TextView storeNameView = (TextView) findViewById(R.id.store_name);
        TextView serviceNameView = (TextView) findViewById(R.id.service_name);
        TextView availableTimesView = (TextView) findViewById(R.id.available_times);
//        TextView totalTimesView = (TextView) findViewById(R.id.total_times);
        
        if(availableServiceModel != null){
        	serviceNameView.setText(availableServiceModel.getServiceName());
        	if(availableServiceModel.getExpresnseType() == 3){
        		availableTimesView.setText("剩余(分钟)："+availableServiceModel.getInventoryData());
        		countsView.setText("时长(分钟)");
        	}else{
        		availableTimesView.setText("剩余(次)："+availableServiceModel.getInventoryData());
        		countsView.setText("次数");
        	}
//        	totalTimesView.setText("");
        }
        
        if(cardListModel != null){
        	storeNameView.setText(cardListModel.getStoreName()+"");
        }
        
        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new AvailableServiceDetailListAdapter(this,this);
        listView.setAdapter(mAdapter);

    }

    private void getData() {

    	Loading.run(this, new Runnable() {
			
			@Override
			public void run() {
				
				try {
					ArrayList<ServiceExpenseHistoryModel> models= VipCardHelper.getServiceExpenseHistory(AvailableServiceDetailActivity.this,
							cardListModel.getStoreMemberId(),
							availableServiceModel.getExpresnseType(),
							cardListModel.getStoreId(),
							availableServiceModel.getServiceId());
					mAdapter.IsEnd = true;
					sendMessage(MSG_GET_SERVICE_EXPENSE_HISTORY_SUCCESS,models);
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
		case MSG_GET_SERVICE_EXPENSE_HISTORY_SUCCESS:	
			ArrayList<ServiceExpenseHistoryModel> models = (ArrayList<ServiceExpenseHistoryModel>) msg.obj;
			mAdapter.setEntityList(models);
			break;

		default:
			break;
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

    @Override
    public void loadMore() {

    }
}
