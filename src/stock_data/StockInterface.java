package stock_data;

public interface StockInterface {
	
	String getCode();
	String getName();
	double getHigh();
	double getLow();
	double getClose();
	int getVolume();
	double getChangePercentage();
	double getChangeValue();
	boolean changeHigh(double newHigh);
	boolean changeLow(double newLow);
	boolean changePrice(double newPrice);
	boolean changeVolume(int newVolume);
	boolean changePertange(double newPertange);
	boolean changeValue(double newValue);
	String toCSV();
	
	
}