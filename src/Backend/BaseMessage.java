package Backend;

public class BaseMessage {

	private final long timestamp;

	public BaseMessage(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
}