package com.cambitc.spark.fueleco;

import java.io.Serializable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class FuelEconomyData implements Serializable,Comparable {

	private int mfyear;
	private String mfrName;
	private String transmissionType;
	private String cylinderDeactivationStatus;
	private String startstopsysStatus;
	private String carline;
	private int fuelCost;
	private int carLineClassCode;
	private String carLineClassDesc;
	
	public int getMfyear() {
		return mfyear;
	}

	public void setMfyear(int mfyear) {
		this.mfyear = mfyear;
	}


	public String getCarLineClassDesc() {
		return carLineClassDesc;
	}

	public void setCarLineClassDesc(String carLineClassDesc) {
		this.carLineClassDesc = carLineClassDesc;
	}

	public String getMfrName() {
		return mfrName;
	}

	public void setMfrName(String mfrName) {
		this.mfrName = mfrName;
	}

	public String getCarline() {
		return carline;
	}

	public void setCarline(String carline) {
		this.carline = carline;
	}

	public int getFuelCost() {
		return fuelCost;
	}

	public void setFuelCost(int fuelCost) {
		this.fuelCost = fuelCost;
	}

	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	public String getCylinderDeactivationStatus() {
		return cylinderDeactivationStatus;
	}

	public void setCylinderDeactivationStatus(String cylinderDeactivationStatus) {
		this.cylinderDeactivationStatus = cylinderDeactivationStatus;
	}

	public String getStartstopsysStatus() {
		return startstopsysStatus;
	}

	public void setStartstopsysStatus(String startstopsysStatus) {
		this.startstopsysStatus = startstopsysStatus;
	}

	public int getCarLineClassCode() {
		return carLineClassCode;
	}

	
	public void setCarLineClassCode(int carLineClassCode) {
		this.carLineClassCode = carLineClassCode;
	}
	   
	@Override
	//Implementing comareTo method , for comparing fuel cost for FuelEconomyData objects.
	public int compareTo(Object o) {
		   FuelEconomyData other = (FuelEconomyData) o;

	        if (this.fuelCost < other.getFuelCost()) {
	            return 1;
	        }

	        if (this.fuelCost > other.getFuelCost()) {
	            return -1;
	        }

	        if (this.fuelCost == other.getFuelCost()) {
	            return 0;
	        }
	        return 0;
	    }

}
