package com.cambitc.util.date;

import java.util.Date;
/**
 * 
 * @author skumar
 *
 */
public class Utils {

	public static void main(String []args){
		
		 try {
			 
			 while(1==1){
				 long ts=new Date().getTime();
				 String d= new Date(ts).toString();
				 
			 System.out.println(ts + " " + d );
			Thread.sleep(6000);
			 }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
