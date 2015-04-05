package MTS2;
import Backend.BaseMessage;

public class Event extends BaseMessage {
	private int type;

	public Event(int type, long timestamp) {
		super(timestamp);
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
