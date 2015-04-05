package MTS2;

public class ClusterBrokerEvent1 extends Event {

	private long totalJobs;
	private long numJobsEachTime;
	private long periodTime;
	
	public ClusterBrokerEvent1(long numJobsEachTime, long periodTime, long totalJobs, long timestamp) {
		super(Consts.CLUSTER_BROKER_GENERATE_DYNAMIC_JOBS_UNTIL_TRESHOLD, timestamp);

		this.numJobsEachTime = numJobsEachTime;
		this.periodTime = periodTime;
		this.totalJobs = totalJobs;
	}
	
	public long getTotalJobs() {
		return totalJobs;
	}

	public long getNumJobsEachTime() {
		return numJobsEachTime;
	}

	public long getPeriodTime() {
		return periodTime;
	}

	public void setTotalJobs(long l) {
		this.totalJobs = l;
	}
}
