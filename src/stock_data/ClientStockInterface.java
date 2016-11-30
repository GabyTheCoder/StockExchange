package stock_data;

public interface ClientStockInterface {
	String getCode();
	int getVolume();
	double getSellAt();
	int getSellAtVolume();
	double getBuyAt();
	int getBuyAtVolume();
	boolean changeVolume(int value);
	boolean changeSellAt(double price);
	boolean changeSellAtVolume(int value);
	boolean changeBuyAt(double price);
	boolean changeBuyAtVolume(int value);
	String toCSV();
}
