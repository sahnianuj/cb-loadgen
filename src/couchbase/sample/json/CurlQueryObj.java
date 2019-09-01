package couchbase.sample.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import couchbase.sample.vo.Cred;

public class CurlQueryObj {

	public String statement = null;
	public List<Cred> creds = new ArrayList<Cred>();

	public CurlQueryObj(String statement, String user, String password) {

		Cred cred = new Cred();
		cred.setPass(password);
		cred.setUser(user);
		
		//add cred element to creds object
		creds.add(cred);
		
		this.setStatement(statement);
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	

	//return object into json object
	public  String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);	
	}

	//converts json to Customer object
	public static CurlQueryObj fromJson(String txnJson) {
		Gson gson = new Gson();
		CurlQueryObj curlQueryTO =  gson.fromJson(txnJson, CurlQueryObj.class);
		return curlQueryTO;
	}//fromJson

}
