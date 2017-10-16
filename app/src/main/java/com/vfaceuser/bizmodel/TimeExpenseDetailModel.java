package com.vfaceuser.bizmodel;

public class TimeExpenseDetailModel {
	
	private String ExpenseId;
	private String OrderNumber;
	private String StoreId;
	private String StoreName;
	private String ServiceId;
	private String ServiceName;
	private String BeginDate;
	private String EndDate;
	private int ScheduleTime;
	private String RemainTime;
	private String ExpensedTime;
	
	public String getExpenseId() {
		return ExpenseId;
	}
	public void setExpenseId(String expenseId) {
		ExpenseId = expenseId;
	}
	public String getOrderNumber() {
		return OrderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}
	public String getStoreId() {
		return StoreId;
	}
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}
	public String getStoreName() {
		return StoreName;
	}
	public void setStoreName(String storeName) {
		StoreName = storeName;
	}
	public String getServiceId() {
		return ServiceId;
	}
	public void setServiceId(String serviceId) {
		ServiceId = serviceId;
	}
	public String getServiceName() {
		return ServiceName;
	}
	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}
	public String getBeginDate() {
		return BeginDate;
	}
	public void setBeginDate(String beginDate) {
		BeginDate = beginDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public int getScheduleTime() {
		return ScheduleTime;
	}
	public void setScheduleTime(int scheduleTime) {
		ScheduleTime = scheduleTime;
	}
	public String getRemainTime() {
		return RemainTime;
	}
	public void setRemainTime(String remainTime) {
		RemainTime = remainTime;
	}
	public String getExpensedTime() {
		return ExpensedTime;
	}
	public void setExpensedTime(String expensedTime) {
		ExpensedTime = expensedTime;
	}
	
}
