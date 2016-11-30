package client;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;

import stock_data.Stock;

public interface ClientInterface {
	HashMap<Integer, String> getStocks() throws RemoteException;
	boolean buyStock(String StockIndex, int volume) throws RemoteException;
	boolean sellStock(String StockIndex, int volume) throws RemoteException;
	boolean customBuyStock(String StockIndex, int price, int volume) throws RemoteException;
	boolean customSellStock(String StockIndex, int price, int volume) throws RemoteException;
	String getClientID() throws RemoteException;
	void sayHello() throws RemoteException;
	
	
}