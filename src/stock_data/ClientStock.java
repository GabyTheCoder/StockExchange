package stock_data;

import java.util.ArrayList;

public class ClientStock implements ClientStockInterface{
	private String stock;
	private String[] stock_data;
	private ArrayList<String> stock_fields = new ArrayList<String>();
	public ClientStock(String stock){
		this.stock = stock;
		stock_data = stock.split(",");
		get_All_Stock_Fields();
	}
	
	private void get_All_Stock_Fields(){
		stock_fields.add(this.getCode());
		stock_fields.add(String.valueOf(this.getVolume()));
		stock_fields.add(String.valueOf(this.getSellAt()));
		stock_fields.add(String.valueOf(this.getSellAtVolume()));
		stock_fields.add(String.valueOf(this.getBuyAt()));
		stock_fields.add(String.valueOf(this.getBuyAtVolume()));
	}
	
	@Override
	public String getCode() {
		return stock_data[0];
	}
	@Override
	public int getVolume() {
		return Integer.valueOf(stock_data[1]);
	}
	@Override
	public double getSellAt() {
		return Double.valueOf(stock_data[2]);
	}
	@Override
	public int getSellAtVolume() {
		return Integer.valueOf(stock_data[3]);
	}
	@Override
	public double getBuyAt() {
		return Double.valueOf(stock_data[4]);
	}
	@Override
	public int getBuyAtVolume() {
		return Integer.valueOf(stock_data[5]);
	}
	@Override
	public boolean changeVolume(int value) {
		try {
			stock_fields.remove(1);
			stock_fields.add(1, String.valueOf(value));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public boolean changeSellAt(double price) {
		try {
			stock_fields.remove(2);
			stock_fields.add(2, String.valueOf(price));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public boolean changeSellAtVolume(int value) {
		try {
			stock_fields.remove(3);
			stock_fields.add(3, String.valueOf(value));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public boolean changeBuyAt(double price) {
		try {
			stock_fields.remove(4);
			stock_fields.add(4, String.valueOf(price));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	@Override
	public boolean changeBuyAtVolume(int value) {
		try {
			stock_fields.remove(5);
			stock_fields.add(5, String.valueOf(value));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public String toCSV() {
		String value="";
		int size = stock_fields.size();
		for(int i=0;i<size-1; i++){
			value+=stock_fields.get(i);
			value+=",";
		}
		value+=stock_fields.get(size-1);
		//new line may be added in the future
		return value;
	}


}
