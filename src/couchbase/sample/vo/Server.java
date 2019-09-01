package couchbase.sample.vo;

import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private String hostname = null;
	private List<String> services = new ArrayList<String>();
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	
	

}
