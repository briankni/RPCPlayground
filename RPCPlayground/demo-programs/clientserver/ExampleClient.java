package clientserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import rpc.ICommunicationChannel;
import rpc.IMessageHandler;
import rpc.IRegistry;
import rpc.MessageHandler;
import rpc.Registry;
import rpc.SocketClientCommunicationChannel;

public class ExampleClient{

	//System components
	static IRegistry registry;
	static ICommunicationChannel channel;
	static IMessageHandler handler;
	
	public static  void main(String[] args) {
		Registry.setLocalPrefix("Client");
		Registry.setRemotePrefix("Server");
		registry = Registry.getRegistry();
		//There is an assumption that the other side has a registry object exposed
		
		handler = new MessageHandler();
		registry.setMessageHandler(handler);
		try {
			channel = new SocketClientCommunicationChannel(handler,new InetSocketAddress("127.0.0.1", 5000));
		} catch (IOException e) {
			System.out.println("Failed to connect: "+ e.getMessage());
		}
		handler.useChannel(channel);
		
		
		try {
			System.out.println("Delaying for 5 seconds to give the other side time to complete initial configuration");
			Thread.sleep(5000);
		} catch (InterruptedException e2) {
			//exit
		}
		System.out.println("Local registry: " + Registry.getRegistry().toString());
		ExampleRemotelyCalledInterface remote = null;
		try {
			remote = (ExampleRemotelyCalledInterface) registry.find("Serverme");
		} catch (Exception e1) {
			System.out.println("Registry lookup failed: "+e1.getMessage());
			return;
		}
		System.out.println("Result of remote call: " + remote.test(5));
		
		channel.close();
		
		System.out.println("Finished");

	}

}
