package com.vfaceuser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vfaceuser.adapter.CommonListAdapter;
import com.vfaceuser.adapter.ConsumeHistoryListAdapter;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.commen.BaseActivity;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.dialog.DatePickerDialog;
import com.vfaceuser.dialog.Loading;
import com.vfaceuser.helper.ExpenseHelper;
import com.vfaceuser.inject.ViewInject;
import com.vfaceuser.utils.PageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HuBin on 15/4/12.
 */
public class ConsumeHistoryActivity extends BaseActivity implements View.OnClickListener, CommonListAdapter.AdapterCallBack {

    private ListView listView;
    private ConsumeHistoryListAdapter mAdapter;

    @ViewInject(id = R.id.searchKeyEdit)
    EditText searchKeyEdit;

    @ViewInject(id = R.id.btn_clear_edit,click = "clearEdit")
    ImageView btnClearEdit;

    @ViewInject(id = R.id.spinnerType)
    Spinner spinnerType;

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

    private String currentKey = "";
    private int selectedType = 0;
    private int currentType = 0;
    private String currentStartTimeText;
    private String currentEndTimeText;
    private CardListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_history);
        initMainView();

        initIntentData();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        currentStartTimeText = df.format(c.getTime());
        currentEndTimeText = df.format(new Date());// new Date()为获取当前系统时间

        startTimeView.setText(currentStartTimeText);
        endTimeView.setText(currentEndTimeText);

        if(model == null){
            getData(currentKey);
        }else{
            searchKeyEdit.setText(model.getStoreName());
            getData(model.getStoreName());
        }

    }

    private void initIntentData() {

        model = (CardListModel) getIntent().getSerializableExtra("model");

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
                Intent intent = new Intent(ConsumeHistoryActivity.this,ConsumeDetailActivity.class);
                intent.putExtra("expenseListModel",expenseListModel);
                startActivity(intent);
            }
        });
        searchKeyEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {//keyCode == 66 获取Enter键

                    String searchKey = searchKeyEdit.getText().toString();
                    getData(searchKey);
                    return true;
                }
                return false;
            }
        });

        final ArrayList<String> typeStringList = new ArrayList<String>();
        typeStringList.add("所有");
        typeStringList.add("商品消费");
        typeStringList.add("快速消费");
        typeStringList.add("计时充值");
        typeStringList.add("计次充值");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_my, typeStringList);
        spinnerType.setAdapter(arrayAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            	// 0: 所有  1: 商品消费   2:  快速消费  5:  计时充值  6: 计次充值
            	switch (position) {
				case 0:
				case 1:
				case 2:
					selectedType = position;
					break;
				case 3:
					selectedType = 5;
					break;
				case 4:
					selectedType = 6;
					break;
				default:
					break;
				}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = 0;
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
                String searchKey = searchKeyEdit.getText().toString();
                getData(searchKey);
                break;
            default:
                break;
        }

    }

    private void getData(final String storeName) {

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
        currentType = selectedType;
        mAdapter.IsEnd = false;
        pageIndex = 1;
        currentKey = storeName;
        currentStartTimeText = startTimeText;
        currentEndTimeText = endTimeText;

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                ifLoading = true;

                try {
                    String memberCardNum = "";//TODO
                    ArrayList<ExpenseListModel> expenseListModels = ExpenseHelper.getExpenseList(ConsumeHistoryActivity.this,
                            memberCardNum,currentType,storeName
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

                String memberCardNum = "";//TODO
                ifLoading = true;

                try {
                    ArrayList<ExpenseListModel> expenseListModels = ExpenseHelper.getExpenseList(ConsumeHistoryActivity.this,
                            memberCardNum,currentType,currentKey
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

    public void clearEdit(View v){
        searchKeyEdit.setText("");
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
