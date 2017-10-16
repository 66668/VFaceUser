package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.ExpenseListModel;


/**
 * Created by HuBin on 15/4/12.
 */
public class ConsumeHistoryListAdapter extends CommonListAdapter{

    public ConsumeHistoryListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
    }

    @Override
    protected View inflateConvertView() {
        return inflater.inflate(R.layout.activity_consume_history_listitem,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView shopTypeView = (TextView) convertView.findViewById(R.id.shopType);
        TextView serialNumberView = (TextView) convertView.findViewById(R.id.serial_number);
        TextView dateTimeView = (TextView) convertView.findViewById(R.id.date_time);
        TextView moneyView = (TextView) convertView.findViewById(R.id.money);
        TextView expenseTypeView = (TextView) convertView.findViewById(R.id.expenseType);

        ExpenseListModel expenseListModel = (ExpenseListModel) entityList.get(position);
        userNameView.setText(expenseListModel.getStoreName());
        shopTypeView.setText(expenseListModel.getBusinessTypeName());
        serialNumberView.setText(expenseListModel.getOrderNumber());
        dateTimeView.setText(expenseListModel.getExpenseDate());
        moneyView.setText(expenseListModel.getTotalAmount()+"");
        expenseTypeView.setText(getExpenseName(expenseListModel.getExpenseType()));
    }

    public static String getExpenseName(int type){

        String name = "商品消费";
        switch (type){
            case 1:
                name = "商品消费";
                break;
            case 2:
                name = "快速消费";
                break;
            case 5:
                name = "计时充值";
                break;
            case 6:
                name = "计次充值";
                break;
            default:
                break;
        }
        return name;
    }
}
