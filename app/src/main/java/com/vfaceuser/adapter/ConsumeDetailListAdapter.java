package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.ExpenseDetailModel;


/**
 * Created by HuBin on 15/4/12.
 */
public class ConsumeDetailListAdapter extends CommonListAdapter{

    private int expenseType = 0;


    public ConsumeDetailListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
    }

    @Override
    protected View inflateConvertView() {
        return inflater.inflate(R.layout.activity_consume_detail_item,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

        ExpenseDetailModel expenseDetailModel = (ExpenseDetailModel) entityList.get(position);

        TextView nameView = (TextView) convertView.findViewById(R.id.name);
        TextView countsView = (TextView) convertView.findViewById(R.id.counts);
        TextView priceView = (TextView) convertView.findViewById(R.id.price);
        TextView totalView = (TextView) convertView.findViewById(R.id.total);

        nameView.setText(expenseDetailModel.getCommodityName());
        countsView.setText(expenseDetailModel.getQuantity()+"");
        if(expenseType == 6){
            priceView.setVisibility(View.GONE);
        }else{
            priceView.setVisibility(View.VISIBLE);
        }
        priceView.setText(expenseDetailModel.getPrice()+"元");
        totalView.setText(expenseDetailModel.getAmount()+"元");

    }

    public void setExpenseType(int i) {
        expenseType = i;
    }
}
