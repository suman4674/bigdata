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
 * Take only the review text from Yelp data
 * Save the entire review text as a txt file for MapReduce
 * 
 * reference code from:
 * http://crunchify.com/how-to-read-json-object-from-file-in-java/
 * I wrote my code based on the program posted on the above web site. 
 * 
 */

public class ParseJson4 {
	    
	    public static void main(String[] args) {
	    	
	        JSONParser parser = new JSONParser();
	        
	        // input JSON file
	        String file = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/review_small.json";
	        //String file = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/yelp_academic_dataset_review.json";
	        
	        // output txt file
	        //String output = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/reviewtextBig.txt";
	        String output = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/demo.txt";
		       
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        	
	        	String line;
	        	
	        	while ((line = br.readLine()) != null) {
	        		
	        		// Parse one line at a time since one JSON per line
	        		Object obj = parser.parse(line);
	        		JSONObject jsonObject = (JSONObject) obj;
	        		
	        		// Get relevant data out of JSON
	        		String reviewText = (String) jsonObject.get("text");
	        		
	        		// Add a new line for texts from different reviews
	        		reviewText = reviewText + "\n";
	        		
	        		// Write the formatted data
	        		generateOutputFile(output, reviewText); 
	            
	        	}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private static void generateOutputFile(String sFileName, String dataToWrite) {
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
