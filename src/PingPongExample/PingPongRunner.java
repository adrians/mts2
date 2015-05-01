package PingPongExample;

import Backend.SimulationManager;

/**
 * Simple example for using the backend.
 *
 * When a node receives a message, print to the console a text, then reply back with a decremented message.
 * Everything stops when the counter inside the message reaches 0.
 */
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