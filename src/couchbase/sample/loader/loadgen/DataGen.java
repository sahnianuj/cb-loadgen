package couchbase.sample.loader.loadgen;

import java.util.Date;
import java.util.Random;
import couchbase.sample.to.CustomerTO;
import couchbase.sample.to.TransactionTO;
import couchbase.sample.util.DateFormatter;
import couchbase.sample.vo.Address;
import couchbase.sample.vo.CreditCard;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.person.PersonProperties;

public class DataGen {

	public static String CUSTOMER_SCHEMA = "1.0";
	public static String CUSTOMER_TYPE = "cust";
	public static int CUSTOMER_VERSION = 1;

	/**
	 * This method returns transaction details when credit card number is passed
	 * @param cardNumber Credit card number that need to be associated to this transaction
	 * @return Transaction object
	 */
	public  TransactionTO getTransactionData(long cardNumber) {

		TransactionTO txn = new TransactionTO();
		Random rand = new Random();
		String formatedDate = DateFormatter.getFormatedDate(new Date());

		//create a random vendor detail
		Fairy fairy = Fairy.create();
		Person vendor = fairy.person();

		//set transaction details
		txn.setVendor(vendor.getFullName());
		txn.setAmount(rand.nextInt(100) + 1);
		txn.setCardNumber(cardNumber);
		txn.setTxnTime(DateFormatter.getEpocTime(formatedDate));
		txn.setTxnTimeAsString(formatedDate);

		return txn;
	}//getTransactionData

	/**
	 * This method is used to populate some meaning information into CustomerTO. This method
	 * is CPU intensive and should not be called often while running the performance test.  
	 * @return CustomerTO
	 */
	public CustomerTO getCustomerData() {

		CustomerTO customerTO = new CustomerTO();
		Address address = null;
		CreditCard creditCard = null;
		
		io.codearte.jfairy.producer.person.Address personAddress = null;
		io.codearte.jfairy.producer.payment.CreditCard cardGen = null;

		//set metadata attributes first
		customerTO.setSchema(CUSTOMER_SCHEMA);
		customerTO.setVersion(CUSTOMER_VERSION);
		customerTO.setType(CUSTOMER_TYPE);

		//create a random adult person object
		Fairy fairy = Fairy.create();

		Person person = fairy.person(PersonProperties.ageBetween(20, 40));
		//generate credit-card details
		cardGen = fairy.creditCard();

		//assign person details into customer object
		customerTO.setFirstName(person.getFirstName());
		customerTO.setLastName(person.getLastName());
		customerTO.setEmail(person.getEmail());
		customerTO.setGender(person.getSex().toString());
		customerTO.setDob(person.getDateOfBirth().toString());

		//create customer address
		address = new Address();
		//create random address for the person
		personAddress = person.getAddress();
		//assign details to address POJO
		address.setStreet(personAddress.getStreet());
		address.setStreetNumber(personAddress.getStreetNumber());
		address.setCity(personAddress.getCity());
		address.setZip(personAddress.getPostalCode());

		//set address to customer object
		customerTO.addAddress(address);

		//Now create a credit-card detail
		creditCard = new CreditCard();
		creditCard.setVendor(cardGen.getVendor());
		creditCard.setNumber(new Date().getTime());
		creditCard.setExpiryDate(cardGen.getExpiryDateAsString());

		//add creditcard number to customer
		customerTO.addCreditCard(creditCard);


		/*			





		try {
			System.out.println("Printing 10 transactions for each card number");
			//wait for key press
			System.in.read();

			//generate 10 txn per card number
			long cardNumber = 0;
			int counter=0;
			TransactionTO transaction = null;

			for(int i=0;i<numberOfObjects;i++) {
				cardNumber = cardNumbers[i];

				//now print 10 txn each
				for(int j=0;j<10;j++) {
					transaction = DataGen.getTransactionData(cardNumber);
					jsonString = transaction.toJson();
					System.out.println("Transaction: " + ++counter + "\n" +  jsonString);

					//upsert transaction information into the txn_sample bucket
					txnDAO.upsert(transaction.getId(), jsonString);
				}//eof for(j)
			}//eof for(i)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		return customerTO;
	}//EOF getCustomerData()
}
