package couchbase.sample.to;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import couchbase.sample.util.StringUtil;
import couchbase.sample.vo.Address;
import couchbase.sample.vo.CreditCard;

public class CustomerTO extends BaseTO 
{
	//auto generated information
	private String customerId = StringUtil.getShortUUID();

	//user provided information
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String dob;
	private String phone;
	
	private List<Address> addressList = new ArrayList<Address>();
	private List<CreditCard> cardList = new ArrayList<CreditCard>();

	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getId()
	{
		return customerId;
	}
	public void setId(String id)
	{
		this.customerId = id;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void addAddress(Address address) {
		this.addressList.add(address);
	}

	public void addCreditCard(CreditCard card) {
		this.cardList.add(card);
	}



	//return object into json object
	public  String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);	
	}

	//converts json to Customer object
	public static CustomerTO fromJson(String txnJson) {
		Gson gson = new Gson();
		CustomerTO customer =  gson.fromJson(txnJson, CustomerTO.class);
		return customer;
	}//fromJson


	
}
