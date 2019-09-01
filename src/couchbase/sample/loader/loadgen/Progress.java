package couchbase.sample.loader.loadgen;

public class Progress implements Runnable{
	
	private boolean exit = false;
	
	public void run() {
		
		while(!exit){

			try {
				System.out.print(".");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//EOF while
	}//run
	
	// for stopping the thread 
    public void stop() 
    { 
        exit = true; 
    } 
}
