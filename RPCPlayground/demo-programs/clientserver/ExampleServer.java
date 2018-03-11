package clientserver;


import java.io.IOException;

import rpc.IMessageHandler;
import rpc.IRegistry;
import rpc.MessageHandler;
import rpc.Registry;
import rpc.SocketServerCommunicationChannel;


public class ExampleServer implements ExampleRemotelyCalledInterface {

	//System components
	static IRegistry registry;
	static SocketServerCommunicationChannel channel;
	static IMessageHandler handler;
	
	public static void main(String[] args) {
		System.out.println("Creating a registry component to store objects that may be accessed remotely");
		Registry.setLocalPrefix("Server");
		Registry.setRemotePrefix("Client");
		registry = Registry.getRegistry();
		
		System.out.println("Directing remote calls to objects in the registry");
		handler = new MessageHandler();
		registry.setMessageHandler(handler);
		System.out.println("Listening for TCP/IP connections on port 5000");
		try {
			channel = new SocketServerCommunicationChannel(handler,5000);
			channel.waitForConnection();
		} catch (IOException | InterruptedException e) {
			System.out.println("Connection error: "+ e.getMessage());
			System.exit(-1);
		}
		handler.useChannel(channel);
		
		
		System.out.println("Registering a test component with both sides of the connection");
		ExampleServer me = new ExampleServer();
		try {
			registry.register("me", me);
		} catch (Exception e1) {
			System.out.println("Could not register interface with the client: "+ e1.getMessage());
			System.exit(-1);
		}
		System.out.println("Local registry: " + Registry.getRegistry().toString());
		try {
			channel.waitForClose();
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
		System.out.println("Finished");
	}

	@Override
	public String test(Integer parameter) {
		System.out.println("Received a test method call with parameter: " + parameter);
		return "Returned this String after receiving: "+parameter ;
	}

}
