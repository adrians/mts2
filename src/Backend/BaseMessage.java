package Backend;

/**
 * The superclass used for all the messages inside the simulation.
 */
public class BaseMessage {
	/**
	 * The timestamp when the message will be received.
	 */
	private final long timestamp;

	public BaseMessage(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
}