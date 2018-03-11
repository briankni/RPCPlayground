package rpc;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClientCommunicationChannel extends AbstractCommunicationChannel {
	public SocketClientCommunicationChannel(IMessageHandler handler,SocketAddress endpoint) throws IOException{
		this.registerMessageHandler(handler);
		Socket client = new Socket();
		
		client.connect(endpoint);
		
		//Only one connection allowed	
		this.useSocket(client);
	}


}