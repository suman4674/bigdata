package com.cambitc.spark.weather.utils;



public class CLI {

	public static final int INPUT_DIR_INDEX = 0;
	public static final int STATION_DIR_INDEX = 1;
	public static final int OUTPUT_DIR_INDEX = 2;

	public static final String CMD_SEPERATOR = "=";

	private String[] args;

	private String inputDir;
	private String stationDir;
	private String outputDir;

	// Method to check the number of input parameters passed to the main method; if its not 3 then RAISE ERROR
	public CLI(String[] args) {
		if (args.length != Constants.ARGS_LEN) {
			System.err.println("Format: -input=<input> -station=<station> -output=<output>");
			System.exit(Constants.ERROR);
		}

		this.args = args;
		// Assigning the values to the variables from the arguments
		setInputDir(args[INPUT_DIR_INDEX]);
		setStationDir(args[STATION_DIR_INDEX]);
		setOutputDir(args[OUTPUT_DIR_INDEX]);
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getInputDir() {
		return inputDir;
	}

	/**
	 * Set the argument input parameter
	 *
	 * @param inputDir
	 */
	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getStationDir() {
		return stationDir;
	}

	/**
	 * Set the argument input parameter
	 *
	 * @param inputDir
	 */
	public void setStationDir(String stationDir) {
		this.stationDir = stationDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	/**
	 * Set the argument output parameter
	 *
	 * @param outputDir
	 */
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

}
