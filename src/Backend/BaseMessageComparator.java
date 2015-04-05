package Backend;

import java.util.Comparator;

/*
 * Comparator used when extracting messages from the inbox queue, in the BaseNode class.
 * This allows BaseMessages to have other orderings, imposed by the user of the library,
 * while maintaining the correctness of the simulation.
 */

final class BaseMessageComparator implements Comparator<BaseMessage> {
	@Override
	public int compare(BaseMessage msg1, BaseMessage msg2) {
		if (msg1.getTimestamp() < msg2.getTimestamp()){
			return -1;
		} else if(msg1.getTimestamp() > msg2.getTimestamp()){
			return 1;
		} else {
			return 0;
		}
	}
}
