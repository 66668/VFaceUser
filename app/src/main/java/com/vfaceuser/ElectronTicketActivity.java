package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vfaceuser.adapter.CountsExpenseDetailListAdapter;
import com.vfaceuser.bizmodel.CountsExpenseDetailModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.bizmodel.TimeExpenseDetailModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ExpenseHelper;
import com.vfaceuser.utils.PageUtil;

/**
 * Created by HuBin on 15/4/24.
 */
public class ElectronTicketActivity extends BaseActivity implements View.OnClickListener {

    private static final int MSG_GET_COUNTS_EXPENSE_DATA_SUCCESS = -9;
	private static final int MSG_GET_TIME_EXPENSE_DATA_SUCCESS = -8;
	private ExpenseListModel electronTicketModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        cloneModel();
        if(electronTicketModel == null){
        	PageUtil.DisplayToast("数据出错");
        	finish();
        	return;
        }
        
        if(electronTicketModel.getType() == 1){
        	initNormalTypeView();
        }else if(electronTicketModel.getType() == 2){
        	if(electronTicketModel.getExpenseType() == 3){
        		initTimeTypeView();
        		loadTimeExpenseData();
        	}else if(electronTicketModel.getExpenseType() == 4){
            	initCountTypeView();
            	loadCountsExpenseData();
        	}

        }else{
        	finish();
        }
       
    }
    
    private void initTopBar() {

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText("电子票号");

    }
    
    //普通消费界面
    private void initNormalTypeView(){
    	 setContentView(R.layout.activity_electron_ticket);
         initTopBar();
         TextView ticketNumView = (TextView) findViewById(R.id.ticket_num);
         TextView moneyView = (TextView) findViewById(R.id.money);
         TextView storeNameView = (TextView) findViewById(R.id.store_name);
         TextView timeView = (TextView) findViewById(R.id.time);
         
         ticketNumView.setText("电子票号："+electronTicketModel.getOrderNumber());
         storeNameView.setText("商家："+electronTicketModel.getStoreName());
         moneyView.setText("金额："+electronTicketModel.getTotalAmount()+"元");
         timeView.setText("消费时间："+electronTicketModel.getExpenseDate());
    }

    //查看消费详情－－普通消费
    public void seeDetail(View v){
        Intent intent = new Intent(this, ConsumeDetailActivity.class);
        intent.putExtra("expenseListModel",electronTicketModel);
        startActivity(intent);
    }
    
    
    //计次消费界面
    private void initCountTypeView(){
    	setContentView(R.layout.activity_electron_ticket_for_count_type);
        initTopBar();
        
    }
    
    private CountsExpenseDetailListAdapter countsExpenseDetailListAdapter;
    private void refreshCountTypeView(CountsExpenseDetailModel countsModel){
    	TextView storeNameView = (TextView) findViewById(R.id.store_name);
        TextView timeView = (TextView) findViewById(R.id.time);
        TextView countsView = (TextView) findViewById(R.id.counts);
        TextView ticketNumView = (TextView) findViewById(R.id.ticket_num);
        
        ListView listView = (ListView) findViewById(R.id.listView);
        countsExpenseDetailListAdapter = new CountsExpenseDetailListAdapter(this,new CountsExpenseDetailListAdapter.AdapterCallBack() {
			
			@Override
			public void loadMore() {
				//nothing
			}
		});
        listView.setAdapter(countsExpenseDetailListAdapter);
        storeNameView.setText("商家："+countsModel.getStoreName());
        countsView.setText("消费次数："+countsModel.getTotalExpenseData());
        timeView.setText("消费时间："+countsModel.getExpenseDate());
        countsExpenseDetailListAdapter.setEntityList(countsModel.getExpenseDetails());

        ticketNumView.setText("电子票号："+countsModel.getOrderNumber());
    }
    
    private void loadCountsExpenseData(){
    	Loading.run(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					CountsExpenseDetailModel model = ExpenseHelper.getCountsExpenseDetail(
							ElectronTicketActivity.this, electronTicketModel.getStoreId(),
							electronTicketModel.getOrderNumber());
					sendMessage(MSG_GET_COUNTS_EXPENSE_DATA_SUCCESS,model);
				} catch (MyHttpException e) {
					e.printStackTrace();
					sendToastMessage(e.getMessage());
				}
			}
		});
    }
    
    //计时消费界面
    private void initTimeTypeView(){
    	setContentView(R.layout.activity_electron_ticket_for_time_type);
    	initTopBar();
//    	TextView ticketNumView = (TextView) findViewById(R.id.ticket_num);
//        ticketNumView.setText("电子票号："+electronTicketModel.getOrderNumber());
    }
    
    private void refreshTimeTypeView(TimeExpenseDetailModel model){
    	TextView storeNameView = (TextView) findViewById(R.id.store_name);
        TextView expenseNameView = (TextView) findViewById(R.id.expense_name);
        TextView startTimeView = (TextView) findViewById(R.id.start_time);
        TextView endTimeView = (TextView) findViewById(R.id.end_time);
        TextView expenseTimeView = (TextView) findViewById(R.id.expense_time);
        TextView remainingTimeView = (TextView) findViewById(R.id.remaining_time);
        TextView ticketNumView = (TextView) findViewById(R.id.ticket_num);
        
        storeNameView.setText("商家："+model.getStoreName());
        expenseNameView.setText("消费项目："+model.getServiceName());
        startTimeView.setText("开始时间："+model.getBeginDate());
        endTimeView.setText("结束时间："+model.getEndDate());
        expenseTimeView.setText("消费时长："+model.getExpensedTime()+"分钟");
        remainingTimeView.setText("剩余时长："+model.getRemainTime()+"分钟");
        ticketNumView.setText("电子票号："+model.getOrderNumber());
        
    }
    
    private void loadTimeExpenseData(){
    	Loading.run(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					TimeExpenseDetailModel model = ExpenseHelper.getTimeExpenseDetail(
							ElectronTicketActivity.this, electronTicketModel.getStoreId(),
							electronTicketModel.getOrderNumber());
					sendMessage(MSG_GET_TIME_EXPENSE_DATA_SUCCESS,model);
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
		case MSG_GET_COUNTS_EXPENSE_DATA_SUCCESS:
			CountsExpenseDetailModel countsModel = (CountsExpenseDetailModel) msg.obj;
			refreshCountTypeView(countsModel);
			break;
		case MSG_GET_TIME_EXPENSE_DATA_SUCCESS:
			TimeExpenseDetailModel timeModel = (TimeExpenseDetailModel) msg.obj;
			refreshTimeTypeView(timeModel);
			break;
		default:
			break;
		}
    }

    private void cloneModel() {

        electronTicketModel = new ExpenseListModel();
        ExpenseListModel clickTicketModel = MyApplication.getInstance().ClickMember;
        electronTicketModel.setExpenseDate(clickTicketModel.getExpenseDate());
        electronTicketModel.setExpenseId(clickTicketModel.getExpenseId());
        electronTicketModel.setExpenseType(clickTicketModel.getExpenseType());
        electronTicketModel.setMemberName(clickTicketModel.getMemberName());
        electronTicketModel.setOrderNumber(clickTicketModel.getOrderNumber());
        electronTicketModel.setStoreId(clickTicketModel.getStoreId());
        electronTicketModel.setStoreMemberId(clickTicketModel.getStoreMemberId());
        electronTicketModel.setStoreName(clickTicketModel.getStoreName());
        electronTicketModel.setTotalAmount(clickTicketModel.getTotalAmount());
        electronTicketModel.setTotalPoints(clickTicketModel.getTotalPoints());
        electronTicketModel.setTotalQuantity(clickTicketModel.getTotalQuantity());
        electronTicketModel.setType(clickTicketModel.getType());
        MyApplication.getInstance().ClickMember = null;

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
}
