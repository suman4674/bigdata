package  com.cambitc.spark.fueleco;

//Importing required classes.
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import scala.Tuple2;

public class FuelEfficiencyProject {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Creating object of type ICsvBeanWriter, which will be used to create
		// csvs for results after data processing.
		// csv will contain results as top 2 leaders (having lowest Annual
		// Fuel Cost) , per class of vehicle for years 2012, 2013, 2014,2015 and
		// 2016.
		ICsvBeanWriter beanWriterFor2012 = null;
		ICsvBeanWriter beanWriterFor2013 = null;
		ICsvBeanWriter beanWriterFor2014 = null;
		ICsvBeanWriter beanWriterFor2015 = null;
		ICsvBeanWriter beanWriterFor2016 = null;

		// Storing path of input data file (csv) in string variable 'inputFile'.
		String inputFile = args[0];

		// Storing path of output data file (csv) in string variables .
		// Each output csv file will contains records and fields necessary for
		// visualizing top two leaders
		// from each vehicle class type for each year.
		String outputFileFor2012 = args[1];
		String outputFileFor2013 = args[2];
		String outputFileFor2014 = args[3];
		String outputFileFor2015 = args[4];
		String outputFileFor2016 = args[5];
		
		//Storing path of output data file (csv) in string variable 
		//This output csv is produced by using sparksql API com.databricks.spark.csv
		//This output csv will contain records and fields necessary for multi linear regression
		//predicting annual fuel cost (target variable) and transmission type, cylinder deactivation status and 
		//start stop system status as features. 
		String outputFileForLR = args[6];

		// SparkConf():Create a SparkConf that loads defaults from system
		// properties and the classpath.
		// setAppName(String name):Set a name for application.
		// setMaster(String master):The master URL to connect to, such as
		// "local" to run locally with one thread.
		SparkConf sparkConf = new SparkConf().setAppName("fueleconomy")
				.setMaster("local");
		// Create a JavaSparkContext that loads settings from sparkConf object
		// specifying Spark parameters.
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		// SQLContext:The entry point for working with structured data (rows and
		// columns) in Spark. Allows the creation of DataFrame objects as well
		// as the execution of SQL queries.
		SQLContext sqlContext = new SQLContext(ctx);
		// Creating RDD variable 'inputRDD' which accepts input text from input
		// csv file.
		JavaRDD<String> inputRDD = ctx.textFile(inputFile);
		// Creating RDD variable 'fedRDD' which stores objects of type
		// <FuelEconomyData>.
		// Load data from 'inputRDD' and convert each line to a Java Bean of
		// type <FuelEconomyData>.
		JavaRDD<FuelEconomyData> fedRDD = inputRDD
				.map(new Function<String, FuelEconomyData>() {
					@Override
					public FuelEconomyData call(String line) {
						// Splitting content of each line on the basis of (",").
						String[] parts = line.split(",");

						// Creating object 'fd' of type FuelEconomyData.
						FuelEconomyData fd = new FuelEconomyData();
						// Checking for null and empty string for each field and
						// converting string to integer for fields mfyear
						// (Manufacture Year)
						// fuelCost and carLineClassCode. Then setting
						// properties (fields) for object 'fd' of type
						// <FuelEconomyData>.Using trim() method to eliminate
						// leading and trailing spaces.
						if (parts[0] != null && !"".equals(parts[0])) {
							try {
								fd.setMfyear(Integer.parseInt(parts[0].trim()));
							} catch (NumberFormatException e) {
								System.out.println("This is not a number");
								System.out.println(e.getMessage());
							}
						}
						if (parts[1] != null && !"".equals(parts[1])) {
							try {
								fd.setMfrName(parts[1].trim());
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}

						if (parts[2] != null && !"".equals(parts[2])) {
							try {
								fd.setCarline(parts[2].trim());
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}

						if (parts[3] != null && !"".equals(parts[3])) {
							try {
								fd.setFuelCost(Integer.parseInt(parts[3].trim()));
							} catch (NumberFormatException e) {
								System.out.println("This is not a number");
								System.out.println(e.getMessage());
							}
						}
						if (parts[4] != null && !"".equals(parts[4])) {
							try {
								fd.setCarLineClassCode(Integer
										.parseInt(parts[4].trim()));
							} catch (NumberFormatException e) {
								System.out.println("This is not a number");
								System.out.println(e.getMessage());
							}
						}

						if (parts[5] != null && !"".equals(parts[5])) {
							try {
								fd.setCarLineClassDesc(parts[5].trim());
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}
						if (parts[6] != null && !"".equals(parts[6])) {
							try {
								fd.setTransmissionType(parts[6].trim());
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}
						if (parts[7] != null && !"".equals(parts[7].trim())) {
							try {
								fd.setCylinderDeactivationStatus(parts[7]);
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}
						if (parts[8] != null && !"".equals(parts[8])) {
							try {
								fd.setStartstopsysStatus(parts[8].trim());
							} catch (Exception e) {

								System.out.println(e.getMessage());
							}
						}
						return fd;

					}
				});

		// Creating Java function class 'FilterByVehicleClassType'. It filters
		// out objects of type <FuelEconomyData> on the basis of
		// vehicle class code. Vehicle class code is obtained through method
		// :getCarLineClassCode(). class code value is passed
		// as a parameter.
		class FilterByVehicleClassType implements
				Function<FuelEconomyData, Boolean> {
			private Integer classCode;

			public FilterByVehicleClassType(Integer classCode) {
				this.classCode = classCode;
			}

			public Boolean call(FuelEconomyData f) throws Exception {
				// TODO Auto-generated method stub
				if (f.getCarLineClassCode() == classCode) {
					return true;
				} else {
					return false;
				}
			}
		}
		// Creating Java function class 'FilterByYear'. It filters out objects
		// of type <FuelEconomyData> on the basis of
		// manufacture Year. Vehicle manufacture year is obtained through method
		// :getMfyear(). manufacture year value is passed
		// as a parameter.
		class FilterByYear implements Function<FuelEconomyData, Boolean> {
			private Integer mfyear;

			public FilterByYear(Integer mfyear) {
				this.mfyear = mfyear;
			}

			public Boolean call(FuelEconomyData f) throws Exception {
				// TODO Auto-generated method stub
				if (f.getMfyear() == mfyear) {
					return true;
				} else {
					return false;
				}
			}
		}

		// 'grpByYearPairRDD' JavaPair RDD is created which contains key value
		// pairs having manufacture year as key
		// and object of type <FuelEconomyData> as value. groupByKey() method is
		// used to group them by key 'manufacture year'
		// Then printing "Total number of distinct years  : " . Its value will
		// come as 5 , as we have data from 2012 to 2016, i.e. total 5 distinct
		// years.
		// groupByKey():Group the values for each key in the RDD into a single
		// sequence.
		JavaPairRDD<Integer, Iterable<FuelEconomyData>> grpByYearPairRDD = fedRDD
				.mapToPair(
						new PairFunction<FuelEconomyData, Integer, FuelEconomyData>() {
							public Tuple2<Integer, FuelEconomyData> call(
									FuelEconomyData fd) {
								return new Tuple2(fd.getMfyear(), fd);
							}
						}).groupByKey();

		// count():Return the number of elements in the RDD.
		System.out.println("Total number of distinct years  :  "
				+ grpByYearPairRDD.count());

		// 'grpByVehicleClassPairRDD' JavaPair RDD is created which contains key
		// value pairs having Vehicle Class Type as key
		// and object of type <FuelEconomyData> as value. groupByKey() method is
		// used to group them by key 'Vehicle Class Type'
		// groupByKey():Group the values for each key in the RDD into a single
		// sequence.
		JavaPairRDD<Integer, Iterable<FuelEconomyData>> grpByVehicleClassPairRDD = fedRDD
				.mapToPair(
						new PairFunction<FuelEconomyData, Integer, FuelEconomyData>() {
							public Tuple2<Integer, FuelEconomyData> call(
									FuelEconomyData fd) {
								return new Tuple2(fd.getCarLineClassCode(), fd);
							}
						}).groupByKey();

		// keys():Return an RDD with the keys of each tuple.
		// collect():Return an array that contains all of the elements in this
		// RDD.

		// Creating List 'listofkeys' which contains keys of
		// 'grpByVehicleClassPairRDD'. These are nothing but class type if
		// vehicles 1 to 33, which we have in input data set.
		List<Integer> listofkeys = grpByVehicleClassPairRDD.keys().collect();

		// Creating List 'listofkeysYears' which contains keys of
		// 'grpByYearPairRDD'. These are nothing but years 2012 to 2016.
		List<Integer> listofkeysYears = grpByYearPairRDD.keys().collect();

		// Creating List 'leadersforeachYearforeachClass' which contains objects
		// of type <FuelEconomyData>.
		// It contains top 2 leaders i.e. objects from each vehicle class, per
		// year.
		// Outer for loop is used to traverse through list of years and inner
		// for loop is used to traverse through list of vehicle class type.

		List<FuelEconomyData> leadersforeachYearforeachClass = new ArrayList<FuelEconomyData>();

		for (Integer yr : listofkeysYears) {
			for (Integer cl : listofkeys) {

				// first filter is applied to filter according to 'manufacture
				// year' in present iteration and second filter is applied to
				// filter
				// according to 'vehicle class type' in present iteration.
				// top(2) is used to give top two records.
				// addAll() :method appends all of the elements in the specified
				// collection to the end of this list, in the order that they
				// are
				// returned by the specified collection's Iterator.
				leadersforeachYearforeachClass.addAll(fedRDD
						.filter(new FilterByYear(yr))
						.filter(new FilterByVehicleClassType(cl)).top(2));

			}
		}

		try {
			// CsvBeanWriter writes a CSV file by mapping each field on the bean
			// to a column in the CSV file (using the supplied name mapping).
			// Before reading or writing CSV files, we must supply the
			// reader/writer with some preferences.
			// For STANDARD_PREFERENCE, Quote character=
			// "	Delimiter character="," End of line symbols = \r\n
			// First argument is path to output csv.
			beanWriterFor2012 = new CsvBeanWriter(new FileWriter(
					outputFileFor2012), CsvPreference.STANDARD_PREFERENCE);

			beanWriterFor2013 = new CsvBeanWriter(new FileWriter(
					outputFileFor2013), CsvPreference.STANDARD_PREFERENCE);

			beanWriterFor2014 = new CsvBeanWriter(new FileWriter(
					outputFileFor2014), CsvPreference.STANDARD_PREFERENCE);

			beanWriterFor2015 = new CsvBeanWriter(new FileWriter(
					outputFileFor2015), CsvPreference.STANDARD_PREFERENCE);

			beanWriterFor2016 = new CsvBeanWriter(new FileWriter(
					outputFileFor2016), CsvPreference.STANDARD_PREFERENCE);

			String[] header = new String[] { "mfyear", "carLineClassCode",
					"carLineClassDesc", "mfrName", "carline", "fuelCost" };
			// Super CSV has CellProcessors that convert Java data types to
			// Strings and vice-versa.
			// They automate the data type conversions, and enforce constraints
			CellProcessor[] processors = new CellProcessor[] { new NotNull(), // mfyear
					new NotNull(), // carLineClassCode
					new NotNull(), // carLineClassDesc
					new NotNull(), // mfrName
					new NotNull(), // carline
					new NotNull() // fuelCost
			};

			// write the header for each output csv file for years
			// 2012,2013,2014,2015 and 2016.
			beanWriterFor2012.writeHeader(header);
			beanWriterFor2013.writeHeader(header);
			beanWriterFor2014.writeHeader(header);
			beanWriterFor2015.writeHeader(header);
			beanWriterFor2016.writeHeader(header);

			// in this for loop, we are iterating through list
			// 'leadersforeachYearforeachClass' containing objects of type
			// <FuelEconomyData>,
			// and writing the beans to output csv for each year according to
			// manufacture year value of FuelEconomyData object.
			for (FuelEconomyData fed : leadersforeachYearforeachClass) {
				// System.out.println(fed.getCarLineClassCode()+"    "+fed.getCarLineClassDesc()+"   "+fed.getMfrName()+"   "+fed.getCarline()+"   "+fed.getFuelCost());
				if (fed.getMfyear() == 2012) {
					beanWriterFor2012.write(fed, header, processors);
				} else if (fed.getMfyear() == 2013) {
					beanWriterFor2013.write(fed, header, processors);
				} else if (fed.getMfyear() == 2014) {
					beanWriterFor2014.write(fed, header, processors);
				} else if (fed.getMfyear() == 2015) {
					beanWriterFor2015.write(fed, header, processors);
				} else if (fed.getMfyear() == 2016) {
					beanWriterFor2016.write(fed, header, processors);
				}

			}
		}

		catch (IOException ex) {
			System.err.println("Error writing the CSV file: " + ex);
		}
		// closing all beanWriter objects in finally block.
		finally {
			if (beanWriterFor2012 != null && beanWriterFor2013 != null && beanWriterFor2014 != null && beanWriterFor2015 != null && beanWriterFor2016 != null ) {

				beanWriterFor2012.close();
				beanWriterFor2013.close();
				beanWriterFor2014.close();
				beanWriterFor2015.close();
				beanWriterFor2016.close();

			}
			
			
		}

		// Apply a schema to an RDD of Java Beans and register it as a table
		// "fed".

		DataFrame schemaPeople = sqlContext.createDataFrame(fedRDD,
				FuelEconomyData.class);
		schemaPeople.registerTempTable("fed");

		// The sql function on a SQLContext enables applications to run SQL
		// queries programmatically and
		// returns the result as a DataFrame.
		// We are getting fields
		// "mfyear,transmissionType,cylinderDeactivationStatus,startstopsysStatus,fuelCost"
		// from temp table 'fed'. Then result is stored in dataframe 'dfforLR'.

		DataFrame dfforLR = sqlContext
				.sql("SELECT mfyear,transmissionType,cylinderDeactivationStatus,startstopsysStatus,fuelCost FROM fed");

		// We want to save a dataframe as CSV file.We do not want header in csv
		// for linear regression, so setting header as false.
		// by default different partions of output are generated, we want a
		// single csv file, so using coalesce(1).
		dfforLR.select("mfyear", "transmissionType",
				"cylinderDeactivationStatus", "startstopsysStatus", "fuelCost")
				.coalesce(1).write().format("com.databricks.spark.csv")
				.option("header", "false").save(outputFileForLR);

	}

}
