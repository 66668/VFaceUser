package com.vfaceuser.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.vfaceuser.bizmodel.MemberModel;
import com.vfaceuser.entity.UserEntity;

import org.json.JSONObject;

public class ConfigUtil {
	protected SharedPreferences sp;
	protected SharedPreferences.Editor editor;
	
	
	private static final String USERID = "userid";
	private static final String ACCOUNT = "account";
	private static final String IS_FIRST_USE = "is_first_use";
	private static final String AUTO_LOGIN = "auto_login";
	private static final String USERENTITY = "user_entity";
	private static final String MEMBER_ENTITY = "member_entity";
	private static final String PUSH_ID = "push_id";
	
	private static final String IF_NO_DIATURBING = "if_no_diaturbing";
	private static final String NO_DIATURBING_START_TIME = "no_diaturbing_start_time";
	private static final String NO_DIATURBING_END_TIME = "no_diaturbing_end_time";


	@SuppressLint("CommitPrefEdits")
	public ConfigUtil(Context context) {
		try {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			editor = sp.edit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetConfig() {
		setUserId(null);
		setAccount("");
		setIsFirstUse(true);
		setAutoLogin(true);
	}

	public void put(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public String get(String key) {
		return sp.getString(key, "");
	}

	public void putBoolean(String key, Boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public Boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}
	

	public UserEntity getUserEntity() {
		String string = sp.getString(USERENTITY, null);
		if(string!=null && string.length()>0){
			try {
				return (UserEntity)UserEntity.toEntityBase(new JSONObject(string), UserEntity.class);
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
		}
		return null;
	}
	
	public void setUserEneity(UserEntity userEntity) {
		editor.putString(USERENTITY, userEntity.toJSON().toString());
		editor.commit();
	}
	
	public MemberModel getMemberEntity() {
		String string = sp.getString(MEMBER_ENTITY, null);
		if(string!=null && string.length()>0){
			try {
				return new Gson().fromJson(string,MemberModel.class);
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
		}
		return null;
	}
	
	public void setMemberEntity(MemberModel memberModel) {
		editor.putString(MEMBER_ENTITY, new Gson().toJson(memberModel)  );
		editor.commit();
	}
	
	
	
	public String getUserId() {
		return sp.getString(USERID, null);
	}
	
	public void setUserId(String id) {
		editor.putString(USERID, id);
		editor.commit();
	}

	public String getAccount() {
		return sp.getString(ACCOUNT, "");
	}

	public void setAccount(String account) {
		editor.putString(ACCOUNT, account);
		editor.commit();
	}
 
	public boolean getAutoLogin() {
		return sp.getBoolean(AUTO_LOGIN, true);
	}

	public void setAutoLogin(boolean play) {
		editor.putBoolean(AUTO_LOGIN, play);
		editor.commit();
	}

	public boolean getIsFirstUse() {
		return sp.getBoolean(IS_FIRST_USE, true);
	}
	
	public void setIsFirstUse(boolean isFirstUse) {
		editor.putBoolean(IS_FIRST_USE, isFirstUse);
		editor.commit();
	}

	public boolean getIfNoDiaturbing() {
		return sp.getBoolean(IF_NO_DIATURBING, false);
	}
	
	public void setIfNoDiaturbing(boolean ifNoDiaturbing){
		editor.putBoolean(IF_NO_DIATURBING, ifNoDiaturbing);
		editor.commit();
	}

	public String getNoDiaturbingStartTime() {
		return sp.getString(NO_DIATURBING_START_TIME,"23:00");
	}
	
	public void setNoDiaturbingStartTime(String noDiaturbingStartTime){
		editor.putString(NO_DIATURBING_START_TIME, noDiaturbingStartTime);
		editor.commit();
	}


	public String getNoDiaturbingEndTime() {
		return sp.getString(NO_DIATURBING_END_TIME,"8:00");
	}
	
	public void setNoDiaturbingEndTime(String noDiaturbingEndTime){
		editor.putString(NO_DIATURBING_END_TIME, noDiaturbingEndTime);
		editor.commit();
	}

	public void setPushId(String pushId) {
		editor.putString(PUSH_ID, pushId);
		editor.commit();
	}
	
	public String getPushId() {
		return sp.getString(PUSH_ID, "");
	}
	
	
}
