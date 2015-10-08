import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author ergindoganyildiz
 *
 *May 18, 2015
 */
public class DateUtils {

	public static Date getTarih(String dateString, String koseYazisiLink){
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			
			return sdf.parse(dateString);
		} catch (ParseException e) {
			System.err.println("Date Parse Error : " + koseYazisiLink + " Date String : " + dateString);
			return null;
		}
	}
}
