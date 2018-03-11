package rpc;

import rpc.exceptions.RPCException;

public interface IMessageHandler {

	/**
	 * Route received message to either an object waiting for a response or a message handler to 
	 * generate a response.
	 * @param incoming message received from remote connection
	 * @throws RPCException
	 */
	public void handleIncomingMessage(IMessage incoming) throws RPCException;
	
	/**
	 * Send the message to the remote side
	 * @param sending message to send
	 * @throws RPCException
	 */
	public void send(IMessage sending) throws RPCException; 
	
	/**
	 * Send the command and block until a timeout or a response is received
	 * @param command
	 * @return response object
	 * @throws RPCException
	 */
	public IResponse sendCommand(ICommand command) throws RPCException; 
	
	/**
	 * Assign a communication channel
	 * @param channelToUse
	 */
	public void useChannel(ICommunicationChannel channelToUse);

}
