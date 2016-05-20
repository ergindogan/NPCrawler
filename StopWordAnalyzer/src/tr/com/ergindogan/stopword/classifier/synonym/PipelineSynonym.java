package tr.com.ergindogan.stopword.classifier.synonym;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
import tr.com.ergindogan.stopword.loader.DistinctAuthorLoader;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import Dictionary.Pos;
import WordNet.Literal;
import WordNet.SynSet;
import WordNet.WordNet;

public class PipelineSynonym {

	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/koklerine_ayrilmis");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, 310, 120, true, 2);
		
		for(String authorName : myMap.keySet()){
			System.out.println(authorName + " : " + myMap.get(authorName).size());
		}
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		
		WordNet wordNet = new WordNet("/Users/ergindoganyildiz/Downloads/balkanet.xml", new Locale("tr"));
		
		LinkedHashMap<String, List<Literal>> synonymSetMap = new LinkedHashMap<String, List<Literal>>();
		
		
		
		LinkedHashMap<String, List<String>> synDic = new LinkedHashMap<String, List<String>>();
		
		//Construct lookup synset dictionary and synsetMap
		for(SynSet ss:wordNet.getSynSets()){
			if(ss.getPos().equals(Pos.NOUN)){
				List<Literal> literals = ss.getSynonym().getLiterals();
				synonymSetMap.put(ss.getId(), literals);
				
				for(Literal literal:literals){
					String meaning = literal.getName();
					List<String> ids = synDic.get(meaning);
					
					if(ids == null){
						ids = new ArrayList<String>();
					}
					
					ids.add(ss.getId());
					synDic.put(meaning, ids);
					
				}
				
			}
		}
		
		System.out.println("Synset size : " + synonymSetMap.size());
		
		System.out.println(synDic.get("baba"));
		System.out.println(synDic.get("peder"));
		
		//Classify...
		SynonymNBClassifier myClassifier = new SynonymNBClassifier(myMap, synonymSetMap, synDic, 90, 10);
		myClassifier.classify();
		
	}
	
}
