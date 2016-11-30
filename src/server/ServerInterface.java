package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import client.ClientInterface;
import stock_data.Stock;

public interface ServerInterface extends Remote{
	HashMap<Integer, String> getTodayStocks() throws RemoteException;
	boolean processRequestBuy(String clientId, String stockIndex, int volume) throws RemoteException;
	boolean processRequestSell(String clientId, String stockIndex, int volume) throws RemoteException;
	boolean processRequestCustomBuy(String clientId, String stockIndex, int volume, double price) throws RemoteException;
	boolean processRequestCustomSell(String clientId, String stockIndex, int volume, double price) throws RemoteException;
	boolean processCancelCustomBuy(String clientId, String stockIndex, int volume, double price) throws RemoteException;
	boolean processCancelCustomSell(String clientId, String stockIndex, int volume, double price) throws RemoteException;
	boolean closeStockExchange() throws RemoteException;
	void sayHello() throws RemoteException;
	int findStockByIndex(HashMap<Integer, String> stocks, String Index) throws RemoteException;
	String getStockByIndex(HashMap<Integer, String> stocks, String Index) throws RemoteException;
	boolean addClient(String clientID) throws RemoteException;
	
	
}
