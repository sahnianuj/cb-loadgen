package couchbase.sample.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import couchbase.sample.vo.Server;

public class ClusterMapObj {
	List<Server> results = new ArrayList<Server>();

	public List<Server> getResults() {
		return results;
	}

	public void setResults(List<Server> results) {
		this.results = results;
	}
	

	//return object into json object
	public  String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);	
	}

	//converts json to ClusterMapResultJson object
	public static ClusterMapObj fromJson(String txnJson) {
		Gson gson = new Gson();
		ClusterMapObj resultJson =  gson.fromJson(txnJson, ClusterMapObj.class);
		return resultJson;
	}//fromJson
}
