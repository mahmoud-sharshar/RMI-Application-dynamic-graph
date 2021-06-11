package rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import rmi.registery.GraphService;

public class Client extends Thread {
	public void run() {
			try {
				System.err.println("ClientID: "+ Thread.currentThread().getId());
				GraphService graphService = getGraphService();
				ArrayList<String> requests = generateRequestsBatch();
				Random randomGenerator = new Random();
				for(String request : requests) {
					//System.err.println("ClientID: "+ Thread.currentThread().getId() + "  " + request);
					int sleepTime = randomGenerator.nextInt(100);
					Thread.sleep(sleepTime);
				}
			} catch (RemoteException | NotBoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private GraphService getGraphService() throws RemoteException, NotBoundException {
		String name = "GraphService";
		Registry registry = LocateRegistry.getRegistry("localhost");
		GraphService graphService = (GraphService) registry.lookup(name);
		return graphService;
	}
	
	private ArrayList<String> generateRequestsBatch(){
		ArrayList<String> requests = new ArrayList<String>();
		RequestGenerator requestGenerator = new RequestGenerator(0.5, 10 , 20);
		Random randomGenerator = new Random();
		int numOfRequests = randomGenerator.nextInt(10)+1;
		for(int i=0;i<numOfRequests;i++) {
			String request = requestGenerator.getReqeust();
			requests.add(request);
		}
		return requests;
	}
	

}
