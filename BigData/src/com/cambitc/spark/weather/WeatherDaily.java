package com.cambitc.spark.weather;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import com.cambitc.spark.weather.utils.CLI;
import com.cambitc.spark.weather.utils.CalcTemperatureSummary;
import com.cambitc.spark.weather.utils.MAStationFilter;
import com.cambitc.spark.weather.utils.SanityFilterDaily;
import com.cambitc.spark.weather.utils.TemperatureDaily;
import com.cambitc.spark.weather.utils.TemperatureDailyMap;



public class WeatherDaily {

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
		SparkConf conf = new SparkConf().setAppName("WeatherDaily").setMaster("local");
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

			//Collecting the station IDs into token from the rows which belongs to MA   
			@Override
			public String call(String s) {
				String[] temp = s.trim().split("\\|");
				return temp[0];
			}
		});

		// Filter the rows by checking the conditions which belongs to State = "MA" and the quality of data is good.
		JavaRDD<String> saneLines = distFile.filter(new SanityFilterDaily(stationIDs.collect()));

		//Doing a sanity check on the rows and ignore the bad ones which has a incorrect or missing information  
		JavaPairRDD<String, TemperatureDaily> mapperOp = saneLines.mapToPair(new TemperatureDailyMap());

		// Create RDD Pair<StationID + YearMonth, TemperatureDaily> as a key value pair; Key Value is StationID and YearMonth
		JavaPairRDD<String, Iterable<TemperatureDaily>> groupByStation = mapperOp.groupByKey();

		// Create RDD Pair <Year, TemperatureDaily> to take a summary of average
		// Min, Max, Avg
		JavaPairRDD<String, String> avgTemps = groupByStation.mapValues(new CalcTemperatureSummary());
		
		//Writing the files to output directory
		avgTemps.saveAsTextFile(cli.getOutputDir());

		//Closing the spark context once the process completed
		sc.close();

	}

}
