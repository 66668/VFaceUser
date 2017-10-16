package com.vfaceuser.bizmodel;

import java.io.Serializable;

/**
 * Created by HuBin on 15/4/24.
 */
public class CardListModel implements Serializable{

    private String StoreId;
    private String StoreName;
    private String GradeName;
    private String MemberCardNumber;
    private String ExpiredDateText;
    private String TotalMoney;
    private String TotalPoints;
    private String StoreMemberId;
    private int Businesstype;
    private String Businesstypename;

    
    public int getBusinesstype() {
		return Businesstype;
	}

	public void setBusinesstype(int businesstype) {
		Businesstype = businesstype;
	}

	public String getBusinesstypename() {
        return Businesstypename;
    }

    public void setBusinesstypename(String businesstypename) {
        Businesstypename = businesstypename;
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

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String gradeName) {
        GradeName = gradeName;
    }

    public String getMemberCardNumber() {
        return MemberCardNumber;
    }

    public void setMemberCardNumber(String memberCardNumber) {
        MemberCardNumber = memberCardNumber;
    }

    public String getExpiredDateText() {
        return ExpiredDateText;
    }

    public void setExpiredDateText(String expiredDateText) {
        ExpiredDateText = expiredDateText;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        TotalPoints = totalPoints;
    }

    public String getStoreMemberId() {
        return StoreMemberId;
    }

    public void setStoreMemberId(String storeMemberId) {
        StoreMemberId = storeMemberId;
    }
}
