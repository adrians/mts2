package MTS2;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * jobs with no preemption scheduled by minimum job length
 * @author raluca
 *
 */
public class SJFscheduler implements Scheduler {

	private PriorityQueue<Job> tasks;

	public SJFscheduler() {
		this.init();
	}

	@Override
	public void init() {
		this.tasks = new PriorityQueue<Job>(1, new Comparator<Job>()
				{
			/**
			 * Sort jobs by length
			 * Extend this class for different comparisons
			 */
			@Override
			public int compare(Job o1, Job o2) {
				if (o1.getLength() < o2.getLength())
				{
					return 1;
				}
				else if (o1.getLength() > o2.getLength())
				{
					return -1;
				}

				return 0;
			}
		});
	}

	@Override
	public void schedule(Job job) {
		//by default, it can be scheduled
		tasks.add(job);
	}

	@Override
	public Job processAt(long processingTimestamp) {
		if (tasks.isEmpty()) {
			return null;
		}
		
		//process a task
		Job currentJob = tasks.remove();
		currentJob.setStartProcessingTimestamp(processingTimestamp);

		System.out.println("Processed job with length " + currentJob.getLength() + "at timestamp: " + processingTimestamp);
		currentJob.setFinishProcessingTimestamp(processingTimestamp + currentJob.getLength());

		//by default, no error when processing a task
		return currentJob;
	}

	@Override
	public long getLoading() {
		return this.tasks.size();
	}

	@Override
	public Job deleteCurrentJob() {
		if (tasks.isEmpty()) {
			return null;
		}

		return tasks.remove();
	}
}
