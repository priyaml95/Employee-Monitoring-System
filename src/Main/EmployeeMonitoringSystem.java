package Main;

import java.io.IOException;

public class EmployeeMonitoringSystem implements FileUploader {
	
	private FileHandler fileHandler;
	private FTPHandler ftpHandler;
	private Scheduler scheduler;
	private KeyLogger keyLogger;
	
	public EmployeeMonitoringSystem() {
		fileHandler = FileHandler.getInstance();
		ftpHandler = new FTPHandler();
		scheduler = new Scheduler(this);
		keyLogger = new KeyLogger();
	}
	
    public static void main(String[] args) {
    	EmployeeMonitoringSystem employeeMonitor = new EmployeeMonitoringSystem();
    	
    	employeeMonitor.checkIfPreviousDirectoryExists();
    	employeeMonitor.setupKeyListener();
    	employeeMonitor.checkPeriodically();
    }
	
    public void checkIfPreviousDirectoryExists() {
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
    	keyLogger.initializeKeyListener();
    }
    
    public void checkPeriodically() {
    	checkIfFileExists();
    	scheduler.checkUploadHour();
    }
    
    public void checkIfFileExists() {
    	if(!fileHandler.fileExists()) {
    		fileHandler.createFile();
    	}
    }
	
	@Override
	public void uploadFile(String directoryPath, String date) {
		ftpHandler.createUserDirectory();
		ftpHandler.createDateDirectory(date);
		ftpHandler.uploadFile(fileHandler.getFile(), directoryPath);
		fileHandler.deleteFile();
		fileHandler.createFile();
	}
}
