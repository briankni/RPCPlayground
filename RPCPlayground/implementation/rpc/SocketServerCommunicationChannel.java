package rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

public class SocketServerCommunicationChannel extends AbstractCommunicationChannel {
	Semaphore connectionSemaphore ;
	public SocketServerCommunicationChannel(IMessageHandler handler,int p) throws IOException{
		connectionSemaphore = new Semaphore(1);
		try {
			connectionSemaphore.acquire();
		} catch (InterruptedException e1) {
			//exit
		}
		this.registerMessageHandler(handler);
		ServerSocket server = new ServerSocket(p);
		Thread acceptThread = new Thread(new Runnable(){

			@Override
			public void run() {
				//Only one connection allowed	
				try {
					SocketServerCommunicationChannel.this.useSocket(server.accept());
					connectionSemaphore.release();
					server.close();
				} catch (IOException e) {
					//exit thread
				}
				
			}
				
			});
		acceptThread.start();

	}
	
	public void waitForConnection() throws InterruptedException{
		connectionSemaphore.acquire();
		connectionSemaphore.release();
		
	}
}
