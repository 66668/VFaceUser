package com.vfaceuser.bizmodel;

import java.util.ArrayList;

public class CountsExpenseDetailModel {
	
	private String ExpenseId;
	private String OrderNumber;
	private String StoreId;
	private String StoreName;
	private int TotalExpenseData;
	private String ExpenseDate;
	private ArrayList<ExpenseDetail> ExpenseDetails;

	public class ExpenseDetail{
		private String ExpenseDetailId;
		private String CommodityId;
		private String CommodityName;
		private int ExpenseData;
		public String getExpenseDetailId() {
			return ExpenseDetailId;
		}
		public void setExpenseDetailId(String expenseDetailId) {
			ExpenseDetailId = expenseDetailId;
		}
		public String getCommodityId() {
			return CommodityId;
		}
		public void setCommodityId(String commodityId) {
			CommodityId = commodityId;
		}
		public String getCommodityName() {
			return CommodityName;
		}
		public void setCommodityName(String commodityName) {
			CommodityName = commodityName;
		}
		public int getExpenseData() {
			return ExpenseData;
		}
		public void setExpenseData(int expenseData) {
			ExpenseData = expenseData;
		}
		
	}

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

	public int getTotalExpenseData() {
		return TotalExpenseData;
	}

	public void setTotalExpenseData(int totalExpenseData) {
		TotalExpenseData = totalExpenseData;
	}

	public String getExpenseDate() {
		return ExpenseDate;
	}

	public void setExpenseDate(String expenseDate) {
		ExpenseDate = expenseDate;
	}

	public ArrayList<ExpenseDetail> getExpenseDetails() {
		return ExpenseDetails;
	}

	public void setExpenseDetails(ArrayList<ExpenseDetail> expenseDetails) {
		ExpenseDetails = expenseDetails;
	}
	
}
