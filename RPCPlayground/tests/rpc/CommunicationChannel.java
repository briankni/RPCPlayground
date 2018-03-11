package rpc;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.Test;

import rpc.exceptions.RPCException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class CommunicationChannel implements IMessageHandler {
	SocketClientCommunicationChannel client;
	SocketServerCommunicationChannel server;
	Command testCommand1;
	Command testCommand2;
	
	IMessage lastReceived;
	
	@Before // Create a loopback channel and two commands to send/receive during tests
	 public void before() throws IOException {
		server = new SocketServerCommunicationChannel(this, 5000);
        client =  new SocketClientCommunicationChannel(this, new InetSocketAddress("127.0.0.1", 5000));
        testCommand1 = new Command("TargetObjectName","Method1",new Object[]{1, 2, 3});
        testCommand2 = new Command("TargetObjectName","Method2", new Object[]{4, 5, 6});
        
    }
	
	@Test
	public synchronized void serverToClient() {
		try {
			try {
				server.send(testCommand1);
			} catch (RPCException e) {
				Assert.fail("Failed to write command");
			}
			this.wait();
			Assert.assertEquals(testCommand1.getInvocationTarget(), ((ICommand) this.lastReceived).getInvocationTarget());
		} catch (InterruptedException e) {
			Assert.fail("test did not finish");
		}
	}
	
	
	@Test
	public synchronized void clientToServer() {
		try {
			try {
				client.send(testCommand1);
			} catch (RPCException e) {
				Assert.fail("Failed to write command");
			}
			this.wait();
			Assert.assertEquals(testCommand1.getInvocationTarget(), ((ICommand) this.lastReceived).getInvocationTarget());
		} catch (InterruptedException e) {
			Assert.fail("test did not finish");
		}
	}
	
	@Test
	public synchronized void clientToServerTwice() {
		try {
			try {
				server.send(testCommand1);
				this.wait();
				server.send(testCommand2);
			} catch (RPCException e) {
				Assert.fail("Failed to write command");
			}
			this.wait();
			Assert.assertEquals(testCommand2.getInvocationTarget(), ((ICommand) this.lastReceived).getInvocationTarget());
		} catch (InterruptedException e) {
			Assert.fail("test did not finish");
		}
	}
	

	@Override
	public synchronized void handleIncomingMessage(IMessage incoming) {
		lastReceived = incoming;
		this.notifyAll();
		
	}
	
	@After// setup()
	 public void after() {
		client.close();
		server.close();
		
	}

	@Override
	public void send(IMessage sending) throws RPCException {
		//not required
		
	}

	@Override
	public IResponse sendCommand(ICommand command) throws RPCException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void useChannel(ICommunicationChannel channelToUse) {
		// TODO Auto-generated method stub
		
	}
	

}
