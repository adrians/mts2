package Backend;

import java.util.concurrent.PriorityBlockingQueue;

public abstract class BaseNode {
	private long nodeId;

	/* the Queue should support parallel insertion and serial extraction,
	 * with extraction in the order of the receive-timestamps
	 */
	private PriorityBlockingQueue<BaseMessage> inbox;

	public BaseNode(long nodeId) {
		this.nodeId = nodeId;
		this.inbox = new PriorityBlockingQueue<>(2, new BaseMessageComparator());
	}

	public void update(long timestamp) throws Exception {
		BaseMessage msg;

		while ((inbox.peek() != null) && (inbox.peek().getTimestamp() == timestamp)) {
			msg = inbox.poll();
			processMessage(msg);
		}
	}

	abstract public void processMessage(BaseMessage msg) throws Exception;

	public void push(BaseMessage msg) {
		inbox.put(msg); /* should accept concurrent adding! */
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public long getNodeId() {
		return this.nodeId;
	}
}