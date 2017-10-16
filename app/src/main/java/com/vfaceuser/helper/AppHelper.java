package com.vfaceuser.helper;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.UpgradeModel;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.HttpParameter;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;


/**
 * 系统相关 - 检查更新之类的
 * 
 * @author Fangweidong
 * 
 */
public class AppHelper {

    public static UpgradeModel getUpgradeInfo(Context context, int currentVersion) throws MyHttpException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        String url = WebAPI.Common.CLIENT_UPGRADE_URL;

        url += "2/";//手机类型DeviceType(1,iOS 2,Android)
        url += "2/";//客户端类型ClientType(1,商家2,消费者)
        url += currentVersion;//currentVersion

        HttpResult hr = APIUtils.getResult(url);

        if (hr.hasError()) {
            throw hr.getError();
        }

        UpgradeModel model = (new Gson()).fromJson(hr.Data.toString(), UpgradeModel.class);

        if(!TextUtils.isEmpty(model.getPackageUrl())){
            model.setIsexistsnewversion(true);
        }

        return model;

    }
    
	public static void setPushKey(Context context, String pushKey)
			throws MyHttpException {
		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		HttpResult hr = APIUtils.postForObject(
				WebAPI.Common.CLIENT_SET_PUSH_KEY_URL,
				HttpParameter.create()
						.add("MemberCode", UserHelper.getCurrentUser().getMemberCode() + "")
						.add("DeviceType", "2")
                        .add("PushKey", pushKey)
						.add("IsOnline", "true"));
		if (hr.hasError()) {
			throw hr.getError();
		}
	}

	public static void setPushOffline(Context context, String pushKey)
			throws MyHttpException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(
                WebAPI.Common.CLIENT_SET_PUSH_KEY_URL,
                HttpParameter.create()
                        .add("MemberCode", UserHelper.getCurrentUser().getMemberCode() + "")
                        .add("DeviceType", "2")
                        .add("PushKey", pushKey)
                        .add("IsOnline", "false"));
        if (hr.hasError()) {
            throw hr.getError();
        }
	}

}
