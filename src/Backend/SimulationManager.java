package Backend;

import org.LiveGraph.LiveGraph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

public class SimulationManager {

	static SimulationManager instance = null;

	private TreeSet<SimulationEvent> eventList; // Sorted data structure
	private long simulationStep;
	private HashMap<Long, BaseNode> nodes;
	private long maxNodeId;
	private boolean simulationFinishedFlag;

	/*
	 * Variables required for parallel execution
	 */
	private ExecutorService executor;
	private List<Future<Void>> currentStepResults;
	private List<ExecWrapper> currentExecWrappers;
	private ConcurrentLinkedQueue<SimulationEvent> tempEventList;

	/*
	 * Variables required for data visualisation
	 */
	private PrintWriter logger;

	private SimulationManager() {
		eventList = new TreeSet<>();
		nodes = new HashMap<>();
		maxNodeId = 0;
		simulationStep = -1;
		simulationFinishedFlag = false;

		if(BackendSettings.PARALLEL_MODE_ENABLED) {
			executor = Executors.newFixedThreadPool(BackendSettings.NUM_THREADS);
			currentExecWrappers = new ArrayList<>(BackendSettings.NUM_THREADS);
			currentStepResults = new ArrayList<>(BackendSettings.NUM_THREADS);
			tempEventList = new ConcurrentLinkedQueue<>();
		}

		if(BackendSettings.VISUALISATION_ENABLED) {
			try {
				logger = new PrintWriter("log.lgd");
				/* write the header for the format documented at
				 * http://www.live-graph.org/ref/dataFile.html
				 */
				logger.println("##,##");
				logger.println("Simulation Step, Message Counter, Node Counter");
				LiveGraph app = LiveGraph.application();
				app.execStandalone(new String[]{
						"-dfs", "settings/livegraph/log.lgdfs",
						"-gs" , "settings/livegraph/log.lggs" ,
						"-dss", "settings/livegraph/log.lgdss"});
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static SimulationManager getInstance() {
		if (instance == null) {
			instance = new SimulationManager();
		}
		return instance;
	}

	public long addNode(BaseNode n){
		nodes.put(maxNodeId, n);
		n.setNodeId(maxNodeId);
		maxNodeId++;
		return maxNodeId-1;
	}

	public void buildNodes(long num_nodes, Class c) {
		try {
			Constructor<?> constructor = c.getConstructor(long.class);

			for (long i = maxNodeId; i < num_nodes + maxNodeId; i++) {
				nodes.put(i, (BaseNode) constructor.newInstance(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		maxNodeId += num_nodes;
	}

	public void buildNode(Class c){
		buildNodes(1, c);
	}

	public void runStep() throws Exception {
		int nodeCounter = 0;
		int messageCounter = 0;

		if(simulationFinishedFlag) {
			return;
		}

		simulationStep = eventList.first().getTimestamp();

		/* Cleanup & initialisation for parallel mode*/
		if(BackendSettings.PARALLEL_MODE_ENABLED) {
			currentExecWrappers.clear();
			currentStepResults.clear();

			for (int i = 0; i < BackendSettings.NUM_THREADS; ++i) {
				currentExecWrappers.add(new ExecWrapper(simulationStep));
			}
		}

		/* Main loop */
		while ((!eventList.isEmpty()) && (eventList.first().getTimestamp() == simulationStep)) {
			messageCounter++; // ToDo: useless at the moment
			SimulationEvent s = eventList.pollFirst();
			nodeCounter++;
			if( BackendSettings.PARALLEL_MODE_ENABLED ){
				currentExecWrappers.get(nodeCounter % BackendSettings.NUM_THREADS).addHostId(s.getNodeId());
			} else {
				getNode(s.getNodeId()).update(simulationStep);
			}
		}

		/* Start the threads */
		if(BackendSettings.PARALLEL_MODE_ENABLED) {
			for (int i = 0; i < BackendSettings.NUM_THREADS; ++i) {
				currentStepResults.add(executor.submit(currentExecWrappers.get(i)));
			}
		}

		/* Write logging data */
		if(BackendSettings.VISUALISATION_ENABLED){
			logger.println(simulationStep + "," + messageCounter + "," + nodeCounter);
			logger.flush();
		}

		if(BackendSettings.PARALLEL_MODE_ENABLED) {
			/* Wait for the threads to finish */
			for(int i = 0; i < BackendSettings.NUM_THREADS; ++i) {
				currentStepResults.get(i).get();
			}
			/* Drain the generated events into the main event list */
			eventList.addAll(tempEventList);
			tempEventList.clear();
		}

		if(eventList.isEmpty()){
			finishSimulation();
		}
	}

	public void finishSimulation(){
		simulationFinishedFlag = true;

		if(BackendSettings.PARALLEL_MODE_ENABLED) {
			executor.shutdown();
		}

		if(BackendSettings.VISUALISATION_ENABLED){
			logger.close();
		}
	}

	public long runContinous() throws Exception {
		long steps = 0;

		while (!simulationFinishedFlag) {
			runStep();
			steps++;
		}
		return steps;
	}

	public boolean getSimulationFinishedFlag(){
		return simulationFinishedFlag;
	}

	private void checkIfValidEvent(long destinationId, BaseMessage message) throws Exception {
		if (message == null) {
			throw new Exception("Null message found");
		}
		if ((destinationId >= maxNodeId) || (destinationId < 0)) {
			throw new Exception("Sent message to unexisting node");
		}
		if (message.getTimestamp() <= simulationStep) {
			throw new Exception("Causality violation! Cannot send message to ts:"+message.getTimestamp()+
								" while in ts:"+simulationStep);
		}
	}

	public void sendMessage (long destinationId, BaseMessage message) throws Exception {
		checkIfValidEvent(destinationId, message);

		getNode(destinationId).push(message);
		if (BackendSettings.PARALLEL_MODE_ENABLED){
			tempEventList.add(new SimulationEvent(destinationId, message.getTimestamp()));
		} else {
			eventList.add(new SimulationEvent(destinationId, message.getTimestamp()));
		}
	}

	public void sendMessageAsync(long destinationId, BaseMessage message) throws Exception {
		checkIfValidEvent(destinationId, message);

		getNode(destinationId).push(message);
		eventList.add(new SimulationEvent(destinationId, message.getTimestamp()));
	}

	/* basic getters*/
	public long getSimulationStep(){
		return simulationStep;
	}

	public HashMap<Long, BaseNode> getNodes() {
		return nodes;
	}

	public BaseNode getNode(long nodeId){
		return nodes.get(nodeId);
	}
}
