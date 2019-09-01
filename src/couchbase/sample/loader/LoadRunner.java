package couchbase.sample.loader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.couchbase.client.deps.io.netty.util.internal.StringUtil;

import couchbase.sample.exception.ArgException;
import couchbase.sample.loader.loadgen.Progress;
import couchbase.sample.loader.loadgen.WorkerNode;
import couchbase.sample.util.ArgumentReader;

public class LoadRunner {

	private static String CB_USER = null;
	private static String CB_PASSWORD = null;
	private static String CB_BUCKET = null;
	private static String CB_CLUSTER = null;
	
	//TLS related attributes
	private static boolean SSL_ENABLED = false;
	private static String KS_PATH = null;
	private static String KS_PASSWORD = null;
	

	public static void main(String[] args) {
		

		System.out.println("Initializing ..");
		

		try{
			Hashtable<String, String> argsHash = ArgumentReader.getArguments(args);
			
			//1. CB User
			CB_USER = argsHash.get("-u");
			CB_USER = StringUtil.isNullOrEmpty(CB_USER)?"Administrator":CB_USER;
			
			//2. CB password
			CB_PASSWORD = argsHash.get("-p");
			CB_PASSWORD = StringUtil.isNullOrEmpty(CB_PASSWORD)?"password":CB_PASSWORD;
			
			//3. CB Bucket 
			CB_BUCKET = argsHash.get("-b");
			CB_BUCKET = StringUtil.isNullOrEmpty(CB_BUCKET)?"default":CB_BUCKET;
			
			//4. CB cluster host FQDN or IP
			CB_CLUSTER = argsHash.get("-h");
			CB_CLUSTER = StringUtil.isNullOrEmpty(CB_CLUSTER)?"localhost":CB_CLUSTER;
			
			//5. SSL enabled or not
			SSL_ENABLED = Boolean.parseBoolean(argsHash.get("-e"));
			//6. Keystore full path
			KS_PATH = argsHash.get("-ks");
			//7. Keystore password
			KS_PASSWORD = argsHash.get("-kp");
			
			// a) number of client threads ;
			int threads = Integer.parseInt(argsHash.get("-t"));
			// assign 2 if set to less than 0
			threads=threads>0?threads:1;

			//b) number of documents inserted in total;
			int docs = Integer.parseInt(argsHash.get("-d"));
			//assign 100 if set to less than 0
			docs = docs>0?docs:100;
			
			//Docs per thread
			int docPerThread = docs/threads;

			System.out.print("Loading customer data into database ..");
			
			//show progress dot
			Progress p = new Progress();
			Thread pt = new Thread(p);
			pt.start();

			List<Thread> tList = new ArrayList<Thread>();
			WorkerNode wn = null;
			
			//Start clock time now
			long start = System.currentTimeMillis();
			
			//Spawn n number of threads in parallel so they all can make connection to database
			for(int i = 0; i< threads;i++){
				wn = new WorkerNode(docPerThread, CB_CLUSTER, CB_USER, CB_PASSWORD, CB_BUCKET, 
						SSL_ENABLED, KS_PATH, KS_PASSWORD);

				//start a new thread to generate load on the system
				Thread t = new Thread(wn, "Thread "+i);
				t.start();
				
				//add thread t to the tList
				tList.add(t);
				
			}//EOF for
			
			// Now wait for all of the threads to complete
			for (Thread t : tList) {
			    t.join();
			}
			
			
			
			// Stop the clock so we can measure time elapsed
			long stop = System.currentTimeMillis();
			//stop progress dots
			p.stop();
			
			DecimalFormat df = new DecimalFormat("#.##");
			long elapsedTime = stop-start;
			int throuput = (int) ((docs*1000)/elapsedTime); 
			double latency = (double) elapsedTime/docs;
			System.out.println("\n*****************************************************************");
			System.out.println("Time elapsed: " + elapsedTime + " ms");
			System.out.println("Average Throughput: " + throuput + " ops/sec");
			System.out.println("Average Latency: " + df.format(latency) + " ms");
			System.out.println("*****************************************************************");
			
			
		}catch(ArgException ae){
			System.out.println("Error: " + ae.getMessage());
			System.exit(1);
		}catch(Exception e){
			e.printStackTrace();
		}

	}//EOF main

}
