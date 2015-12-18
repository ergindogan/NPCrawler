package tr.com.ergindogan.stopword.reader.passage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class Passage {
	
	private String author;
	private String title;
	private Date publishDate;
	private String passage;
	
	private List<String> paragraphs;
	
	/**
	 * @param author
	 * @param title
	 * @param publishDate
	 * @param passage
	 */
	public Passage(String author, String title, Date publishDate, String passage) {
		super();
		setAuthor(author);
		setTitle(title);
		setPublishDate(publishDate);
		setPassage(passage);
	}
	
	public Passage(){
		setTitle("");
		setAuthor("");
		setPassage("");
	}
	
	public void setWriterNameHeadlineDate(String myLine){
		String[] attributes = myLine.split("-");
		
		setPublishDate(parseDateToUtilDate(attributes[attributes.length - 1].trim()));
		setAuthor(attributes[0].trim());
		setTitle(attributes);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	private void setTitle(String[] attributes){
		for(int i = 1; i < attributes.length - 1; i++){
			title += attributes[i] + " ";
		}
		title = title.trim();
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
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

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<String> paragraphs) {
		this.paragraphs = paragraphs;
	}
}
