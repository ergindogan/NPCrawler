package tr.com.ergindogan.stopword.classifier.nominal;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
import tr.com.ergindogan.stopword.loader.DistinctAuthorLoader;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import tr.com.ergindogan.stopword.reader.stopword.StopWordReader;

public class PipelineNominal {
	
	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/koklerine_ayrilmis");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, 200, -1, true, -1);
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		
		File myFrequencyFile = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/"
				+ "koklerine_ayrilmis/wordFrequency.txt");
		
		StopWordReader stopWordReader = new StopWordReader(myFrequencyFile);
		LinkedHashMap<String, Integer> myWordMap = stopWordReader.readFileToMap(190,30);
		
		//Classify...
		NominalNBClassifier myClassifier = new NominalNBClassifier(myMap, myWordMap, 90, 10);
		myClassifier.classify();
		
	}

}
