package Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Utils {
	
	private Utils() {}
	
	public static String getDate() {
		Date dateObject = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		return dateFormat.format(dateObject);
	}
	
	public static String getYesterdaysDate() {
	    final Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DATE, -1);
	    Date dateObject = calendar.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(dateObject);
	}
	
	public static String getUsername() {
		return System.getProperty("user.name");
	}
	
	public static String getDirectoryPath(String date) {
		String directoryPath = "/" + getUsername() + "/" + date + "/";
		return directoryPath;
	}
	
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		return currentHour;
	}
}
