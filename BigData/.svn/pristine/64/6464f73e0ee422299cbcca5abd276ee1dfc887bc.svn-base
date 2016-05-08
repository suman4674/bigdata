package com.cambitc.neo4j.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Neo4jServiceManager {




		private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
		final static String txUri = SERVER_ROOT_URI + "transaction/commit";

		//http://localhost:7474/db/data/transaction/commit 

		public static void main( String[] args ) throws URISyntaxException
		{
			Neo4jServiceManager pb2= new Neo4jServiceManager();
			checkDatabaseIsRunning();
			String actorIdentifier="";
			String statement="";
			
			actorIdentifier="Keanu";
			statement=statement + " " +"MATCH(Keanu:Actor{name:'Keanu Reeves' })";
			
			
			String movieIdentifier= "John";
			statement=statement + " " +	pb2.createMovie("John Wick" ,movieIdentifier, "2016-03-12");
			
			//Create Actor Keanu Reeves relation
			statement=statement + " " +pb2.createRoles(actorIdentifier, "ACTS_IN",movieIdentifier);
		
			
			
			String directorIdentifier="Chad";
//			statement=statement + " " +pb2.createPerson("Chad Stahelski", directorIdentifier);
			statement=statement + " " +pb2.createDirector("Chad Stahelski", directorIdentifier);
			statement=statement + " " +pb2.createRoles(directorIdentifier, "DIRECTED",movieIdentifier);
			
			directorIdentifier="David";
//			statement=statement + " " +pb2.createPerson("David Leitch",directorIdentifier);
			statement=statement + " " +pb2.createDirector("David Leitch",directorIdentifier);
			statement=statement + " " +pb2.createRoles(directorIdentifier, "DIRECTED",movieIdentifier);

			
			actorIdentifier="William";
//			statement=statement + " " +pb2.createPerson("William Dafoe",actorIdentifier);
			statement=statement + " " +pb2.createActor("William Dafoe",actorIdentifier);
			statement=statement + " " +pb2.createRoles(actorIdentifier, "ACTS_IN",movieIdentifier);
			
			actorIdentifier="Michael";
//			statement=statement + " " +pb2.createPerson("Michael Nyquist",actorIdentifier);
			statement=statement + " " +pb2.createActor("Michael Nyquist",actorIdentifier);
			statement=statement + " " +pb2.createRoles(actorIdentifier, "ACTS_IN",movieIdentifier);
			
		
			
			
			sendTransactionalCypherQuery(statement);

			sendTransactionalCypherQuery( "MATCH (n) WHERE has(n.name) RETURN n.name AS name" );
		}
		
		
		
		/**
		 * 
		 * @param title
		 * @param year
		 */
		private String createRoles(String identifier, String roles, String movieIdentifier){
			String createMovie="CREATE ("+identifier+") -[:"+roles+"]->("+ movieIdentifier+ ") " ;
//			sendTransactionalCypherQuery(createMovie);
			return createMovie;

		}
		
		
		/**
		 * 
		 * @param title
		 * @param year
		 */
		private String createMovie( String title, String identifier,String year){
			String createMovie="CREATE ("+identifier+":Movie { title : '"+ title+"', year : '"+ year+"' }) ";
//			sendTransactionalCypherQuery(createMovie);
			return createMovie;

		}
		
		/**
		 * 
		 * @param name
		 */
		private String createPerson(String name ,String identifier){

			String createDirector="CREATE ("+identifier+":Person { name:'" + name+ "' }) ";
//			sendTransactionalCypherQuery(createDirector);
			return createDirector;

		}
		/**
		 * 
		 * @param name
		 */
		private String createDirector(String name,String identifier){

			String createDirector="CREATE ("+identifier+":Director { name:'" + name+ "' }) ";
//			sendTransactionalCypherQuery(createDirector);
			return createDirector;

		}
		
		/**
		 * 
		 * @param name
		 */
		private String createActor(String name,String identifier){

			String createActor="CREATE ("+identifier+":Actor { name:'" + name+ "' }) ";
//			sendTransactionalCypherQuery(createActor);
			return createActor;

		}
		
		/**
		 * Execute Transactional Cypher query
		 * @param query
		 */
		private static void sendTransactionalCypherQuery(String query) {
			// START SNIPPET: queryAllNodes

			WebResource resource = Client.create().resource( txUri );

			String payload = "{\"statements\" : [ {\"statement\" : \"" +query + "\"} ]}";
			ClientResponse response = resource
					.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( payload )
					.post( ClientResponse.class );

			System.out.println( String.format(
					"POST [%s] to [%s], status code [%d], returned data: "
							+ System.lineSeparator() + "%s",
							payload, txUri, response.getStatus(),
							response.getEntity( String.class ) ) );

			response.close();
		}

		private static URI addRelationship( URI startNode, URI endNode,
				String relationshipType, String jsonAttributes )
						throws URISyntaxException
		{
			URI fromUri = new URI( startNode.toString() + "/relationships" );
			String relationshipJson = generateJsonRelationship( endNode,
					relationshipType, jsonAttributes );

			WebResource resource = Client.create()
					.resource( fromUri );
			// POST JSON to the relationships URI
			ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( relationshipJson )
					.post( ClientResponse.class );

			final URI location = response.getLocation();
			System.out.println( String.format(
					"POST to [%s], status code [%d], location header [%s]",
					fromUri, response.getStatus(), location.toString() ) );

			response.close();
			return location;
		}


		private static String generateJsonRelationship( URI endNode,
				String relationshipType, String... jsonAttributes )
		{
			StringBuilder sb = new StringBuilder();
			sb.append( "{ \"to\" : \"" );
			sb.append( endNode.toString() );
			sb.append( "\", " );

			sb.append( "\"type\" : \"" );
			sb.append( relationshipType );
			if ( jsonAttributes == null || jsonAttributes.length < 1 )
			{
				sb.append( "\"" );
			}
			else
			{
				sb.append( "\", \"data\" : " );
				for ( int i = 0; i < jsonAttributes.length; i++ )
				{
					sb.append( jsonAttributes[i] );
					if ( i < jsonAttributes.length - 1 )
					{ // Miss off the final comma
						sb.append( ", " );
					}
				}
			}

			sb.append( " }" );
			return sb.toString();
		}
		
		/**
		 * Check if Database is running 
		 */
		private static void checkDatabaseIsRunning()
		{

			WebResource resource = Client.create()
					.resource( SERVER_ROOT_URI );
			ClientResponse response = resource.get( ClientResponse.class );

			System.out.println( String.format( "GET on [%s], status code [%d]",
					SERVER_ROOT_URI, response.getStatus() ) );
			response.close();

		}
		private static void findSingersInBands( URI startNode )
				throws URISyntaxException
		{
			// START SNIPPET: traversalDesc
			// TraversalDefinition turns into JSON to send to the Server
			TraversalDefinition t = new TraversalDefinition();
			t.setOrder( TraversalDefinition.DEPTH_FIRST );
			t.setUniqueness( TraversalDefinition.NODE );
			t.setMaxDepth( 10 );
			t.setReturnFilter( TraversalDefinition.ALL );
			t.setRelationships( new Relation( "singer", Relation.OUT ) );
			// END SNIPPET: traversalDesc

			// START SNIPPET: traverse
			URI traverserUri = new URI( startNode.toString() + "/traverse/node" );
			WebResource resource = Client.create()
					.resource( traverserUri );
			String jsonTraverserPayload = t.toJson();
			ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( jsonTraverserPayload )
					.post( ClientResponse.class );

			System.out.println( String.format(
					"POST [%s] to [%s], status code [%d], returned data: "
							+ System.lineSeparator() + "%s",
							jsonTraverserPayload, traverserUri, response.getStatus(),
							response.getEntity( String.class ) ) );
			response.close();

		}


		private static void addMetadataToProperty( URI relationshipUri,
				String name, String value ) throws URISyntaxException
		{
			URI propertyUri = new URI( relationshipUri.toString() + "/properties" );
			String entity = toJsonNameValuePairCollection( name, value );
			WebResource resource = Client.create()
					.resource( propertyUri );
			ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( entity )
					.put( ClientResponse.class );

			System.out.println( String.format(
					"PUT [%s] to [%s], status code [%d]", entity, propertyUri,
					response.getStatus() ) );
			response.close();
		}



		private static String toJsonNameValuePairCollection( String name,
				String value )
		{
			return String.format( "{ \"%s\" : \"%s\" }", name, value );
		}

		private static URI createNode()
		{
			// START SNIPPET: createNode
			// final String txUri = SERVER_ROOT_URI + "transaction/commit";
			final String nodeEntryPointUri = SERVER_ROOT_URI + "node";
			// http://localhost:7474/db/data/node

			WebResource resource = Client.create()
					.resource( nodeEntryPointUri );
			// POST {} to the node entry point URI
			ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( "{}" )
					.post( ClientResponse.class );

			final URI location = response.getLocation();
			System.out.println( String.format(
					"POST to [%s], status code [%d], location header [%s]",
					nodeEntryPointUri, response.getStatus(), location.toString()));
			response.close();

			return location;
			
		}

		
		



		private static void addProperty( URI nodeUri, String propertyName,
				String propertyValue )
		{

			// http://localhost:7474/db/data/node/{node_id}/properties/{property_name}
			String propertyUri = nodeUri.toString() + "/properties/" + propertyName;


			WebResource resource = Client.create()
					.resource( propertyUri );
			ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
					.type( MediaType.APPLICATION_JSON )
					.entity( "\"" + propertyValue + "\"" )
					.put( ClientResponse.class );

			System.out.println( String.format( "PUT to [%s], status code [%d]",
					propertyUri, response.getStatus() ) );
			response.close();

		}

		
	}


