package main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class Query {
	public static BasicDBObject getRentalDurationQuery() {
		BasicDBObject query = new BasicDBObject("basic.rental_period", 2147483647);
		
		return query;
	}
	
	// 'cclass':'master',
    // 'license_end':{$gt:new Date()},
    // "content_format" : { $ne:'sd'}
	public static BasicDBObject getProgramProducts() {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> gt = new HashMap<String,Object>();
		Map<String,Object> ne = new HashMap<String,Object>();
		gt.put("$gt", new Date());
		ne.put("$ne", "sd");
		
		map.put("cclass", "master");
		map.put("license_end", gt);
		map.put("content_format", ne);
		
		BasicDBObject query = new BasicDBObject(map);
		return query;
	}
	
	public static BasicDBObject getThumb(String cid) {
		Map<String,Object> map = new HashMap<String,Object>();

		map.put("filename", cid);
		map.put("pclass", "thumbnail");
		
		BasicDBObject query = new BasicDBObject(map);
		return query;
	}
	
	public static BasicDBObject getPrograms(String cid) {
		Map<String,Object> map = new HashMap<String,Object>();

		map.put("_id", new ObjectId(cid));
		
		BasicDBObject query = new BasicDBObject(map);
		return query;
	}
}
