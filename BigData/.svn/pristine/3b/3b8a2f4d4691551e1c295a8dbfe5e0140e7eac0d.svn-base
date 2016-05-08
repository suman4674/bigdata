package com.cambitc.neo4j.bolt.backend;



import spark.Spark;

import com.cambitc.neo4j.bolt.util.Util;

/**
 * @author Michael Hunger @since 22.10.13
 */
public class MovieServer {

    public static void main(String[] args) {
    	Spark.setPort(Util.getWebPort());
        Spark.externalStaticFileLocation("src/main/webapp");
        final MovieService service = new MovieService(Util.getNeo4jUrl());
        new MovieRoutes(service).init();
    }
}
