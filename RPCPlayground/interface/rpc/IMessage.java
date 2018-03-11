package rpc;


public interface IMessage {
	/**
	 * Unique number assigned by the sender.  This allows blocking on the 
	 * sending side while waiting for a response while using asynchronous communication
	 * @return unique number
	 */
	public long getCommanderNumber();
	
	/**
	 * Get the object meant to respond or which has responded
	 * @return full name of the message recipient
	 */
	public String getResponder();
}
