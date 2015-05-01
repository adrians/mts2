package MTS2;
import Backend.BaseMessage;
import Backend.BaseNode;

public class Node extends BaseNode {

	protected Scheduler scheduler;

	public Node(long nodeId) {
		super(nodeId);
		this.scheduler = new FCFSscheduler();
	}
	
	public Node(long nodeId, Scheduler scheduler) {
		super(nodeId);
		this.scheduler = scheduler;
	}

	@Override
	public void processMessage(BaseMessage msg) {
		if (msg instanceof JobMessage) {
			JobMessage currentJob = (JobMessage)msg;
			schedule(currentJob.getJob(), msg.getTimestamp());
		} else if (msg instanceof Event) {
			Event currentEvent = (Event)msg;
			processEvent(currentEvent, msg.getTimestamp());
		}
	}

	public void processEvent(Event currentEvent, long timestamp) {
		System.out.println("Node " + getNodeId() + " woke up at event timestamp = " + timestamp);
	}

	public void schedule(Job job, long timestamp) {
		scheduler.schedule(job);
		scheduler.processAt(timestamp);
	}
}