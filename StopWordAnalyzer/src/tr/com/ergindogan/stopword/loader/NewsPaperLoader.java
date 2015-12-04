package tr.com.ergindogan.stopword.loader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.reader.BaseReader;
import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import tr.com.ergindogan.stopword.reader.passage.PassageReader;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class NewsPaperLoader extends BaseReader{

	/**
	 * @param fileToRead
	 */
	public NewsPaperLoader(File fileToRead) {
		super(fileToRead);
	}
	
	public Map<NewsPaper,Map<String,List<Passage>>> loadData(){
		PassageReader passageReader;
		Map<NewsPaper,Map<String,List<Passage>>> dataMap = new HashMap<NewsPaper,Map<String,List<Passage>>>();
		
		FilenameFilter myFileter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.contains("Crawler")){
					return true;
				}else{
					return false;
				}
			}
		};
		
		for(File fileToAnalyse:getFileToRead().listFiles(myFileter)){
			NewsPaper currentNewsPaper = getNewsPaper(fileToAnalyse.getName());
			
			passageReader = new PassageReader(fileToAnalyse);
			Map<String,List<Passage>> myMap = passageReader.readPassagesIntoMap();
			
			dataMap.put(currentNewsPaper, myMap);
		}
		
		return dataMap;
	}
	
	private NewsPaper getNewsPaper(String fileName){
		for(NewsPaper newsPaperName:NewsPaper.values()){
			if(fileName.toLowerCase().contains(newsPaperName.toString().toLowerCase().substring(0, 5))){
				return newsPaperName;
			}
		}
		return NewsPaper.UNDEFINED;
	}

}
