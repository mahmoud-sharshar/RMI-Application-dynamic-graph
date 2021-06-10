package rmi.client;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import rmi.registery.GraphService;

public class GraphManipulation {
	public static void main(String args[]) {
		try {
			String name = "GraphService";
			Registry registry = LocateRegistry.getRegistry("localhost");
			GraphService graphService = (GraphService) registry.lookup(name);
			String batch = new Scanner(new File("batch_1.txt")).useDelimiter("\\Z").next();
			System.out.println(batch);
			System.out.println("get the graph before edit");
			System.out.println(graphService.getCurrentGraph());
			System.out.println("perform operations");
			System.out.println(graphService.excuteBatchOperations(batch));
			System.out.println("get the graph after edit");
			System.out.println(graphService.getCurrentGraph());
		} catch (Exception e) {
			System.err.println("GraphService exception:");
			e.printStackTrace();
		}
	}
}
