package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmi.registery.GraphService;

public class GraphServer implements GraphService {

	private Graph localGraph;

	public GraphServer() {
		super();
		localGraph = new Graph("local_graph.txt");
	}

	@Override
	public String excuteBatchOperations(String batch) throws RemoteException {
		Request newRequest = parseBatchRequest(batch);
		return newRequest.performAllOperations();
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
			System.out.println("GraphServer bound");
		} catch (Exception e) {
			System.err.println("GraphServer exception:");
			e.printStackTrace();
		}
	}

}
