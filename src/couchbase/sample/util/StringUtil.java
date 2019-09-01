package couchbase.sample.util;

import java.util.UUID;

public class StringUtil {
	
	public static String getUUID(){
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
	
	public static String getShortUUID(){
		String uuid = getUUID();
		uuid=uuid.replaceAll("-", "");
		return uuid;
		
	}//getShortUUID
	
	public static boolean isEmpty(String str){
		boolean flag = true;
		
		if(str!=null && str.trim().length()>0)
			flag=false;
		
		return flag;
	}//isEmpty
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
}
