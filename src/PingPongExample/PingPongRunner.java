package PingPongExample;

import Backend.SimulationManager;

class PingPongRunner {

	public static void main(String[] args) {
		SimulationManager simulation = SimulationManager.getInstance();
		simulation.buildNodes(2, PingPongNode.class);
		try {
			simulation.sendMessage(0, new PingPongMessage(1, 20));
			simulation.runContinous();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}