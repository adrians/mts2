package Backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

class ExecWrapper implements Callable<Void> {
	private List<Long> hostIds;
	private long simulationStep;

	public ExecWrapper(long simulationStep){
		this.simulationStep = simulationStep;
		hostIds = new ArrayList<>();
	}

	public void addHostId(long l){
		hostIds.add(l);
	}

	@Override
	public Void call() throws Exception {
		for(Long i: hostIds) {
			SimulationManager.getInstance().getNode(i).update(simulationStep);
		}
		return null;
	}
}
