package couchbase.sample.util;

import java.util.Hashtable;

import couchbase.sample.exception.ArgException;

public class ArgumentReader {

	public static Hashtable<String, String>getArguments(String[] args) throws ArgException{
		Hashtable<String, String> argsTable = new Hashtable<String, String>();

		if(args!=null && args.length>0){
			int len = args.length;
			//System.out.println("Number of arguments: " + len);
			
			if(len%2==0){
				//loop to half the times of args length as it is name/value pair
				for(int i=0;i<len/2;i++){
					//add arguments and its value in the argsTable
					argsTable.put(args[2*i], args[2*i+1]);
					//System.out.println(argsTable);
					
				}//EOF if
			}else{
				throw new ArgException("Some argument missing the value. Please check the arguments.");
			}

		}else{
			throw new ArgException("You must provide # of threads & # of docs to write.");
		}
		return argsTable;
	}

}
