package Backend;

final class SimulationEvent implements Comparable{

	private final long nodeId;
	private final long timestamp;

	public SimulationEvent(long destinationId, long timestamp){
		this.nodeId = destinationId;
		this.timestamp = timestamp;
	}

	public long getNodeId() {
		return nodeId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int compareTo(Object o) {
		if (o instanceof SimulationEvent) {
			SimulationEvent s = ((SimulationEvent) o);

			if (s.getTimestamp() < this.timestamp ) {
				return 1;
			} else if (s.getTimestamp() > this.timestamp) {
				return -1;
			} else if (s.getNodeId() < this.nodeId){
				return 1;
			} else if (s.getNodeId() > this.nodeId){
				return -1;
			}
			return 0;
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof SimulationEvent){
			SimulationEvent s = (SimulationEvent) obj;
			if( (s.getNodeId() == this.nodeId) && (s.getTimestamp() == this.timestamp)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return new StringBuilder().append("{Timestamp:").append(timestamp).append(",node:").append(nodeId).append("}").toString();
	}
}
