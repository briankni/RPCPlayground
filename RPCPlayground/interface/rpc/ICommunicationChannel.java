package rpc;

import rpc.exceptions.RPCException;

public interface ICommunicationChannel {
	/**
	 * send a message 
	 * @param sending message to send
	 * @throws RPCException
	 */
	public void send(IMessage sending) throws RPCException;
	
	/**
	 * Register a handler component that will be passed all incoming messages.
	 * Reads will be blocked until a handler is registered to avoid losing messages.
	 * @param handler
	 */
	public void registerMessageHandler(IMessageHandler handler);
	
	/**
	 * close the channel
	 */
	public void close();
	
	/**
	 * Block until the channel is closed.
	 * Used to wait for the other side of the connection to complete 
	 * and close first.
	 * @throws InterruptedException
	 */
	public void waitForClose() throws InterruptedException;
}
