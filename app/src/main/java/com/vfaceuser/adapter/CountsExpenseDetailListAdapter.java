package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.CountsExpenseDetailModel;
import com.vfaceuser.bizmodel.CountsExpenseDetailModel.ExpenseDetail;


public class CountsExpenseDetailListAdapter extends CommonListAdapter{

	public CountsExpenseDetailListAdapter(Context context,
			AdapterCallBack callBack) {
		super(context, callBack);
	}

	@Override
	protected View inflateConvertView() {
		return View.inflate(context, R.layout.activity_electron_ticket_for_count_type_listitem, null);
	}

	@Override
	protected void initViewData(int position, View convertView) {
		
		TextView expenseNameView = (TextView) convertView.findViewById(R.id.expense_name);
		TextView expenseTimesView = (TextView) convertView.findViewById(R.id.expense_times);
		TextView remainTimesView = (TextView) convertView.findViewById(R.id.remain_times);
		CountsExpenseDetailModel.ExpenseDetail expenseDetailModel = (ExpenseDetail) entityList.get(position);
		expenseNameView.setText(expenseDetailModel.getCommodityName());
		expenseTimesView.setText(expenseDetailModel.getExpenseData()+"");
		remainTimesView.setVisibility(View.GONE);//TODO
		
	}

}
