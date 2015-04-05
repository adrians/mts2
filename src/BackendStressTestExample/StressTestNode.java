package BackendStressTestExample;

import Backend.BaseMessage;
import Backend.BaseNode;
import Backend.SimulationManager;

import java.util.Random;

final public class StressTestNode extends BaseNode {
	static private final boolean ENABLE_DELAY = false;
	static private final int DELAY_MILLIS = 1;
	private Random rng;

	public StressTestNode(long nodeId) {
		super(nodeId);
		rng = new Random(nodeId);
	}

	@Override
	public void processMessage(BaseMessage msg) {
		StressTestMessage stressMsg;
		if (msg instanceof StressTestMessage) {
			stressMsg = (StressTestMessage) msg;
		} else {
			return;
		}
		try {
			if (stressMsg.getPayload() > 0) {
				if( ENABLE_DELAY ){
					Thread.sleep(DELAY_MILLIS);
				}
				SimulationManager.getInstance().sendMessage(getNodeId(), new StressTestMessage(stressMsg.getTimestamp() + rng.nextInt(40) + 1, stressMsg.getPayload() - 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}