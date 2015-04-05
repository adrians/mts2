package BackendStressTestExample;

import Backend.SimulationManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class StressTestRunner {
	private static final int NUM_NODES = 1_000_000;
	private static final long NUM_MESSAGSES_PER_NODE = 100;

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) {
		SimulationManager simulation = SimulationManager.getInstance();
		simulation.buildNodes(NUM_NODES, StressTestNode.class);
		try {
			for(int i = 0; i < NUM_NODES; ++i) {
				simulation.sendMessageAsync(i, new StressTestMessage(1, NUM_MESSAGSES_PER_NODE));
			}
			Date date1 = new java.util.Date();
			System.out.println(new Timestamp(date1.getTime()));

			simulation.runContinous();

			Date date2 = new java.util.Date();
			System.out.println(new Timestamp(date2.getTime()));
			System.out.println("Difference: "+ getDateDiff(date1, date2, TimeUnit.MILLISECONDS));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
