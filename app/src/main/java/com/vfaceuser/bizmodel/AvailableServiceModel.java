package com.vfaceuser.bizmodel;

import java.io.Serializable;

public class AvailableServiceModel implements Serializable{
	
	private String ServiceId;
	private String ServiceName;
	private int InventoryData;
	private int expresnseType;
	
	
	public int getExpresnseType() {
		return expresnseType;
	}
	public void setExpresnseType(int expresnseType) {
		this.expresnseType = expresnseType;
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
	public int getInventoryData() {
		return InventoryData;
	}
	public void setInventoryData(int inventoryData) {
		InventoryData = inventoryData;
	}
	
	
}
