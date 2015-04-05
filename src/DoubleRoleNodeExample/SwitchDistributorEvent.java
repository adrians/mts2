package DoubleRoleNodeExample;

import MTS2.Event;

public class SwitchDistributorEvent extends Event {

	protected long nodeId;

	public SwitchDistributorEvent(int type, long timestamp, long nodeId) {
		super(type, timestamp);
		this.nodeId = nodeId;
	}
	
	public long getNodeId() {
		return nodeId;
	}
}
