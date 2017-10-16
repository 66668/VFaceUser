package com.vfaceuser.entity;


import com.vfaceuser.db.ColumnInfo;
import com.vfaceuser.db.TableInfo;

public class ParameterTable  extends TableInfo
{
public static String C_TableName = "parameter";

public static String C_ParamKey = "param_key";
public static String C_ParamValue = "param_value";
public static String C_UpdateTime = "update_time";
public static String C_UserId = "user_id";


public ParameterTable()
{
_tableName = "parameter";
}

protected static ParameterTable _current;
public static ParameterTable Current()
{
if (_current == null )
{
Initial();
}
return _current;
}

private static void Initial()
{
_current = new ParameterTable();

 _current.Add(C_ParamKey, new ColumnInfo(C_ParamKey,"ParamKey", true, "String"));	
 _current.Add(C_ParamValue, new ColumnInfo(C_ParamValue,"ParamValue", false, "String"));	
 _current.Add(C_UpdateTime, new ColumnInfo(C_UpdateTime,"UpdateTime", false, "String"));	
 _current.Add(C_UserId, new ColumnInfo(C_UserId,"UserId", false, "Integer"));	
}


public ColumnInfo ParamKey()
{
return GetColumnInfoByName(C_ParamKey);
}

public ColumnInfo ParamValue()
{
return GetColumnInfoByName(C_ParamValue);
}

public ColumnInfo UpdateTime()
{
return GetColumnInfoByName(C_UpdateTime);
}

public ColumnInfo UserId()
{
return GetColumnInfoByName(C_UserId);
}
}
