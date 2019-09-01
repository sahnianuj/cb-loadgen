package couchbase.sample.to;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TransactionTO {

	//auto generated information
	private String id = Long.toString(new Date().getTime());
	private String type = "txn";

	//user provided information
	private String vendor;
	private int amount; 
	private long cardNumber;
	private String txnTimeAsString;
	private long txnTime;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getTxnTimeAsString() {
		return txnTimeAsString;
	}
	public void setTxnTimeAsString(String txnTimeAsString) {
		this.txnTimeAsString = txnTimeAsString;
	}
	public long getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(long txnTime) {
		this.txnTime = txnTime;
	}

	//return object into json object
	public  String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);	
	}//toJson
	
	//converts json to Transaction object
	public static TransactionTO fromJson(String txnJson) {
		Gson gson = new Gson();
		TransactionTO txn =  gson.fromJson(txnJson, TransactionTO.class);
		return txn;
	}//fromJson
}
