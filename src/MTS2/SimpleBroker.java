package MTS2;

public class SimpleBroker extends ClusterBroker {

	protected long destinationNodeId;
	
	public SimpleBroker(long brokerId, Class<?> jobType, long destinationNodeId) {
		super(brokerId, jobType);
		this.destinationNodeId = destinationNodeId;
	}

	@Override
	public long getDestinationNodeId() {
		return this.destinationNodeId;
	}
}
