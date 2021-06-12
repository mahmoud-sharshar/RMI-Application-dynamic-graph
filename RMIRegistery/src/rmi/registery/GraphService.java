package rmi.registery;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GraphService extends Remote {
	public String excuteBatchOperations(String batch, char algoType) throws RemoteException;
	public String getCurrentGraph() throws RemoteException;
}
