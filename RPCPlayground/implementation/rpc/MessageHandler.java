package rpc;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rpc.exceptions.RPCException;
import rpc.proxy.RespondToCommandHandler;

public class MessageHandler implements IMessageHandler {

	public static int TIMEOUT_IN_MS = 5000;
	
	private ICommunicationChannel chanel = null; 
	private Long commandNumber = (long) 0;
	
	//One exchanger per command send.
	Map<Long, Exchanger<IResponse>> openCommands;
	
	
	public MessageHandler(){
		
		openCommands = new HashMap<Long, Exchanger<IResponse>>();
	}
	
	@Override
	public void handleIncomingMessage(IMessage incoming) {
		//Message is a command, that has been received
		if(incoming instanceof ICommand ){
			this.handleIncomingCommand((ICommand)incoming);
		} else {
			//Message is a response to a command
			this.handleIncomingResponse((IResponse)incoming);
		}
		
	}
	
	private void handleIncomingCommand(ICommand incoming) {
			new RespondToCommandHandler(this, incoming);
		
	}

	public IResponse sendCommand(ICommand command) throws RPCException {
		Long myNumber;
		synchronized(commandNumber){
			myNumber = commandNumber++;
		}
		command.setCommanderNumber(myNumber);
		Exchanger<IResponse> commandExchanger = new Exchanger<IResponse>();
		synchronized(openCommands){
			openCommands.put(myNumber, commandExchanger);
		}
		try {
			this.chanel.send(command);
		} catch (RPCException e1) {
			throw new RPCException("Could not write command", e1);
		}
		//Wait for the message handling thread to release the semaphore
		try {
			return (IResponse) commandExchanger.exchange(null, TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			//Signal to exit thread.  No action required.
		} catch (TimeoutException e) {
			throw new RPCException("Command timed out after "+MessageHandler.TIMEOUT_IN_MS+" milliseconds.", e);
		}
		return null;		
	}
	
	private void handleIncomingResponse(IResponse response){
		Exchanger<IResponse> commandExchanger = null;
		synchronized(openCommands){
			commandExchanger = openCommands.get(response.getCommanderNumber());
		}
		if(commandExchanger != null){
			try {
				commandExchanger.exchange(response);
			} catch (InterruptedException e) {
				//Signal to exit thread.  No action required.
			}
		}
				
	}

	@Override
	public void send(IMessage sending) throws RPCException {		
		if(sending instanceof ICommand ){
			//Message is a command that is awaiting a response
			this.sendCommand((ICommand)sending);
		} else {
			//Message is a response to a command
			try {
				chanel.send(sending);
			} catch (RPCException e) {
				throw new RPCException("Could not write response", e);
			}
		}
		
		
	}

	@Override
	public void useChannel(ICommunicationChannel channelToUse) {
		chanel = channelToUse;
		
	}



}
