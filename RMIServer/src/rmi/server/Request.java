package rmi.server;

import java.util.ArrayList;

public class Request {
	private int id;
	private long taken_time;
	private ArrayList<Operation> operations;

	public Request() {
		this.operations = new ArrayList<>();
	}

	public void addOperation(Operation operation) {
		operations.add(operation);
	}

	public String performAllOperations() {
		long startTime = System.nanoTime();
		String queryResults = "";
		for (Operation operation : this.operations) {
			operation.perform();
			if (operation.getType() == 'Q' || operation.getType() == 'q') {
				queryResults += (operation.getQueryResult() + "\n");
			}
		}
		this.taken_time = System.nanoTime() - startTime;
		return queryResults;
	}

	public ArrayList<Operation> getOperations() {
		return this.operations;
	}
}
