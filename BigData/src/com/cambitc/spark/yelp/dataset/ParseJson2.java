package com.cambitc.spark.yelp.dataset;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Reads one line at a time from a file that has multiple JSONs (one JSON per line)
 * 
 * original author: Crunchify.com
 * http://crunchify.com/how-to-read-json-object-from-file-in-java/
 */

public class ParseJson2 {
	    
	    public static void main(String[] args) {
	        JSONParser parser = new JSONParser();
	        String file = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/review_small.json";
	        
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        	
	        	String line;
	        	while ((line = br.readLine()) != null) {
	            Object obj = parser.parse(line);
	 
	            JSONObject jsonObject = (JSONObject) obj;
	 
	            String reviewID = (String) jsonObject.get("review_id");
	            Long rating = (Long) jsonObject.get("stars");
	            String reviewText = (String) jsonObject.get("text");
	 
	            System.out.println("reviewID: " + reviewID);
	            System.out.println("rating: " + rating);
	            System.out.println("reviewText:" + reviewText + "\n");
	        	}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
