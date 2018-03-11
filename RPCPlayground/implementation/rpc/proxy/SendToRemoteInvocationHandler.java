package rpc.proxy;

import java.lang.reflect.Method;

import rpc.Command;
import rpc.IClassTemplate;
import rpc.ICommand;
import rpc.IMessageHandler;
import rpc.IResponse;
import rpc.exceptions.RPCException;

public class SendToRemoteInvocationHandler implements java.lang.reflect.InvocationHandler, IClassTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6861789729322343947L;
	String target;
	Class<?>[] interfaces;
	IMessageHandler handler;
	
	public SendToRemoteInvocationHandler(String targetName, Class<?>[] targetInterfaces, IMessageHandler handlerToUse) {
		target = targetName;
		interfaces = targetInterfaces;
		handler = handlerToUse;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String target = this.getTargetObject();
		ICommand c = new Command(target, method.getName(), args);
		if(handler == null){
			throw new RPCException("The command handler was not set in this invocation handler");
		}
		IResponse response = handler.sendCommand(c);
		if(response.getReply() instanceof Exception){
			//Remote exceptions get thrown on the calling side
			throw (Exception)response.getReply();
		}
		return response.getReply();
		
				
	}

	@Override
	public String getTargetObject() {
		return target;
	}

	@Override
	public Class<?>[] getInterfaces() {
		return interfaces;
	}

}
