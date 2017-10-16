package com.vfaceuser.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vfaceuser.R;
import com.vfaceuser.bizmodel.CardListModel;


/**
 * Created by HuBin on 15/4/12.
 */
public class VipCardListAdapter extends CommonListAdapter{

    public VipCardListAdapter(Context context, AdapterCallBack callBack) {
        super(context, callBack);
    }

    @Override
    protected View inflateConvertView() {
        return inflater.inflate(R.layout.activity_vip_card_manage_listitem,null);
    }

    @Override
    protected void initViewData(int position, View convertView) {

        TextView shopNameView = (TextView) convertView.findViewById(R.id.shop_name);
        TextView cardNumView = (TextView) convertView.findViewById(R.id.card_number);
        TextView cardGradeView = (TextView) convertView.findViewById(R.id.card_grade);
        TextView cardPointsView = (TextView) convertView.findViewById(R.id.card_points);
        TextView moneyView = (TextView) convertView.findViewById(R.id.money);
        TextView businesstypenameView = (TextView) convertView.findViewById(R.id.businesstype_name);

        CardListModel model = (CardListModel) entityList.get(position);
        shopNameView.setText(model.getStoreName());
        cardNumView.setText("卡号："+model.getMemberCardNumber());
        cardGradeView.setText("等级："+model.getGradeName()+"");
        cardPointsView.setText("积分："+model.getTotalPoints()+"分");
        moneyView.setText("余额："+model.getTotalMoney()+"元");
        businesstypenameView.setText("行业："+model.getBusinesstypename());
    }
}
