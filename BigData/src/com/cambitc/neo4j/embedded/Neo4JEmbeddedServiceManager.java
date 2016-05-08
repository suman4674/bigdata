package com.cambitc.neo4j.embedded;



import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

public class Neo4JEmbeddedServiceManager {
	
	private static enum RelTypes implements RelationshipType
	{
		KNOWS
	}
	public static void main(String []args){
		
		
		File file=new File("data/graph.db");
		
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
	    .newEmbeddedDatabaseBuilder( file )
	    .setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
	    .setConfig( GraphDatabaseSettings.string_block_size, "60" )
	    .setConfig( GraphDatabaseSettings.array_block_size, "300" )
	    .newGraphDatabase();
		
		Node firstNode=null;
		Node secondNode=null;
		Relationship relationship=null;
		try ( Transaction tx = graphDb.beginTx() )
		{
		    // Database operations go here
			firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" );

			relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
			relationship.setProperty( "message", "brave Neo4j " );
		    tx.success();
		    
		    
		    System.out.print( firstNode.getProperty( "message" ) );
		    System.out.print( relationship.getProperty( "message" ) );
		    System.out.print( secondNode.getProperty( "message" ) );
		    
		 // let's remove the data
//		    firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
//		    firstNode.delete();
//		    secondNode.delete();
		    
		    graphDb.shutdown();
		}
	}

}
