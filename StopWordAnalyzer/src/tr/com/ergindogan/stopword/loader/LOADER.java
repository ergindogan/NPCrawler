package tr.com.ergindogan.stopword.loader;

import java.io.File;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class LOADER {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/Loadtest");
		
		NewsPaperLoader loader = new NewsPaperLoader(folderToLoad);
		
		System.out.println("Load Started!");
		long startTime = System.currentTimeMillis();
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData();
		long endTime   = System.currentTimeMillis();
		System.out.println("Load Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to load " + humanReadableByteCount(folderSize(folderToLoad), true) + " of data.");
		System.out.println("Number of total passages read : " + getTotalPassageCount(myMap));
	}
	
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static int getTotalPassageCount(Map<NewsPaper,Map<String,List<Passage>>> myMap){
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
}
