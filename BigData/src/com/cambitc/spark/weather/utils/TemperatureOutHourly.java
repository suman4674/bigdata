package com.cambitc.spark.weather.utils;

import java.text.DecimalFormat;

import org.apache.spark.api.java.function.Function;

public class TemperatureOutHourly implements Function<TemperatureHourly, String>

{

	private static final long serialVersionUID = 1L;

	@Override
	public String call(TemperatureHourly lstTmp) throws Exception {
		// converting the decimal points to 2 digits for temperatures
		DecimalFormat df2 = new DecimalFormat(".##");

		return lstTmp.getFullDate() + "," + lstTmp.getYear().toString() + "," + lstTmp.getMonth().toString() + ","
				+ lstTmp.getDate().toString() + ","
				+ ("0000" + lstTmp.getTimeInMins().toString())
						.substring(("0000" + lstTmp.getTimeInMins().toString()).length() - 4)
				+ "," + df2.format(lstTmp.getVisibility()).toString() + ","
				+ df2.format(lstTmp.getDryBulbFarenheit()).toString() + ","
				+ df2.format(lstTmp.getWetBulbFarenheit()).toString() + ","
				+ df2.format(lstTmp.getDewPointFarenheit()).toString() + ","
				+ df2.format(lstTmp.getRelativeHumidity()).toString() + ","
				+ df2.format(lstTmp.getWindSpeed()).toString() + "," + lstTmp.getWindDirection().toString() + ","
				+ df2.format(lstTmp.getStationPressure()).toString() + ","
				+ df2.format(lstTmp.getSeaLevelPressure()).toString() + ","
				+ df2.format(lstTmp.getAltimeter()).toString();
	}

}
