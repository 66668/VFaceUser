package com.vfaceuser.entity;


import com.vfaceuser.db.EntityBase;

public class ConfigEntity extends EntityBase {
	public ConfigTable TableSchema() {
		return (ConfigTable) _tableSchema;
	}

	public ConfigEntity() {
		_tableSchema = ConfigTable.Current();
	}

        
	public Integer getUserid() {
		return (Integer) GetData(ConfigTable.C_Userid);
	}
	
	public void setUserid(Integer value) {
		SetData(ConfigTable.C_Userid, value);
	}
        
	public String getLanguage() {
		return (String) GetData(ConfigTable.C_Language);
	}
	
	public void setLanguage(String value) {
		SetData(ConfigTable.C_Language, value);
	}
        
	public Integer getAllowPush() {
		return (Integer) GetData(ConfigTable.C_AllowPush);
	}
	
	public void setAllowPush(Integer value) {
		SetData(ConfigTable.C_AllowPush, value);
	}
        
	public String getPrivacy() {
		return (String) GetData(ConfigTable.C_Privacy);
	}
	
	public void setPrivacy(String value) {
		SetData(ConfigTable.C_Privacy, value);
	}
        
	public String getCurrentversion() {
		return (String) GetData(ConfigTable.C_Currentversion);
	}
	
	public void setCurrentversion(String value) {
		SetData(ConfigTable.C_Currentversion, value);
	}
        
	public String getCachePath() {
		return (String) GetData(ConfigTable.C_CachePath);
	}
	
	public void setCachePath(String value) {
		SetData(ConfigTable.C_CachePath, value);
	}
        
	public Integer getUpdateTime() {
		return (Integer) GetData(ConfigTable.C_UpdateTime);
	}
	
	public void setUpdateTime(Integer value) {
		SetData(ConfigTable.C_UpdateTime, value);
	}
}
