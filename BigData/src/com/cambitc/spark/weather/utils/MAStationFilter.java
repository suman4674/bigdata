package com.cambitc.spark.weather.utils;

import org.apache.spark.api.java.function.Function;

import au.com.bytecode.opencsv.CSVParser;


public class MAStationFilter implements Function<String, Boolean> {

	private static final long serialVersionUID = 1L;
	private MAStationSanity sanity = new MAStationSanity();

	@Override
	public Boolean call(String s) throws Exception {
		// using csv Parser; splitting the row into tokens by "|" delimited
		CSVParser parser = new CSVParser('|', '"');
		return sanity.sanityFails(parser.parseLine(s));
	}

}
