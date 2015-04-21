package DoubleRoleNodeExample;

import Backend.SimulationManager;
import MTS2.ExponentialDistribution;
import MTS2.JobMessage;

public class MTCLoadBalancingAlgorithm {
	
	public static void main(String args[]) {
		long totalNodes = 16;
		long totalJobs = 100000;
		long distributorNodeId = 0;
		long brokerId = totalNodes;

		SimulationManager simulation = SimulationManager.getInstance();
		
		//add distributor
		DoubleRoleNode distributor = new DoubleRoleNode(0, brokerId, totalNodes, true);
		long distributorId = simulation.addNode(distributor);

	//	simulation.buildNodes(5, Node.class);
		
		//add workers
		for (int i = 1; i < totalNodes; i++) {
			DoubleRoleNode worker = new DoubleRoleNode(i, brokerId, totalNodes, false);
			simulation.addNode(worker);
		}

		//add broker that communicates with the distributor
		SimpleBrokerCommunicator broker = new SimpleBrokerCommunicator(brokerId,
				JobMessage.class, distributorNodeId);
		broker.changeDistribution(new ExponentialDistribution());
		long nid = simulation.addNode(broker);

		assert(nid == totalNodes);
		
		broker.generateDynamicJobsUntilThreshold(10000, 1, totalJobs);
		
		//start
		simulation.runContinous();
	}
}
