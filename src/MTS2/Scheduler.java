package MTS2;

public interface Scheduler {

	void init();
	void schedule(Job job);
	Job processAt(long processingTimestamp);
	Job deleteCurrentJob();
	long getLoading();
}
