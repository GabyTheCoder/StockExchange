package server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

import client.Client;
import client.ClientInterface;
import extract_data.FileHandler;
import extract_data.FileHandlerInterface;
import stock_data.ClientStock;
import stock_data.ClientStockInterface;
import stock_data.Stock;
import stock_data.StockInterface;

public class Server implements ServerInterface{
	private FileHandlerInterface filehandler = null;
	private ArrayList<String> clientsList = new ArrayList<String>();
	private volatile HashMap<Integer, String> stocks;
	//	private HashMap<String, String> stocks_by_index;

	public Server(FileHandlerInterface filehandler){
		this.filehandler = filehandler;
		ArrayList<String> list = filehandler.getAllFileNames("ClientResources");
		System.out.println(list);
		stocks = LoadStocksFromCSV();	
	}
	
	private synchronized void hashReplace(int stock_index, String stock_string){
		stocks.replace(stock_index, stock_string);
	}

	public boolean addClient(String clientID){
		if(clientsList.contains(clientID)){
			return false;
		}else{
			clientsList.add(clientID);
			return true;
		}
	}

	private boolean isClient(String clientId){
		if(clientsList.contains(clientId)){
			return true;
		}
		return false;
	}

	private boolean hashmap2CSV(String folder, String filename, HashMap<Integer, String> data){
		filehandler.createCSVFile(folder, filename, data);
		return true;
	}

	private HashMap<Integer, String> LoadStocksFromCSV() {
		String name = filehandler.getMostRecentFileName();
		HashMap<Integer, String> aux = filehandler.readCSVFile(name);
		return aux;
	}

	public HashMap<Integer, String> getTodayStocks() {
		return stocks;
	}

	public boolean closeStockExchange() {
		DateFormat dateformat = new SimpleDateFormat("20YY/MM/dd");
		Calendar calendar = Calendar.getInstance();
		String savefilename = dateformat.format(calendar.getTime());
		String[] splits = savefilename.split("/");
		String filename = "";
		for(int i = 0; i<splits.length; i++){
			filename+=splits[i];
		}
		if(filehandler.createCSVFile("Resources", filename, stocks)) //save all the changes in done in a day
			return true;
		return false;
	}

	private void changePrice(int stock_index, int volume, int option){
		String aux = stocks.get(stock_index);
		StockInterface stock = new Stock(aux);
		double price = stock.getClose();
		double newprice;
		// 0 is for buy, 1 is for sell
		if(option == 0)
			newprice = price + price * 0.0001 * (double)volume;
		else
			newprice = price - price * 0.0001 * (double)volume;

		stock.changePrice(newprice);
		String stock_string = stock.toCSV();
		hashReplace(stock_index, stock_string);
	}

	private void changeVolume(int stock_index, int volume){
		String aux = stocks.get(stock_index);
		StockInterface stock = new Stock(aux);
		int current_volume = stock.getVolume();
		stock.changeVolume(current_volume + volume);
		String stock_string = stock.toCSV();
		hashReplace(stock_index, stock_string);
	}

	private boolean changeClientVolume(String clientId, String stockIndex, int volume, int option) {
		HashMap<Integer, String> client_data = filehandler.readClientFile(clientId + ".csv");
		int client_stock_index = findClientStockByIndex(client_data, stockIndex);
		String client_stock = getClientStockByIndex(client_data, stockIndex);
		ClientStockInterface clientstock = new ClientStock(client_stock);
		int volume_init = clientstock.getVolume();
		if(option == 0){
			clientstock.changeVolume(volume_init + volume);
		}else{
			if(volume > volume_init){
				//				throw new Exception("invalid quantity");
				return false;
			}
			clientstock.changeVolume(volume_init - volume);
			System.out.println("selling baybe");
			System.out.println("volume_init : " + volume_init);
			System.out.println("after volume : " + (volume_init - volume));
		}
		String newvalue = clientstock.toCSV();
		client_data.replace(client_stock_index, newvalue);

		hashmap2CSV("ClientResources", clientId, client_data);

		return true;
	}

	public synchronized boolean processRequestBuy(String clientId, String stockIndex, int volume) {
		if(isClient(clientId)){
			System.out.println("begin buying process");
			int stock_index = findStockByIndex(stocks, stockIndex);
			changeVolume(stock_index, volume);
			//0 is for buy, 1 is for sell
			changeClientVolume(clientId, stockIndex, volume, 0);
			changePrice(findStockByIndex(stocks, stockIndex), volume, 0);
			System.out.println("buy successfull");
			return true;
		}
		System.out.println("client unrecognized");
		return false;
	}

	public synchronized boolean processRequestSell(String clientId, String stockIndex, int volume) {
		if(isClient(clientId)){
			int stock_index = findStockByIndex(stocks, stockIndex);
			changeVolume(stock_index, volume);
			//0 is for buy, 1 is for sell
			changeClientVolume(clientId, stockIndex, volume, 1);
			changePrice(findStockByIndex(stocks, stockIndex), volume, 1);
			return true;
		}
		return false;
	}

	public synchronized boolean processRequestCustomBuy(String clientId, String stockIndex, int volume, double price) {
		// TODO Auto-generated method stub
		int stock_index;
		double intendedPrice;
		boolean canBuy = false;

		stock_index = findStockByIndex(stocks, stockIndex);
		String temp = stocks.get(stockIndex);

		intendedPrice = new Stock(temp).getClose();

		while((new Stock(temp).getClose() < price - 0.001) && (new Stock(temp).getClose() > price + 0.001)){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		intendedPrice = new Stock(temp).getClose();

		changeVolume(stock_index, volume);
		//0 is for buy, 1 is for sell
		changeClientVolume(clientId, stockIndex, volume, 0);
		changePrice(findStockByIndex(stocks, stockIndex), volume, 0);
		return true;
	}



	public synchronized boolean processRequestCustomSell(String clientId, String stockIndex, int volume, double price) {
		int stock_index;
		double intendedPrice;

		stock_index = findStockByIndex(stocks, stockIndex);
		String temp = stocks.get(stockIndex);

		intendedPrice = new Stock(temp).getClose();

		while((new Stock(temp).getClose() < price - 0.001) && (new Stock(temp).getClose() > price + 0.001)){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		intendedPrice = new Stock(temp).getClose();

		changeVolume(stock_index, volume);
		//0 is for buy, 1 is for sell
		changeClientVolume(clientId, stockIndex, volume, 1);
		changePrice(findStockByIndex(stocks, stockIndex), volume, 1);
		
		return true;
	}

	public boolean processCancelCustomBuy(String clientId, String stockIndex, int volume, double price) {
		int stock_index;
		double intendedPrice;

		stock_index = findStockByIndex(stocks, stockIndex);
		String temp = stocks.get(stockIndex);

		intendedPrice = new Stock(temp).getClose();

		while((new Stock(temp).getClose() < price - 0.001) && (new Stock(temp).getClose() > price + 0.001)){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		intendedPrice = new Stock(temp).getClose();

		changeVolume(stock_index, volume);
		//0 is for buy, 1 is for sell
		changeClientVolume(clientId, stockIndex, volume, 1);
		changePrice(findStockByIndex(stocks, stockIndex), volume, 1);
		
		return true;
	}

	public boolean processCancelCustomSell(String clientId, String stockIndex, int volume, double price) {
		int stock_index;
		double intendedPrice;

		stock_index = findStockByIndex(stocks, stockIndex);
		String temp = stocks.get(stockIndex);

		intendedPrice = new Stock(temp).getClose();

		while((new Stock(temp).getClose() < price - 0.001) && (new Stock(temp).getClose() > price + 0.001)){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		intendedPrice = new Stock(temp).getClose();

		changeVolume(stock_index, volume);
		//0 is for buy, 1 is for sell
		changeClientVolume(clientId, stockIndex, volume, 1);
		changePrice(findStockByIndex(stocks, stockIndex), volume, 1);
		
		return true;
	}

	private int findClientStockByIndex(HashMap<Integer, String> stocks, String Index){
		for(int i = 0; i < stocks.size(); i++){
			String s = stocks.get(i);
			ClientStockInterface si = new ClientStock(s);
			String var = si.getCode();
			if(Index.equals(var)){
				return i;
			}
		}
		return -1;
	}

	private String getClientStockByIndex(HashMap<Integer, String> stocks, String Index) {
		HashMap<Integer, String> stock = stocks;
		for(int i = 0; i < stock.size(); i++){
			String s = stock.get(i);
			ClientStockInterface si = new ClientStock(s);
			String var = si.getCode();
			if(Index.equals(var)){
				return s;
			}
		}
		return "";
	}

	public int findStockByIndex(HashMap<Integer, String> stocks, String Index){
		for(int i = 0; i < stocks.size(); i++){
			String s = stocks.get(i);
			StockInterface si = new Stock(s);
			String var = si.getCode();
			if(Index.equals(var)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public String getStockByIndex(HashMap<Integer, String> stocks, String Index) {
		HashMap<Integer, String> stock = stocks;
		for(int i = 0; i < stock.size(); i++){
			String s = stock.get(i);
			StockInterface si = new Stock(s);
			String var = si.getCode();
			if(Index.equals(var)){
				return s;
			}
		}
		return "";
	}

	@Override
	public void sayHello() {
		System.out.println("Server is online!");
	}

	public static void main(String args[]){
		Scanner scanner = new Scanner(System.in);
		String filePath = System.getProperty("user.dir") + "/Resources/";
		String fileName = "20161015.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
		try{
			Server obj = new Server(handler);
			ServerInterface stub =  (ServerInterface) UnicastRemoteObject.exportObject(obj, 0);

			//Bind
			Registry registry = LocateRegistry.createRegistry(5099);
			registry.bind("ServerInterface", stub);
			int stayopen = 1;
			while(stayopen==1){
            	System.out.println("0 - close the server");
            	System.out.print("input : ");
            	stayopen = scanner.nextInt();
            }
			stub.closeStockExchange();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}