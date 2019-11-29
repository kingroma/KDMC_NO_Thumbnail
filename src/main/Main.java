package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Main {
	public static DBCollection ppCollection = null ;
	public static DBCollection pCollection = null ;
	public static DBCollection iCollection = null ;
	public static List<String> list = new ArrayList<String>() ;
	public static StringBuilder sb = new StringBuilder();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MongoConnection connection = new MongoConnection();
		ppCollection = connection.getCollection("program.products");
		pCollection = connection.getCollection("programs");
		iCollection = connection.getCollection("image.meta");
		
		DBCursor ppCur = ppCollection.find(Query.getProgramProducts());
		int ppCurFindCnt = ppCur.count();
		int noThumb = 0 ; 
		
		String errCid = "";
		int cnt = 0 ;
		
		System.out.println("총 확인해야할 건수 : " + ppCurFindCnt);
		
		while ( ppCur.hasNext() ) {
			try {
				DBObject pp = ppCur.next();
				String cid = pp.get("cid") != null ? pp.get("cid").toString() : null ;
				errCid = cid ; 
				if (cid != null && !hasThumbnail (cid) ) {
					DBCursor pCur = pCollection.find(Query.getPrograms(cid));
					
					if ( pCur.hasNext() ) {
						DBObject p = pCur.next();
						
						if ( p.get("config") != null ) {
							DBObject config = (DBObject)p.get("config");
							if ( config.get("parameter") != null ) {
								DBObject parameter = (DBObject)config.get("parameter");
								List<Map<String,Object>> params = (List<Map<String,Object >>)parameter;
								
								if ( params != null ) {
									for ( Map<String,Object> m : params) {
										if ( m != null && !m.isEmpty()) {
											if ( m.get("name") != null && "Run_Time".equals(m.get("name").toString())) {
												String value = m.get("value").toString();
												if ( isPass(value) ) {
													list.add(p.get("pid")!=null ? p.get("pid").toString() : "");
													sb.append(p.get("pid")!=null ? p.get("pid").toString() : "");
													sb.append("\n");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			
			} catch (Exception e) {
				System.out.println("ERRROR CID : " + errCid);
			}
			
			cnt ++ ;
			if ( cnt % 1000 == 0 ) {
				System.out.println( ppCurFindCnt + " / " + cnt );
			}
			
			try {
				Thread.sleep(1);	
			} catch (Exception e) {

			}
			
		}
		
		
		System.out.println("finish");
		for (String l : list ) {
			// System.out.println(l);
		}
		
		write(sb.toString(),"output.txt");
		
		connection.close();
	}

	
	
	public static void write(String str , String filePath) {
		File file = new File(filePath);
        OutputStream os = null ;
        OutputStreamWriter osw = null ; 
        try {
        	os = new FileOutputStream(file);
        	osw = new OutputStreamWriter(os, "UTF8");
            osw.write(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	osw.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public static boolean isPass(String time) {
		
		boolean ret = false ;
		try {
			int value =  ( 2 * 60 + 30 ) ; 
			if ( time != null && !time.isEmpty() && time.split(":").length == 3 ) {
				String[] split = time.split(":");
				int hour = Integer.parseInt(split[0]) * 3600 ; 
				int min = Integer.parseInt(split[1]) * 60 ; 
				int sec = Integer.parseInt(split[2]) ;
		        if ( ( hour + min + sec ) > value )
		            ret = true ;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR"+time);
		}
		return ret ; 
	}
	
	public static boolean hasThumbnail(String cid) {
		boolean ret = false ; 
		int cnt = iCollection.find(Query.getThumb(cid)).count();
	    if( cnt > 0 ){
	        ret = true ;
	    }
	    return ret ;
	}
}
