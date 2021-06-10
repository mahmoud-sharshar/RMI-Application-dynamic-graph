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
		return batch;
	}

	@Override
	public String getCurrentGraph() throws RemoteException {
		return localGraph.serializeGraph();
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
