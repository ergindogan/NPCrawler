package tr.com.ergindogan.stopword.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class StopWordReader {
	
	private File fileToRead;
	
	public StopWordReader(File fileToRead){
		setFileToRead(fileToRead);
	}
	
	public LinkedHashMap<String, Integer> readFileToMap(){
		LinkedHashMap<String, Integer> myWordMap = new LinkedHashMap<String, Integer>();
		
		BufferedReader br = null;
		
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(getFileToRead().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null) {
				String[] keyAndValue = sCurrentLine.split("__");
				
				String key = keyAndValue[0].trim();
				Integer value = Integer.parseInt(keyAndValue[1].trim());
				
				myWordMap.put(key, value);
			}
			
		} catch (IOException e) {
			System.err.println("Error while reading from file. Path : " + getFileToRead().getAbsolutePath());
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				System.out.println("Error while closing file. Path : " + getFileToRead().getAbsolutePath());
			}
		}
		
		System.out.println("File successfully read to map...");
		
		return myWordMap;
	}

	public File getFileToRead() {
		return fileToRead;
	}

	private void setFileToRead(File fileToRead) {
		this.fileToRead = fileToRead;
	}

}
