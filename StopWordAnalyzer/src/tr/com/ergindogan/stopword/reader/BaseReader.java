package tr.com.ergindogan.stopword.reader;

import java.io.File;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public abstract class BaseReader {
	
	private File fileToRead;
	
	public BaseReader(File fileToRead){
		setFileToRead(fileToRead);
	}

	public File getFileToRead() {
		return fileToRead;
	}

	public void setFileToRead(File fileToRead) {
		this.fileToRead = fileToRead;
	}

}
