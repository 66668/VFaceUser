package com.vfaceuser.entity;


import com.vfaceuser.db.EntityBase;

public class UserEntity extends EntityBase {
	public UserTable TableSchema() {
		return (UserTable) _tableSchema;
	}

	public UserEntity() {
		_tableSchema = UserTable.Current();
	}

        
	public Integer getUserId() {
		return (Integer) GetData(UserTable.C_UserId);
	}
	
	public void setUserId(Integer value) {
		SetData(UserTable.C_UserId, value);
	}
        
	public String getFullname() {
		return (String) GetData(UserTable.C_Fullname);
	}
	public void setFullname(String value) {
		SetData(UserTable.C_Fullname, value);
	}
	
	public Integer getStoreId() {
		return (Integer) GetData(UserTable.C_StoreId);
	}
	
	public void setStoreId(Integer value) {
		SetData(UserTable.C_StoreId, value);
	}
      
	public String getStoreName() {
		return (String) GetData(UserTable.C_StoreName);
	}

	public void setStoreName(String value) {
		SetData(UserTable.C_StoreName, value);
	}
	 
        
	public String getAccount() {
		return (String) GetData(UserTable.C_Account);
	}
	
	public void setAccount(String value) {
		SetData(UserTable.C_Account, value);
	}
        
	public String getPassword() {
		return (String) GetData(UserTable.C_Password);
	}
	
	public void setPassword(String value) {
		SetData(UserTable.C_Password, value);
	}
       

}
