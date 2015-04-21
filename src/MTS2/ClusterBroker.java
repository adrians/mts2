package MTS2;
import Backend.BaseMessage;
import Backend.BaseNode;
import Backend.SimulationManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public abstract class ClusterBroker extends BaseNode {

	protected Class<?> jobType;
	protected SimulationManager simulationManager;
	protected Distribution distribution;

	public ClusterBroker(long brokerId, Class<?> jobType) {
		super(brokerId);

		this.jobType = jobType;
		this.distribution = new UniformDistribution();
		this.simulationManager = SimulationManager.getInstance();
	}

	public abstract long getDestinationNodeId();

	@Override
	public void processMessage(BaseMessage msg)
	{	
		if (msg instanceof ClusterBrokerEvent0) {
			ClusterBrokerEvent0 currentEvent = (ClusterBrokerEvent0)msg;
			processEvent0(currentEvent);
		} else if (msg instanceof ClusterBrokerEvent1) {
			ClusterBrokerEvent1 currentEvent = (ClusterBrokerEvent1)msg;
			processEvent1(currentEvent);
		}
	}

	protected void processEvent0(ClusterBrokerEvent0 event) {
		ClusterBrokerEvent0 nextEvent;

		if (event.getTimestamp() >= event.getTotalTime()) {
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

	protected void processEvent1(ClusterBrokerEvent1 event) {
		ClusterBrokerEvent1 nextEvent;
		
		if (event.getTotalJobs() <= 0) {
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

	/**
	 * 
	 * @param numJobsEachTime number of jobs generated once at each \periodTime units 
	 * @param periodTime time interval between each generation of jobs
	 * @param totalTime
	 */
	public void generateDynamicJobs(long numJobsEachTime, long periodTime, long totalTime)
	{
		try {
			this.simulationManager.sendMessage(this.getNodeId(),
					new ClusterBrokerEvent0(numJobsEachTime, periodTime, totalTime, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param numJobsEachTime number of jobs generated once at each \periodTime units 
	 * @param periodTime time interval between each generation of jobs
	 * @param totalJobs
	 */
	public void generateDynamicJobsUntilThreshold(long numJobsEachTime, long periodTime, long totalJobs)
	{
		try {
			this.simulationManager.sendMessage(this.getNodeId(),
					new ClusterBrokerEvent1(numJobsEachTime, periodTime, totalJobs, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void distributeJobs(long totalJobs, long timestamp) {
		BaseNode currentNode;

		for (long i = 0; i < totalJobs; i++) {
			currentNode = getNextNode();
			if (currentNode == null) {
				continue;
			}

			JobMessage currentJob = this.makeJob(timestamp);
			try {
				this.simulationManager.sendMessage(this.getDestinationNodeId(), currentJob);

						System.out.println("Generated task of length " + currentJob.getJob().getLength() +
								" for timestamp " + currentJob.getTimestamp());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private BaseNode getNextNode() {
		BaseNode node;

		while(true) {
			node = this.simulationManager.getNode(this.getDestinationNodeId());

			//don't send a job to any other existing brokers
			if (!(node instanceof ClusterBroker)) {
				break;
			}
		}

		return node;
	}

	private JobMessage makeJob(long timestamp) {
		JobMessage currentJob = null;
		long length = this.getDistribution().getJobLength();

		try {
			Constructor<?> constructor = this.jobType.getConstructor(long.class, long.class);
			currentJob = (JobMessage) constructor.newInstance(length, timestamp);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return currentJob;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public void changeDistribution(Distribution distribution) {
		this.distribution = distribution;
	}
}