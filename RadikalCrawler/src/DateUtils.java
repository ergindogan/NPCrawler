import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class DateUtils {
	
	private static String[] aylar = {"Ocak","Å�ubat","Mart","Nisan","MayÄ±s","Haziran","Temmuz","AÄŸustos","EylÃ¼l","Ekim","KasÄ±m","AralÄ±k"};
	
	public static Date getDate(String dateString){
		Calendar cal = Calendar.getInstance();
		
		String[] items = dateString.split(" ");
		
		int dayOfMonth = Integer.parseInt(items[0]);
		int month = getMonth(items[1]);
		int year = Integer.parseInt(items[2]);
		
		cal.set(year, month, dayOfMonth, 0, 0, 0);
		
		return cal.getTime();
	}
	
	private static int getMonth(String month){
		int i = 0;
		for(String ay:aylar){
			if(ay.equals(month)){
				return i;
			}
			i++;
		}
		return i;
	}

}
