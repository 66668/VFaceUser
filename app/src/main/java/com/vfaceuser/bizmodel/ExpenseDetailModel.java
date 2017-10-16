package com.vfaceuser.bizmodel;

/**
 * Created by HuBin on 15/4/23.
 */
public class ExpenseDetailModel {

    private String ExpenseDetailId;
    private String ExpenseId;
    private String CommodityId;
    private String CommodityName;
    private String Quantity;
    private String Amount;


    private String Price;
    private String DiscountMoney;
    private String Points;


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscountMoney() {
        return DiscountMoney;
    }

    public void setDiscountMoney(String discountMoney) {
        DiscountMoney = discountMoney;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getExpenseDetailId() {
        return ExpenseDetailId;
    }

    public void setExpenseDetailId(String expenseDetailId) {
        ExpenseDetailId = expenseDetailId;
    }

    public String getExpenseId() {
        return ExpenseId;
    }

    public void setExpenseId(String expenseId) {
        ExpenseId = expenseId;
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
