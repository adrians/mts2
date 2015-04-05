package PingPongExample;

import Backend.BaseMessage;

final class PingPongMessage extends BaseMessage {

	private final int payload;

	public PingPongMessage(long timestamp, int payload){
		super(timestamp);
		this.payload = payload;
	}

	public int getPayload() {
		return payload;
	}
}