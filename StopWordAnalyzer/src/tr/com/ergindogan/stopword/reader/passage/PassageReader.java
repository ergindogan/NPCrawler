package tr.com.ergindogan.stopword.reader.passage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	
	public Map<String,List<Passage>> readPassagesIntoMap(){
		BufferedReader br = null;
		int i = 0;
		
		List<Passage> myPassageList;
		Map<String,List<Passage>> passageMap = new HashMap<String,List<Passage>>();
		
		try {

			String sCurrentLine;
			Passage passage = new Passage();

			br = new BufferedReader(new FileReader(getFileToRead().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null) {
				if(i % 5 == 0){
					passage.setWriterNameHeadlineDate(sCurrentLine);
				}else if(i % 5 == 1){
					passage.setPassage(sCurrentLine);
				}else if(i % 5 == 4){
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
				}
				i++;
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

}
