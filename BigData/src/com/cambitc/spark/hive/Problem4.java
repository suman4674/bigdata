package com.cambitc.spark.hive;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.execution.QueryExecution;
import org.apache.spark.sql.hive.HiveContext;


public class Problem4 {


	public static void main(String[] args) {
		

		SparkConf conf = new SparkConf().setAppName("com.cambitc.spark").setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		HiveContext  hContext= new HiveContext(context);
		QueryExecution qe= hContext.executeSql("select order_item_order_id, count(*) as cnt from order_items group by order_item_order_id order by cnt desc");
		RDD<InternalRow> orders=qe.toRdd();
		Row[] result= (Row[]) orders.collect();
		


	}
}
