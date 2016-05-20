package com.cambitc.spark.weather.utils;

import java.io.Serializable;
import java.util.List;

public class RecordSanityDaily implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This method checks if the record passes all the sanity tests
	 *
	 * @param record
	 * @return boolean This returns true if the flight/record fails the sanity
	 *         test
	 */
	public boolean sanityFails(String[] record, List<String> stationIds) {
		// Checking whether the row count matching to no of columns expected; If not then Ignore the record 
		if (record.length == Constants.NO_OF_COLS) {

			// Checking whether the first column in our row is "WBAN" value; If yes Ignore the record; because it is a header record 
			if (record[Constants.WBAN].equals(Constants.ST_WBAN)) return false;
			// Checking whether the value in our temperature column is "M" value; If yes Ignore the record; because it is a missing reading 
			if (record[Constants.TAVG].equals("M") || record[Constants.TMIN].equals("M") || record[Constants.TMAX].equals("M")) return false;

			// Checking whether the value in our temperature column is numeric; If not Ignore the record; because it is a n correct value 
			// removing characters like "*" which is known data where it can be ignored for our analysis purpose
			try {
				@SuppressWarnings("unused")
				Double tmp = Double.parseDouble(record[Constants.TAVG].replace("*", "").trim());
				@SuppressWarnings("unused")
				Double tMin = Double.parseDouble(record[Constants.TMIN].replace("*", "").trim());
				@SuppressWarnings("unused")
				Double tMax = Double.parseDouble(record[Constants.TMAX].replace("*", "").trim());
			} catch (NumberFormatException e) {
				return false;
			}

			// check the StationID column value has our any of our StationID value from station file, if its not "MA" then ignore the record 
			if (stationIds.contains(record[Constants.WBAN])) return true;
		} 
		// Checking whether the row count matching to no of columns expected; If not then Ignore the record; 
		//this is applicable when processing processing the data which is before 2008 because the structure of the file is different      
		else if (record.length == Constants.NO_OF_COLS_OLD) {
			// Checking whether the first column in our row is "WBAN_Number" value; If yes Ignore the record; because it is a header record 
			if (record[Constants.WBAN].equals(Constants.ST_WBAN_old)) return false;
			// Checking whether the value in our temperature column is "M" value; If yes Ignore the record; because it is a missing reading 
			if (record[Constants.TAVG_old].trim().equals("M") || record[Constants.TMIN_old].trim().equals("M")
					|| record[Constants.TMAX_old].trim().equals("M"))
				return false;

			// Checking whether the value in our temperature column is numeric; If not Ignore the record; because it is a n correct value
			// removing characters like "*" which is known data where it can be ignored for our analysis purpose
			try {
				@SuppressWarnings("unused")
				Double tmp = Double.parseDouble(record[Constants.TAVG_old].replace("*", "").trim());
				@SuppressWarnings("unused")
				Double tMin = Double.parseDouble(record[Constants.TMIN_old].replace("*", "").trim());
				@SuppressWarnings("unused")
				Double tMax = Double.parseDouble(record[Constants.TMAX_old].replace("*", "").trim());
			} catch (NumberFormatException e) {
				return false;
			}

			// check the StationID column value has our any of our StationID value from station file, if its not "MA" then ignore the record 
			if (stationIds.contains(record[Constants.WBAN])) return true;
		}
		return false;
	}

}
