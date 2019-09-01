package couchbase.sample.exception;

public class ArgException extends Exception {

	private static final long serialVersionUID = 1L;

	public ArgException(String message){
		super(message);
	}
}
