package com.cambitc.spark.weather.utils;

import java.io.IOException;

import org.apache.spark.api.java.function.PairFunction;

import au.com.bytecode.opencsv.CSVParser;
import scala.Tuple2;

public class TemperatureDailyMap implements PairFunction<String, String, TemperatureDaily> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple2<String, TemperatureDaily> call(String line) throws IOException {
		CSVParser csvReader = new CSVParser(',', '"');
		String[] fields = csvReader.parseLine(line.toString());

		Integer SunRise = 0;
		Integer SunSet = 0;
		try {
			SunRise = Integer.parseInt(fields[Constants.SUNRISE]);
		} catch (NumberFormatException e) {
		}

		try {
			SunSet = Integer.parseInt(fields[Constants.SUNSET]);
		} catch (NumberFormatException e) {
		}

		if (fields.length == Constants.NO_OF_COLS)
			return new Tuple2<String, TemperatureDaily>(fields[Constants.WBAN] + "," + fields[Constants.YEARMONTHDAY].substring(0, 6),
					new TemperatureDaily(fields[Constants.WBAN],
							fields[Constants.YEARMONTHDAY].substring(0, 4) + "-" + fields[Constants.YEARMONTHDAY].substring(4, 6) + "-"
									+ fields[Constants.YEARMONTHDAY].substring(6, 8),
							fields[Constants.H_Date].substring(0, 4), fields[Constants.H_Date].substring(4, 6),
							fields[Constants.H_Date].substring(6, 8), Double.parseDouble(fields[Constants.TMIN].replace("*", "").trim()),
							Double.parseDouble(fields[Constants.TMAX].replace("*", "").trim()),
							Double.parseDouble(fields[Constants.TAVG].replace("*", "").trim()), SunRise.toString(),
							SunSet.toString(), fields[Constants.SNOWFALL], fields[Constants.SEALEVEL]));
		else if (fields.length == Constants.NO_OF_COLS_OLD) return new Tuple2<String, TemperatureDaily>(
				fields[Constants.WBAN] + "," + fields[Constants.YEARMONTHDAY].substring(0, 6),
				new TemperatureDaily(fields[Constants.WBAN],
						fields[Constants.YEARMONTHDAY].substring(0, 4) + "-" + fields[Constants.YEARMONTHDAY].substring(4, 6) + "-"
								+ fields[Constants.YEARMONTHDAY].substring(6, 8),
						fields[Constants.H_Date].substring(0, 4), fields[Constants.H_Date].substring(4, 6), fields[Constants.H_Date].substring(6, 8),
						Double.parseDouble(fields[Constants.TMIN_old].trim().replace("*", "").trim()),
						Double.parseDouble(fields[Constants.TMAX_old].trim().replace("*", "")),
						Double.parseDouble(fields[Constants.TAVG_old].replace("*", "").trim()), "0000", "0000", "0000", "0000"));
		return null;

	}

}
