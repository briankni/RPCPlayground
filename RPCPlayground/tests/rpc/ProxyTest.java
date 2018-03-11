package rpc;

import org.junit.Assert;
import org.junit.Test;

import rpc.exceptions.RPCException;
import rpc.proxy.ClassTemplate;
import rpc.proxy.ProxyFactory;
import rpc.proxy.ProxyTemplate;

public class ProxyTest implements ProxyTestInterface, IMessageHandler {

	@Test
	public void responseViaProxy(){
		String response;
		
		ProxyTemplate p = new ProxyTemplate();
		p.setMessageHandler(this); // Redirects command sends back to this unit test, which 
								   // doubles as a mock IMessageHandler
		ProxyTestInterface proxyToInterface = (ProxyTestInterface)ProxyFactory.createProxy(new ClassTemplate("AbsoluteName", this),this);
		
		response = proxyToInterface.returnTestString();
		Assert.assertEquals(response, this.returnTestString());
		
	}
	
	@Override
	public String returnTestString() {
		return "Dummy String";
	}

	@Override
	public void handleIncomingMessage(IMessage incoming) throws RPCException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(IMessage sending) throws RPCException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IResponse sendCommand(ICommand command) throws RPCException {
		Assert.assertEquals(command.getInvocationTarget(), "returnTestString");
		return new Response(command, this.returnTestString());
	}

	@Override
	public void useChannel(ICommunicationChannel channelToUse) {
		// TODO Auto-generated method stub
		
	}
	
	
}
