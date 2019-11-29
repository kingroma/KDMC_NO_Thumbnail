package main;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoConnection {
	private String ip = "192.168.10.180";
	private int port = 27017;
	private MongoClient mongoClient = null; 
	
	private DB db = null ; 
	
	public MongoConnection(){
		try {
			mongoClient = new MongoClient(ip,port);
			db = mongoClient.getDB("windmill");
			System.out.println("start connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MongoConnection(String ip , String port) {
		try {
			this.ip = ip ; 
			this.port = Integer.parseInt(port) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		try {
			mongoClient = new MongoClient(this.ip,this.port);
			db = mongoClient.getDB("windmill");
			System.out.println("start connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DBCollection getCollection(String name) {
		return db.getCollection(name);
	}
	
	public void close() {
		if ( mongoClient != null )
			mongoClient.close();
	}
}
