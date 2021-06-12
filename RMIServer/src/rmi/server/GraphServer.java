package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmi.registery.GraphService;

public class GraphServer implements GraphService {

	private Graph localGraph;
	private static AppLogger logger = new AppLogger();
	private ArrayList<Request> requests;

	public GraphServer() {
		super();
		localGraph = new Graph("local_graph.txt");
		requests = new ArrayList<>();
	}

	@Override
	public String excuteBatchOperations(String batch, char algoType) throws RemoteException {
		logger.logInfo("New batch request");
		Request newRequest = parseBatchRequest(batch);
		String results = newRequest.performAllOperations(algoType);
		requests.add(newRequest);
		logger.logInfo("End batch request");
		return results;
	}

	@Override
	public String getCurrentGraph() throws RemoteException {
		return localGraph.serializeGraph();
	}

	private Request parseBatchRequest(String batch) {
		String[] operations = batch.split("\n");
		Request newRequest = new Request();
		for (String operation : operations) {
			if (operation.equals("F") || operation.equals("f"))
				break;
			String[] parts = operation.split(" ", 3);
			newRequest.addOperation(new Operation(parts[0].charAt(0), Integer.parseInt(parts[1]),
					Integer.parseInt(parts[2]), localGraph));
		}
		return newRequest;
	}

	public static void main(String[] args) {
		try {
			String name = "GraphService";
			GraphService server = new GraphServer();
			GraphService stub = (GraphService) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry(); // run on local host and on post 1099
			registry.rebind(name, stub);
			logger.logInfo("Server register graph service into RMI registery");
		} catch (Exception e) {
			logger.logInfo("GraphServer exception while registering graph service into RMI registery");
			e.printStackTrace();
		}
	}
}
