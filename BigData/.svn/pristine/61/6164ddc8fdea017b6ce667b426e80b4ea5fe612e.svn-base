package com.cambitc.neo4j.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Neo4jJDBCConnector {
	public static void main(String []args){
		// Make sure Neo4j Driver is registered
		try {
			Class.forName("org.neo4j.jdbc.Driver");
			Properties props = new Properties();
			props.setProperty("user","neo4j");
			props.setProperty("password","Useme123!");
//			props.setProperty("ssl","true");
			// Connect
			Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/",props);

			// Querying
			try(Statement stmt = con.createStatement())
			{
			    ResultSet rs = stmt.executeQuery("MATCH (n:User) RETURN n.fname");
			    while(rs.next())
			    {
			        System.out.println(rs.getString("n.fname"));
			    }
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	

}
