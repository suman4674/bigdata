package com.cambitc.spark.weather.utils;

import java.util.List;

import org.apache.spark.api.java.function.Function;

import au.com.bytecode.opencsv.CSVParser;

public class SanityFilterDaily implements Function<String, Boolean> {

	private static final long serialVersionUID = 1L;
	private RecordSanityDaily sanity = new RecordSanityDaily();
	private List<String> idList;

	public SanityFilterDaily(List<String> ids) {
		idList = ids;

	}

	@Override
	public Boolean call(String s) throws Exception {
		// using csv Parser; splitting the row into tokens by "," delimited which is default
		CSVParser parser = new CSVParser();
		return sanity.sanityFails(parser.parseLine(s), idList);
	}

}
