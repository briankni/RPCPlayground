package rpc;



import org.junit.Assert;
import org.junit.Test;

import rpc.exceptions.RPCException;



public class RegistryTest implements IMessageHandler {

	private ICommand lastCommand;

	@Test
	public void registerAndRetrieveLocal() {
		IRegistry r = Registry.getRegistry();
		Object local = new Object();
		Object test = null;
		
		r.registerLocal("MyLocalTest", local);
		try {
			test = r.find(r.getLocalPrefix()+"MyLocalTest");
		} catch (Exception e) {
			Assert.fail("No exception expected at this point");
		}
		Assert.assertEquals(local, test);
		
	}
	
	@Test(expected = Exception.class)
	public void retrieveMissing() throws Exception{
		IRegistry r = Registry.getRegistry();
		r.find("ThisDoesntExist");
	}
	
	
	@Test
	public void registerLocalAndRemote(){
		Registry.setLocalPrefix("Test");
		Registry.setRemotePrefix("TestRemote");
		IRegistry r = Registry.getRegistry();
		Object test = new Object();
		r.setMessageHandler(this);
		
		try {
			r.register("MyLocalTest", test);
		} catch (Exception e) {
			Assert.fail("Exception not expected");
		}
		
		//If the registry mechanism is working, the test should be able to intercept
		//a command to the remote registry telling it to register the new object's interfaces
		
		Assert.assertEquals("registerRemote", this.lastCommand.getInvocationTarget());
		Assert.assertEquals("TestRemoteRegistry", this.lastCommand.getResponder() );
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
		
		this.lastCommand = command;
		return new Response(command,null);
	}

	@Override
	public void useChannel(ICommunicationChannel channelToUse) {
		// TODO Auto-generated method stub
		
	}
	

}
