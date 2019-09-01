package couchbase.sample.report;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import couchbase.sample.json.ClusterMapObj;
import couchbase.sample.util.CBRestAPIUtil;
import couchbase.sample.vo.Server;

public class ClusterStatReport extends BaseReport {

	public static String INDEX = "index";
	public static String DATA = "kv";
	public static String FTS = "fts";
	public static String QUERY = "n1ql";

	/**
	 * 
	 * @param queryHostname
	 * @param user
	 * @param password
	 * @return A HashMap with key as the name of the service (like fts, kv, n1ql etc) and value as the List<String> of hostname
	 */
	protected  HashMap<String, ArrayList<String>> getClusterMap(String queryHostname, String user, String password){

		HashMap<String, ArrayList<String>> clusterMap = new HashMap<String, ArrayList<String>>();

		String queryStmt = super.getQueryStmtForClusterMap(queryHostname, user, password);

		try {
			//run REST call to get the cluster map
			String resultJson = CBRestAPIUtil.getPOSTServiceOutput(super.getQueryServiceURL(queryHostname), queryStmt);

			//convert resultJson to ClusterMapObj
			ClusterMapObj clusterMapObj =  ClusterMapObj.fromJson(resultJson);

			if(clusterMapObj !=null) {
				//get servers from the clusterMap object
				List<Server> servers = new ArrayList<Server>();
				servers = clusterMapObj.getResults();

				String hostname = null;
				List<String> serviceHosts = null;

				//now iterate through each and populate the map
				for(Server server: servers) {
					hostname = server.getHostname();
					//remove :port from the hostname
					hostname = hostname.substring(0, hostname.indexOf(':'));

					List<String> services = server.getServices();
					for(String serviceName: services) {
						//if service already exist then add this hostname to the list
						if(clusterMap.containsKey(serviceName)){
							serviceHosts = clusterMap.get(serviceName);
							//add new host to the list
							serviceHosts.add(hostname);
						}else {
							//create a new list of serviceHost and add it for the serviceName as the key
							ArrayList<String> hostnames = new ArrayList<String>();
							hostnames.add(hostname);

							//add this list to the map with serviceName as the key
							clusterMap.put(serviceName, hostnames);
						}
					}//eof for

				}//eof for
			}//eof if

			System.out.println(clusterMap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return clusterMap;
	} //getClusterMap()


	protected  HashMap<String, ArrayList<String>> getResidentRatioForIndexes(String queryHostname, String user, String password){

		String queryStmt = null;
		String resultJson = null;

		//get cluster map definition first
		HashMap<String, ArrayList<String>> clusterMap = this.getClusterMap(queryHostname, user, password);
		//System.out.println(clusterMap);
		
		if(clusterMap != null) {
			//get all index types
			ArrayList<String> indexHosts = clusterMap.get(INDEX);
			//System.out.println(indexHosts);

			//iterate through the list and display resident ratios of each index in the index node
			for(String indexHostname: indexHosts) {
				queryStmt = super.getQueryMemoryResidentIndex(indexHostname, user, password);
				try {
					//run REST call to get the cluster map
					resultJson = CBRestAPIUtil.getPOSTServiceOutput(getQueryServiceURL(queryHostname), queryStmt);
					System.out.println(resultJson);

				}catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//EOF for
		}//EOF if(clusterMap!=null)


		return clusterMap;
	} //getClusterMap()

	public static void main(String[] args) {

		ClusterStatReport clusterStatReport = new ClusterStatReport();

		try {
			//clusterStatReport.getClusterMap("ec2-54-202-253-193.us-west-2.compute.amazonaws.com", "Administrator", "p0lar1s");
			clusterStatReport.getResidentRatioForIndexes("ec2-52-12-206-206.us-west-2.compute.amazonaws.com", "Administrator", "p0lar1s");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}//main

}
