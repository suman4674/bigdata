package com.cambitc.spark.yelp.dataset;
import java.io.*;
import java.util.*;

/**
	Program Goal:
		Selectively save the words that match the pre-defined lists of positive and 
		negative words for downstream visualization 
	Program Summary:
		1. MapReduce raw results are stored as a HashMap
		2. Lists of positive and negative words are stored as ArrayLists.
		3. Words that are found in both are taken and saved in a new txt file. 
 */
 
public class WordFiltering {

	public static void main (String[] args) 
		throws FileNotFoundException {
		
		// Read the MapReduce results in
		Scanner in = new Scanner( new File( "part-r-00000-big.txt"));
		
		// Store the results as a HashMap
		// key = word
		// value = count
		Map<String, Integer> wordCountMap = getCountMap(in);
		
		//System.out.println("The total size: " + wordCountMap.size());
		//System.out.println("good: " + wordCountMap.get("good"));
		
		// Read in the lists of positive and negative words
		Scanner listPost = new Scanner( new File ("positive-words.txt"));
		Scanner listNeg = new Scanner( new File ("negative-words.txt"));
		
		// Build a list of the pre-defined words
		ArrayList<String> postwords = buildList(listPost);
		ArrayList<String> negwords = buildList(listNeg);
		
		//System.out.println("first positive: " + postwords.get(0));
		//System.out.println("first negative: " + negwords.get(0));
		
		// Select only the words that are in these lists
		// Write them out in a csv file
		PrintStream output1 = new PrintStream(new File("filteredWordCountPostBig.csv"));
		filterAndWrite(output1, wordCountMap, postwords);
		PrintStream output2 = new PrintStream(new File("filteredWordCountNegBig.csv"));
		filterAndWrite(output2, wordCountMap, negwords);
		
	}
	
	public static void filterAndWrite(PrintStream output, Map<String, Integer> wordCountMap, ArrayList<String> wordlist) {
	
		// write the heading for the csv
		output.println("words,counts");
		
		for (int i = 0; i < wordlist.size(); i++) {
		
			// get each positive word from the list
			String key = wordlist.get(i);
			
			// see if this exists in the map
			if (wordCountMap.containsKey(key)) {
				int value = wordCountMap.get(key);
				output.println("" + key + "," + value);
			}
		}
	}
	
	public static ArrayList<String> buildList(Scanner in) {
		
		ArrayList<String> wordlist = new ArrayList<String>();
		
		while (in.hasNext()) {
			String w = in.next();
			wordlist.add(w.toLowerCase()); 
		}
		
		return wordlist;
	}
	
	public static Map<String, Integer> getCountMap(Scanner in) {
		
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		
		while (in.hasNext()) {
			// read in one line at a time
			String l = in.nextLine();
			// break the line by tab
			String delim = "\t";
			String[] twoinputs = l.split(delim);
			// the first one is a word
			// the second one is its count
			wordCountMap.put(twoinputs[0], Integer.parseInt(twoinputs[1]));
		}
		
		return wordCountMap;
	}
}