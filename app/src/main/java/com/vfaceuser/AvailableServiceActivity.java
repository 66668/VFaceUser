package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vfaceuser.adapter.AvailableServiceListAdapter;
import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.bizmodel.AvailableServiceModel;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.VipCardHelper;

import java.util.ArrayList;



/**
 * Created by HuBin on 15/5/21.
 */
public class AvailableServiceActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

    private static final int MSG_GET_AVAILABLE_SERVICE_SUCCESS = -9;
	private CardListModel cardListModel;
    private boolean ifLoading;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_service_list);
        cardListModel = (CardListModel) getIntent().getSerializableExtra("model");
        if(cardListModel == null){
            finish();
            return;
        }
        initTopBar();
        initMainView();
        getData();
    }

    private void initTopBar() {
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.vip_card_detail));
    }

    AvailableServiceListAdapter mAdapter;
    private void initMainView() {
        TextView cardNumber = (TextView) findViewById(R.id.card_number);
        TextView shopName = (TextView) findViewById(R.id.shop_name);
        TextView leverView = (TextView) findViewById(R.id.level);
        TextView moneyView = (TextView) findViewById(R.id.money);
        TextView pointsView = (TextView) findViewById(R.id.points);

        cardNumber.setText(cardListModel.getMemberCardNumber()+"");
        shopName.setText(cardListModel.getStoreName()+"");
        leverView.setText(cardListModel.getGradeName());
        moneyView.setText(cardListModel.getTotalMoney());
        pointsView.setText(cardListModel.getTotalPoints());

        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new AvailableServiceListAdapter(this,this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AvailableServiceActivity.this,AvailableServiceDetailActivity.class);
                AvailableServiceModel availableServiceModel = (AvailableServiceModel) mAdapter.getItem(position);
                intent.putExtra("AvailableServiceModel", availableServiceModel);
                intent.putExtra("CardListModel", cardListModel);
                startActivity(intent);
            }
        });
    }


    /**
     * 
     * @param 
     */
    private void getData() {

    	if(ifLoading){
    		return;
    	}
    	
    	Loading.run(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					ifLoading = true;
					//businessType 计时TimeExpense = 3,计次CountExpense = 4

					ArrayList<AvailableServiceModel> models = new ArrayList<AvailableServiceModel>();
					
					try {
						ArrayList<AvailableServiceModel> countExpenseModels = VipCardHelper.getAvailableService(AvailableServiceActivity.this,cardListModel.getStoreMemberId(), 4, cardListModel.getStoreId());
						models.addAll(countExpenseModels);
					}catch (MyHttpException e) {
						e.printStackTrace();
						sendToastMessage(e.getMessage());
					}
					
					try {
						ArrayList<AvailableServiceModel> timeExpenseModels = VipCardHelper.getAvailableService(AvailableServiceActivity.this,cardListModel.getStoreMemberId(), 3, cardListModel.getStoreId());
						models.addAll(timeExpenseModels);
					}catch (MyHttpException e) {
						e.printStackTrace();
						sendToastMessage(e.getMessage());
					}


					mAdapter.IsEnd = true;
					sendMessage(MSG_GET_AVAILABLE_SERVICE_SUCCESS,models);
				} catch (Exception e) {
					e.printStackTrace();
					ifLoading = false;
					sendToastMessage(e.getMessage());
				}
			}
		});
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
    public void loadMore() {

    }
    
    @Override
    protected void handleMessage(Message msg) {
    	super.handleMessage(msg);
    	switch (msg.what) {
		case MSG_GET_AVAILABLE_SERVICE_SUCCESS:
			mAdapter.setEntityList((ArrayList<AvailableServiceModel>)msg.obj);
			ifLoading = false;
			break;

		default:
			break;
		}
    }
}
