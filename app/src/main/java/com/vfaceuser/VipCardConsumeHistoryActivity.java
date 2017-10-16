package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.adapter.ConsumeHistoryListAdapter;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.DatePickerDialog;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.VipCardHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.PageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VipCardConsumeHistoryActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

    private ListView listView;
    private ConsumeHistoryListAdapter mAdapter;

    @ViewInject(id = R.id.txt_start_time)
    TextView startTimeView;

    @ViewInject(id = R.id.txt_end_time)
    TextView endTimeView;

    @ViewInject(id = R.id.search_btn)
    TextView searchBtn;

    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean ifLoading;
    private final int MSG_GET_DATA_SUCCESS = -9;
    private final int MSG_GET_DATA_FAILED = -8;
    private final int MSG_LOADMORE_SUCCESS = -7;

    
    private String storeId;
    private String currentStartTimeText;
    private String currentEndTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_card_consume_history);
        initMainView();

        initIntentData();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        currentEndTimeText = df.format(new Date());// new Date()为获取当前系统时间
        
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        currentStartTimeText = df.format(c.getTime());
        startTimeView.setText(currentStartTimeText);
        
        endTimeView.setText(currentEndTimeText);

        if(storeId != null){
            getData();
        }else{
        	finish();
        }

    }

    private void initIntentData() {

    	CardListModel model = (CardListModel) getIntent().getSerializableExtra("model");
        storeId = model.getStoreId();
    }

    private void initMainView() {

        initTopBar();
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ConsumeHistoryListAdapter(this,this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseListModel expenseListModel = (ExpenseListModel) mAdapter.getItem(position);
                Intent intent = new Intent(VipCardConsumeHistoryActivity.this,ConsumeDetailActivity.class);
                intent.putExtra("expenseListModel",expenseListModel);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(this);
    }

    private void initTopBar() {

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.consume_history));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_btn:
            	getData();
            	break;
            default:
                break;
        }

    }

    private void getData() {

        final String startTimeText = startTimeView.getText().toString();
        if(TextUtils.isEmpty(startTimeText)){
            PageUtil.DisplayToast("请选择开始时间");
            return;
        }
        final String endTimeText = endTimeView.getText().toString();
        if(TextUtils.isEmpty(endTimeText)){
            PageUtil.DisplayToast("请选择结束时间");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            if(!startTimeText.equals(endTimeText)){
                Date startTimeDate = sdf.parse(startTimeText);
                Date endTimeDate = sdf.parse(endTimeText);
                boolean ifRightDate = startTimeDate.before(endTimeDate);
                if(!ifRightDate){
                    PageUtil.DisplayToast("结束时间要大于开始时间!");
                    return;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(ifLoading){
            return;
        }
        mAdapter.IsEnd = false;
        pageIndex = 1;
        currentStartTimeText = startTimeText;
        currentEndTimeText = endTimeText;

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;

                try {
                	int expenseType = 0;
                	String keywords = "";
                	
                    ArrayList<ExpenseListModel> expenseListModels = VipCardHelper.getVipCardExpenseList(
                    		
                    		VipCardConsumeHistoryActivity.this,
                            storeId,expenseType,keywords
                            ,startTimeText,endTimeText, pageIndex, pageSize);

                    if(expenseListModels.size() < pageSize){
                        mAdapter.IsEnd = true;
                    }
                    sendMessage(MSG_GET_DATA_SUCCESS, expenseListModels);
                    pageIndex++;

                } catch (MyHttpException e) {
                    e.printStackTrace();
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
        Loading.run(this, new Runnable() {
            @Override
            public void run() {

                ifLoading = true;

                try {
                	int expenseType = 0;
                	String keywords = "";
                	
                    ArrayList<ExpenseListModel> expenseListModels = VipCardHelper.getVipCardExpenseList(
                    		VipCardConsumeHistoryActivity.this,
                            storeId,expenseType,keywords
                            ,currentStartTimeText,currentEndTimeText, pageIndex, pageSize);

                    if(expenseListModels.size() < pageSize){
                        mAdapter.IsEnd = true;
                    }
                    sendMessage(MSG_LOADMORE_SUCCESS, expenseListModels);
                    pageIndex++;

                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendMessage(MSG_GET_DATA_FAILED);
                }

            }
        });

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
            case MSG_LOADMORE_SUCCESS:
                mAdapter.addEntityList((ArrayList) msg.obj);
                ifLoading = false;
                break;
        }

        super.handleMessage(msg);
    }


    public void selectStartTime(View v){
        showDataPickerDialog(TIME_TYPE_START,currentStartTimeText);
    }

    public void selectEndTime(View v){
        showDataPickerDialog(TIME_TYPE_END,currentEndTimeText);
    }

    private final int TIME_TYPE_START = 0;
    private final int TIME_TYPE_END = 1;
    private void showDataPickerDialog(final int whichTime,String currentDate){

        DatePickerDialog dialog = new DatePickerDialog(this,currentDate,new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void confirm(String date) {
                PageUtil.DisplayToast(date);
                if(whichTime == TIME_TYPE_START){
                    startTimeView.setText(date);
                    currentStartTimeText = date;
                }else{
                    endTimeView.setText(date);
                    currentEndTimeText = date;
                }

            }
        });

        dialog.show();

    }
}
