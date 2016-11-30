package extract_data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface FileHandlerInterface {
	HashMap<Integer, String> readCSVFile();
	HashMap<Integer, String> readCSVFile(String filename);
	String getLineAtIndex(int lineIndex);
	int getNumOfLines();
	String getMostRecentFileName();
	HashMap<Integer, String> readClientFile(String clientId);
	boolean searchClientFile(String clientID);
	boolean createCSVFile(String folder, String filename, HashMap<Integer, String> data);
	ArrayList<String> getAllFileNames(String filePath);
}
