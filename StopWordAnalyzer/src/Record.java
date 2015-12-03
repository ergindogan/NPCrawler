import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ergindoganyildiz
 * 
 * Dec 3, 2015
 */
public class Record {
	
	private String writerName;
	private String headline;
	private Date publishDate;
	private String passage;
	
	public Record(){
		writerName = "";
		headline = "";
		passage = "";
	}
	
	public void setWriterNameHeadlineDate(String myLine){
		String[] attributes = myLine.split("-");
		
		setPublishDate(parseDateToUtilDate(attributes[attributes.length - 1].trim()));
		setWriterName(attributes[0].trim());
		setHeadline(attributes);
		
	}
	
	private void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	private void setHeadline(String[] attributes) {
		for(int i = 1; i < attributes.length - 1; i++){
			headline += attributes[i] + " ";
		}
		headline = headline.trim();
	}

	private void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getWriterName() {
		return writerName;
	}
	public String getHeadline() {
		return headline;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public String getPassage() {
		return passage;
	}
	public void setPassage(String passage) {
		this.passage = passage;
	}
	
	private Date parseDateToUtilDate(String dateString){
		Date myDate;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
		try {
			myDate = sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return myDate;
	}

}
