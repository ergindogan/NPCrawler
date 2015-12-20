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
	
	public Map<NewsPaper,Map<String,List<Passage>>> loadData(boolean paragraph){
		System.out.println("Load Started!");
		long startTime = System.currentTimeMillis();
		
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
		Map<String,List<Passage>> myMap;
		
		for(File fileToAnalyse:getFileToRead().listFiles(myFileter)){
			NewsPaper currentNewsPaper = getNewsPaper(fileToAnalyse.getName());
			
			passageReader = new PassageReader(fileToAnalyse);
			if(paragraph){
				myMap = passageReader.readPassagesWithSeperatorsIntoMap();
			}else{
				myMap = passageReader.readPassagesIntoMap();
			}
			
			dataMap.put(currentNewsPaper, myMap);
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Load Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to load " + humanReadableByteCount(folderSize(getFileToRead()), true) + " of data.");
		System.out.println("Number of total passages read : " + getTotalPassageCount(dataMap));
		
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
	
	private String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	private int getTotalPassageCount(Map<NewsPaper,Map<String,List<Passage>>> myMap){
		int counter = 0;
		for(NewsPaper newsPaper:myMap.keySet()){
			Map<String,List<Passage>> innerMap = myMap.get(newsPaper);
			for(String authorName : innerMap.keySet()){
				List<Passage> passages = innerMap.get(authorName);
				counter = counter + passages.size();
			}
		}
		return counter;
	}
	
	private long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile()){
	        	if(file.getName().contains("Crawler")){
	        		length += file.length();
	        	}
	        }else{
	        	length += folderSize(file);
	        }
	    }
	    return length;
	}

}
