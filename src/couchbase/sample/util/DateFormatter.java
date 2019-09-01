package couchbase.sample.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	private static String pattern = "MM/dd/yy";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	public static String getFormatedDate(Date date) {
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}//getCurrentTimeString
	
	public static String getDateFromEpoc(int timeInHrs){
		//Handle the int to long casting carefully otherwise you would get negative number
		long time = timeInHrs * (long)(1000 * 3600);
		//System.out.println("Time in Hrs: " + timeInHrs + ", converted time in long: " + time );
		Date date = new Date(time);
		String formattedDate = getFormatedDate(date);
		return formattedDate;
	}
	
	public static int getTimeInHrs(long timeMillis){
		int timeInHrs = (int) (timeMillis/1000/3600);
		return timeInHrs;
	}
	/**
	 * This method can take date strings in two formats: a) MM/dd/yy and b) MM/dd/yy HH:mm:ss
	 * If valid format date string is passed then it converts date into time 
	 * @param formattedDate - Date represented into MM/dd/yy or MM/dd/yy HH:mm:ss
	 * @return long representation of date into milliseconds
	 */
	public static long getEpocTime(String formattedDate) {
		long time = 0;


		try {
			if(formattedDate!=null && formattedDate.length()>0) {
				formattedDate=formattedDate.trim();

				//check if milliseconds are present in the formattedDate
				int milli = formattedDate.indexOf('.');
				int sec = formattedDate.lastIndexOf(':');

				//if seconds exist but not milliseconds then append milliseconds
				if(sec>0 && milli<0)
					formattedDate+=".000";
				else 
					formattedDate+=" 00:00:00.000";

				Date date = simpleDateFormat.parse(formattedDate);
				time = date.getTime();
			}//formattedDate is null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}//getTime

	public static int getEpocTimeinHrs(Date date) {
		int time = 0;
		System.out.println("Date: " + date);
		if(date!=null){
			String formattedDate = simpleDateFormat.format(new Date());
			long longTime = getEpocTime(formattedDate);
			System.out.println("Long Date: " + longTime);
			time = DateFormatter.getTimeInHrs(longTime);
		}
		
		return time;
	}
	
	
	public static void main(String args[]) {
		Date currentDate = new Date();
		String formattedDate = DateFormatter.getFormatedDate(currentDate);
		int timeInHrs = DateFormatter.getEpocTimeinHrs(currentDate);
		String dateFromTime = DateFormatter.getDateFromEpoc(timeInHrs);
		
		
		System.out.println("Formatted Date: " + formattedDate);
		System.out.println("Time in Int: " + timeInHrs);
		System.out.println("Date from TimeInHrs: " + dateFromTime);
	}
}
