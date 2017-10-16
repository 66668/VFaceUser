package com.vfaceuser.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.AvailableServiceModel;
import com.vfaceuser.bizmodel.CardListModel;
import com.vfaceuser.bizmodel.ExpenseListModel;
import com.vfaceuser.bizmodel.ServiceExpenseHistoryModel;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;

import java.util.ArrayList;
import java.util.List;


public class VipCardHelper {

	/**
	 * 会员卡信息(列表)
	 * 
	 * @param context
	 * @return
	 * @throws MyHttpException
	 */
	public static ArrayList<CardListModel> getCardList(Context context,
			int businessType, String storeName) throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String vfaceMemberCode = UserHelper.getCurrentUser().getMemberCode();

		String url = WebAPI.VipCard.GET_MEMBER_CARD_LIST_URL + vfaceMemberCode;
		url += "?Businesstype=" + businessType;
		url += "&StoreName=" + storeName;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}

		return (new Gson()).fromJson(hr.DataList.toString(),
				new TypeToken<List<CardListModel>>() {
				}.getType());

	}

	/**
	 * 获取可用服务列表
	 * 
	 * @param context
	 * @param expenseType
	 * @param storeId
	 * @return
	 * @throws MyHttpException
	 */
	public static ArrayList<AvailableServiceModel> getAvailableService(
			Context context, String storeMemberId, int expenseType,
			String storeId) throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.VipCard.GET_AVAILABLE_SERVICE_URL;
		url += "storeMemberId=" + storeMemberId;
		url += "&expenseType=" + expenseType;
		url += "&storeId=" + storeId;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}

		ArrayList<AvailableServiceModel> models = (new Gson()).fromJson(
				hr.DataList.toString(),
				new TypeToken<List<AvailableServiceModel>>() {
				}.getType());

		for (AvailableServiceModel availableServiceModel : models) {
			availableServiceModel.setExpresnseType(expenseType);
		}

		return models;

	}

	/**
	 * 获取计时和计次服务的详细使用明细
	 * 
	 * @param context
	 * @param expenseType
	 * @param storeId
	 * @param serviceId
	 * @return
	 * @throws MyHttpException
	 */
	public static ArrayList<ServiceExpenseHistoryModel> getServiceExpenseHistory(
			Context context, String storeMemberId, int expenseType,
			String storeId, String serviceId) throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.VipCard.GET_EXPENSE_HISTORY_URL;
		url += "storeMemberId=" + storeMemberId;
		url += "&expenseType=" + expenseType;// TimeExpense = 3, CountExpense =
												// 4
		url += "&storeId=" + storeId;
		url += "&serviceId=" + serviceId;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}

		return (new Gson()).fromJson(hr.DataList.toString(),
				new TypeToken<List<ServiceExpenseHistoryModel>>() {
				}.getType());

	}

	/**
	 * 获取门店消费列表（包括子店消费记录）
	 * 
	 * @param context
	 * @param storeId
	 * @param expenseType
	 * @param keywords
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws MyHttpException
	 */
	public static ArrayList<ExpenseListModel> getVipCardExpenseList(
			Context context, String storeId, int expenseType, String keywords,
			String startDate, String endDate, int pageIndex, int pageSize)
			throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.VipCard.GET_STORE_EXPENSE_LIST_URL;

		String vfaceMemberCode = UserHelper.getCurrentUser().getMemberCode();
		
		url += "&vfaceMemberCode=" + vfaceMemberCode;
		url += "&storeId=" + storeId;
		url += "&pageIndex=" + pageIndex;
		url += "&pageSize=" + pageSize;
		url += "&expenseType=" + expenseType;
		url += "&startDate=" + startDate;
		url += "&endDate=" + endDate;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}

		return (new Gson()).fromJson(hr.DataList.toString(),
				new TypeToken<List<ExpenseListModel>>() {
				}.getType());

	}

}
