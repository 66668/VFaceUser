package com.vfaceuser;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.adapter.ConsumeDetailListAdapter;
import com.vfaceuser.adapter.ConsumeHistoryListAdapter;
import com.vfaceuser.bizmodel.ExpenseDetailModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ExpenseHelper;
import com.vfaceuser.utils.PageUtil;

import java.util.ArrayList;

/**
 * Created by HuBin on 15/4/12.
 */
public class ConsumeDetailActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

    private ListView listView;
    private ConsumeDetailListAdapter mAdapter;

    private boolean ifLoading;
    private final int MSG_GET_DATA_SUCCESS = -9;
    private final int MSG_GET_DATA_FAILED = -8;

    private ExpenseListModel expenseListModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_detail);
        expenseListModel = (ExpenseListModel) getIntent().getSerializableExtra("expenseListModel");
        if(expenseListModel == null){
            PageUtil.DisplayToast("数据有误");
            finish();
            return;
        }
        initMainView();
        getData();

    }

    private TextView serialNumberView;
    private TextView moneyView;
    private TextView pointsView;
    private TextView shopNameView;
    private TextView timeView;
    private TextView countView;
    private TextView priceView;
    private TextView consumeTypeView;
    private LinearLayout listBar;
    private void initMainView() {

        initTopBar();
        serialNumberView = (TextView) findViewById(R.id.serial_number);
        moneyView = (TextView) findViewById(R.id.money);
        pointsView = (TextView) findViewById(R.id.points);
        shopNameView = (TextView) findViewById(R.id.shop_name);
        timeView = (TextView) findViewById(R.id.time);
        consumeTypeView = (TextView) findViewById(R.id.consume_type);
        if(expenseListModel.getOrderNumber() != null)
            serialNumberView.setText(expenseListModel.getOrderNumber());

        moneyView.setText(expenseListModel.getTotalAmount()+"元");

        pointsView.setText(expenseListModel.getTotalPoints()+"");
        if(expenseListModel.getStoreName() != null)
            shopNameView.setText(expenseListModel.getStoreName());

        if(expenseListModel.getExpenseDate() != null)
            timeView.setText(expenseListModel.getExpenseDate());

        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ConsumeDetailListAdapter(this,this);
        listView.setAdapter(mAdapter);

        listBar = (LinearLayout) findViewById(R.id.list_bar);
        countView = (TextView) findViewById(R.id.counts);
        priceView = (TextView) findViewById(R.id.price);

        String expenseTypeName = ConsumeHistoryListAdapter.getExpenseName(expenseListModel.getExpenseType());
        consumeTypeView.setText(expenseTypeName);
        if(expenseListModel.getExpenseType() == 2){//快速消费
            listBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        }else if(expenseListModel.getExpenseType() == 5){ //计时消费
            countView.setText("分钟");
            priceView.setVisibility(View.GONE);
            mAdapter.setExpenseType(6);
        }else if(expenseListModel.getExpenseType() == 6){ //计次消费
            countView.setText("数量(次)");
        }
    }

    private void initTopBar() {

        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleView = (TextView) findViewById(R.id.title_textview);
        backBtn.setOnClickListener(this);
        titleView.setText(getString(R.string.electronic_invoice));

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

    private void getData() {

        if(ifLoading){
            return;
        }
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;

                try {
                    ArrayList<ExpenseDetailModel> expenseDetailModels = ExpenseHelper.getExpenseDetailModel(ConsumeDetailActivity.this, expenseListModel.getStoreId(), expenseListModel.getExpenseId(), expenseListModel.getExpenseType());
                    mAdapter.IsEnd = true;
                    sendMessage(MSG_GET_DATA_SUCCESS, expenseDetailModels);
                } catch (MyHttpException e) {
                    e.printStackTrace();
                    sendMessage(MSG_GET_DATA_FAILED);
                }

            }
        });

    }

    @Override
    public void loadMore() {
        getData();
    }

    @Override
    protected void handleMessage(Message msg) {

        switch (msg.what){
            case MSG_GET_DATA_SUCCESS:
                mAdapter.addEntityList((ArrayList) msg.obj);
                ifLoading = false;
                break;
            case MSG_GET_DATA_FAILED:
                ifLoading = false;
                break;
        }

        super.handleMessage(msg);
    }
}
