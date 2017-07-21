package server;

import java.util.ArrayList;
import java.util.Iterator;

public class Logger {
	private static Logger instance = new Logger();
	public static Logger getInstance(){
		return instance;
	}
	private Logger(){}
	
	private ArrayList<String> logs = new ArrayList<String>();

	public void addLog(String log){
		logs.add(log);
		print_last_log();
	}
	
	public ArrayList<String> getLogs(){
		return logs;
	}
	
	public void print_all_logs(){
		for (Iterator iterator = logs.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
	}
	
	public void print_last_log(){
		System.out.println(logs.get(logs.size()-1));
	}
	
	
}
