package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;

import server.Server;
import server.ServerInterface;
import stock_data.Stock;

public class Client implements ClientInterface{
	private ServerInterface server;
	private String clientID;
	
	public Client(ServerInterface server, String clientID){
		this.server = server;
		this.clientID = clientID;
	}
	
	public String getClientID(){
		return clientID;
	}
	
	@Override
	public HashMap<Integer, String> getStocks() throws RemoteException {
		// TODO Auto-generated method stub
		server.sayHello();
		HashMap<Integer, String> stocks = server.getTodayStocks();
		return stocks;
	}

	public boolean buyStock(String stockIndex, int volume) throws RemoteException {
//		System.out.println("client start buy");
//		System.out.println(server.getTodayStocks().get(1));
		try {
			server.processRequestBuy(clientID, stockIndex, volume);
//			System.out.println("did you bought anything ?");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sellStock(String stockIndex, int volume) {
		try {
			server.processRequestSell(clientID, stockIndex, volume);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public boolean customBuyStock(String stockIndex, int price, int volume) {
		try {
			server.processRequestCustomBuy(clientID, stockIndex, volume, price);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public boolean customSellStock(String stockIndex, int price, int volume) {
		try {
			server.processRequestCustomSell(clientID, stockIndex, volume, price);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}


	public void sayHello() {
		System.out.println("client " + clientID + " is online!");
	}
	
	public static void main(String[] args){
		String clientId = "12365";
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("Input the client id : ");
			clientId = scanner.nextLine();
			System.out.println("id is : " + clientId);
			int stayopen = 1;
            Registry registry = LocateRegistry.getRegistry(5099);
            ServerInterface server = (ServerInterface) registry.lookup("ServerInterface");
            ClientInterface client = new Client(server, clientId);
            server.addClient(clientId);
            server.sayHello();
            while(stayopen==1){
            	client.getStocks();
            	client.buyStock("AAPL", 1000);
            	System.out.println("0 - close the client");
            	System.out.print("input : ");
            	stayopen = scanner.nextInt();
            }
            client.sayHello();
        }
        catch (Exception e){
            e.printStackTrace();
        }
	}
}

