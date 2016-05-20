package com.cambitc.spark.weather.utils;

import java.io.IOException;

import org.apache.spark.api.java.function.PairFunction;

import au.com.bytecode.opencsv.CSVParser;
import scala.Tuple2;

public class TemperatureHourlyMap implements PairFunction<String, String, TemperatureHourly> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple2<String, TemperatureHourly> call(String line) throws IOException {
		CSVParser csvReader = new CSVParser(',', '"');
		String[] fields = csvReader.parseLine(line.toString());
		Double tmp_Visibility = -1.0;

		//When ever the visibility data is invalid; default it to -1 so we can predict this value in R  
		try {
			tmp_Visibility = Double.parseDouble(fields[Constants.H_Visibility]);
		} catch (NumberFormatException e) {
		}

		if (fields.length == Constants.H_NO_OF_COLS)
			return new Tuple2<String, TemperatureHourly>(fields[Constants.WBAN], new TemperatureHourly(fields[Constants.WBAN],
					fields[Constants.H_Date].substring(0, 4) + "-" + fields[Constants.H_Date].substring(4, 6) + "-"
							+ fields[Constants.H_Date].substring(6, 8),
					fields[Constants.H_Date].substring(0, 4), fields[Constants.H_Date].substring(4, 6), fields[Constants.H_Date].substring(6, 8),
					Integer.parseInt(fields[Constants.H_Time]), tmp_Visibility, Double.parseDouble(fields[Constants.H_DryBulbFarenheit]),
					Double.parseDouble(fields[Constants.H_WetBulbFarenheit]), Double.parseDouble(fields[Constants.H_DewPointFarenheit]),
					Double.parseDouble(fields[Constants.H_RelativeHumidity]), Double.parseDouble(fields[Constants.H_WindSpeed]),
					Integer.parseInt(fields[Constants.H_WindDirection]), Double.parseDouble(fields[Constants.H_StationPressure]),
					Double.parseDouble(fields[Constants.H_Altimeter]), Double.parseDouble(fields[Constants.H_SeaLevelPressure])));

		return null;
	}

}
