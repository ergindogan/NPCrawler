package tr.com.ergindogan.stopword.reader.stopword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import tr.com.ergindogan.stopword.reader.BaseReader;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class StopWordReader extends BaseReader{
	
	public StopWordReader(File fileToRead){
		super(fileToRead);
	}
	
	public LinkedHashMap<String, Integer> readFileToMap(int wordCount){
		LinkedHashMap<String, Integer> myWordMap = new LinkedHashMap<String, Integer>();
		
		BufferedReader br = null;
		int counter = 0;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(getFileToRead().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null && ( wordCount <= 0 || counter < wordCount) ) {
				String[] keyAndValue = sCurrentLine.split("__");
				
				String key = keyAndValue[0].trim();
				
				if(!key.equals("UNK")){
					Integer value = Integer.parseInt(keyAndValue[1].trim());
					
					myWordMap.put(key, value);
					counter++;
				}
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

}
