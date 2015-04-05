package MTS2;

import java.util.Collection;
import java.util.HashMap;

public class DHToverlay implements Overlay {

	private HashMap<Integer, Long> neighbours;
	private int nextId;
	private int totalNodes;
	
	public DHToverlay(long nodeId, int totalNodes) {
		this.neighbours = new HashMap<Integer, Long>(1);
		this.totalNodes = totalNodes;
		this.nextId = 0;
		this.computeNeighbours(nodeId, totalNodes);
	}
	
	@Override
	public long getNextNeighbour() {
		this.nextId = (this.nextId + 1) % this.neighbours.size();
		return this.neighbours.get(this.nextId);
	}

	@Override
	public Collection<Long> getAllNeighbours() {
		return neighbours.values();
	}

	@Override
	public void computeNeighbours(long nodeId, int totalNodes) {
		int maxNeighbours = (int) (Math.ceil(Math.log10(this.totalNodes) / Math.log10(2)));
		double maxNodes = Math.pow(2, maxNeighbours); //on the chord ring

		for (int i = 1; i <= maxNeighbours; i++) {
			long neighborIndex = (long) ((nodeId + Math.pow(2, i - 1) % maxNodes) % maxNodes); 
			
			this.neighbours.put(this.nextId, neighborIndex);
			this.nextId++;
		}

		//make getNextNeighbour() return correctly
		this.nextId = 0;
	}

	@Override
	public int getTotalNodes() {
		return this.totalNodes;
	}
}
