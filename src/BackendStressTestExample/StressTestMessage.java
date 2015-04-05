package BackendStressTestExample;

import Backend.BaseMessage;

final class StressTestMessage extends BaseMessage {

	private final long payload;

	public StressTestMessage(long timestamp, long payload){
		super(timestamp);
		this.payload = payload;
	}

	public long getPayload() {
		return payload;
	}
}