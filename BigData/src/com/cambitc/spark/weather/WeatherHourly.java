package com.cambitc.spark.weather;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import com.cambitc.spark.weather.utils.CLI;
import com.cambitc.spark.weather.utils.MAStationFilter;
import com.cambitc.spark.weather.utils.SanityFilterHourly;
import com.cambitc.spark.weather.utils.TemperatureHourly;
import com.cambitc.spark.weather.utils.TemperatureHourlyMap;
import com.cambitc.spark.weather.utils.TemperatureOutHourly;



public class WeatherHourly {

	public static void main(String[] args) {
		CLI cli = new CLI(args);
		runSpark(cli);
	}

	/**
	 * This method configures and runs the Spark program.
	 *
	 * @param cli
	 */

	private static void runSpark(CLI cli) {

		//Initialize the spark context
		SparkConf conf = new SparkConf().setAppName("WeatherHourly").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// Read the weather data which is daily files from the input directory
		JavaRDD<String> distFile = sc.textFile(cli.getInputDir());

		// Read the station data which is from the input directory
		JavaRDD<String> stationFile = sc.textFile(cli.getStationDir());

		// cache the RDD to be used again for other calculations
		distFile.cache();

		// Apply MA state station filter
		JavaRDD<String> MAStations = stationFile.filter(new MAStationFilter());

		// Take the list of stationIds into an array
		JavaRDD<String> stationIDs = MAStations.map(new Function<String, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String call(String s) {
				String[] temp = s.trim().split("\\|");
				return temp[0];

			}
		});

		// Filter the rows by checking the conditions which belongs to State = "MA" and the quality of data is good.
		JavaRDD<String> saneLines = distFile.filter(new SanityFilterHourly(stationIDs.collect()));
		// saneLines.saveAsTextFile(cli.getOutputDir());

		//Doing a sanity check on the rows and ignore the bad ones which has a incorrect or missing information  
		JavaPairRDD<String, TemperatureHourly> mapperOp = saneLines.mapToPair(new TemperatureHourlyMap());

		// Create RDD Pair <Year, TemperatureHourly> to take a data rows
		JavaPairRDD<String, String> HourlyTemps = mapperOp.mapValues(new TemperatureOutHourly());
		HourlyTemps.saveAsTextFile(cli.getOutputDir());

		sc.close();

	}

}
