package rpc.proxy;

import java.io.Serializable;

import rpc.IMessageHandler;

/**
 * This class 
 * 
 */
public class ProxyTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6459158265825306464L;
	

	IMessageHandler handler;
	
	public ProxyTemplate() {
		handler = null;
	}
	
	
	public ProxyTemplate(IMessageHandler handlerToUse) {
		handler = handlerToUse;
	}


	public void setMessageHandler(IMessageHandler messageHandler){
		handler = messageHandler;
	}
	
	public IMessageHandler getMessageHandler() {
		return handler;
	}

}
