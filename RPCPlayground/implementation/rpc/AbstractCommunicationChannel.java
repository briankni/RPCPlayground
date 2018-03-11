package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import rpc.exceptions.RPCException;

public abstract class AbstractCommunicationChannel implements ICommunicationChannel {
	
	private Socket socketConnection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Thread receiveThread;
	private IMessageHandler handler;
	private Semaphore readyToReadSemaphore;
	
	public AbstractCommunicationChannel(){
		readyToReadSemaphore = new Semaphore(1);
		try {
			readyToReadSemaphore.acquire(1);
		} catch (InterruptedException e) {
			//exit
		}
	}
	
	protected void useSocket(Socket s) throws IOException{
		socketConnection = s;
		output = new ObjectOutputStream(socketConnection.getOutputStream());
		input = new ObjectInputStream(socketConnection.getInputStream());
		receiveThread = new Thread(new Runnable(){
		
		
			@Override
			public void run() {
				try {
					readyToReadSemaphore.acquire(1);
				} catch (InterruptedException e1) {
					return;
				}
					while(true){
						Object incoming;
						try {
							incoming = AbstractCommunicationChannel.this.input.readObject();
							AbstractCommunicationChannel.this.handler.handleIncomingMessage((IMessage) incoming);
						} catch (Exception e) {
							if(! (e instanceof IOException)){
								//Record exception and stop, but do not log IOexceptions caused by socket closing
								AbstractCommunicationChannel.this.receiveThread.getUncaughtExceptionHandler().uncaughtException(AbstractCommunicationChannel.this.receiveThread,e);
							}
							break;
						}
						
					}
		
				
			}
			
		},"Socket Reader");
		receiveThread.start();
	}
	
	public void send(IMessage message) throws RPCException{
		try {
			output.writeObject(message);
		} catch (IOException e) {
			throw new RPCException("Failed to send the message: ", e);
		}
		
	}
	
	public void registerMessageHandler(IMessageHandler newHandler){
		handler = newHandler;
		readyToReadSemaphore.release();
	}
	
	public void close(){

			try {
				socketConnection.close();
				input.close();
				output.close();
				try {
					receiveThread.join(5000);
				} catch (InterruptedException e) {
					//continue to exit
				}
			} catch (IOException e) {
				//continue to exit
			}
			

	}
	
	public void waitForClose() throws InterruptedException{
		receiveThread.join();
	}
}
