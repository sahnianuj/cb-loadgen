package couchbase.sample.dao;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

import couchbase.sample.exception.ArgException;
import couchbase.sample.to.TransactionTO;
import couchbase.sample.util.DateFormatter;

public class TransactionDAO extends BaseDAO{

	public TransactionDAO() throws ArgException {
		super();
	}
	public TransactionDAO(String ip, String user, String passwd, String bucket) throws ArgException {
		super(ip, user, passwd, bucket);
		// TODO Auto-generated constructor stub
	}

	//CREATE INDEX `txn_type_txnTime_index` ON txn_sample(type,txnTime) USING GSI;
	//SELECT * FROM txn_sample WHERE type="txn" and txnTime>=1531595139204 and txnTime<=1531595139205;

	/**
	 * This method returns list of transactions that are executed from a specific time to a specific
	 * time. All the time values are converted into milliseconds so user need to convert the date/time
	 * into the millisecond first and then pass those as the arguments.
	 * @param fromTime - Time from which transactions need to be fetched. If 0 is passed then all the 
	 * transactions would be returned, so make sure one never set 0 value to this argument.
	 * @param toTime - Time up till which transactions need to be fetched. If 0 is passed then query
	 *  will be unbounded.
	 * @return
	 */
	protected List<TransactionTO> getTransactions(long fromTime, long toTime){

		List<TransactionTO> txnList = new ArrayList<TransactionTO>();

		String n1qlQuery = "SELECT * FROM txn_sample WHERE type=\"txn\"";

		//first check if fromTime is greater than 0 and toTime is greater than fromTime
		if(fromTime>0 && toTime>=fromTime)
			n1qlQuery = n1qlQuery + " AND txnTime>=" + fromTime + " AND txnTime<=" + toTime;
		//else check if fromTime is greater than 0
		else if(fromTime>0 && toTime==0)
			n1qlQuery = n1qlQuery + " AND txnTime>=" + fromTime;

		System.out.println("Query to be executed:> \n\t" + n1qlQuery + "\n");


		N1qlQueryResult queryResult = super.executeQuery(n1qlQuery);
		//iterate through the query results
		for (N1qlQueryRow result: queryResult) {
			String txnJson = result.value().get("txn_sample").toString();
			//System.out.println(txnJson);
			//convert json to Transaction object
			TransactionTO txn = TransactionTO.fromJson(txnJson);
			System.out.println(txn.toJson());
			txnList.add(txn);
		}
		return txnList;
	} //getTransaction()

	/**
	 * Overloaded method which takes formatted date string and first convert those strings into 
	 * milliseconds and then call getTransaction(long,long) method. The date formats that are acceptable
	 * are MM/dd/yy or MM/dd/yy HH:mm:ss
	 * @param fromTime - Formatted date from which transaction need to be searched from
	 * @param toTime - Formatted date upto which transactions need to be searched to
	 * @return
	 */
	public List<TransactionTO> getTransactions(String fromTime, String toTime){
		long from = DateFormatter.getEpocTime(fromTime);
		long to = DateFormatter.getEpocTime(toTime);
		return getTransactions(from,to);
	}//getTransactions


	public static void main(String[] args) {
		try{
			List<TransactionTO> txnList = new TransactionDAO().
					getTransactions("07/13/18 16:34:33", "07/14/18 16:34:34");
			System.out.println("Size of the Transaction list is: " + txnList.size());
			//new TransactionDAO().getTransactions("07/13/18 ", "07/14/18 17:10:10");
		}catch(ArgException ae){
			ae.printStackTrace();
		}

	}//main

}
