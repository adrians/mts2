package Backend;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * The superclass used for all the entities inside the simulation.
 */
public abstract class BaseNode {
	private long nodeId;

	/**
	 * The queue used for delivering messages.
	 *
	 * Because this may be run inside a parallel environment, the queue should support parallel insertion and serial
	 * extraction.
	 *
	 * To keep the simulation consistent, the extraction must be done in the order of the receive-timestamps.
	 */
	private PriorityBlockingQueue<BaseMessage> inbox;

	public BaseNode(long nodeId) {
		this.nodeId = nodeId;
		this.inbox = new PriorityBlockingQueue<>(2, new BaseMessageComparator());
	}

	/**
	 * This function is called by the SimulationManager to notify the node that it has messages in its inbox that
	 * must be delivered
	 *
	 * @param timestamp The current timestamp. All the messages with the same timestamp will be processed in a call.
	 * @throws Exception
	 */
	public void update(long timestamp) throws Exception {
		BaseMessage msg;

		while ((inbox.peek() != null) && (inbox.peek().getTimestamp() == timestamp)) {
			msg = inbox.poll();
			processMessage(msg);
		}
	}

	/**
	 * The function where a single message is processed. Must be implemented in subclasses.
	 *
	 * @param msg The received message
	 * @throws Exception
	 */
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