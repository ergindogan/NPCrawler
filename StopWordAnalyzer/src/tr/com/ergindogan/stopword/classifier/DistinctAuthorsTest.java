package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tr.com.ergindogan.stopword.loader.NewsPaperLoader;
import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class DistinctAuthorsTest {
	
	public static void main(String args[]){
		
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/Loadtest");
		
		NewsPaperLoader loader = new NewsPaperLoader(folderToLoad);
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData(true);
		
		Map<String,List<Passage>> distinctAuthorMap = new HashMap<String,List<Passage>>();
		
		Set<String> authorSet = new HashSet<String>();
		List<String> authorList = new ArrayList<String>();
		
		//getting distinct and total author list.
		for(NewsPaper newsPaper : NewsPaper.values()){
			if(myMap.containsKey(newsPaper)){
				Map<String,List<Passage>> authorMap = myMap.get(newsPaper);
				for(String authorName : authorMap.keySet()){
					authorSet.add(authorName.toLowerCase());
					authorList.add(authorName.toLowerCase());
				}
			}
		}
		
		System.out.println("Toplam yazar sayısı : " + authorList.size());
		System.out.println("Farklı yazar sayısı : " + authorSet.size());
		
		//create distinct author map.
		for (Iterator<String> iterator = authorSet.iterator(); iterator.hasNext();) {
			String authorName = (String) iterator.next();
			distinctAuthorMap.put(authorName, new ArrayList<Passage>());
		}
		
		//putting passages into their unique author lists. This is above newspaper.
		for(NewsPaper newsPaper : NewsPaper.values()){
			if(myMap.containsKey(newsPaper)){
				Map<String,List<Passage>> authorMap = myMap.get(newsPaper);
				for(String authorName : authorMap.keySet()){
					distinctAuthorMap.get(authorName.toLowerCase()).addAll(authorMap.get(authorName));
				}
			}
		}
		
		for(String authorName : distinctAuthorMap.keySet()){
			System.out.println("Author : " + authorName + ", Total passage count : " + distinctAuthorMap.get(authorName).size());
		}
		
		
	}
	

}
