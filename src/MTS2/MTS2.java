package MTS2;
import Backend.SimulationManager;

public class MTS2 {

	public static void main(String[] args){
		long totalNodes = 2;
		long totalJobs = 100;

		SimulationManager simulation = SimulationManager.getInstance();
		simulation.buildNodes(totalNodes, Node.class);

		long destinationNodeId = 0;
		long brokerId = totalNodes;
		SimpleBroker broker = new SimpleBroker(brokerId, JobMessage.class, destinationNodeId);
		broker.changeDistribution(new ExponentialDistribution());
		long nid = simulation.addNode(broker);

		assert(nid == totalNodes);
		
		long currentNodeId = 1;
		brokerId = nid + 1;
		RoundRobinBroker broker2 = new RoundRobinBroker(brokerId, totalNodes, JobMessage.class, currentNodeId);
		simulation.addNode(broker2);

		broker.distributeJobs(totalJobs, 0);
		broker2.distributeJobs(totalJobs, 400);
		broker.distributeJobs(totalJobs, 500);
		
		broker.changeDistribution(new UniformDistribution());
		broker.distributeJobs(totalJobs, 550);

		broker.generateDynamicJobs(3, 5, 10000);
		broker2.generateDynamicJobsUntilThreshold(4, 4, 10000);

		simulation.runContinous();
	}
}