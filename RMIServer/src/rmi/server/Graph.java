package rmi.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Graph {
	private HashMap<Integer, HashSet<Integer>> graphEdges;
	private ReadWriteLock readWriteLock;
	private AppLogger logger = new AppLogger();

	public Graph(String graphFilePath) {
		graphEdges = new HashMap<Integer, HashSet<Integer>>();
		readWriteLock = new ReentrantReadWriteLock();
		initializeGraph(graphFilePath);
	}

	public void addEdge(int node1, int node2) {
		readWriteLock.writeLock().lock();
		if (graphEdges.get(node1) == null) {
			HashSet<Integer> neighbors = new HashSet<>();
			neighbors.add(node2);
			graphEdges.put(node1, neighbors);
		} else {
			graphEdges.get(node1).add(node2);
		}
		if (graphEdges.get(node2) == null) {
			HashSet<Integer> neighbors = new HashSet<>();
			graphEdges.put(node2, neighbors);
		}
		logger.logInfo("Add new edge to the graph: " + node1 + " " + node2);
		readWriteLock.writeLock().unlock();
	}

	public void removeEdge(int node1, int node2) {
		readWriteLock.writeLock().lock();
		HashSet<Integer> node1Neighbors = graphEdges.get(node1);
		if (node1Neighbors != null) {
			node1Neighbors.remove(node2);
		}
		logger.logInfo("Remove edge from the graph: " + node1 + " " + node2);
		readWriteLock.writeLock().unlock();
	}

	public int getShortestPath(int node1, int node2) {
		readWriteLock.readLock().lock();
		Queue<Integer> queue = new LinkedList<>();
		HashMap<Integer, Integer> distances = new HashMap<>();
		queue.add(node1);
		distances.put(node1, 0);
		while (queue.isEmpty() == false) {
			int node = queue.remove();
			int nodeDist = distances.get(node);
			if (node == node2) {
				readWriteLock.readLock().unlock();
				logger.logInfo("Query: shortest path distance between: " + node1 + " and " + node2 + " is " + nodeDist);
				return nodeDist;
			}
			HashSet<Integer> neighbors = graphEdges.get(node);
			if(neighbors == null ) break;
			for (int n : neighbors) {
				if (distances.get(n) == null) {
					queue.add(n);
					distances.put(n, nodeDist + 1);
				}
			}
		}
		logger.logInfo("Query: There is no path between " + node1 + " and " + node2);
		readWriteLock.readLock().unlock();
		return -1;
	}

	public String serializeGraph() {
		readWriteLock.readLock().lock();
		String edges = "";
		Iterator<Entry<Integer, HashSet<Integer>>> hmIterator = graphEdges.entrySet().iterator();
		while (hmIterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapElement = (Map.Entry) hmIterator.next();
			int node = (int) mapElement.getKey();
			@SuppressWarnings("unchecked")
			HashSet<Integer> neighbors = (HashSet<Integer>) mapElement.getValue();
			for (int n : neighbors) {
				edges += (node + " " + n + "\n");
			}
		}
		readWriteLock.readLock().unlock();
		return edges;
	}

	private void initializeGraph(String graphFilePath) {
		logger.logInfo("Start initializing the local graph." );
		ArrayList<String> edgesLines = parseEdgesFromFile(graphFilePath);
		for (int i = 0; i < edgesLines.size(); i++) {
			String[] nodes = edgesLines.get(i).split(" ", 2);
			int node1 = Integer.parseInt(nodes[0]);
			int node2 = Integer.parseInt(nodes[1]);
			addEdge(node1, node2);
		}
		logger.logInfo("Initial graph is initialized successfully." );
	}

	private ArrayList<String> parseEdgesFromFile(String graphFilePath) {
		ArrayList<String> edges = new ArrayList<>();
		try {
			File myObj = new File(graphFilePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.equals("S") || data.equals("s"))
					break;
				edges.add(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return edges;
	}
}
