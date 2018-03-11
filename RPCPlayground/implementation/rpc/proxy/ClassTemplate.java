package rpc.proxy;

import rpc.IClassTemplate;

public class ClassTemplate implements IClassTemplate{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8929814743666387812L;
	
	String responder;
	Class<?>[] interfaces;
	
	public String getTargetObject(){
		return responder;
	}
	
	public Class<?>[] getInterfaces() {
		return interfaces;
	}

	public ClassTemplate(String targetObjectName, Object responderObject) {
		responder = targetObjectName;
		interfaces = responderObject.getClass().getInterfaces();
	}
	
	public String toString(){
		return "Template: " + responder;
	}
}
