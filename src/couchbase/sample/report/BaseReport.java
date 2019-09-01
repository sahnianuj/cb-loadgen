package couchbase.sample.report;

import java.net.MalformedURLException;
import java.net.URL;

import couchbase.sample.json.CurlQueryObj;

public class BaseReport {

	public static URL getQueryServiceURL(String queryHostname) throws MalformedURLException {

		String path = "http://" + queryHostname + ":8093/query/service";
		URL url = new URL(path);
		return url;
	}//getQueryServiceURL

	/*
	 * This method returns formatted curl statement with credentials attached to it so it can be run on the 
	 */
	private static String getCurlForClusterMap(String hostname, String user, String password) {
		String curl = "CURL('" + hostname + ":8091/pools/default', {'user':'" + user + ":" + password + "'})";
		return curl;
	}

	private static String getCurlForStorageStats(String hostname, String user, String password) {
		String curl = "CURL('" + hostname + ":9102/stats/storage', {'user':'" + user + ":" + password + "'})";
		return curl;
	}

	/**
	 *  This method return fully formated json document with credentials and curl statement to be submitted to the
	 *  POST end point to fetch cluster map of any given cluster when queryHostname and user/password is know. 
	 * @param hostname - Any query hostname 
	 * @param user - Username of the administrator user
	 * @param password - Admin password
	 * @return
	 */
	public static String getQueryStmtForClusterMap(String hostname, String user, String password) {

		String curlStmt = getCurlForClusterMap(hostname, user, password);
		//System.out.println(curlStmt);

		String queryStmt = "SELECT h.hostname, h.services FROM " + curlStmt
				+ " n UNNEST n.nodes as h ORDER BY h.services";

		CurlQueryObj json = new CurlQueryObj(queryStmt, user, password);
		String curlQueryJson = json.toJson();
		//System.out.println(curlQueryJson);

		return curlQueryJson;
	}

	public static String getQueryMemoryResidentIndex(String indexHostname, String user, String password) {

		String curlStmt = getCurlForStorageStats(indexHostname, user, password);
		//System.out.println(curlStmt);

		String queryStmt = " SELECT s.indexname, \n" + 
				"s.items, \n" + 
				"ROUND(s.mmsi/(1024*1024),2) as memory_size_index_MB,\n" + 
				"ROUND(s.mms/(1024*1024),2) as memory_size_MB,\n" + 
				"ROUND(((s.ma)*100-(s.mf)*100)/(((s.mso)-(s.msi))+(s.ma-s.mf)), 2) as resident_ratio\n" + 
				"FROM\n" + 
				"( SELECT pl.plasma.`Index` as indexname, \n" + 
				"pl.plasma.Stats.MainStore.count items,\n" + 
				"pl.plasma.Stats.MainStore.num_rec_allocs as ma,\n" + 
				"pl.plasma.Stats.MainStore.num_rec_frees as mf, \n" + 
				"pl.plasma.Stats.MainStore.num_rec_swapout as mso,\n" + 
				"pl.plasma.Stats.MainStore.num_rec_swapin as msi,\n" + 
				"pl.plasma.Stats.MainStore.memory_size_index as mmsi,\n" + 
				"pl.plasma.Stats.MainStore.memory_size mms\n" + 
				"FROM (SELECT plasma FROM " + curlStmt + " plasma) pl) as s\n" + 
				"ORDER by resident_ratio DESC";

		CurlQueryObj json = new CurlQueryObj(queryStmt, user, password);
		String curlQueryJson = json.toJson();
		//System.out.println(curlQueryJson);

		return curlQueryJson;
	}
}
