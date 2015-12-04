import java.util.Calendar;
import java.util.Date;


/**
 * @author ergindoganyildiz
 * 
 * Oct 14, 2015
 */
public class DateUtils {
	private static String[] aylar = {"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};
	
	public static Date getDate(String dateString){
		Calendar cal = Calendar.getInstance();
		
		String[] items = dateString.split(" ");
		
		
		int month = getMonth(items[0]);
		int dayOfMonth = Integer.parseInt(items[1].substring(0, items[1].length() - 1));
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
