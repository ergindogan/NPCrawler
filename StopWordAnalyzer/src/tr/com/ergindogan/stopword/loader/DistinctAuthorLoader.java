package tr.com.ergindogan.stopword.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
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
	
	//We have to ignore passages that has a title named null. Because these passages couldn't fetched and their
	//body equals to Read time out.
	public Map<String,List<Passage>> loadAndSelectQualifiedAuthors(CrossValidationType type, int topPassageCount, boolean paragraphs, int yazarSayisi){
		List<Passage> tempList = new ArrayList<Passage>();
		List<Passage> finalTempList = new ArrayList<Passage>();
		
		int eklenenYazarSayisi = 0;
		
		Map<String,List<Passage>> qualifiedAuthorMap = new HashMap<String,List<Passage>>();
		int dividor = 0;
		int passageAdded = 0;
		
		if(type == CrossValidationType._90_10){
			dividor = 10;
		}
		
		Map<String,List<Passage>> distinctAuthorMap = loadData(paragraphs);
		
		for(String authorName : distinctAuthorMap.keySet()){
			List<Passage> passages = distinctAuthorMap.get(authorName);
			if(passages.size() >= dividor){
				int passagesToSelect = passages.size() - (passages.size() % dividor);
				if(topPassageCount > 0 && topPassageCount % 10 == 0 && passagesToSelect >= topPassageCount){
					passagesToSelect = topPassageCount;
				}
				for(int i = 0; i < passages.size(); i++){
					if(!passages.get(i).getTitle().equals("null")){
						if(passageAdded == passagesToSelect){
							break;
						}else{
							tempList.add(passages.get(i));
							passageAdded++;
						}
					}
				}
				if(tempList.size() >= dividor){
					int finalPassagesToSelect = 0;
					if(tempList.size() % dividor != 0){
						finalPassagesToSelect = tempList.size() - (tempList.size() % dividor);
						for(int i = 0; i < finalPassagesToSelect; i++){
							finalTempList.add(tempList.get(i));
						}
						qualifiedAuthorMap.put(authorName, finalTempList);
						eklenenYazarSayisi++;
					}else{
						qualifiedAuthorMap.put(authorName, tempList);
						eklenenYazarSayisi++;
					}
				}
				finalTempList = new ArrayList<Passage>();
				tempList = new ArrayList<Passage>();
				passageAdded = 0;
			}
			if(yazarSayisi > 0 && eklenenYazarSayisi == yazarSayisi){
				break;
			}
		}
		
		return qualifiedAuthorMap;
	}
	
	public Map<String,List<Passage>> loadData(boolean paragraphs){
		Map<String,List<Passage>> distinctAuthorMap = new HashMap<String,List<Passage>>();
		
		NewsPaperLoader loader = new NewsPaperLoader(getFileToRead());
		
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData(paragraphs);
		
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
