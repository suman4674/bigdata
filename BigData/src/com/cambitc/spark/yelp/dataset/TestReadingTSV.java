package com.cambitc.spark.yelp.dataset;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Reads a TSV file and splits by a tab
 * This program is for checking how the file is read
 * 
 */

public class TestReadingTSV {

	public static void main(String[] args) {
		
		// input TSV file
		String file = "/Users/yejineun/Documents/CS63_Spring2016/FinalProject/data.tsv";
    
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    	
			String line;
    	
			while ((line = br.readLine()) != null) {
    		
				String [] l = line.split("\t");
				int counter = 1;
    		
				for (int i = 0; i < l.length; i++) {
    			
					// to know how many "elements" are contained in one line
					System.out.println("******* " + counter + " ********");
					// print out the element
					System.out.println("" + l[i]);
					// print out the length of the element
					System.out.println("" + l[i].length());
					counter++;
				}
			}
    	
		} catch  (Exception e) {
			e.printStackTrace();
		}
    }
}

