package com.cambitc.spark.weather.utils;

import java.text.DecimalFormat;

import org.apache.spark.api.java.function.Function;

public class TemperatureOutDaily implements Function<TemperatureDaily, String>

{

	private static final long serialVersionUID = 1L;

	@Override
	public String call(TemperatureDaily lstTmp) throws Exception {
		// converting the decimal points to 2 digits for temperatures
		DecimalFormat df2 = new DecimalFormat(".##");

		//Returning the object collection 
		//year + month + Date + Min Temp + Max Temp + Avg Temp + sunRise +SunSet + SnowFall + sealevel
		return lstTmp.getYearMonthDay().toString() + "," + lstTmp.getYear().toString() + ","
				+ lstTmp.getMonth().toString() + "," + lstTmp.getDate().toString() + ","
				+ df2.format(lstTmp.gettMin()).toString() + "," + df2.format(lstTmp.gettMax()).toString() + ","
				+ df2.format(lstTmp.gettAvg()).toString() + "," + lstTmp.getSunRise().toString() + ","
				+ lstTmp.getSunSet().toString() + "," + lstTmp.getSnowFall().toString() + ","
				+ lstTmp.getSeaLevel().toString();
	}

}
