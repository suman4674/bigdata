package com.cambitc.spark.weather.utils;

import java.text.DecimalFormat;

import org.apache.spark.api.java.function.Function;



public class CalcTemperatureSummary implements Function<Iterable<TemperatureDaily>, String>

{

	private static final long serialVersionUID = 1L;

	@Override
	public String call(Iterable<TemperatureDaily> lstTmp) throws Exception {
		DecimalFormat df2 = new DecimalFormat(".##");
		Double avgAvgTemp = 0.0;
		Double avgMinTemp = 0.0;
		Double avgMaxTemp = 0.0;
		Integer avgSunRiseTime = 0;
		Integer avgSunSetTime = 0;
		Integer avgSunRiseTimeMin = 0;
		Integer avgSunSetTimeMin = 0;

		// we need to know the count to apply in the denominator to calculate the average of Minimum, Maximum and average temperature
		int count = 0;
		//Looping through the Iterable object to sum all the individual values
		for (TemperatureDaily tmp : lstTmp) {
			avgAvgTemp += tmp.gettAvg();
			avgMinTemp += tmp.gettMin();
			avgMaxTemp += tmp.gettMax();

			avgSunRiseTimeMin += (Integer.parseInt(tmp.getSunRise()) / 100) * 60
					+ Integer.parseInt(tmp.getSunRise()) % 100;

			avgSunSetTimeMin += (Integer.parseInt(tmp.getSunSet()) / 100) * 60
					+ Integer.parseInt(tmp.getSunSet()) % 100;
			count++;
		}
		//  calculating the average of Minimum, Maximum and average temperature
		avgAvgTemp = (avgAvgTemp / count);
		avgMinTemp = (avgMinTemp / count);
		avgMaxTemp = (avgMaxTemp / count);
		avgSunRiseTime = (avgSunRiseTimeMin / count);
		avgSunSetTime = (avgSunSetTimeMin / count);

		Integer avgSunRiseTimeHrs = (avgSunRiseTime / 60);
		Integer avgSunRiseTimeMins = (avgSunRiseTime % 60);

		Integer avgSunSetTimeHrs = (avgSunSetTime / 60);
		Integer avgSunSetTimeMins = (avgSunSetTime % 60);

		//Returning the collection concatenated with "," Comma 
		//Min Temp + Max Temp + Avg Temp + sunRise +SunSet
		return df2.format(avgMinTemp).toString() + "," + df2.format(avgMaxTemp).toString() + ","
				+ df2.format(avgAvgTemp).toString() + "," + avgSunRiseTimeHrs.toString()
				+ ("0" + avgSunRiseTimeMins.toString()).substring(0, 2) + "," + avgSunSetTimeHrs.toString()
				+ ("0" + avgSunSetTimeMins.toString()).substring(0, 2);
	}

}
