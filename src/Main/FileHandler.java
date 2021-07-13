package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	
	private static FileHandler single_file = null;
	private File file;
	
	private FileHandler() {
		file  = new File("KeyStrokes.txt"); 
	}

    public static FileHandler getInstance() {
        if(single_file == null) {
        	single_file = new FileHandler();
        }
        return single_file;
    }
    
    public File getFile() {
    	return file;
    }
	
	public void createFile() {
		try {  
            file.createNewFile();
        } catch (Exception e) {
        	System.out.println("Error : " + e.getMessage());
        }  
	}
	
	public void writeToFile(String text) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
	
	public void appendToFile(String text) {
		try {
			FileWriter fileWriter = new FileWriter(file,true);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
	
	public void deleteFile() {
		boolean fileDeleted = file.delete();
		if(!fileDeleted) {
			System.out.println("Error in deleting file.");
		}
	}
	
	public boolean fileExists() {
		return file.exists();
	}
}
