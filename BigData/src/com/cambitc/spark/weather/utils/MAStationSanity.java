package com.cambitc.spark.weather.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MAStationSanity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This method checks if the record passes all the sanity tests
	 *
	 * @param record
	 * @return boolean This returns true if the flight/record fails the sanity
	 *         test
	 * @throws IOException
	 */
	public boolean sanityFails(String[] record) throws IOException {

		//If the row has the first column value as "WBAN" or "WBAN_NUMBER"; Ignore those because those are headers 
		if (record[Constants.WBAN].toUpperCase().equals(Constants.ST_WBAN)) return false;
		if (record[Constants.WBAN].toUpperCase().equals(Constants.ST_WBAN_old)) return false;

		//If the no of columns in the record if not as expected; Ignore those rows 
		if (record.length != Constants.ST_NO_OF_COLS) return false;
		//the files before 2008 doesn't have the state as a separate column; 
		//parsing the column which has state embedded and filtering the row which has state value as "MA"   
		List<String> State = Arrays.asList(record[Constants.STATE].toUpperCase().trim().split(","));
		if (State.contains("MA")) {
			return true;
		}
		return false;
	}

}
