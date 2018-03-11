package rpc;
import java.io.Serializable;

public interface IClassTemplate extends Serializable{
	
	/**
	 * The full/absolute name of the object being described
	 * @return absolute name, Ex. Registry object on Server is ServerRegistry
	 */
	public String getTargetObject();
	
	
	/**
	 * The interface to the object
	 * @return array of class objects
	 */
	public Class<?>[] getInterfaces();
}
