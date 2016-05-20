package tr.com.ergindogan.stopword.loader;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class LoaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe");
		
//		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
//		Map<String,List<Passage>> myMap = loader.loadData(true);
//		
//		int paragraphCounter = 0;
//		for(String authorName : myMap.keySet()){
//			paragraphCounter = 0;
//			for(Passage passage:myMap.get(authorName)){
//				paragraphCounter += passage.getParagraphs().size();
//			}
//			System.out.println("Author : " + authorName + ", Total passage count : " + myMap.get(authorName).size() + "Total paragraph count : " + paragraphCounter);
//		}
		
		NewsPaperLoader loader = new NewsPaperLoader(folderToLoad);
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData(true);
		
		for(NewsPaper newsPaper:myMap.keySet()){
			int newsPaperCoulmCounter = 0;
			Map<String,List<Passage>> innerMap = myMap.get(newsPaper);
			for(String authorName : innerMap.keySet()){
				List<Passage> passages = innerMap.get(authorName);
				newsPaperCoulmCounter = newsPaperCoulmCounter + passages.size();
			}
			
			System.out.println(newsPaper.toString() + " yazar sayisi : " + innerMap.keySet().size() + " yazı sayısı : " + newsPaperCoulmCounter);
		}
		
	}
}
