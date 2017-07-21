package user_interface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import client.Client;
import client.ClientInterface;
import server.ServerInterface;

public class RunClient {
	
	public static void main(String[] args){
		String clientId = "12365";
		Scanner scanner = new Scanner(System.in);
		try {
//			System.out.print("Input the client id : ");
//			clientId = scanner.nextLine();
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
