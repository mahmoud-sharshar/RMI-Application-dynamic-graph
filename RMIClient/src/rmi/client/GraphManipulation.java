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
			for(int i=0;i<2;i++) {
				Client clientThread = new Client();
				clientThread.start();
			}
		} catch (Exception e) {
			System.err.println("GraphService exception:");
			e.printStackTrace();
		}
	}

}
