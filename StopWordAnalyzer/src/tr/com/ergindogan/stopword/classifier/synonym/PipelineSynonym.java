package tr.com.ergindogan.stopword.classifier.synonym;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
import tr.com.ergindogan.stopword.loader.DistinctAuthorLoader;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import Dictionary.Pos;
import WordNet.SynSet;
import WordNet.WordNet;

public class PipelineSynonym {

	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/koklerine_ayrilmis");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, 200, -1, true, -1);
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		
		WordNet wordNet = new WordNet("/Users/ergindoganyildiz/Downloads/balkanet.xml", new Locale("tr"));
		
		LinkedHashMap<String, String> synonymSetMap = new LinkedHashMap<String, String>();
		
		for(SynSet ss:wordNet.getSynSets()){
			if(ss.getPos().equals(Pos.NOUN)){
				synonymSetMap.put(ss.getId(), ss.getSynonym().getLiteralString());
			}
		}
		
		//Classify...
		SynonymNBClassifier myClassifier = new SynonymNBClassifier(myMap, synonymSetMap, 90, 10);
		myClassifier.classify();
		
	}
	
}
