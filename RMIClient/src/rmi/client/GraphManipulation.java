package rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.registery.GraphService;

public class GraphManipulation {
	public static void main(String args[]) {
//		if (System.getSecurityManager() == null) {
//			System.setSecurityManager(new SecurityManager());
//		}
		try {
			String name = "GraphService";
			Registry registry = LocateRegistry.getRegistry("localhost");
			GraphService graphService = (GraphService) registry.lookup(name);
			System.out.println(graphService.getCurrentGraph());
		} catch (Exception e) {
			System.err.println("GraphService exception:");
			e.printStackTrace();
		}
	}
}
