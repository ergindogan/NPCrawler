package tr.com.ergindogan.stopword.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tr.com.ergindogan.stopword.classifier.crossover.CrossoverType;
import tr.com.ergindogan.stopword.reader.BaseReader;
import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class DistinctAuthorLoader extends BaseReader {

	/**
	 * @param fileToRead
	 */
	public DistinctAuthorLoader(File fileToRead) {
		super(fileToRead);
	}
	
	public Map<String,List<Passage>> loadAndSelectQualifiedAuthors(CrossoverType type){
		List<Passage> tempList = new ArrayList<Passage>();
		Map<String,List<Passage>> qualifiedAuthorMap = new HashMap<String,List<Passage>>();
		int dividor = 0;
		
		if(type == CrossoverType._90_10){
			dividor = 100;
		}
		
		Map<String,List<Passage>> distinctAuthorMap = loadData();
		
		for(String authorName : distinctAuthorMap.keySet()){
			List<Passage> passages = distinctAuthorMap.get(authorName);
			if(passages.size() > dividor){
				int passagesToSelect = passages.size() - (passages.size() % dividor);
				for(int i = 0; i < passagesToSelect; i++){
					tempList.add(passages.get(i));
				}
			}
			qualifiedAuthorMap.put(authorName, tempList);
			tempList = new ArrayList<Passage>();
		}
		
		return qualifiedAuthorMap;
	}
	
	public Map<String,List<Passage>> loadData(){
		Map<String,List<Passage>> distinctAuthorMap = new HashMap<String,List<Passage>>();
		
		NewsPaperLoader loader = new NewsPaperLoader(getFileToRead());
		
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData();
		
		Set<String> authorSet = new HashSet<String>();
		
		//getting distinct and total author list.
		for(NewsPaper newsPaper : NewsPaper.values()){
			if(myMap.containsKey(newsPaper)){
				Map<String,List<Passage>> authorMap = myMap.get(newsPaper);
				for(String authorName : authorMap.keySet()){
					authorSet.add(authorName.toLowerCase());
				}
			}
		}
		
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
		
		return distinctAuthorMap;
		
	}

}
