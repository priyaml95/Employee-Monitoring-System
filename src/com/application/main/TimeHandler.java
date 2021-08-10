package com.application.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TimeHandler {
	
	private final static int BREAK_TIME_IN_MINUTES = 1;
	private final static int MINUTE_TO_SECONDS = 60;
	
	private static TimeHandler singleInstance = null;
	private FileHandler fileHandler;
	private long startTime;
	private long endTime;
	private float totalIdleTime;
	
	private TimeHandler() {
		startTime = Utils.getSystemTime();
		totalIdleTime = 0;
		fileHandler = new FileHandler("Times.txt");
	}
	
	public static TimeHandler getInstance() {
		if(singleInstance==null) {
			return new TimeHandler();
		}
		return singleInstance;
	}
	
	public File getTimeFile() {
		return fileHandler.getFile();
	}
	
	public void refreshFile() {
		fileHandler.deleteFile();
		fileHandler.createFile();
	}
	
	public void writeTimes(String times) {
		fileHandler.writeToFile(times);
	}
	
	public String getLoginTime() throws IOException, InterruptedException {
		return windowsLogChecker("6005");
	}
	
	public String getLogoutTime() throws IOException, InterruptedException {
		return windowsLogChecker("6006");
	}
	
	public String getPreviousIdleTime() throws IOException, InterruptedException {
		return fileHandler.readFile();
	}
	
	public String getIdleTime() {
		return String.valueOf((int)totalIdleTime);
	}
	
	public String windowsLogChecker(String EventId) throws IOException, InterruptedException {
		Process uptimeProcess = null;
		uptimeProcess = Runtime.getRuntime().exec("powershell -Command \"get-eventlog System | where-object {$_.EventID -eq '" + EventId + "'} | sort -desc TimeGenerated\"");
		uptimeProcess.waitFor();
		return extractTime(uptimeProcess);
	}
	
	public void calculateIdleTime() {
		endTime = Utils.getSystemTime();
		float idleTime = (endTime - startTime)/1000F;
		//System.out.println(idleTime);
		if(idleTime > BREAK_TIME_IN_MINUTES*MINUTE_TO_SECONDS) {
			totalIdleTime += idleTime;
			fileHandler.writeToFile(String.valueOf(((int)totalIdleTime)/MINUTE_TO_SECONDS));
		}
		startTime = endTime;
	}
	
	public String extractTime(Process uptimeProcess) throws IOException {
		String extractedTime;
	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uptimeProcess.getInputStream()));
	    int index = 0;
	    String logLine = null;
	    
	    while(index<4) {
	    	logLine = bufferedReader.readLine();
	    	index++;
	    }
	    
	    String[] PartsOfLogLine = logLine.split(" ");
	    extractedTime = PartsOfLogLine[7];
	    return extractedTime;
	}
}
