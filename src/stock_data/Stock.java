package stock_data;

import java.util.ArrayList;

public class Stock implements StockInterface{
	private String stock;
	private String[] stock_data;
	private ArrayList<String> stock_fields = new ArrayList<String>();
	public Stock(String stock){
		this.stock = stock;
		stock_data = stock.split(",");
		get_All_Stock_Fields();
		changeVolume(0);
	}
	
	private void get_All_Stock_Fields(){
		stock_fields.add(this.getCode());
		stock_fields.add(this.getName());
		stock_fields.add(String.valueOf(this.getHigh()));
		stock_fields.add(String.valueOf(this.getLow()));
		stock_fields.add(String.valueOf(this.getClose()));
		stock_fields.add(String.valueOf(this.getVolume()));
		stock_fields.add(String.valueOf(this.getChangePercentage()));
		stock_fields.add(String.valueOf(this.getChangeValue()));		
	}
	public boolean changeHigh(double newHigh){
		try {
			stock_fields.remove(2);
			stock_fields.add(2, String.valueOf(newHigh));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public boolean changeLow(double newLow){
		try {
			stock_fields.remove(3);
			stock_fields.add(3, String.valueOf(newLow));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public boolean changePrice(double newPrice){
		try {
			stock_fields.remove(4);
			stock_fields.add(4, String.valueOf(newPrice));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	public boolean changeVolume(int newVolume){
		try {
			stock_fields.remove(5);
			stock_fields.add(5, String.valueOf(newVolume));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	public boolean changePertange(double newPertange){
		try {
			stock_fields.remove(6);
			stock_fields.add(6, String.valueOf(newPertange));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	public boolean changeValue(double newValue){
		try {
			stock_fields.remove(7);
			stock_fields.add(7, String.valueOf(newValue));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public String toCSV(){
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
	
	public ArrayList<String> getStock(){
		return stock_fields;
	}
	
	/**
	 * any change in the stock array, will require to possibly change the order of the stock_data array. 
	 */
	@Override
	public String getCode() {
		
		return stock_data[0];
	}

	@Override
	public String getName() {
		return stock_data[1];
	}

	@Override
	public double getHigh() {
		return Double.valueOf(stock_data[2]);
	}

	@Override
	public double getLow() {
		return Double.valueOf(stock_data[3]);
	}

	@Override
	public double getClose() {
		return Double.valueOf(stock_data[4]);
	}
	/**
	 * the shares volume is always an int.
	 * you can only trade int number of shares on stock market.
	 */
	@Override
	public int getVolume() {
		return Integer.valueOf(stock_data[5]);
	}

	@Override
	public double getChangePercentage() {
		return Double.valueOf(stock_data[6]);
	}

	@Override
	public double getChangeValue() {
		return Double.valueOf(stock_data[7]);
	}
}