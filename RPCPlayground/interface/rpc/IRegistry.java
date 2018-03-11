package rpc;

import rpc.exceptions.RPCException;

public interface IRegistry {
	public String getLocalPrefix();
	
	/**
	 * Add an object to only the local list of registered objects
	 * @param relativeName the local prefix will be prepended
	 * @param target reference to the object
	 */
	public void registerLocal(String relativeName, Object target);
	
	/**
	 * Add an object to only the list of registered remote objects
	 * @param absoluteName full name by which local and remote refer to the object
	 * @param template
	 */
	public void registerRemote(String absoluteName, IClassTemplate template);
	
	/**
	 * Obtain a reference to a named object
	 * @param target full name including prefix
	 * @return reference to the object
	 * @throws RPCException
	 */
	public Object find(String target) throws RPCException;
	
	/**
	 * Add an object to local and remote registries
	 * @param relativeName prefix will be prepended
	 * @param target target object
	 * @throws RPCException
	 */
	public void register(String relativeName, Object target) throws RPCException;
	
	/**
	 * Message handler used to broker messages
	 * @param handlerToUse
	 */
	public void setMessageHandler(IMessageHandler handlerToUse);
	
}
