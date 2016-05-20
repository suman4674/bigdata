package com.cambitc.spark.weather.utils;

import java.io.Serializable;

public class TemperatureHourly implements Serializable {

	private static final long serialVersionUID = 1L;

	public TemperatureHourly(String wBAN, String FullDate, String year, String month, String date, Integer timeInMins,
			Double visibility, Double dryBulbFarenheit, Double wetBulbFarenheit, Double dewPointFarenheit,
			Double relativeHumidity, Double windSpeed, Integer windDirection, Double stationPressure, Double altimeter,
			Double seaLevelPressure) {
		super();
		this.wBAN = wBAN;
		this.month = month;
		this.year = year;
		this.date = date;
		this.FullDate = FullDate;
		this.timeInMins = timeInMins;
		this.visibility = visibility;
		this.dryBulbFarenheit = dryBulbFarenheit;
		this.wetBulbFarenheit = wetBulbFarenheit;
		this.dewPointFarenheit = dewPointFarenheit;
		this.relativeHumidity = relativeHumidity;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.stationPressure = stationPressure;
		this.seaLevelPressure = seaLevelPressure;
		this.altimeter = altimeter;

	}

	public String getwBAN() {
		return wBAN;
	}

	public void setwBAN(String wBAN) {
		this.wBAN = wBAN;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTimeInMins() {
		return timeInMins;
	}

	public void setTimeInMins(Integer timeInMins) {
		this.timeInMins = timeInMins;
	}

	public Double getVisibility() {
		return visibility;
	}

	public void setVisibility(Double visibility) {
		this.visibility = visibility;
	}

	public Double getDryBulbFarenheit() {
		return dryBulbFarenheit;
	}

	public void setDryBulbFarenheit(Double dryBulbFarenheit) {
		this.dryBulbFarenheit = dryBulbFarenheit;
	}

	public Double getWetBulbFarenheit() {
		return wetBulbFarenheit;
	}

	public void setWetBulbFarenheit(Double wetBulbFarenheit) {
		this.wetBulbFarenheit = wetBulbFarenheit;
	}

	public Double getDewPointFarenheit() {
		return dewPointFarenheit;
	}

	public void setDewPointFarenheit(Double dewPointFarenheit) {
		this.dewPointFarenheit = dewPointFarenheit;
	}

	public Double getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Integer getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Integer windDirection) {
		this.windDirection = windDirection;
	}

	public Double getStationPressure() {
		return stationPressure;
	}

	public void setStationPressure(Double stationPressure) {
		this.stationPressure = stationPressure;
	}

	public Double getSeaLevelPressure() {
		return seaLevelPressure;
	}

	public void setSeaLevelPressure(Double seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}

	public Double getAltimeter() {
		return altimeter;
	}

	public void setAltimeter(Double altimeter) {
		this.altimeter = altimeter;
	}

	public String getFullDate() {
		return FullDate;
	}

	public void setFullDate(String fullDate) {
		FullDate = fullDate;
	}

	private String wBAN;
	private String month;
	private String year;
	private String date;
	private Integer timeInMins;
	private Double visibility;
	private Double dryBulbFarenheit;
	private Double wetBulbFarenheit;
	private Double dewPointFarenheit;
	private Double relativeHumidity;
	private Double windSpeed;
	private Integer windDirection;
	private Double stationPressure;
	private Double seaLevelPressure;
	private Double altimeter;
	private String FullDate;

}
