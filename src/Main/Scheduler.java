package Main;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class Scheduler extends TimerTask{
	
	final static int MILLISECONDS_TO_MINUTE = 60*1000;
	final static int CHECK_IN_MINUTES = 1;
	final static int UPLOAD_HOUR = 2;
	private Timer timer;
	private FileUploader fileUploader;
	
	public Scheduler(FileUploader fileUploader) {
		timer = new Timer();
		this.fileUploader = fileUploader;
	}
	
	public void checkUploadHour() {
		TimerTask timerTask = new Scheduler(fileUploader);
		timer.schedule(timerTask, 0, MILLISECONDS_TO_MINUTE*CHECK_IN_MINUTES);
	}
	
	public void run(){
		System.out.println(Thread.currentThread().getName());
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		if(currentHour == UPLOAD_HOUR){
			this.fileUploader.uploadFile();
		}
	}
}
