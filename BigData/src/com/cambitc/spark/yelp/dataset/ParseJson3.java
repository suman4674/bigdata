package com.cambitc.spark.yelp.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Reads one line at a time from a file that has multiple JSONs (one JSON per line)
 * Take the relevant JSON elements and formats it as a CS string
 * Converted strings are saved as one CSV file
 * 
 * original author: Crunchify.com
 * http://crunchify.com/how-to-read-json-object-from-file-in-java/
 * I wrote my code based on the program posted on the above web site. 
 * 
 */

public class ParseJson3 {
	    
	    public static void main(String[] args) {
	    	
	        JSONParser parser = new JSONParser();
	        
	        // input JSON file
	        String file = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/review_small.json";
	        
	        // output TSV file
	        String output = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/data.tsv";
	        
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        	
	        	String line;
	        	
	        	while ((line = br.readLine()) != null) {
	        		
	        		// Parse one line at a time since one JSON per line
	        		Object obj = parser.parse(line);
	        		JSONObject jsonObject = (JSONObject) obj;
	        		
	        		// Get relevant data out of JSON
	        		String reviewID = (String) jsonObject.get("review_id");
	        		Long rating = (Long) jsonObject.get("stars");
	        		String reviewText = (String) jsonObject.get("text");
	            
	        		// Format the data
	        		String dataToWrite = reviewID + "\t" + rating + "\t" + reviewText.replaceAll("\t" , "") + "\n";
	        		
	        		// Write the formatted data
	        		generateTsvFile(output, dataToWrite); 
	            
	        		// Print to console
	        		System.out.println("reviewID: " + reviewID);
	        		System.out.println("rating: " + rating);
	        		System.out.println("reviewText:" + reviewText + "\n");
	        	}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private static void generateTsvFile(String sFileName, String dataToWrite) {
	    	try {
	    		// This allows for appending rather than overwriting
	    		PrintWriter out = new PrintWriter(new FileWriter(sFileName, true));
	    		out.append(dataToWrite); 
	    		out.close();
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    	} 
	    }
}
