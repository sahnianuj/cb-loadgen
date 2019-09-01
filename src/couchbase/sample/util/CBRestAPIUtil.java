package couchbase.sample.util;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLConnection;

public class CBRestAPIUtil {

	public static String getPOSTServiceOutput(URL serviceUrl, String jsonDocument) {
		
		//System.out.println(serviceUrl);
		String response = null;

		try {
			
			URLConnection connection = serviceUrl.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(jsonDocument);
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer sb = new StringBuffer();

			while((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();

			response = sb.toString();


		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}//runPOSTService

}
