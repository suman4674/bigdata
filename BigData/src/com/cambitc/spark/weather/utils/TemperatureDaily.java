package com.cambitc.spark.weather.utils;

import java.io.Serializable;

public class TemperatureDaily implements Serializable {

	private static final long serialVersionUID = 1L;

	public TemperatureDaily(String wBAN, String yearMonthDay, String year, String month, String date, Double tMin,
			Double tMax, Double tAvg, String sunRise, String sunSet, String snowFall, String seaLevel) {
		super();
		this.wBAN = wBAN;
		this.yearMonthDay = yearMonthDay;
		this.year = year;
		this.month = month;
		this.date = date;
		this.tMin = tMin;
		this.tMax = tMax;
		this.tAvg = tAvg;
		this.sunRise = sunRise;
		this.sunSet = sunSet;
		this.snowFall = snowFall;
		this.seaLevel = seaLevel;

	}

	public String getwBAN() {
		return wBAN;
	}

	public void setwBAN(String wBAN) {
		this.wBAN = wBAN;
	}

	public String getYearMonthDay() {
		return yearMonthDay;
	}

	public void setYearMonthDay(String yearMonthDay) {
		this.yearMonthDay = yearMonthDay;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double gettMin() {
		return tMin;
	}

	public void settMin(Double tMin) {
		this.tMin = tMin;
	}

	public Double gettMax() {
		return tMax;
	}

	public void settMax(Double tMax) {
		this.tMax = tMax;
	}

	public Double gettAvg() {
		return tAvg;
	}

	public void settAvg(Double tAvg) {
		this.tAvg = tAvg;
	}

	public String getSunRise() {
		return sunRise;
	}

	public void setSunRise(String sunRise) {
		this.sunRise = sunRise;
	}

	public String getSunSet() {
		return sunSet;
	}

	public void setSunSet(String sunSet) {
		this.sunSet = sunSet;
	}

	public String getSnowFall() {
		return snowFall;
	}

	public void setSnowFall(String snowFall) {
		this.snowFall = snowFall;
	}

	public String getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(String seaLevel) {
		this.seaLevel = seaLevel;
	}

	private String wBAN;
	private String yearMonthDay;
	private String year;
	private String month;
	private String date;
	private Double tMin;
	private Double tMax;
	private Double tAvg;
	private String sunRise;
	private String sunSet;
	private String snowFall;
	private String seaLevel;

}
