package com.cambitc.spark.weather.utils;

import java.io.Serializable;
import java.util.List;

public class RecordSanityHourly implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This method checks if the record passes all the sanity tests
	 *
	 * @param record
	 * @return boolean This returns true if the flight/record fails the sanity
	 *         test
	 */
	public boolean sanityFails(String[] record, List<String> stationIds) {
		// System.out.println(record.length);
		if (record.length == Constants.H_NO_OF_COLS) {
			if (record[Constants.WBAN].equals(Constants.ST_WBAN)) return false;
			if (record[Constants.H_Date].length() != 8) return false;

			//If any of the data is not valid value ignore those rows because its not a valid measurement 
			try {
				@SuppressWarnings("unused")
				Double tmp_Altimeter = Double.parseDouble(record[Constants.H_Altimeter]);
				@SuppressWarnings("unused")
				Double tmp_Date = Double.parseDouble(record[Constants.H_Date]);
				@SuppressWarnings("unused")
				Double tmp_DewPointFarenheit = Double.parseDouble(record[Constants.H_DewPointFarenheit]);
				@SuppressWarnings("unused")
				Double tmp_DryBulbFarenheit = Double.parseDouble(record[Constants.H_DryBulbFarenheit]);
				@SuppressWarnings("unused")
				Double tmp_RelativeHumidity = Double.parseDouble(record[Constants.H_RelativeHumidity]);
				@SuppressWarnings("unused")
				Double tmp_SeaLevelPressure = Double.parseDouble(record[Constants.H_SeaLevelPressure]);
				@SuppressWarnings("unused")
				Double tmp_StationPressure = Double.parseDouble(record[Constants.H_StationPressure]);
				@SuppressWarnings("unused")
				Double tmp_Time = Double.parseDouble(record[Constants.H_Time]);
				@SuppressWarnings("unused")
				Double tmp_WetBulbFarenheit = Double.parseDouble(record[Constants.H_WetBulbFarenheit]);
				@SuppressWarnings("unused")
				Double tmp_WindDirection = Double.parseDouble(record[Constants.H_WindDirection]);
				@SuppressWarnings("unused")
				Double tmp_WindSpeed = Double.parseDouble(record[Constants.H_WindSpeed]);
			} catch (NumberFormatException e) {
				return false;
			}

			// Filtering MA state from other RDD
			if (stationIds.contains(record[Constants.WBAN])) return true;
		}

		return false;
	}

}
