package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.ServiceExpenseHistoryModel;



/**
 * Created by HuBin on 15/5/21.
 */
public class AvailableServiceDetailListAdapter extends CommonListAdapter {

    public AvailableServiceDetailListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
    }

    @Override
    protected View inflateConvertView() {
        return View.inflate(context, R.layout.activity_available_service_detail_listitem,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

    	ServiceExpenseHistoryModel model = (ServiceExpenseHistoryModel) entityList.get(position);
        TextView dateTimeView = (TextView) convertView.findViewById(R.id.date_time);
        TextView timesView = (TextView) convertView.findViewById(R.id.times);
        TextView operatorView = (TextView) convertView.findViewById(R.id.operator);

        dateTimeView.setText(model.getExpenseDate());
        timesView.setText(model.getExpenseCount()+"");
        operatorView.setText(model.getOperator());

    }
}
