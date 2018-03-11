package rpc;

import java.io.Serializable;

public class Response implements IResponse {

	long commandNumber = 0;
	Serializable reply = null;
	String responder;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5112504060313390537L;

	public Response(ICommand incoming, Serializable replyToSend) {
		commandNumber = incoming.getCommanderNumber();
		reply = replyToSend;
		responder = incoming.getInvocationTarget();
	}

	@Override
	public long getCommanderNumber() {
		return commandNumber;
	}

	@Override
	public String getResponder() {
		return responder;
	}

	@Override
	public Object getReply() {
		return reply;
	}

}
