package com.cambitc.streaming.kafka;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
/**
 * 
 * @author SumanKu
 *
 */
public class KafkaProducer {
	private String fileName;
	private String colHeader;
	Producer<String, String> producer=null;
	public static void main(String[] args) {
		KafkaProducer myProducer= new KafkaProducer();
		Random rnd = new Random();

		Properties props = new Properties();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("partitioner.class", "com.cambitc.streaming.kafka.KafkaPartitioner");
		props.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(props);

		myProducer.producer = new Producer<String, String>(config);

		try{
			while(true) { 
				long runtime = new Date().getTime();  
				String ipAdress="localhost";
				String msg =""+rnd.nextInt(10);
				KeyedMessage<String, String> data = new KeyedMessage<String, String>("OBDTopics",ipAdress , msg);
//				myProducer.producer.send(data);
				myProducer.initFileConfig("data/insured.csv");
				myProducer.sendFileDataToKafka("OBDTopics");
				Thread.sleep(1000);
			}
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			myProducer.producer.close();
		}
	}
	
	/**
	 * Initialize configuration for file to be read (csv)
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void initFileConfig(String fileName) throws IOException, Exception {
		this.fileName = fileName;

		try {
			InputStream inStream = new FileInputStream(fileName); //this.getClass().getClassLoader().getResourceAsStream(this.fileName);
			Reader reader = new InputStreamReader(inStream);
			BufferedReader buffReader = IOUtils.toBufferedReader(reader);

			// Get the header line to initialize CSV parser
			colHeader = buffReader.readLine();
			//System.out.println("File header :: " + colHeader);

			if (StringUtils.isEmpty(colHeader)) {
				throw new Exception("Column header is null, something is wrong");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	/**
	 * Send csv file data to the named topic on Kafka broker
	 * 
	 * @param topic
	 * @throws IOException
	 */
	public void sendFileDataToKafka(String topic) throws IOException {

		Iterable<CSVRecord> csvRecords = null;

		// Parse the CSV file, using the column header
		try {
			InputStream inStream = new FileInputStream(fileName);
			Reader reader = new InputStreamReader(inStream);

			String[] colNames = StringUtils.split(colHeader, ',');
			csvRecords = CSVFormat.DEFAULT.withHeader(colNames).parse(reader);
		} catch (IOException e) {
			System.out.println(e);
			throw e;
		}

		// We iterate over the records and send each over to Kafka broker
		// Get the next record from input file
		CSVRecord csvRecord = null;
		Iterator<CSVRecord> csvRecordItr = csvRecords.iterator();
		boolean firstRecDone = false;
		while (csvRecordItr.hasNext()) {
			try {
				csvRecord = csvRecordItr.next();
				if (!firstRecDone) {
					firstRecDone = true;
					continue;
				}
				// Get a map of column name and value for a record
				Map<String, String> keyValueRecord = csvRecord.toMap();

				// Create the message to be sent
				String message = "";
				int size = keyValueRecord.size();
				int count = 0;
				for (String key : keyValueRecord.keySet()) {
					count++;
					message = message
							+ StringUtils.replace(key, "\"", "")
							+ "="
							+ StringUtils.replace(keyValueRecord.get(key),
									"\"", "");
					if (count != size) {
						message = message + ",";
					}
				}

				// Send the message
				//System.out.println(message);
				KeyedMessage<String, String> data = new KeyedMessage<String, String>(
						topic, message);
				producer.send(data);

			} catch (NoSuchElementException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Cleanup stuff
	 */
	public void cleanup() {
		producer.close();
	}
}