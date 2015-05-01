package PingPongExample;

import Backend.BaseMessage;
import Backend.BaseNode;
import Backend.SimulationManager;

/*
 * Node classes should be public because the reflection framework requires the constructors to be publicly visible.
 */
final public class PingPongNode extends BaseNode {

	public PingPongNode(long nodeId) {
		super(nodeId);
	}

	@Override
	public void processMessage(BaseMessage msg) throws Exception {
		PingPongMessage pingMsg;
		if (msg instanceof PingPongMessage) {
			pingMsg = (PingPongMessage) msg;
		} else {
			return;
		}
		if (getNodeId() == 0) {
			System.out.println("PING " + pingMsg.getPayload());
			if (pingMsg.getPayload() - 1 > 0) {
				SimulationManager.getInstance().sendMessage(1, new PingPongMessage(pingMsg.getTimestamp() + 1, pingMsg.getPayload() - 1));
			}
		} else if (getNodeId() == 1) {
			System.out.println("PONG " + pingMsg.getPayload());
			if (pingMsg.getPayload() - 1 > 0) {
				SimulationManager.getInstance().sendMessage(0, new PingPongMessage(pingMsg.getTimestamp() + 1, pingMsg.getPayload() - 1));
			}
		}
	}
}