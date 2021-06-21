package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class FTPHandler {
	
	private final static String SERVER = "192.168.1.196";
	private final static String USERNAME = "ftpadmin";
	private final static String PASSWORD = "admin";
	private final static int PORT = 21;
	
	private FTPClient ftpClient;
	private String directoryPath;
	
	public FTPHandler() {
		ftpClient = new FTPClient();
	}
	
	public void uploadToServer(File file) {
		connectToServer();
		createDirectories();
		uploadFile(file);
	}
	
	public void uploadFile(File file) {
		try {		
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        String fileName = file.getName();
            String filePath = directoryPath + fileName;
            InputStream inputStream = new FileInputStream(fileName);
            
            boolean uploadedSuccessfully = ftpClient.storeFile(filePath, inputStream);
            if (uploadedSuccessfully) {
                System.out.println("The file has been uploaded successfully.");
            }
            inputStream.close();
		} catch(IOException ex) {
			System.out.println("Error in uploading file.");
		} finally {
			logOut();
        }
	}
	
	public void createDirectories() {
		String username = System.getProperty("user.name");
		String userDirectoryPath = "/" + username + "/";
		String date = getDate();
		directoryPath = userDirectoryPath + date + "/";
		
		createDirectory(userDirectoryPath);
		createDirectory(directoryPath);
	}
	
	public void createDirectory(String directoryPath) {
		boolean created = false;
		
		try {
			created = ftpClient.makeDirectory(directoryPath);
		} catch (IOException e) {
			System.out.println("Error in creating directory");
		}
		if(!created) {
			System.out.println(directoryPath + " already exists");
		}
		else {
			System.out.println("Directory created successfully");
		}
	}
	
	public void connectToServer() {
		try {
			ftpClient.connect(SERVER, PORT);
	        ftpClient.login(USERNAME, PASSWORD);
	        ftpClient.enterLocalPassiveMode();
		} catch(IOException ex) {
			System.out.println("Error in connecting.");
		}
	}
	
	public void logOut() {
		try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
        	System.out.println("Error in disconnecting.");
        }
	}
	
	public String getDate() {
		Date dateObject = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		return dateFormat.format(dateObject);
	}
}