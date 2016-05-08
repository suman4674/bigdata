package com.cambitc.neo4j.bolt.util;



/**
 * 
 * @author SumanKu
 *
 */
public class Util {
    public static final String DEFAULT_URL = "bolt://suman4674:Useme123!@localhost";
    public static final String WEBAPP_LOCATION = "src/main/webapp/";

    public static int getWebPort() {
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            return 8080;
        }
        return Integer.parseInt(webPort);
    }

    public static String getNeo4jUrl() {
        String urlVar = System.getenv("NEO4J_URL_VAR");
        if (urlVar==null) urlVar = "NEO4J_URL";
        String url =  System.getenv(urlVar);
        if(url == null || url.isEmpty()) {
            return DEFAULT_URL;
        }
        return url;
    }
    
    public static void main(String [] args){
    	for(int i=0; i<1000000; i+=60)
    	System.out.println(System.currentTimeMillis()+i);
    }
}
