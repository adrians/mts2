package MTS2;

public class RoundRobinBroker extends ClusterBroker {
	
	protected static long currentNodeId;
	protected final long totalNodes;

	public RoundRobinBroker(long brokerId, long totalNodes, Class<?> jobType, long firstNodeId) {
		super(brokerId, jobType);
		RoundRobinBroker.currentNodeId = firstNodeId - 1;
		this.totalNodes = totalNodes;
	}

	@Override
	public long getDestinationNodeId() {
		RoundRobinBroker.currentNodeId = (RoundRobinBroker.currentNodeId + 1) % this.getTotalNodes();
		return RoundRobinBroker.currentNodeId;
	}
	
	public long getTotalNodes() {
		return totalNodes;
	}
}
