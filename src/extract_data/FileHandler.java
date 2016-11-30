package extract_data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileHandler implements FileHandlerInterface{
	private String filePath;
	private String fileName;
	
	private HashMap<Integer, String> content = new HashMap<Integer, String>();	
	public FileHandler(String filePath, String fileName){
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	public ArrayList<String> getAllFileNames(String filePath){
		ArrayList<String> file_names = new ArrayList<String>();
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()){
				file_names.add(listOfFiles[i].getName());
			}
		}
		return file_names;
	}
	
	private String getLatestStocks() {
		// TODO Auto-generated method stub
		String filename = "";
		String filenameAct = "";
		Integer last = 0;
		Integer act = 0;
		Integer i = 0;
		String string = System.getProperty("user.dir") + "/Resources/";
		File[] fileList = getFileList(string);
		for(File file : fileList){
			// System.out.println(file.getName());
			filenameAct = file.getName();
			act = 0;
			for(i = 0; i<8; i++){
			act = act *10;
			act += filenameAct.charAt(i);
			}
			if(act > last){
				last = act;
				filename = filenameAct;
			}
		}
		return filename;
	}
	
 	
	
	public HashMap<Integer, String> readCSVFile() {
		String name = filePath.concat(fileName);
		int lineIndex = 0;
		try{
			Scanner scanner = new Scanner(new File(name));
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				content.put(lineIndex, scanner.nextLine());  
				lineIndex++;
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		return content;  
	}
	
	public HashMap<Integer, String> readClientFile(String clientId) {
		String filePath = System.getProperty("user.dir") + "\\ClientResources\\";
		String name = filePath.concat(clientId);
		int lineIndex = 0;
		try{
			Scanner scanner = new Scanner(new File(name));
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				content.put(lineIndex, scanner.nextLine());  // put the line in hashmap
				// nume_hashmap.put(numarul liniei, valoarea liniei (ca string) 
				lineIndex++;
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		return content;  
	}
	
	
	public String getLineAtIndex(int lineIndex) {
		String line = content.get(lineIndex);
		if(line!=null){
			return line;
		}else{
			return "missing data at line " + lineIndex;
		}
	}
	
	
	public int getNumOfLines(){
		return content.size();
	}
	
	private static File[] getFileList(String dirPath) {
        File dir = new File(dirPath);   

        File[] fileList = dir.listFiles(new FilenameFilter(){ 
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        });
        return fileList;
	}

	public String getMostRecentFileName() {
		return getLatestStocks();
	}

	public HashMap<Integer, String> readCSVFile(String filename) {
		String name = filePath.concat(filename);
		HashMap<Integer, String> stocks = new HashMap<Integer, String>();
		int lineIndex = 0;
		try{
			Scanner scanner = new Scanner(new File(name));
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				stocks.put(lineIndex, scanner.nextLine()); 
				lineIndex++;
			}
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return stocks;  
	}

	public boolean searchClientFile(String clientID) {
		if(getAllFileNames(System.getProperty("user.dir") + "/ClientResources/").contains(clientID+ ".csv") == true){
			return true;
		}
		return false;
	}

	@Override
	public boolean createCSVFile(String folder, String filename, HashMap<Integer, String> data){
		String file = System.getProperty("user.dir") + "/" + folder + "/" + filename + ".csv";
		System.out.println("file : " + file);
		try {
			FileWriter fstream;
			BufferedWriter out;
			fstream = new FileWriter(file);
			out = new BufferedWriter(fstream);
			
			for(int i = 0; i < data.size(); i++){
				out.write(data.get(i) + "\n");
			}
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
