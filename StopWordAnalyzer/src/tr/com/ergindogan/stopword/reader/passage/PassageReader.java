package tr.com.ergindogan.stopword.reader.passage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.reader.BaseReader;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class PassageReader extends BaseReader{
	
	public PassageReader(File fileToRead){
		super(fileToRead);
	}
	
	public Map<String,List<Passage>> readPassagesWithSeperatorsIntoMap(){
		BufferedReader br = null;
		String myPassageString = "";
		
		List<Passage> myPassageList;
		Map<String,List<Passage>> passageMap = new HashMap<String,List<Passage>>();
		
		try {

			String sCurrentLine;
			Passage passage = new Passage();
			String myRecord = "";

			br = new BufferedReader(new FileReader(getFileToRead().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().equals(CONSTANTS.SEPERATOR)){
					//Record is complete
					myPassageString = "";
					String parts[] = myRecord.split(CONSTANTS.RECORD_PART_SEPERATOR);
					parts = removeEmptyElements(parts);
					
					if(parts.length == 0){
						//No record found to add.
						myRecord = "";
						continue;
					}
					
					String [] paragraphs = getPassage(parts).split("_______");
					passage.setParagraphs(Arrays.asList(paragraphs));
					
					for(int i = 0; i < paragraphs.length; i++){
						myPassageString += paragraphs[i];
					}
					passage.setPassage(myPassageString);
					
					if(myPassageString.isEmpty() || myPassageString.contains("Read timed out") 
							|| myPassageString.contains("HTTP error fetching URL") 
							|| myPassageString.contains("Yazi alinamadi.")){
						myRecord = "";
						continue;
					}
					
					passage.setWriterNameHeadlineDate(parts[0]);
					
					if(passageMap.containsKey(passage.getAuthor())){
						passageMap.get(passage.getAuthor()).add(passage);
					}else{
						myPassageList = new ArrayList<Passage>();
						myPassageList.add(passage);
						
						//There are some passages inside of the file that we couldn't retrieve. No need to put them
						//into map.
						if(!passage.getAuthor().isEmpty()){
							passageMap.put(passage.getAuthor(), myPassageList);
						}
					}
					passage = new Passage();
					
					myRecord = "";
				}else{
					myRecord = myRecord + sCurrentLine.trim() +  CONSTANTS.RECORD_PART_SEPERATOR;
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
		
		return passageMap;
	}
	
	public Map<String,List<Passage>> readPassagesIntoMap(){
		BufferedReader br = null;
		
		List<Passage> myPassageList;
		Map<String,List<Passage>> passageMap = new HashMap<String,List<Passage>>();
		
		try {

			String sCurrentLine;
			Passage passage = new Passage();
			String myRecord = "";

			br = new BufferedReader(new FileReader(getFileToRead().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().equals(CONSTANTS.SEPERATOR)){
					//Record is complete
					String parts[] = myRecord.split(CONSTANTS.RECORD_PART_SEPERATOR);
					parts = removeEmptyElements(parts);
					
					if(parts.length == 0){
						//No record found to add.
						myRecord = "";
						continue;
					}
					
					passage.setWriterNameHeadlineDate(parts[0]);
					passage.setPassage(getPassage(parts));
					
					if(passageMap.containsKey(passage.getAuthor())){
						passageMap.get(passage.getAuthor()).add(passage);
					}else{
						myPassageList = new ArrayList<Passage>();
						myPassageList.add(passage);
						
						//There are some passages inside of the file that we couldn't retrieve. No need to put them
						//into map.
						if(!passage.getAuthor().isEmpty()){
							passageMap.put(passage.getAuthor(), myPassageList);
						}
					}
					passage = new Passage();
					
					myRecord = "";
				}else{
					myRecord = myRecord + sCurrentLine.trim() +  CONSTANTS.RECORD_PART_SEPERATOR;
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
		
		return passageMap;
	}
	
	private String getPassage(String parts[]){
		String passage = "";
		for(int i = 1; i < parts.length; i++){
			passage += parts[i] + " ";
		}
		passage = passage.trim();
		return passage;
	}
	
	private String[] removeEmptyElements(String parts[]){
		List<String> elements = new ArrayList<String>();
		for(int i = 0; i < parts.length; i++){
			if(!parts[i].isEmpty()){
				elements.add(parts[i]);
			}
		}
		return elements.toArray(new String[elements.size()]);
	}

}
