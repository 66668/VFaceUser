package com.vfaceuser.entity;


import com.vfaceuser.db.EntityBase;

public class ParameterEntity extends EntityBase {
	public ParameterTable TableSchema() {
		return (ParameterTable) _tableSchema;
	}

	public ParameterEntity() {
		_tableSchema = ParameterTable.Current();
	}

        
	public String getParamKey() {
		return (String) GetData(ParameterTable.C_ParamKey);
	}
	
	public void setParamKey(String value) {
		SetData(ParameterTable.C_ParamKey, value);
	}
        
	public String getParamValue() {
		return (String) GetData(ParameterTable.C_ParamValue);
	}
	
	public void setParamValue(String value) {
		SetData(ParameterTable.C_ParamValue, value);
	}
        
	public String getUpdateTime() {
		return (String) GetData(ParameterTable.C_UpdateTime);
	}
	
	public void setUpdateTime(String value) {
		SetData(ParameterTable.C_UpdateTime, value);
	}
        
	public Integer getUserId() {
		return (Integer) GetData(ParameterTable.C_UserId);
	}
	
	public void setUserId(Integer value) {
		SetData(ParameterTable.C_UserId, value);
	}
}
