package DoubleRoleNodeExample;

import Backend.BaseMessage;
import MTS2.ClusterBrokerEvent0;
import MTS2.ClusterBrokerEvent1;
import MTS2.Event;
import MTS2.SimpleBroker;

public class SimpleBrokerCommunicator extends SimpleBroker {

	public SimpleBrokerCommunicator(long brokerId, Class<?> jobType,
			long destinationNodeId) {
		super(brokerId, jobType, destinationNodeId);
	}

	@Override
	public void processMessage(BaseMessage msg)
	{	
		if (msg instanceof ClusterBrokerEvent0) {
			ClusterBrokerEvent0 currentEvent = (ClusterBrokerEvent0)msg;
			processEvent0(currentEvent);
		} else if (msg instanceof ClusterBrokerEvent1) {
			ClusterBrokerEvent1 currentEvent = (ClusterBrokerEvent1)msg;
			processEvent1(currentEvent);
		} else if (msg instanceof SwitchDistributorEvent) {
			SwitchDistributorEvent currentEvent = (SwitchDistributorEvent)msg;
			this.destinationNodeId = currentEvent.getNodeId();
		}
	}
	
	@Override
	protected void processEvent0(ClusterBrokerEvent0 event) {
		ClusterBrokerEvent0 nextEvent;

		if (event.getTimestamp() >= event.getTotalTime()) {
			//send finish message to the destination node
			Event finishEvent = new Event(DoubleRoleNode.FINISH_SIMULATION,
					this.simulationManager.getSimulationStep() + 1);
			try {
				this.simulationManager.sendMessage(this.getDestinationNodeId(), finishEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return;
		}

		long nextTimestamp = event.getTimestamp() + event.getPeriodTime();

		nextEvent = new ClusterBrokerEvent0(event.getNumJobsEachTime(),
				event.getPeriodTime(), event.getTotalTime(), nextTimestamp);

		this.distributeJobs(event.getNumJobsEachTime(), event.getTimestamp() + 1);

		try {
			this.simulationManager.sendMessage(this.getNodeId(), nextEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void processEvent1(ClusterBrokerEvent1 event) {
		ClusterBrokerEvent1 nextEvent;
		
		if (event.getTotalJobs() <= 0) {
			//send finish message to the destination node
			Event finishEvent = new Event(DoubleRoleNode.FINISH_SIMULATION,
					this.simulationManager.getSimulationStep() + 1);
			try {
				this.simulationManager.sendMessage(this.getDestinationNodeId(), finishEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		long remainingJobs = event.getTotalJobs() - event.getNumJobsEachTime();
		long nextTimestamp = event.getTimestamp() + event.getPeriodTime();

		nextEvent = new ClusterBrokerEvent1(event.getNumJobsEachTime(),
				event.getPeriodTime(), remainingJobs, nextTimestamp);

		this.distributeJobs(event.getNumJobsEachTime(), event.getTimestamp() + 1);

		try {
			this.simulationManager.sendMessage(this.getNodeId(), nextEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
