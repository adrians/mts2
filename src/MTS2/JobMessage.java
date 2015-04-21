package MTS2;
import Backend.BaseMessage;

public class JobMessage extends BaseMessage {

	protected Job job;

	public JobMessage(long length, long timestamp){
		super(timestamp);
		job = new Job(length, timestamp);
	}

	public Job getJob(){
		return job;
	}
}
