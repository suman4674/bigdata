/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cambitc.spark.streaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import scala.Tuple2;
import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 * Usage: JavaDirectKafkaWordCount <brokers> <topics>ß
 *   <brokers> is a list of one or more Kafka brokers
 *   <topics> is a list of one or more kafka topics to consume from
 *
 * Example:
 *    $ bin/run-example streaming.JavaDirectKafkaWordCount broker1-host:port,broker2-host:port topic1,topic2
 */

public final class KafkaDirectStreamGrouping {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: KafkaDirectStream <brokers> <topics>\n" +
          "  <brokers> is a list of one or more Kafka brokers\n" +
          "  <topics> is a list of one or more kafka topics to consume from\n\n" +
          " KafkaDirectStream localhost:9092 OBDTopics");
      System.exit(1);
    }

    //StreamingExamples.setStreamingLogLevels();

    String brokers = args[0];
    String topics = args[1];

    // Create context with a 2 seconds batch interval
    //SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount");
    JavaSparkContext sparkConf = new JavaSparkContext("local[5]", "JavaDirectKafkaWordCount");
    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));
    
    
    SQLContext sqlContext= new SQLContext(sparkConf);

    HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
    HashMap<String, String> kafkaParams = new HashMap<String, String>();
    kafkaParams.put("metadata.broker.list", brokers);
    kafkaParams.put("zookeeper.connect", "localhost:2181");
    kafkaParams.put("group.id", "spark-app");
    System.out.println("Kafka parameters: " + kafkaParams);

    // Create direct kafka stream with brokers and topics
    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
        jssc,
        String.class,
        String.class,
        StringDecoder.class,
        StringDecoder.class,
        kafkaParams,
        topicsSet
    );

    
    
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



    
    // Get the lines, split them into words
    JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
      @Override
      public String call(Tuple2<String, String> tuple2) {
      	System.out.println("*************MY OUTPUT: processing lines: tuple2._1() = " + 
      			tuple2._1() + "; tuple2._2()=" + tuple2._2());
        return tuple2._2();
      }
    });
    lines.print();
    
	//Creating Data Frame
	DataFrame dFrame= sqlContext.createDataFrame(lines, schema);
    
    JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterable<String> call(String x) {
        return Arrays.asList(SPACE.split(x));
      }
    });
    //words.print();
    
 // Reduce function adding two integers, defined separately for clarity
    Function2<Integer, Integer, Integer> reduceFunc = new Function2<Integer, Integer, Integer>() {
      @Override public Integer call(Integer i1, Integer i2) {
        return i1 + i2;
      }
    };

 // Count each word in each batch
    JavaPairDStream<String, Integer> pairs = words.mapToPair(
      new PairFunction<String, String, Integer>() {
        @Override public Tuple2<String, Integer> call(String s) {
          return new Tuple2<String, Integer>(s, 1);
        }
      });
    /*
    JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
      new Function2<Integer, Integer, Integer>() {
        @Override public Integer call(Integer i1, Integer i2) {
          return i1 + i2;
        }
      });
      
      /*  */
    // Reduce last 30 seconds of data, every 10 seconds
    JavaPairDStream<String, Integer> windowedWordCounts = pairs.reduceByKeyAndWindow(
    		reduceFunc, Durations.seconds(30), Durations.seconds(10));
      
    windowedWordCounts.print();

    // Start the computation
    jssc.start();
    jssc.awaitTermination();
  }
}