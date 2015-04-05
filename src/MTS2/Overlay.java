package MTS2;

import java.util.Collection;

public interface Overlay {
	void computeNeighbours(long nodeId, int totalNodes);
	long getNextNeighbour();
	Collection<Long> getAllNeighbours();
	int getTotalNodes();
}
