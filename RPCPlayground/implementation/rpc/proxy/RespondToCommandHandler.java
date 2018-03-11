package rpc.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

import rpc.ICommand;
import rpc.IMessageHandler;
import rpc.IResponse;
import rpc.Registry;
import rpc.Response;
import rpc.exceptions.RPCException;


public class RespondToCommandHandler  {
	
	Thread executionThread;
	
	public RespondToCommandHandler(IMessageHandler messageHandler, ICommand incoming) {
		
		executionThread = new Thread(new Runnable(){

			@Override
			public void run() {
				Serializable returnValue = RespondToCommandHandler.this.invoke(incoming);
				try {
					RespondToCommandHandler.this.respond(messageHandler, incoming, returnValue);
				} catch (Exception e) {
					executionThread.getUncaughtExceptionHandler().uncaughtException(executionThread, e);
				}			
				
			}});
		executionThread.start();
	}

	private void respond(IMessageHandler handlerToUse, ICommand command, Serializable returnValue) throws Exception {
		IResponse response = new Response(command, returnValue);
		handlerToUse.send(response);	
		
	}
	
	private Serializable invoke(ICommand command) {
		try{
			Object targetObject = Registry.getRegistry().find(command.getResponder());
			Object[] args = command.getArguments();
			Method[] allMethods = targetObject.getClass().getDeclaredMethods();
		    for (Method m : allMethods) {
				String mname = m.getName();
				if (!mname.equals(command.getInvocationTarget())){
				    continue;
				}
		 		Class<?>[] pType = m.getParameterTypes();
		 		if(pType.length != args.length){
		 			continue;
		 		}
		 		boolean match = true;
		 		//Check all arguments for compatibility
		 		for(int i=0;i<pType.length;i++){
		 			if(!pType[i].isAssignableFrom(args[i].getClass())){
		 				match = false;
		 				break;
		 			}
		 		}
		 		if(match){
		 			return (Serializable) m.invoke(targetObject,args);
		 		} 

		    }
		    throw new RPCException("No matching method found for " + command +".  This may be caused by mixing primitives and boxed values such as int vs. Integer.");
			
		}catch(Exception E){
			return E;
		}
			
	}

}
