package com.vfaceuser.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.vfaceuser.R;
import com.vfaceuser.bizmodel.MemberModel;
import com.vfaceuser.commen.MyApplication;
import com.vfaceuser.commen.MyHttpException;
import com.vfaceuser.commen.WebAPI;
import com.vfaceuser.utils.APIUtils;
import com.vfaceuser.utils.ConfigUtil;
import com.vfaceuser.utils.HttpParameter;
import com.vfaceuser.utils.HttpResult;
import com.vfaceuser.utils.NetworkManager;

import java.util.HashMap;

/**
 * 用户帮助类（包含当前登录的用户信息和用户的一些基本操作）
 * 
 * @author don
 * 
 */
public class UserHelper {
	static MemberModel  mCurrentUser = null;

	/*
	 * 用户的性别
	 */
	public enum UserGender {
		Unknown(0), Male(1), Female(2);

		private int value = 0;

		private UserGender(int value) {
			this.value = value;
		}

		public static UserGender valueOf(int value) {
			switch (value) {
			case 0:
				return Unknown;
			case 1:
				return Male;
			case 2:
				return Female;
			default:
				return Unknown;
			}
		}

		public int value() {
			return this.value;
		}

	}

	static String configAccount = null;

	/**
	 * 获取配置的用户账号
	 * 
	 * @return
	 */
	public static String getConfigUserAccount() {
		if (configAccount == null) {
			ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
			configAccount = config.getAccount();
		}
		return configAccount;
	}

	static String configUserId = null;

	/**
	 * 获取配置的用户账号
	 * 
	 * @return
	 */
	public static String getConfigUserId() {
		if (configUserId == null) {
			ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
			configUserId = config.getUserId() + "";
		}
		return configUserId;
	}

	/**
	 * 获取当前登录的用户
	 * 
	 * @param isAutoLoad
	 *            是否防止内存回收后自动加载默认用户
	 * @return
	 */
	public static MemberModel getCurrentUser(boolean isAutoLoad) {
		if (mCurrentUser == null && isAutoLoad) {
			ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
			String account = config.getAccount();
			if (!"".equals(account)) {
				mCurrentUser = config.getMemberEntity();
			}
		}
		return mCurrentUser;
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public static String changePassword(Context context, String oldPwd,
			String newPwd) throws MyHttpException {
		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		HttpResult hr = APIUtils.postForObject(
				WebAPI.User.CHANGE_MEMBER_PASSWORD_URL,
				HttpParameter.create()
						.add("MemberCode", getCurrentUser().getMemberCode() + "")
						.add("OldPsw", oldPwd)
                        .add("NewPsw", newPwd));
		if (hr.hasError()) {
			throw hr.getError();
		}
		return hr.Message;
	}


	/**
	 * 获取当前用户
	 *
	 * @return
	 */
	public static MemberModel getCurrentUser() {
		return getCurrentUser(true);
	}

	/**
	 * 在线登录
	 */
	public static void loginOnline(Context context, String account,
			String password) throws MyHttpException {
		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}

		HttpResult hr = APIUtils.postForObject(
				WebAPI.User.MLogin_URL,
				HttpParameter.create()
						.add("PhoneNumber", account)
                        .add("Password", password));
		if (hr.hasError()) {
			throw hr.getError();
		}
		MemberModel memberModel  =new Gson().fromJson(hr.Data.toString(), MemberModel.class);

		ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
		config.setAccount(account);
		config.setMemberEntity(memberModel);

		mCurrentUser = memberModel;
	}

	/**
	 * 获取会员账户信息
	 * @param context
	 * @throws MyHttpException
	 */
	public static void getMemberBalance(Context context) throws MyHttpException {
		if (!NetworkManager.isNetworkAvailable(context)) {
			throw new MyHttpException(R.string.network_invalid);
		}
		String memberCode = UserHelper.getCurrentUser().getMemberCode();
		String url = WebAPI.User.GET_MEMBER_BALANCE_URL;
		url += memberCode;
		HttpResult hr = APIUtils.getResult(url);
		if (hr.hasError()) {
			throw hr.getError();
		}
		MemberModel memberModel  =new Gson().fromJson(hr.Data.toString(), MemberModel.class);

		ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
		config.setMemberEntity(memberModel);

		mCurrentUser = memberModel;
	}

	/**
	 * Execute Logout
	 *
	 * @param context
	 */
	public static void logout(Context context)
			throws MyHttpException {
		try {
			ConfigUtil configUtil = new ConfigUtil(context);
			configUtil.setAutoLogin(false);
			MyApplication.getInstance().setIsLogin(false);
			AppHelper.setPushOffline(context, configUtil.getPushId());
		} finally {
			configAccount = null;
			mCurrentUser = null;
		}
	}


    /**
     * 完善个人信息
     * @param context
     * @param parameters
     * @throws MyHttpException
     */
    public static void editVFaceMember(Context context,HashMap<String, String>parameters) throws MyHttpException{

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }
        HttpParameter httpParameter = HttpParameter.create();
        for (String key : parameters.keySet()) {
            httpParameter.add(key, parameters.get(key));
        }
        HttpResult hr = APIUtils.postForObject(WebAPI.User.EDIT_VFACE_MEMBER_URL,
                httpParameter);
        if (hr.hasError()) {
            throw hr.getError();
        }

        MemberModel memberModel  =new Gson().fromJson(hr.Data.toString(), MemberModel.class);

        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        config.setMemberEntity(memberModel);
        MemberModel memberModel1 = config.getMemberEntity();
        String address = memberModel1.getAddress();

        mCurrentUser = memberModel;

    }


    /**
     * 找回密码,发送验证码
     * @param context
     * @param PhoneNumber
     * @throws MyHttpException
     */
    public static void sendAuthCodeForGetPsw(Context context, String PhoneNumber) throws MyHttpException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(
                WebAPI.User.SEND_AUTH_CODE_FOR_RESET_PWD_URL,
                HttpParameter.create()
                        .add("PhoneNumber", PhoneNumber));
        if (hr.hasError()) {
            throw hr.getError();
        }
    }

    /**
     * 注册接口/找回密码,重置密码
     * @param context
     * @param phoneNumber
     * @param authenticode
     * @param userPsw
     * @throws MyHttpException
     */
    public static void regist(Context context, String phoneNumber,String userPsw,String authenticode) throws MyHttpException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(
                WebAPI.User.SET_MEMBER_PASSWORD_URL,
                HttpParameter.create()
                        .add("PhoneNumber", phoneNumber)
                        .add("Authenticode", authenticode)
                        .add("UserPsw", userPsw)
        );
        if (hr.hasError()) {
            throw hr.getError();
        }
    }


    /**
     * 发送注册手机验证码短信
     * @param context
     * @param phoneNumber
     * @throws MyHttpException
     */
    public static void sendAuthCode(Context context, String phoneNumber) throws MyHttpException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyHttpException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(
                WebAPI.User.SEND_AUTH_CODE_URL,
                HttpParameter.create()
                        .add("PhoneNumber", phoneNumber)
        );
        if (hr.hasError()) {
            throw hr.getError();
        }
    }

}
