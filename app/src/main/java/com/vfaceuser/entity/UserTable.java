package com.vfaceuser.entity;


import com.vfaceuser.db.ColumnInfo;
import com.vfaceuser.db.TableInfo;

public class UserTable  extends TableInfo
{
public static String C_TableName = "user";

public static String C_UserId = "user_id";
public static String C_Fullname = "fullname";
public static String C_StoreId = "store_id";
public static String C_StoreName = "store_name";
public static String C_Account = "account";
public static String C_Password = "password"; 


public UserTable()
{
_tableName = "user";
}

protected static UserTable _current;
public static UserTable Current()
{
if (_current == null )
{
Initial();
}
return _current;
}

private static void Initial()
{
_current = new UserTable();

 _current.Add(C_UserId, new ColumnInfo(C_UserId,"UserId", true, "Integer"));
 _current.Add(C_Fullname, new ColumnInfo(C_Fullname,"Fullname", false, "String"));	 	
 _current.Add(C_StoreId, new ColumnInfo(C_StoreId,"StoreId", false, "Integer"));	
 _current.Add(C_StoreName, new ColumnInfo(C_StoreName,"StoreName", false, "String"));	
  
 _current.Add(C_Account, new ColumnInfo(C_Account,"Account", false, "String"));	
 _current.Add(C_Password, new ColumnInfo(C_Password,"Password", false, "String"));	 
}


public ColumnInfo UserId()
{
return GetColumnInfoByName(C_UserId);
}

public ColumnInfo  Fullname()
{
return GetColumnInfoByName(C_Fullname);
}

public ColumnInfo StoreId()
{
return GetColumnInfoByName(C_StoreId);
}

public ColumnInfo StoreName()
{
return GetColumnInfoByName(C_StoreName);
}
 
public ColumnInfo Account()
{
return GetColumnInfoByName(C_Account);
}

public ColumnInfo Password()
{
return GetColumnInfoByName(C_Password);
} 
}
