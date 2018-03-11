package rpc.proxy;

import java.lang.reflect.Proxy;

import rpc.IClassTemplate;
import rpc.IMessageHandler;

public class ProxyFactory {

	public static Object createProxy(IClassTemplate template, IMessageHandler handler) {
		ProxyTemplate p = new ProxyTemplate(handler);
		return Proxy.newProxyInstance(p.getClass().getClassLoader(), template.getInterfaces(), new SendToRemoteInvocationHandler(template.getTargetObject(),template.getInterfaces(),handler));
		
	}

}
