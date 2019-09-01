package couchbase.sample.loader.loadgen;

import couchbase.sample.dao.CustomerDAO;
import couchbase.sample.exception.ArgException;
import couchbase.sample.to.CustomerTO;
import couchbase.sample.util.StringUtil;

public class WorkerNode implements Runnable{

	int docsPerThread = 0;
	CustomerTO customerTO = null;
	CustomerDAO customerDAO = null;
	
	public WorkerNode(int docs, String host, String user, String password, String bucket, boolean sslEnabled, 
			String ksPath, String ksPassword) 
					throws ArgException
	{
		this.docsPerThread=docs;
		customerDAO = new CustomerDAO( host, user, password, bucket, sslEnabled, ksPath, ksPassword);
	}
	public void run() {

		// Create Customer json once so its cost is not added to performance 
		customerTO = new DataGen().getCustomerData();
		

		// We are creating a simple loop which will run and allow us to take
		// a look into how the different threads run.
		for (int i = 0; i < docsPerThread; i++) {

			//create new customerId each time with rest of the content same
			customerTO.setId(StringUtil.getUUID());
			
			//Note: Marshling/Unmarshiling is a time consuming task
			String jsonString = customerTO.toJson();
			//System.out.println( jsonString);
			
			//write customer json into the sample-bucket
			try {
				//upsert customer information into the txn_sample bucket
				//System.out.println("\tGoing to upsert with ttl set to 10 seconds");
				customerDAO.upsert(customerTO.getId(), jsonString, 10*60);
				//System.out.println("\t\tDone with upsert");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// display customer information
			//System.out.println("\n" + Thread.currentThread().getName() + "\t inserting customer ..." + i +"\n");
			  

		}//EOF for()
	}//EOF run()


}
