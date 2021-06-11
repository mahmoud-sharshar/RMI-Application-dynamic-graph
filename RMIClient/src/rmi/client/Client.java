package rmi.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
				GraphService graphService = this.getGraphService();
				ArrayList<Request> requests = this.generateRequestsBatch();
				Random randomGenerator = new Random();
				for(Request request : requests) {
					long startTime = System.currentTimeMillis();
					String response = graphService.excuteBatchOperations(request.getOperations());
					long endTime = System.currentTimeMillis();
					long responseTime = endTime-startTime;
					request.setReponse(response);
					request.setResponseTime(responseTime);
					this.logInformation(request);
					int sleepTime = randomGenerator.nextInt(100);
					Thread.sleep(sleepTime);
				}
			} catch (NotBoundException | InterruptedException | IOException e) {
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
	
	private ArrayList<Request> generateRequestsBatch(){
		ArrayList<Request> requests = new ArrayList<Request>();
		Random randomGenerator = new Random();
//		int writePercentage = randomGenerator.nextInt(100)+1;
		RequestGenerator requestGenerator = new RequestGenerator(0.2, 50 , 15);
		int numOfRequests =  150 ;// randomGenerator.nextInt(10)+1;
		for(int i=0;i<numOfRequests;i++) {
			Request request = requestGenerator.getReqeust();
//			request.setWritePercentage(writePercentage);
			requests.add(request);
		}
		
		return requests;
	}
	
	private void logInformation(Request request) throws IOException {
		File logFile = new File("log"+Thread.currentThread().getId() + ".txt");
		if(!logFile.exists()) {
			logFile.createNewFile();
		}
		FileWriter logFileWriter = new FileWriter(logFile , true);
		logFileWriter.write(request.getWritePercentage() + "," + request.getResponseTime() + "\n");
//		logFileWriter.write("Request : \n");
//		logFileWriter.write(request);
//		logFileWriter.write("\n Response : \n");
//		logFileWriter.write(response);
//		logFileWriter.write("response time : " + responseTime + "\n");
//		logFileWriter.write("-------------------------------\n");
		logFileWriter.close();
	}

}
