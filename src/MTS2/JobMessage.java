package MTS2;
import Backend.BaseMessage;

public class JobMessage extends BaseMessage {

	protected Job job;

	public JobMessage(long length, long timestamp){
		super(timestamp);
		job = new Job(length);
	}

	public long getLength() {
		return job.getLength() ;
	}

	public long getStartProcessingTimestamp() {
		return job.getStartProcessingTimestamp();
	}

	public long getFinishProcessingTimestamp() {
		return job.getFinishProcessingTimestamp();
	}
	
	public void setStartProcessingTimestamp(long startProcessingTimestamp) {
		job.setStartProcessingTimestamp(startProcessingTimestamp);
	}

	public void setFinishProcessingTimestamp(long finishProcessingTimestamp) {
		job.setFinishProcessingTimestamp(finishProcessingTimestamp);
	}
}
