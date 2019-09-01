package couchbase.sample.dao;

import couchbase.sample.exception.ArgException;

public class CustomerDAO extends BaseDAO{

	public CustomerDAO() throws ArgException {
		super();
	}
	/**
	 * 
	 * @param host - Couchbase server FQDN
	 * @param user - RBAC user that has access to access data from the bucket
	 * @param passwd - User credential password
	 * @param bucket - Couchabse bucket name where data need to be accessed from
	 * @throws ArgException
	 */
			
	public CustomerDAO(String host, String user, String passwd, String bucket) throws ArgException {
		super(host, user, passwd, bucket);
	}
	
	/**
	 * When SSL is enabled then use this constructor to set all the required fields
	 * @param host - Couchbase server FQDN
	 * @param user - RBAC user that has access to access data from the bucket
	 * @param passwd - User credential password
	 * @param bucket - Couchabse bucket name where data need to be accessed from
	 * @param sslEnabled - true if SSL is enabled 
	 * @param ksFilePath - Keystore File path
	 * @param ksPwd - Keystore password
	 * @throws ArgException
	 */
	
	public CustomerDAO(String host, String user, String passwd, String bucket,
			boolean sslEnabled, String ksFilePath, String ksPwd) throws ArgException 
	{
		super(host, user, passwd, bucket, sslEnabled, ksFilePath, ksPwd);
	}
	
}
