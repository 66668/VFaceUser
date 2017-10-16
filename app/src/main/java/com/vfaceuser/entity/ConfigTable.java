package com.vfaceuser.entity;


import com.vfaceuser.db.ColumnInfo;
import com.vfaceuser.db.TableInfo;

public class ConfigTable  extends TableInfo
{
public static String C_TableName = "config";

public static String C_Userid = "userid";
public static String C_Language = "language";
public static String C_AllowPush = "allow_push";
public static String C_Privacy = "privacy";
public static String C_Currentversion = "currentversion";
public static String C_CachePath = "cache_path";
public static String C_UpdateTime = "update_time";


public ConfigTable()
{
_tableName = "config";
}

protected static ConfigTable _current;
public static ConfigTable Current()
{
if (_current == null )
{
Initial();
}
return _current;
}

private static void Initial()
{
_current = new ConfigTable();

 _current.Add(C_Userid, new ColumnInfo(C_Userid,"Userid", true, "Integer"));
 _current.Add(C_Language, new ColumnInfo(C_Language,"Language", false, "String"));	
 _current.Add(C_AllowPush, new ColumnInfo(C_AllowPush,"AllowPush", false, "Integer"));	
 _current.Add(C_Privacy, new ColumnInfo(C_Privacy,"Privacy", false, "String"));	
 _current.Add(C_Currentversion, new ColumnInfo(C_Currentversion,"Currentversion", false, "String"));	
 _current.Add(C_CachePath, new ColumnInfo(C_CachePath,"CachePath", false, "String"));	
 _current.Add(C_UpdateTime, new ColumnInfo(C_UpdateTime,"UpdateTime", false, "Integer"));	
}


public ColumnInfo Userid()
{
return GetColumnInfoByName(C_Userid);
}

public ColumnInfo Language()
{
return GetColumnInfoByName(C_Language);
}

public ColumnInfo AllowPush()
{
return GetColumnInfoByName(C_AllowPush);
}

public ColumnInfo Privacy()
{
return GetColumnInfoByName(C_Privacy);
}

public ColumnInfo Currentversion()
{
return GetColumnInfoByName(C_Currentversion);
}

public ColumnInfo CachePath()
{
return GetColumnInfoByName(C_CachePath);
}

public ColumnInfo UpdateTime()
{
return GetColumnInfoByName(C_UpdateTime);
}
}
