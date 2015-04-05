package MTS2;

public class ClusterBrokerEvent0 extends Event {

	private long totalTime;
	private long numJobsEachTime;
	private long periodTime;

	public ClusterBrokerEvent0(long numJobsEachTime, long periodTime, long totalTime, long timestamp) {
		super(Consts.CLUSTER_BROKER_GENERATE_DYNAMIC_JOBS, timestamp);

		this.numJobsEachTime = numJobsEachTime;
		this.periodTime = periodTime;
		this.totalTime = totalTime;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public long getNumJobsEachTime() {
		return numJobsEachTime;
	}

	public long getPeriodTime() {
		return periodTime;
	}
}
