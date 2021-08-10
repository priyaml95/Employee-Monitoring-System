package com.application.main;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler extends TimerTask{
	
	private final static int MILLISECONDS_TO_MINUTE = 60*1000;
	private final static int CHECK_IN_MINUTES = 60;
	private final static int UPLOAD_HOUR = 0;
	private FileUploader fileUploader;
	private Timer timer;
	
	public Scheduler(FileUploader fileUploader) {
		timer = new Timer();
		this.fileUploader = fileUploader;
	}
	
	public void checkUploadHour() {
		TimerTask timerTask = new Scheduler(fileUploader);
		timer.schedule(timerTask, 0, MILLISECONDS_TO_MINUTE*CHECK_IN_MINUTES);
	}
	
	public void run(){
		String todaysDate = Utils.getDate();
		String directoryPath = Utils.getDirectoryPath(todaysDate);
		int currentHour = Utils.getCurrentHour();
		if(currentHour == UPLOAD_HOUR){
			fileUploader.uploadFile(directoryPath, todaysDate);
		}
	}
}