package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	
	private static FileHandler single_file = null;
	private File file;
	
	private FileHandler() {
		file  = new File("RawData.txt"); 
	}

    public static FileHandler getInstance() {
        if(single_file == null) {
        	single_file = new FileHandler();
        }
        return single_file;
    }
	
	public void createNewFile() {
		try {  
            file.createNewFile();  
            String path = file.getAbsolutePath();   
            System.out.print(path); 

        } catch (Exception e) {
        	System.out.println("Error while creating file.");
        }  
	}
	
	public void writeToFile(String text) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while writing file.");
		}
	}
	
	public void appendToFile(String text) {
		try {
			FileWriter fileWriter = new FileWriter(file,true);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while appending to file.");
		}
	}
}
