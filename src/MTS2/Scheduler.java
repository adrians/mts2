package MTS2;

public interface Scheduler {

	void init();
	void schedule(JobMessage job);
	JobMessage processAt(long processingTimestamp);
	JobMessage deleteCurrentJob();
	long getLoading();
}
