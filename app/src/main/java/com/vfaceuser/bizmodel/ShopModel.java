package com.vfaceuser.bizmodel;

import java.io.Serializable;

/**
 * Created by HuBin on 15/4/23.
 */
public class ShopModel implements Serializable {

    private String Storeid;
    private String Storename;
    private String Address;
    private int Businesstype;
    private String Contactperson;
    private String Mobilephonenumber;
    private String Phonenumber;
    private String Description;
    private String Distance;
    private double Latitude;
    private double Longitude;
    private String Businesstypename;
    private String FriendlyDistance;

    public String getBusinesstypename() {
        return Businesstypename;
    }

    public void setBusinesstypename(String businesstypename) {
        Businesstypename = businesstypename;
    }

    public String getFriendlyDistance() {
        return FriendlyDistance;
    }

    public void setFriendlyDistance(String friendlyDistance) {
        FriendlyDistance = friendlyDistance;
    }

    public String getStoreid() {
        return Storeid;
    }

    public void setStoreid(String storeid) {
        Storeid = storeid;
    }

    public String getStorename() {
        return Storename;
    }

    public void setStorename(String storename) {
        Storename = storename;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getBusinesstype() {
        return Businesstype;
    }

    public void setBusinesstype(int businesstype) {
        Businesstype = businesstype;
    }

    public String getContactperson() {
        return Contactperson;
    }

    public void setContactperson(String contactperson) {
        Contactperson = contactperson;
    }

    public String getMobilephonenumber() {
        return Mobilephonenumber;
    }

    public void setMobilephonenumber(String mobilephonenumber) {
        Mobilephonenumber = mobilephonenumber;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
