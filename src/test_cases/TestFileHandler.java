package test_cases;


import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import client.Client;
import client.ClientInterface;
import extract_data.FileHandler;
import extract_data.FileHandlerInterface;
import server.Server;
import server.ServerInterface;
import stock_data.ClientStock;
import stock_data.ClientStockInterface;
import stock_data.Stock;
import stock_data.StockInterface;

public class TestFileHandler {

	@Test
	public void testFileRead() {
		/*e1-15 = 0.000 000 000 000 001 femto f*/
		final double DELTA = 1e-15;  
		// D:/Programming/Java8Eclipse/StockExchange/
		String filePath = System.getProperty("user.dir") + "\\Resources\\";
		String fileName = "20161015.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
		HashMap<Integer, String> data = handler.readCSVFile();
		for (Map.Entry<Integer, String> entry : data.entrySet()) {
			assert(entry.getValue().length() > 15);
		}
		assertEquals(289, handler.getNumOfLines());
		String line = handler.getLineAtIndex(0);
		assertEquals("AAAP,Advanced Accele. Ads,38.75,37.87,38.65,26500,0.1,0.26", line);
		StockInterface stock = new Stock(line);
		assertEquals("AAAP", stock.getCode());
		assertEquals("Advanced Accele. Ads", stock.getName());
		assertEquals(38.75, stock.getHigh(), DELTA);
		assertEquals(37.87, stock.getLow(), DELTA);
		assertEquals(38.65, stock.getClose(), DELTA);
		assertEquals(26500, stock.getVolume());
		assertEquals(0.1, stock.getChangePercentage(), DELTA);
		assertEquals(0.26, stock.getChangeValue(), DELTA);
	}
	@Test
	public void testClientFileHandler(){
		final double DELTA = 1e-15;  
		// D:/Programming/Java8Eclipse/StockExchange/
		String filePath = System.getProperty("user.dir") + "\\ClientResources\\";
		String fileName = "12365.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
		HashMap<Integer, String> data = handler.readCSVFile();
		assertEquals(3, handler.getNumOfLines());
		String line = handler.getLineAtIndex(0);
		System.out.println(line);
		ClientStockInterface stock = new ClientStock(line);
		assertEquals("ATRA", stock.getCode());
		assertEquals(4200, stock.getVolume());
		assertEquals(45.3, stock.getSellAt(), DELTA);
		assertEquals(200, stock.getSellAtVolume());
		assertEquals(30.45, stock.getBuyAt(), DELTA);
		assertEquals(2411, stock.getBuyAtVolume());
	}
	
	
	@Test
	public void testLastLine(){
		final double DELTA = 1e-15;  
		String filePath = System.getProperty("user.dir") + "\\Resources\\";
		String fileName = "20161015.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
		HashMap<Integer, String> data = handler.readCSVFile();
		
		String line = handler.getLineAtIndex(288);
		assertEquals("AZPN,Aspen Technology Cmn,46.42,46.01,46.05,228500,-0.5,1.07", line);
		
		StockInterface stock = new Stock(line);
		assertEquals("AZPN", stock.getCode());
		assertEquals("Aspen Technology Cmn", stock.getName());
		assertEquals(46.42, stock.getHigh(), DELTA);
		assertEquals(46.01, stock.getLow(), DELTA);
		assertEquals(46.05, stock.getClose(), DELTA);
		assertEquals(228500, stock.getVolume());
		assertEquals(-0.5, stock.getChangePercentage(), DELTA);
		assertEquals(1.07, stock.getChangeValue(), DELTA);
	}
	
	@Test
	public void testServerInterface(){
		final double DELTA = 1e-15;  
		String filePath = System.getProperty("user.dir") + "\\Resources\\";
		String fileName = "20161015.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
		Server server_stocks = new Server(handler);
		HashMap<Integer, String> var = handler.readCSVFile();
		String line = var.get(15);
		HashMap<Integer, String> stocks = server_stocks.getTodayStocks();
		server_stocks.closeStockExchange();
	}
	
	@Test
	public void testClient() throws RemoteException{
		final double DELTA = 1e-15;  
		String filePath = System.getProperty("user.dir") + "\\Resources\\";;
		String fileName = "20161015.csv";
		FileHandlerInterface handler = new FileHandler(filePath, fileName);
//		handler.getAllFileNames(filePath);
		ServerInterface server = new Server(handler);
		assertEquals(true, server.addClient("12365"));
		assertEquals(true,  server.addClient("18779")); 
		ClientInterface client = new Client(server, "12365");
		
	    HashMap<Integer, String> stocks = client.getStocks();
//	    System.out.println(server.findStockByIndex(stocks,"AAPL"));
//	    System.out.println(server.getStockByIndex(stocks,"AAPL"));
	    
	    assertEquals(true, client.buyStock("AAPL", 2000));
	    assertEquals(true, client.sellStock("AAPL", 10000));
	    
	}
	@Test
	public void testFileHandler(){
		final double DELTA = 1e-15;  
		String filePath = System.getProperty("user.dir") + "\\Resources\\";
		FileHandler handler = new FileHandler(filePath, "");
		String mostRecentFileName = handler.getMostRecentFileName();
		HashMap<Integer, String> readCSVFile = handler.readCSVFile(mostRecentFileName);
		assertEquals(true, handler.searchClientFile("12365"));
		assertEquals(false, handler.searchClientFile("45648"));
	}
	
	@Test
	public void testStock(){
		final double DELTA = 1e-15;  
		String filePath = System.getProperty("user.dir") + "\\Resources\\";
		FileHandlerInterface handler = new FileHandler(filePath, "");
		String mostRecentFileName = handler.getMostRecentFileName();
		HashMap<Integer, String> data = handler.readCSVFile(mostRecentFileName);
		int size = data.size();
		Stock stock = new Stock(data.get(50));
//		System.out.println(stock.getStock());
//		System.out.print(stock.getStock().get(0));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(1));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(2));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(3));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(4));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(5));
//		System.out.print(", ");
//		System.out.print(stock.getStock().get(6));
//		System.out.print(", ");
//		System.out.println(stock.getStock().get(7));
		stock.changeHigh(64.22);
		stock.changeLow(20.2);
		stock.changePrice(162.44);
		stock.changeVolume(456484);
		stock.changePertange(0.34);
		stock.changeValue(10.1);
		
//		System.out.println(stock.getStock());
//		System.out.println(stock.toCSV());

//		System.out.println(stock.toString());
	}
	@Test
	public void testDateFormat(){
		
		DateFormat dateformat = new SimpleDateFormat("20YY/MM/dd");
		Calendar calendar = Calendar.getInstance();
		String savefilename = dateformat.format(calendar.getTime());
		String[] splits = savefilename.split("/");
		String filename = "";
		for(int i = 0; i<splits.length; i++){
			filename+=splits[i];
		}
		System.out.println("filename : " + filename);
	}
}