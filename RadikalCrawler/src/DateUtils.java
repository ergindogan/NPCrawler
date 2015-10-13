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
	
	public static Date getDate(String dateString) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date myDate = sdf.parse(dateString);
		return myDate;
	}

}
