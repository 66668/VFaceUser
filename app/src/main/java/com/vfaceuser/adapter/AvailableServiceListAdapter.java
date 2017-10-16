package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.AvailableServiceModel;


/**
 * Created by HuBin on 15/5/21.
 */
public class AvailableServiceListAdapter extends CommonListAdapter{

    public AvailableServiceListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
    }

    @Override
    protected View inflateConvertView() {
        return View.inflate(context, R.layout.activity_available_service_listitem,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

    	AvailableServiceModel model = (AvailableServiceModel) entityList.get(position);
        TextView availableTimesView = (TextView) convertView.findViewById(R.id.available_times);
        TextView totalTimesView = (TextView) convertView.findViewById(R.id.total_times);
        TextView serviceNameView = (TextView) convertView.findViewById(R.id.service_name);

        serviceNameView.setText(model.getServiceName());
        //TimeExpense = 3,计次CountExpense = 4
        if(model.getExpresnseType() == 3){
            availableTimesView.setText(model.getInventoryData()+"分钟可用");
        }else{
        	availableTimesView.setText(model.getInventoryData()+"次可用");
        }
        
        totalTimesView.setVisibility(View.GONE);//TODO

    }
}
