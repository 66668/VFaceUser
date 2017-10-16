package com.vfaceuser.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.CountsExpenseDetailModel;
import com.vfaceuser.bizmodel.ExpenseDetailModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.bizmodel.TimeExpenseDetailModel;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;

import java.util.ArrayList;
import java.util.List;


public class ExpenseHelper {
	/**
     * 获取用户消费记录
     * @param context
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws MyHttpException
     */
    public static ArrayList<ExpenseListModel> getExpenseList(Context context,String memberCardNumber,int expenseType,String storeName,String startDate,String endDate,int pageIndex,int pageSize) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String vfaceMemberCode = UserHelper.getCurrentUser().getMemberCode();

        String url = WebAPI.User.GET_USER_EXPENSE_LIST_URL;

        url += "&vfaceMemberCode=" + vfaceMemberCode;
        url += "&expenseType=" +expenseType;
        url += "&pageIndex=" +pageIndex;
        url += "&pageSize=" +pageSize;
//        url += "&memberCardNumber="+memberCardNumber;
        url += "&storeName="+storeName;
        url += "&startDate="+startDate;
        url += "&endDate="+endDate;

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.DataList.toString(),
                new TypeToken<List<ExpenseListModel>>() {
                }.getType());

    }

    /**
     * 获取门店消费明细
     * @param context
     * @param storeId
     * @param commodityExpenseId
     * @param expenseType
     * @return
     * @throws MyHttpException
     */
    public static ArrayList<ExpenseDetailModel> getExpenseDetailModel(Context context,String storeId,String commodityExpenseId
            ,int expenseType) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Expense.SHOP_CONSUME_DETAIL_URL;

        url += "commodityExpenseId=" + commodityExpenseId;
        url += "&storeId=" + storeId;
        url += "&expenseType=" +expenseType;

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.DataList.toString(),
                new TypeToken<List<ExpenseDetailModel>>() {
                }.getType());

    }
    
    /**
     * 获取计次消费详情
     * @param context
     * @param storeId
     * @param orderNumber
     * @return
     * @throws MyHttpException
     */
    public static CountsExpenseDetailModel getCountsExpenseDetail(Context context,String storeId,String orderNumber) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Expense.COUNTS_CONSUME_DETAIL_URL;

        url += "orderNumber=" + orderNumber;
//        url += "&storeId=" + storeId;

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.Data.toString(), CountsExpenseDetailModel.class);

    }
    
    /**
     * 获取计时消费详情
     * @param context
     * @param storeId
     * @param orderNumber
     * @return
     * @throws MyHttpException
     */
    public static TimeExpenseDetailModel getTimeExpenseDetail(Context context,String storeId,String orderNumber) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Expense.TIME_CONSUME_DETAIL_URL;

        url += "orderNumber=" + orderNumber;
//        url += "&storeId=" + storeId;

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return (new Gson()).fromJson(hr.Data.toString(), TimeExpenseDetailModel.class);

    }
    
}
