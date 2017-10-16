package com.vfaceuser.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
	public static String dateFormat="yyyy-mm-dd";
	public static String dateTimeFormat="yyyy-mm-dd HH:mm:ss";
	
	/**
	 * 11:30-9:00 = 2*2+1+1 ==6
	 * @param endDate
	 * @return
	 */
	public static int getRowfrom9(String endDate){
		int number=0;
		Calendar endCal=longstr2Calendar(endDate);
		int hours=endCal.get(Calendar.HOUR_OF_DAY);
		number= (hours-9)*2;
		int minuts=endCal.get(Calendar.MINUTE);
		if(minuts >10)   //only 30 or 0
			number ++;  //plus 1
		number =number+1;  //plus 1 to trans row
		if(number <= 0 || number >25)  //exception process
			number =0;
		return number;
	}
	/**
	 * 11:30 - 9:30 =2:00  get 5 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int endTimeMinusStartTime(String startDate,String endDate){
		Calendar stCal=str2Calendar(startDate);
		Calendar endCal=str2Calendar(endDate);
		return 1+(int)((endCal.getTimeInMillis()-stCal.getTimeInMillis())/(1000*60*30));
	}
	
	/**
	 *  10  -  4 = 6  true
	 *  10  - 5  =5   false
	 *  10  - 6  =4   false
	 * @param startDate
	 * @return
	 */
	public static boolean isAfter5Min(Calendar startDate){
		if(startDate == null)
			return true;
		Calendar now=Calendar.getInstance();
		return ((now.getTimeInMillis()-startDate.getTimeInMillis())/(1000*60)) >5;
	}
	
	/**
	 * example: 23:40
	 * @param date
	 * @return
	 */
	public static String showHHMI(String date){
		if(date !=null && date.length() >=16)
			return date.substring(11, 16);
		else
			return "";
	}
	
	/**
	 * example: Jul 2012
	 * @param date
	 * @return
	 */
	public static String showEngMY(String date){
	    return showEngMY(str2Calendar(date));
	}
	public static String showEngMY(Calendar date){
		String dateFormat="MMM yyyy";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
	    String newDate = format.format(date.getTime());
	    return newDate;
	}
	
	/*
	 * Jul 23
	 */
	public static String showEngMD(String date){
	    return showEngMD(str2Calendar(date));
	}
	public static String showEngMD(Calendar date){
		String dateFormat="MMM d";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
	    String newDate = format.format(date.getTime());
	    return newDate;
	}
	
	/**
	 * Tuesday 
	 * @param date
	 * @return
	 */
	public static String showEngWeek(String date){
	    return showEngWeek(str2Calendar(date));
	}
	public static String showEngWeek(Calendar date){
		String dateFormat="E";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
	    String newDate = format.format(date.getTime());
	    return newDate;
	}
	
	/**
	 * Tuesday Jul 23
	 * @param date
	 * @return
	 */
	public static String showEngDate(Calendar date){
		String dateFormat="EEEE MMM d";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
	    String newDate = format.format(date.getTime());
	    return newDate;
	}
	
	
	
	/**
	 *  date  like yyyy-MM-dd
	 * @param date  
	 * @return
	 */
	public static String showEngDate(String date){
	    return showEngDate(str2Calendar(date));
	}
	
	public static String showEngPreDay(String date){
		Calendar cal=str2Calendar(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
	    String newDate = showEngDate(cal);
	    return newDate;
	}
	
	public static String showEngNextDay(String date){
		Calendar cal=str2Calendar(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		String newDate = showEngDate(cal);
	    return newDate;
	}
	
	public static String preDay(String date){
		Calendar cal=str2Calendar(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String dateFormat="yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
	    String newDate = format.format(cal.getTime());
	    return newDate;
	}
	
	public static String nextDay(String date){
		Calendar cal=str2Calendar(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		String dateFormat="yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
	    String newDate = format.format(cal.getTime());
	    return newDate;
	}
	
	public static String add15d(String selectDate){
		Calendar cal=str2Calendar(selectDate);
		cal.add(Calendar.DAY_OF_MONTH, 15);
		return formatShort(cal);
	}
	
	/**
	 * endDate > stDate return 1;   = return 0  ; < return -1
	 * @param stDate
	 * @param endDate
	 * @return
	 */
	public static int campare(String stDate,String endDate){
		int res=0;
		Calendar firstday=DateUtils.str2Calendar(stDate);  //to yyyy-MM-dd and then compare
		Calendar second=DateUtils.str2Calendar( endDate );
		if(second.after(firstday))
			res=1;
		else if(second.before(firstday))
			res=-1;
		else
			res=0;
				
		return res;
	}
	
	public static Date longstr2Date(String dt){
		Date resDt=null;
		if(null == dt){
			dt= "2000-1-1 00:00:01";
		}
		
		if(!"".equals(dt.trim())){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				resDt= df.parse(dt);
			}catch(Exception ex){ex.printStackTrace();}
		}
		
		return resDt;
	}
	
	public static Date str2Date(String dt){
		Date resDt=null;
		if(null == dt){
			dt= "2000-1-1";
		}
		
		if(!"".equals(dt.trim())){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try{
				resDt= df.parse(dt);
			}catch(Exception ex){ex.printStackTrace();}
		}
		
		return resDt;
	}
	
	public static Calendar str2Calendar(String dt){
		Calendar resDt=null;
		if(null == dt){
			dt= "2000-1-1 00:00:01";
		}
		
		if(!"".equals(dt.trim())){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try{
				resDt=Calendar.getInstance();   
				resDt.setTime(df.parse(dt));
			}catch(Exception ex){ex.printStackTrace();}
		}
		return resDt;
	}
	
	public static Calendar longstr2Calendar(String dt){
		Calendar resDt=null;
		if(null == dt){
			dt= "2000-1-1 00:00:01";
		}
		
		if(!"".equals(dt.trim())){
			SimpleDateFormat df = new SimpleDateFormat(dateTimeFormat);
			try{
				resDt=Calendar.getInstance();   
				resDt.setTime(df.parse(dt));
			}catch(Exception ex){ex.printStackTrace();}
		}
		return resDt;
	}
	
	public static String format(String dt,String currFormat,String targetFormat){
		String strDt="";
		if(null != dt){
			if(!"".equals(dt.trim())){
				SimpleDateFormat df = new SimpleDateFormat(currFormat);
				SimpleDateFormat targetdf = new SimpleDateFormat(targetFormat);
				try{
					Date resDt= df.parse(dt);
					strDt= targetdf.format(resDt);
				}catch(Exception ex){ex.printStackTrace();}
			}
		}
		return strDt;
	}
	
	public static String formatShort(String dt){
		String strDt="";
		if(null != dt){
			if(!"".equals(dt.trim()) && dt.length() > 10){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat shortdf = new SimpleDateFormat("yyyy-MM-dd");
				try{
					Date resDt= df.parse(dt);
					strDt= shortdf.format(resDt);
				}catch(Exception ex){ex.printStackTrace();}
			}else
				strDt = dt;
		}
		return strDt;
	}
	
	public static boolean isSameOperater(Calendar last,Calendar now){
		long len=now.getTimeInMillis() - last.getTimeInMillis();
		if(len < 200)  //<0.2s����һ�β���
			return true;
		else
			return false;
	}
	
	public static String formatShort(Calendar cal){
		String strDt="";
		if(null != cal){
				SimpleDateFormat shortdf = new SimpleDateFormat("yyyy-MM-dd");
				try{
					strDt= shortdf.format(cal.getTime());
				}catch(Exception ex){ex.printStackTrace();}
		}
		return strDt;
	}
	
	public static String format(Date dt){
		String strDt=null;
		if(null != dt){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				strDt= df.format(dt);
		}
		return strDt;
	}
	public static String getToday(){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getNow() {
		String strDt=null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getSimpleToday(){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getDefaultToday(){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getTodayMonth(){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("MM");
				strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getTodayMonth(String date){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("MM");
				strDt= df.format(str2Date(date));
		return strDt;
	}
	
	public static String getYear(long millSec){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				strDt= df.format(new Date(millSec));
		return strDt;
	}
	
	public static String getTodayYear(){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				strDt= df.format(new Date());
		return strDt;
	}
	
	public static String getTodayYear(String date){
		String strDt=null;
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
				strDt= df.format(str2Date(date));
		return strDt;
	}
	
	/**
	 * get yearMonthe for now
	 * @return
	 */
	public static String getYearMonthOfLast(){
		String strDt=null;
		int m=0;
		int year=0;
		Calendar cal = Calendar.getInstance();
		m=cal.get(Calendar.MONTH);
		if(m == 0){ //the last month is 12 when the value is 0
			m=12;
			year = cal.get(Calendar.YEAR)-1;
		}else{
			year = cal.get(Calendar.YEAR);
		}
		if(m < 10)
			strDt=year+"0"+m;
		else
			strDt=year+""+m;
		return strDt;
	}
	
	public static boolean isYearMonth(String strDt) {
		boolean res = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		strDt=strDt.trim();
		if(strDt.length() != 6){
			res = false;
		}else{
			try {
				 df.parse(strDt+"01");
			} catch (Exception ex) {
				res = false;
			}
		}
		return res;
	}
	
	/**
	 * 格式为友好时间，暂未支持中英文
	 * @param timestamp 默认这里传入的时间戳为10位
	 */
	public static String getFormatFriendlyDate(long timestamp) {
			String ftime = "";
			long sys_timestamp = System.currentTimeMillis() / 1000;		
			long lt = timestamp / 86400;     //16318.406111
			long ct = sys_timestamp / 86400;	   // 16317.666
			
			int days = (int)(ct - lt);		
			if(days <= 0) { //今天以内

				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				ftime = sdf.format(new Date(timestamp * 1000L));
			    
			} else if(days == 1){
				
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				ftime = "昨天 " + sdf.format(new Date(timestamp * 1000L));
				
			} else if(days > 1 && days <= 7) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm");
				ftime = sdf.format(new Date(timestamp * 1000L));
				
			} else if(days > 7) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				ftime = sdf.format(new Date(timestamp * 1000L));
				
			}		
			
			return ftime;
	}
	
	/**
	 * 聊天界面判断两个时间是否足够靠近， 5分钟为界限
	 * @param lasttime 上一条时间
	 * @param nowtime 最近的时间
	 * @return 如果返回true， 说明两个时间之间比较近，可以隐藏
	 */
	public static boolean idEnoughShort(long lasttime, long nowtime) {
		long lt = nowtime - lasttime;
		if(lt < 0)
			return false;
		else if(lt < 5 * 60)    //目前暂定为5分钟
			return true;
		else
			return false;
	}
	
	/**
	 * 获取日期yyyy-MM-dd
	 * 
	 * @param timestamp
	 *           秒
	 * @return
	 */
	public static String toDate(int timestamp) {
		return toDate(1000l*timestamp, "yyyy-MM-dd");
	}
	
	/**
	 * 获取日期yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            毫秒
	 * @return
	 */
	public static String toDateTime(int timestamp) {
		return toDate(1000l * timestamp, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 获取日期yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 *            毫秒
	 * @return
	 */
	public static String toDateTime(long datetime) {
		return toDate(datetime, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取日期
	 * 
	 * @param datetime
	 *            毫秒
	 * @param format
	 * @return
	 */
	public static String toDate(long datetime, String format) {
		Date date = new Date(datetime);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
		return dateFormat.format(date);

	}
	
	/**
	 * 根据日期获取时间戳(秒)
	 * @param date  yyyy-mm-dd
	 * @return long
	 */
	public static long toTimeStamp(String date) {
		return ((Long) (Timestamp.valueOf(date + " 00:00:00.0")
				.getTime() / 1000)).intValue();
	}
	
}
