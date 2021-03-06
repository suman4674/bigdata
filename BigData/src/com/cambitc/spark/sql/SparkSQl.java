package com.cambitc.spark.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;


public class SparkSQl {


	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Please provide the input file full path as argument");
			System.exit(0);
		}

		SparkConf conf = new SparkConf().setAppName("com.cambitc.spark").setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		SQLContext sContext= new SQLContext(context);

		//RDD Loaded from emp.txt file
		JavaRDD<String> emps  = context.textFile(args[0]);

		//Row RDD Created with 3 Different DataType Fields
		JavaRDD< Row> employees= emps.map(
				new Function<String, Row>() {
					public Row call(String line) throws Exception {
						String[] fields = line.split(",");
						int i=0;
						return RowFactory.create(fields[i++], 
								Float.parseFloat( fields[i++].trim()), 
								Float.parseFloat( fields[i++].trim()),
								fields[i++],
								Integer.parseInt(fields[i++].trim()), 
								Float.parseFloat( fields[i++].trim()),
								Float.parseFloat( fields[i++].trim()),
								fields[i++],
								Integer.parseInt(fields[i++].trim())

								);

					}
				});

		// Generate the schema based on the string of schema
		List<StructField> fields = new ArrayList<StructField>();
		fields.add(DataTypes.createStructField("auctionid", DataTypes.StringType, true));
		fields.add(DataTypes.createStructField("bid", DataTypes.FloatType, true));
		fields.add(DataTypes.createStructField("bidtime", DataTypes.FloatType, true));
		fields.add(DataTypes.createStructField("bidder", DataTypes.StringType, true));
		fields.add(DataTypes.createStructField("bidderrate", DataTypes.IntegerType, true));
		fields.add(DataTypes.createStructField("openbid", DataTypes.FloatType, true));
		fields.add(DataTypes.createStructField("price", DataTypes.FloatType, true));
		fields.add(DataTypes.createStructField("item", DataTypes.StringType, true));
		fields.add(DataTypes.createStructField("daystolive", DataTypes.IntegerType, true));



		StructType schema = DataTypes.createStructType(fields);


		//Creating Data Frame
		DataFrame dFrame= sContext.createDataFrame(employees, schema);

		// 1.	How many auctions were held?
		dFrame.count();
		
		// 2.	How many bids were made per item?
		dFrame.groupBy("item").count().show(false);

		// o	What's the minimum, maximum, and average bid (price) per item?
		dFrame.groupBy("item").agg(org.apache.spark.sql.functions.max(dFrame.col("price")), org.apache.spark.sql.functions.min(dFrame.col("price")), org.apache.spark.sql.functions.avg(dFrame.col("price"))).show();
//		dFrame.groupBy("item").max("price").show(false);
//		dFrame.groupBy("item").min("price").show(false);
//		dFrame.groupBy("item").avg("price").show(false);
		

		// o	What is the minimum, maximum and average number of bids per item?
		
		
		dFrame.groupBy("item").agg(org.apache.spark.sql.functions.max(dFrame.col("bid")), org.apache.spark.sql.functions.min(dFrame.col("bid")), org.apache.spark.sql.functions.avg(dFrame.col("bid"))).show();
//		dFrame.groupBy("item").max("bid").show(false);
//		dFrame.groupBy("item").min("bid").show(false);
//		dFrame.groupBy("item").avg("bid").show(false);
		

		// 4.	Show the bids with price > 100
		dFrame.where("price > 100").show(false);


		// Register the DataFrame as a table.
		dFrame.registerTempTable("employee");

		// 1.	How many auctions were held?
		DataFrame results = sContext.sql("SELECT count(*) FROM employee" );
		results.show();

		// 2.	How many bids were made per item?
		results = sContext.sql("SELECT count(*) FROM employee group by item" );
		results.show();

		// o	What's the minimum, maximum, and average bid (price) per item?
		results = sContext.sql("SELECT min(price) , max(price), avg(price) FROM employee group by item " );
		results.show();
		// o	What is the minimum, maximum and average number of bids per item?
		results = sContext.sql("SELECT min(bid), max(bid), avg(bid) FROM employee group by item " );
		results.show();

		// 4.	Show the bids with price > 100
		results = sContext.sql("SELECT bid FROM employee where price > 100 " );
		results.show(false);

	}
}
