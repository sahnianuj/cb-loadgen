package couchbase.sample.vo;

import com.google.gson.Gson;

public class CreditCard {

	private String vendor;
	private String expiryDate;
	private long number;

	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}

	//return object into json object
	public  String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);	
	}
}
