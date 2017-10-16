package com.vfaceuser.bizmodel;

public class MemberModel {

    private String MemberId = "";
    private String MemberCode = "";
    private String PicPath = "";
    private int FaceId = 0;
    private int RenId = 0;
    private int RenQunId = 0;
    private String FullName = "";
    private String NickName = "";
    private int Sex = 0;
    private String IdNumber = "";
    private int ProvinceId;
    private int CityId;
    private int AreaId;
    private String Address = "";
    private String Birthday = "";
    private String PhoneNumber = "";
    private String Email = "";
    private String Remark = "";
    public String Authenticode;
    public String CustomWelcome;
    private String CreateTime = "";
    private String Operator = "";
    private boolean ActiveFlg = false;
    private String PushKey = "";
    private int DeviceType;
    private boolean IsOnline;
    private String MemberPassword;
    private boolean IsEnableWelcome;
    private String ProvinceName;
    private String CityName = "";
    private String DistrictName = "";
    private String TotalBalance = "0";
    private String TotalConsumeAmount = "0";
    private String TotalPoints= "0" ;


    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getMemberCode() {
        return MemberCode;
    }

    public void setMemberCode(String memberCode) {
        MemberCode = memberCode;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public int getFaceId() {
        return FaceId;
    }

    public void setFaceId(int faceId) {
        FaceId = faceId;
    }

    public int getRenId() {
        return RenId;
    }

    public void setRenId(int renId) {
        RenId = renId;
    }

    public int getRenQunId() {
        return RenQunId;
    }

    public void setRenQunId(int renQunId) {
        RenQunId = renQunId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getAuthenticode() {
        return Authenticode;
    }

    public void setAuthenticode(String authenticode) {
        Authenticode = authenticode;
    }

    public String getCustomWelcome() {
        return CustomWelcome;
    }

    public void setCustomWelcome(String customWelcome) {
        CustomWelcome = customWelcome;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public boolean isActiveFlg() {
        return ActiveFlg;
    }

    public void setActiveFlg(boolean activeFlg) {
        ActiveFlg = activeFlg;
    }

    public String getPushKey() {
        return PushKey;
    }

    public void setPushKey(String pushKey) {
        PushKey = pushKey;
    }

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int deviceType) {
        DeviceType = deviceType;
    }

    public boolean isOnline() {
        return IsOnline;
    }

    public void setOnline(boolean isOnline) {
        IsOnline = isOnline;
    }

    public String getMemberPassword() {
        return MemberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        MemberPassword = memberPassword;
    }

    public boolean isEnableWelcome() {
        return IsEnableWelcome;
    }

    public void setEnableWelcome(boolean isEnableWelcome) {
        IsEnableWelcome = isEnableWelcome;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getTotalBalance() {
        return TotalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        TotalBalance = totalBalance;
    }

    public String getTotalConsumeAmount() {
        return TotalConsumeAmount;
    }

    public void setTotalConsumeAmount(String totalConsumeAmount) {
        TotalConsumeAmount = totalConsumeAmount;
    }

    public String getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        TotalPoints = totalPoints;
    }
}
