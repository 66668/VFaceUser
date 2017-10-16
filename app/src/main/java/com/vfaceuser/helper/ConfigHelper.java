package com.vfaceuser.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.StoreBusinessTypeModel;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.HttpParameter;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HuBin on 15/4/21.
 */
public class ConfigHelper {

	public static void setWelcomeVoice(Context context, String customWelcome,
			boolean ifEnableWelcome) throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		HttpResult hr = APIUtils.postForObject(
				WebAPI.Config.CONFIG_WELCOME_VOICE_URL,
				HttpParameter
						.create()
						.add("MemberCode",
								UserHelper.getCurrentUser().getMemberCode()
										+ "")
						.add("CustomWelcome", customWelcome)
						.add("IsEnableWelcome", ifEnableWelcome + ""));
		if (hr.hasError()) {
			throw hr.getError();
		}

	}

	/**
	 * 获取省份列表
	 * 
	 * @param context
	 * @return
	 * @throws MyHttpException
	 */
	public static JSONArray getProvinceList(Context context)
			throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.Profile.GET_PROVINCE_URL;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}
		return hr.DataList;

	}

	/**
	 * 获取该省城市列表
	 * 
	 * @param context
	 * @param provinceID
	 * @return
	 * @throws MyHttpException
	 */
	public static JSONArray getCityList(Context context, String provinceID)
			throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.Profile.GET_CITY_URL + provinceID;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}
		return hr.DataList;
	}

	/**
	 * 获取该城市区域列表
	 * 
	 * @param context
	 * @param cityID
	 * @return
	 * @throws MyHttpException
	 */
	public static JSONArray getDistrictList(Context context, String cityID)
			throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.Profile.GET_DISTRICT_URL + cityID;

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}
		return hr.DataList;
	}

	/**
	 * 获取店家购买类型
	 * @param context
	 * @return
	 * @throws MyHttpException
	 */
	public static ArrayList<StoreBusinessTypeModel> getStoreBusinessType(
			Context context) throws MyHttpException {

		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		String url = WebAPI.Common.GET_STORE_BUSINESS_TYPE_URL;
		//TODO
//		url = "http://openapi.newaysoft.com/openapi/Common/GetStoreBusinessType";

		HttpResult hr = APIUtils.getResult(url);

		if (hr.hasError()) {
			throw hr.getError();
		}

		return (new Gson()).fromJson(hr.DataList.toString(),
				new TypeToken<List<StoreBusinessTypeModel>>() {
				}.getType());

	}
}
