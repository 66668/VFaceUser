package com.vfaceuser.db;

import java.util.ArrayList;

public class WhereBuilder 
{
	private String _sqlString;

    public boolean FixFirstCondition;
    public ArrayList<String> Parameters;
    private String _whereString;

    /// <summary>
    /// SQL锟斤拷锟?
    /// </summary>
    public String GetSQLString()
    {
    	String whereString = _whereString.trim();
    	if (FixFirstCondition && whereString.length() > 3)
    	{
    		int first = whereString.indexOf(" ");
    		whereString = whereString.substring(first);
    	}
    	if (whereString.trim().length() > 0)
    		return _sqlString + " WHERE " + whereString;
    	else
    		return _sqlString;                              
        
    }

    public WhereBuilder(String sqlString)
    {
        _whereString = "";
        FixFirstCondition = true;
        _sqlString = sqlString;
        Parameters = new ArrayList<String>();
    }
    public WhereBuilder(String sqlString, ArrayList<String> parameters)
    { 
    	this(sqlString);
    	Parameters = parameters;
    }
    /// <summary>
    /// SelectAll
    /// </summary>
    /// <param name="tableName"></param>
    /// <returns></returns>
    public static WhereBuilder SelectAll(String tableName)
    {
        return new WhereBuilder("SELECT * FROM [" + tableName+"]");
    }
    /// <summary>
    /// 锟斤拷锟绞碉拷锟斤拷锟缴癸拷系为AND锟斤拷锟斤拷锟斤拷
    /// </summary>
    /// <param name="entity"></param>
    public WhereBuilder(EntityBase entity)
    {
        _sqlString = "SELECT * FROM [" + entity.GetOringTableSchema().GetTableName()+"]";
        _whereString = "";
        FixFirstCondition = true;
        Parameters = new ArrayList<String>();
        AddAndCondition(entity);
    }
    
    public void AddAndCondition(EntityBase entity)
    {
        for (String key : entity.GetDataCollection().keySet())
        {
            if (entity.GetData(key) != null)
            {
                if (entity.GetData(key) instanceof String && ((String)entity.GetData(key)).length() == 0)//锟斤拷Hash锟斤拷为String锟斤拷值为锟斤拷锟斤拷锟杰癸拷锟斤拷锟斤拷锟斤拷
                {
                    continue;
                }
                AddAndCondition(key, entity.GetData(key));
            }
        }
    }
    public void AddCondition(String condition)
    {
        _whereString += " " + condition;
    }

    public void AddCondition(String logicalOperator, String left, String preParameter, String parameterName, String sqlOperator, Object parameterValue, String right)
    {
        if (preParameter.length() > 0)
        {
            parameterName = preParameter + "." + parameterName;
        }
        if (_whereString.trim().length() > 0 && _whereString.trim().charAt(_whereString.trim().length() - 1) == '(')
        {
            _whereString += " " + " " + left + " " + parameterName + " ";
        }
        else
        {
            _whereString += " " + logicalOperator+" " + left + " " + parameterName + " ";
        }
        _whereString += sqlOperator.toUpperCase();
 
        _whereString += "?" + right;
        Parameters.add(parameterValue.toString());
    }

    public void AddCondition(String logicalOperator, String left, String preParameter, ColumnInfo parameter, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(logicalOperator, left, preParameter, parameter.ColumnName, sqlOperator, parameterValue, right);
    }


    public void AddAndCondition(String left, String preParameter, String parameterName, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.AND, left, preParameter, parameterName, sqlOperator, parameterValue, right);
    }

    public void AddAndCondition(String preParameter, String parameterName, String sqlOperator, Object parameterValue)
    {
        AddAndCondition("", preParameter, parameterName, sqlOperator, parameterValue, "");
    }

    public void AddAndCondition(String parameterName, String sqlOperator, Object parameterValue)
    {
        AddAndCondition("", "", parameterName, sqlOperator, parameterValue, "");
    }

    public void AddAndCondition(String parameterName, Object parameterValue)
    {
        AddAndCondition("", "", parameterName, "=", parameterValue, "");
    }

    public void AddAndCondition(String left, String preParameter, ColumnInfo parameter, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.AND, left, preParameter, parameter, sqlOperator, parameterValue, right);
    }

    public void AddAndCondition(ColumnInfo parameter, String sqlOperator, Object parameterValue)
    {
        AddAndCondition("", "", parameter, sqlOperator, parameterValue, "");
    }

    public void AddAndCondition(ColumnInfo parameter, Object parameterValue)
    {
        AddAndCondition("", "", parameter, "=", parameterValue, "");
    }

    public void AddORCondition(String left, String preParameter, String parameterName, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.OR, left, preParameter, parameterName, sqlOperator, parameterValue, right);
    }

    public void AddORCondition(String parameterName, String sqlOperator, Object parameterValue)
    {
        AddORCondition("", "", parameterName, sqlOperator, parameterValue, "");
    }

    public void AddORCondition(String parameterName, Object parameterValue)
    {
        AddORCondition("", "", parameterName, "=", parameterValue, "");
    }

    public void AddORCondition(String left, String preParameter, ColumnInfo parameter, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.OR, left, preParameter, parameter, sqlOperator, parameterValue, right);
    }

    public void AddORCondition(ColumnInfo parameter, String sqlOperator, Object parameterValue)
    {
        AddORCondition("", "", parameter, sqlOperator, parameterValue, "");
    }

    public void AddORCondition(ColumnInfo parameter, Object parameterValue)
    {
        AddORCondition("", "", parameter, "=", parameterValue, "");
    }

    
    public void AddNOTCondition(String left, String preParameter, String parameterName, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.NOT, left, preParameter, parameterName, sqlOperator, parameterValue, right);
    }

    public void AddNOTCondition(String parameterName, String sqlOperator, Object parameterValue)
    {
        AddNOTCondition("", "", parameterName, sqlOperator, parameterValue, "");
    }

    public void AddNOTCondition(String parameterName, Object parameterValue)
    {
        AddNOTCondition("", "", parameterName, "=", parameterValue, "");
    }

    public void AddNOTCondition(String left, String preParameter, ColumnInfo parameter, String sqlOperator, Object parameterValue, String right)
    {
        AddCondition(LogicalOperator.NOT, left, preParameter, parameter, sqlOperator, parameterValue, right);
    }

    public void AddNOTCondition(ColumnInfo parameter, String sqlOperator, Object parameterValue)
    {
        AddNOTCondition("", "", parameter, sqlOperator, parameterValue, "");
    }

    public void AddNOTCondition(ColumnInfo parameter, Object parameterValue)
    {
        AddNOTCondition("", "", parameter, "=", parameterValue, "");
    }
    
    
    
	public static class SQLOperator
	{
	    public static String Equal = "=";
	    public static String NotEquals = "<>";
	    public static String Greater = ">";
	    public static String GreaterEquals = ">=";
	    public static String Less = "<";
	    public static String LessEquals = "<=";
	    public static String Like = "LIKE";
	    public static String LikeLeft = "LIKELEFT";
	    public static String LikeRight = "LIKERIGHT";
	}

	public static class LogicalOperator
	{
	    public static String AND = "AND";
	    public static String OR = "OR";
	    public static String NOT = "NOT";
	}
}
