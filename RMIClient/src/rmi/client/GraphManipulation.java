package rmi.client;


public class GraphManipulation {
	public static void main(String args[]) {
		try {
			Client clientThread = new Client();
			clientThread.start();
		} catch (Exception e) {
			System.err.println("GraphService exception:");
			e.printStackTrace();
		}
	}

}
