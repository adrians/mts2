package MTS2;
import Backend.BaseMessage;

public class Job extends BaseMessage {

	protected long length;
	protected long startProcessingTimestamp;
	protected long finishProcessingTimestamp;

	public Job(long length, long timestamp){
		super(timestamp);
		this.length = length;
	}

	public long getLength() {
		return length;
	}

	public long getStartProcessingTimestamp() {
		return startProcessingTimestamp;
	}

	public long getFinishProcessingTimestamp() {
		return finishProcessingTimestamp;
	}
	
	public void setStartProcessingTimestamp(long startProcessingTimestamp) {
		this.startProcessingTimestamp = startProcessingTimestamp;
	}

	public void setFinishProcessingTimestamp(long finishProcessingTimestamp) {
		this.finishProcessingTimestamp = finishProcessingTimestamp;
	}
}
