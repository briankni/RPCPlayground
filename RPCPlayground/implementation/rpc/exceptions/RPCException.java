package rpc.exceptions;


public class RPCException extends Exception {

	public RPCException(String string) {
		super(string);
	}

	public RPCException(String string, Exception e1) {
		super(string,e1);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7292634111370010185L;

}
