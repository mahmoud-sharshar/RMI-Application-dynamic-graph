package rmi.client;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import rmi.registery.GraphService;

public class GraphManipulation {
	public static void main(String args[]) {
		try {
//			String name = "GraphService";
//			Registry registry = LocateRegistry.getRegistry("localhost");
//			GraphService graphService = (GraphService) registry.lookup(name);
			RequestGenerator generator = new RequestGenerator(0.3, 10 , 20);
			System.out.println(generator.getReqeust());
		} catch (Exception e) {
			System.err.println("GraphService exception:");
			e.printStackTrace();
		}
	}

}
