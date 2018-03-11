package rpc;
import java.io.Serializable;

public interface ICommand extends Serializable,IMessage {
	
	/**
	 * Get target method to invoke
	 * @return method name to invoke
	 */
	public String getInvocationTarget();
	
	/**
	 * Assign a unique reference number to use in matching command/response pairs
	 * @param number unique tracking number
	 */
	public void setCommanderNumber(long number);
	
	/**
	 * Get the arguments to be passed to the target during invocation
	 * @return array of arguments
	 */
	public Object[] getArguments();
}
