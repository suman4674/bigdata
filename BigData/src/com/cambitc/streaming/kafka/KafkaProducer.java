package com.cambitc.streaming.kafka;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


/**
 * 
 * @author SumanKu
 *
 */
public class KafkaProducer {
	public static void main(String[] args) {

		Random rnd = new Random();

		Properties props = new Properties();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("partitioner.class", "com.cambitc.streaming.kafka.KafkaPartitioner");
		props.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);

		try{
			while(true) { 
				long runtime = new Date().getTime();  
				String ipAdress="localhost";
				String msg =""+rnd.nextInt(10);
				KeyedMessage<String, String> data = new KeyedMessage<String, String>("OBDTopics",ipAdress , msg);
				producer.send(data);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			producer.close();
		}
	}
}