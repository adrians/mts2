package Backend;

/**
 * The superclass used for all the messages inside the simulation.
 */
public class BaseMessage {
	/**
	 * The timestamp when the message will be received.
	 */
	private final long timestamp;
	private boolean enqueued;

	public BaseMessage(long timestamp) {
		this.timestamp = timestamp;
		enqueued = false;
	}

	public long getTimestamp() {
		return timestamp;
	}

	final boolean isEnqueued() {
		return enqueued;
	}

	final void setEnqueued(){
		enqueued = true;
	}
}