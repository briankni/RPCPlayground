package rpc;
import java.io.Serializable;

public interface IResponse extends Serializable, IMessage {
	/**
	 * Get the return value from a remote call
	 * @return returned deserialized object, or null
	 */
	public Object getReply();
}
