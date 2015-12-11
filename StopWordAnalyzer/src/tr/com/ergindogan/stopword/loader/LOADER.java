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
		
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData();
		
	}
}
