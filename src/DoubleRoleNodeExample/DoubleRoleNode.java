package DoubleRoleNodeExample;

import Backend.BaseMessage;
import Backend.SimulationManager;
import MTS2.*;

import java.util.Collection;

public class DoubleRoleNode extends Node {

	public static final long MAX_LIMIT = 10000;
	public static final int FINISH_SIMULATION = 0;
	public static final int SWITCH_DISTRIBUTOR = 3;
	public static final int FINISHED_TASK = -1;
	public static final long THRESHOLD = 1000;

	private int loadStatus;
	private long brokerId;
	private boolean distributorRole;
	private DHToverlay overlay;
	private boolean finishSimulation;
	private long maxLoading;
	private long minLoading;

	public DoubleRoleNode(long nodeId, long brokerId, long totalNodes, boolean role) {
		super(nodeId);

		this.loadStatus = 0;
		this.brokerId = brokerId;
		this.overlay = new DHToverlay(brokerId, (int)totalNodes);
		this.distributorRole = role;
		this.finishSimulation = false;
	}

	public DoubleRoleNode(long nodeId, long brokerId, long totalNodes, Scheduler scheduler, boolean role) {
		super(nodeId, scheduler);

		this.loadStatus = 0;
		this.brokerId = brokerId;
		this.overlay = new DHToverlay(brokerId, (int)totalNodes);
		this.distributorRole = role;
		this.finishSimulation = false;
	}

	@Override
	public void processMessage(BaseMessage msg)
	{
		if (msg instanceof Event) {
			Event currentEvent = (Event)msg;

			if (currentEvent.getType() == FINISH_SIMULATION) {
				this.setFinishSimulation(true);
			} else if (currentEvent.getType() == SWITCH_DISTRIBUTOR) {
				distributorRole = true;
			} else if (currentEvent.getType() == FINISHED_TASK){
				loadStatus--;
			}
		} else if (msg instanceof JobMessage) {
			JobMessage currentJobMsg = (JobMessage)msg;
			scheduler.schedule(currentJobMsg.getJob());
		} else {
			return;
		}

		if (finishProcessing()) {
			return;
		}

		if (this.getLoading() > MAX_LIMIT) {
			long neighbourId = this.getMinLoadingFromNeighbours();

			if (this.getLoading() - this.minLoading > THRESHOLD) {
				if (distributorRole) {
					SwitchDistributorEvent switchDistributor = new SwitchDistributorEvent(SWITCH_DISTRIBUTOR,
							SimulationManager.getInstance().getSimulationStep() + 1, neighbourId);

					try{
						SimulationManager.getInstance().sendMessage(this.brokerId, switchDistributor);

						distributorRole = false;

						Event switchDistributor2 = new Event(SWITCH_DISTRIBUTOR,
								SimulationManager.getInstance().getSimulationStep() + 2);

						SimulationManager.getInstance().sendMessage(neighbourId, switchDistributor2);

						if (this.finishSimulation) {
							Event finishMsg = new Event(FINISH_SIMULATION,
									SimulationManager.getInstance().getSimulationStep() + 3);

							SimulationManager.getInstance().sendMessage(neighbourId, finishMsg);

							//cancel the finish message
							this.setFinishSimulation(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Job currentJob = null;

					while ((currentJob = this.scheduler.deleteCurrentJob()) != null) {
						JobMessage job = new JobMessage(currentJob.getLength(),
								SimulationManager.getInstance().getSimulationStep() + 1);
						
						try {
							SimulationManager.getInstance().sendMessage(neighbourId, job);
						} catch (Exception e) {
							e.printStackTrace();
						}

						neighbourId = getMinLoadingFromNeighbours();
					}
				}

				return;
			}
		}

		if (distributorRole) {
			long neighbourId = this.overlay.getNextNeighbour();
			//the distributorRole node only stores jobs and sends them forward to the worker nodes
			Job currentJob = this.scheduler.deleteCurrentJob();
			JobMessage job = new JobMessage(currentJob.getLength(), currentJob.getGeneratedTimestamp() + 1);

			try {
				SimulationManager.getInstance().sendMessage(neighbourId, job);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (loadStatus == 0 && this.getLoading() > 0) {
				Job job = scheduler.processAt(msg.getTimestamp());
				Event wakeup = new Event(FINISHED_TASK, job.getFinishProcessingTimestamp());
				try {
					SimulationManager.getInstance().sendMessage(getNodeId(), wakeup);
					loadStatus++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean finishProcessing() {
		if (this.finishSimulation) {
			//simularea trebuie sa se termine
			if (this.getLoading() == 0) {
				if (distributorRole) {
					//trimite catre restul nodurilor mesaj de finish
					for (long neighbourId = 0; neighbourId < this.overlay.getTotalNodes(); neighbourId++) {
						if (neighbourId == this.getNodeId())
							continue;

						Event finishMsg = new Event(FINISH_SIMULATION,
								SimulationManager.getInstance().getSimulationStep() + 1);

						try {
							SimulationManager.getInstance().sendMessage(neighbourId, finishMsg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					return true;
				} else {
					this.getMaxLoadingFromNeighbours();
					if (this.maxLoading == 0) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private Long getMinLoadingFromNeighbours() {
		this.minLoading = Long.MAX_VALUE;
		long result = 0;

		Collection<Long> neighbours = overlay.getAllNeighbours();
		for(Long neighbourId : neighbours) {
			long loading = ((DoubleRoleNode)SimulationManager.getInstance().getNode(neighbourId)).getLoading();

			if (loading < this.minLoading) {
				this.minLoading = loading;
				result = neighbourId;
			}
		}

		return result;
	}

	private Long getMaxLoadingFromNeighbours() {
		this.maxLoading = 0;
		long result = 0;

		Collection<Long> neighbours = overlay.getAllNeighbours();
		for(Long neighbourId : neighbours){
			long loading = ((DoubleRoleNode)SimulationManager.getInstance().getNode(neighbourId)).getLoading();

			if (loading > this.maxLoading) {
				this.maxLoading = loading;
				result = neighbourId;
			}
		}

		return result;
	}

	private void setFinishSimulation(boolean value) {
		finishSimulation = value;
	}

	private long getLoading() {
		return scheduler.getLoading();
	}
}
