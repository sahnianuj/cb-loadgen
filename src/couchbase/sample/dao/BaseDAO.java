package couchbase.sample.dao;


//import java.util.Arrays;
//import java.util.List;

import com.couchbase.client.core.env.KeyValueServiceConfig;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.transcoder.JsonTranscoder;

import couchbase.sample.exception.ArgException;
import couchbase.sample.util.StringUtil;

import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;

public class BaseDAO {

	protected static Cluster cluster = null;
	protected static Bucket sampleBucket = null;

	private static CouchbaseEnvironment env = null;

	public BaseDAO() throws ArgException{

		this("10.112.185.101", "Administrator", "password", "default");
		

	}//BaseDAO()

	/**
	 * This constructor uses single node IP instead to make it easy to connect to server in development environment
	 * @param nodes - One of the node IP/FQDNs in couchbase cluster (This should not be used in production)
	 * @param user - RBAC used with Application Access
	 * @param passwd - password of the user used to connect to the bucket
	 * @param bucket -  where data will be written to
	 * @throws ArgException
	 */
	public BaseDAO(String node, String user, String passwd, String bucket) throws ArgException{
		init(node, user, passwd, bucket, false, null, null);
	}
	
	public BaseDAO(String node, String user, String passwd, String bucket, boolean sslEnabled, String ksFilePath, String ksPwd) 
			throws ArgException
	{
		init(node, user, passwd, bucket, sslEnabled, ksFilePath, ksPwd);
		
	}

	
	/**
	 * This constructor takes Arrays of IP addresses to initialize the object.
	 * @param nodes - ArrayList<String> of Couchabse Cluster IP/FQDNs (preferred way to connect in prod env)
	 * @param user - RBAC used with Application Access
	 * @param passwd - password of the user used to connect to the bucket
	 * @param bucket -  where data will be written to
	 * @throws ArgException
	 */
	public static void init(String node, String user, String passwd, String bucket, boolean sslEnabled, 
			String ksFilePath, String ksPwd) throws ArgException
	{
		//System.out.println("SSL enabled="+ sslEnabled + " env: " + env);
		//initialize CouchbaseEnvironment if not done already
		
		if(env == null) {
			env = DefaultCouchbaseEnvironment
					.builder()
					.mutationTokensEnabled(true)
					.keepAliveInterval(600000) //60 sec or 1 mins
					.computationPoolSize(5)
					.connectTimeout(5000)
					.socketConnectTimeout(5000)
					.queryTimeout(15000)
					.kvTimeout(2500)
					.keyValueServiceConfig(KeyValueServiceConfig.create(2))
					.sslEnabled(sslEnabled)
					.sslKeystoreFile(ksFilePath)
					.sslKeystorePassword(ksPwd)
					.build();
		}
		
		if(StringUtil.isNotEmpty(node) && StringUtil.isNotEmpty(user) && StringUtil.isNotEmpty(passwd)){
			// Initialize the Connection
			if(sampleBucket==null) {
				
				int index = node.indexOf("couchbase://");
				System.out.print("node: " + node + ", user: " + user + ", bucket: " + bucket + ", isSSLEnabled: " +
					 sslEnabled + ", ksFilePath: " + ksFilePath + ", isConnectionStr: " + index);
				
				//confirm node is not a connectionString
				if(index<0) {
					cluster = CouchbaseCluster.create(env, node);
					System.out.println("node: " + node + " is being used to connect.\n");
				}else {
					cluster = CouchbaseCluster.fromConnectionString(env, node);
					System.out.println("connectionString: " + node + " is used to connect.\n");
				}
				cluster.authenticate(user, passwd);
				//if bucket is not set or empty then default to "test" bucket
				if(StringUtil.isEmpty(bucket))
					bucket="test";

				sampleBucket = cluster.openBucket(bucket);
				System.out.println("Connection created.");
			}//EOF if
		}else{
			throw new ArgException("Some of the required arguments to connect to Couchbase Cluster are not present. "
					+ "\nAction: Please try again with -h, -u, -p, -b must have options.");
		}

	}//BaseDAO

	/**
	 * This method writes the json object into the couchbase sample-bucket
	 * @param id - Unique identifier of the document
	 * @param jsonString - Json text that need to be stored
	 * @throws Exception
	 */
	public void upsert(String id, String jsonString) throws Exception {
		upsert( id,  jsonString, 0);

	}

	/**
	 * Method to upsert a document with a desired TTL 
	 * @param id of the document
	 * @param jsonString json document
	 * @param ttl in seconds
	 * @throws Exception
	 */
	public void upsert(String id, String jsonString, int ttl) throws Exception {
		//System.out.println("TTL : " + ttl);
		JsonTranscoder trans = new JsonTranscoder();
		JsonObject jsonObject = trans.stringToJsonObject(jsonString);

		// Store the Document by persisting to NONE and replicating to NONE. If higher durability
		//required then begin with Replicate.ONE
		sampleBucket.upsert(JsonDocument.create(id, jsonObject), PersistTo.NONE,ReplicateTo.NONE);
		sampleBucket.touch(id, ttl);

	}

	/**
	 * This method takes N1QL query string and executes it on the sampleBucket
	 * @param queryString  - N1QL query string
	 * @return N1qlQueryResult
	 */
	public N1qlQueryResult executeQuery(String queryString) {
		N1qlQuery airlineQuery = N1qlQuery.simple(queryString);
		N1qlQueryResult queryResult = sampleBucket.query(airlineQuery);
		return queryResult;
	}//executeQuery
	
	public static void main(String[] args) {
		
		System.out.println("Testing only");
		try {
			BaseDAO baseDAO = new BaseDAO();
			System.out.println(baseDAO);
			
		} catch (ArgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// main
	

}
