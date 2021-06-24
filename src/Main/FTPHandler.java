package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class FTPHandler {
	
	private final static String SERVER = "192.168.1.196";
	private final static String USERNAME = "ftpserver";
	private final static String PASSWORD = "qwerty";
	private final static int PORT = 21;
	
	private FTPClient ftpClient;

	public FTPHandler() {
		ftpClient = new FTPClient();
	}
	
	public void uploadFile(File file, String directoryPath) {
		try {		
			connectToServer();
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        String fileName = file.getName();
            String filePath = directoryPath + fileName;
            InputStream inputStream = new FileInputStream(fileName);
            
            boolean uploadedSuccessfully = ftpClient.storeFile(filePath, inputStream);
            if (uploadedSuccessfully) {
                System.out.println(filePath + " has been uploaded successfully.\n");
            }
            inputStream.close();
		} catch(IOException e) {
			System.out.println("Error : " + e.getMessage());
		} finally {
			logOut();
        }
	}
	
	boolean checkDirectoryExists(String directoryPath) throws IOException {
		connectToServer();
	    ftpClient.changeWorkingDirectory(directoryPath);
	    int returnCode = ftpClient.getReplyCode();
	    if (returnCode == 550) {
	    	logOut();
	        return false;
	    }
	    ftpClient.changeWorkingDirectory("/");
	    logOut();
	    return true;
	}
	
	public void createUserDirectory() {
		connectToServer();
		String username = Utils.getUsername();
		String userDirectoryPath = "/" + username + "/";
		createDirectory(userDirectoryPath);
		logOut();
	}
	
	public void createDateDirectory(String date) {
		connectToServer();
		String username = Utils.getUsername();
		String dateDirectoryPath = "/" + username + "/" + date;
		createDirectory(dateDirectoryPath);
		logOut();
	}
	
	public void createDirectory(String directoryPath) {
		boolean created = false;
		try {
			created = ftpClient.makeDirectory(directoryPath);
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
			return;
		}
		if(!created) {
			System.out.println(directoryPath + " already exists");
		}
		else {
			System.out.println(directoryPath + " created successfully");
		}
	}
	
	public void connectToServer() {
		try {
			ftpClient.connect(SERVER, PORT);
	        ftpClient.login(USERNAME, PASSWORD);
	        ftpClient.enterLocalPassiveMode();
		} catch(IOException e) {
			System.out.println("Error : " + e.getMessage());
		}
	}
	
	public void logOut() {
		try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
        	System.out.println("Error : " + e.getMessage());
        }
	}
	

}