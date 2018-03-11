package rpc;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rpc.exceptions.RPCException;
import rpc.proxy.ClassTemplate;
import rpc.proxy.ProxyFactory;

public class Registry implements IRegistry {
	private Map<String, Object> localObjects;
	private Map<String, IClassTemplate> remoteObjects;
	private IMessageHandler handler;
	
	private static IRegistry instance = null;
	private static String localPrefix = "Me";
	private static String remotePrefix = "You";
	private static String registryName = "Registry";
	
	Registry(){
		localObjects = new ConcurrentHashMap<String, Object>();
		remoteObjects = new ConcurrentHashMap<String, IClassTemplate>();
		this.registerLocal(registryName, this);
		//A trick
		//Assume there is a remote object whose interface is identical to this one
		this.registerRemote(remotePrefix+registryName, new ClassTemplate(remotePrefix+registryName, this));
		
	}
	
	public void setMessageHandler(IMessageHandler handlerToUse){
		handler = handlerToUse;
	}

	@Override
	public String getLocalPrefix() {
		return localPrefix;
	}

	@Override
	public void registerLocal(String relativeName, Object target) {
			localObjects.put(this.getLocalPrefix()+relativeName, target);
	}

	@Override
	public void registerRemote(String absoluteName, IClassTemplate template) {
		remoteObjects.put(absoluteName, template);
		
	}

	@Override
	public Object find(String target) throws RPCException {
		Object result;
		//If the target is on this JVM, simply return the object reference
		result = localObjects.get(target);
		if(result !=null){
			return result;
		}
		//If the target is on the other JVM, create a dynamic proxy that sends commands
		//across the communication channel.
		result = remoteObjects.get(target);
		if(result == null){
			throw new RPCException("Cannot find the target object: " + target);
		} else {
			return ProxyFactory.createProxy((IClassTemplate)result,this.handler);
		}
	}	



	public static IRegistry getRegistry() {
		if(instance == null){
			instance = new Registry();
		}
		return instance;
	}


	public static void setLocalPrefix(String prefix) {
		Registry.localPrefix = prefix;
		
	}


	@Override
	public void register(String relativeName, Object target) throws RPCException {
		this.registerLocal(relativeName, target);
		IRegistry remote = (IRegistry)this.find(remotePrefix+registryName);
		remote.registerRemote(localPrefix+relativeName, new ClassTemplate(localPrefix+relativeName, target));
		
	}

	public static void setRemotePrefix(String prefix) {
		Registry.remotePrefix = prefix;
		
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Local objects:\n");
		sb.append(localObjects.keySet().toString());
		sb.append("\nRemote objects:\n");
		sb.append(remoteObjects.keySet().toString());
		return sb.toString();
		
	}

}
