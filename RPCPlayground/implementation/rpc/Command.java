package rpc;



public class Command implements ICommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2698739092222820465L;
	String method;
	String targetObjectName;
	Object[] argumentArray;
	Long commandNumber = 0L;
	
	/**
	 * Create a new command to send to a remote
	 * @param targetName name of the target object
	 * @param methodToInvoke target object method to invoke
	 * @param arguments array of arguments to pass
	 */
	public Command( String targetName,String methodToInvoke, Object[] arguments){
		this.
		targetObjectName = targetName;
		method = methodToInvoke;
		argumentArray = arguments;
	}



	@Override
	public Object[] getArguments() {
		return argumentArray;
	}


	@Override
	public String getResponder() {
		return targetObjectName;
	}


	@Override
	public void setCommanderNumber(long number) {
		commandNumber =number;
		
	}


	@Override
	public long getCommanderNumber() {
		return commandNumber;
	}


	@Override
	public String getInvocationTarget() {
		return method;
	}

	public String toString(){
		return "Command " + this.targetObjectName + " to " + this.method;
	}
}
