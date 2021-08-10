package com.application.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	
	private File file;
	
	FileHandler(String fileName) {
		file  = new File(fileName); 
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
		} catch (Exception e) {
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
	
	public String readFile() {
		String fileText = "";
		FileReader fileReader;
		BufferedReader bufferedReader;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while((line = bufferedReader.readLine()) != null){
			    fileText += line;
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileText;
	}
}
