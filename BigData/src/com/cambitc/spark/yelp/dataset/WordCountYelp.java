package com.cambitc.spark.yelp.dataset;

import java.io.IOException;
import java.lang.InterruptedException;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
	Program Goal:
		Eliminate special characters, numbers, and stop words. 
	Program Summary:
		1. To eliminate special characters, I used StringTokenizer. This allows removal of
		special characters that stand alone, as well as the special chars that surround 
		normal words (e.g. #that% -> that). I then examined the first character of each token, and
		asked whether it is a letter, using the Character.isLetter() method. The consequence
		of this action is that it also removes numbers, as well as special chars. 
		2. Once the token is perceived as a real word, it is then asked to see if it matches
		any of the provided stop words. I stored the stop words in an ArrayList, in order
		to utilize its contains() method. 
		3. Only when the token passes both of these tests, it is written as a key. 
 */

public class WordCountYelp extends Configured implements Tool {

  	public static class MapClass extends Mapper<Object, Text, Text, IntWritable> {
    
    	private final static IntWritable one = new IntWritable(1);
    	private Text word = new Text();
    
    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      
      		// break each line by using the delimiter characters shown
      		StringTokenizer tokenizer = new StringTokenizer(value.toString(), " \t\n\r\f,.:;?![]()/'-_\"%&#$");
      		
      		while (tokenizer.hasMoreTokens()) {
        		
        		// save each token as a String
        		String s = tokenizer.nextToken();
        		
        		// look at the first character of s to see if it's actually a word, rather than a special character
        		// or number. I will only accept alphabetical characters.
        		char c = s.charAt(0);
        	
        		if (Character.isLetter(c)) {
        				
        			// store the stop words in an ArrayList.			
					String stopwords = "A and am could in is it of on or that the they those these this to was what when where I a about an are as at be by com for from how who will with the you"; 
					String delim = "[ ]";
					String[] stopwordsArray = stopwords.split(delim);
        			ArrayList<String> stopwordArrayList = new ArrayList<String>();
					stopwordArrayList.addAll(Arrays.asList(stopwordsArray));
					
					// Screen for stop words
					boolean isStopword = stopwordArrayList.contains(s);
                	if (isStopword == false) { // not a stop word
        				word.set(s.toLowerCase());
        				context.write(word, one);
        			} 
      			}
      		}
    	}
  	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    	private IntWritable occurrencesOfWord = new IntWritable();

    	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      
         	int sum = 0;
   		   	for (IntWritable val : values) {
        		sum += val.get();
      		}
      		occurrencesOfWord.set(sum);
      		context.write(key, occurrencesOfWord);
      	}
  	}

  	public int run(String[] args) throws Exception {
			
		Configuration conf = getConf();
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);
			
		Job job = Job.getInstance(conf, "WordCountYelp");
		job.setJarByClass(WordCountYelp.class);
			
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setJobName("WordCountYelp");
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);
		job.setMapOutputKeyClass(Text.class); 
		job.setMapOutputValueClass(IntWritable.class); 
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		System.exit(job.waitForCompletion(true)?0:1);
		return 0;
	}
		
	public static void main(String[] args) throws Exception {
			
		int res = ToolRunner.run(new Configuration(), new WordCountYelp(), args);
		System.exit(res);
	}
}
