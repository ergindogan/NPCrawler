package tr.com.ergindogan.stopword.reader.passage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class PassageReaderTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File fileToRead = new File("/Users/ergindoganyildiz/Desktop/LoadTest/SozcuCrawler.txt");
		
		PassageReader passageReader = new PassageReader(fileToRead);
		Map<String,List<Passage>> myMap = passageReader.readPassagesIntoMap();
		
		for(String authorName:myMap.keySet()){
			List<Passage> passageList = myMap.get(authorName);
			System.out.println("Author name : " + authorName + ", Passages read : " + passageList.size());
		}
	}

}
