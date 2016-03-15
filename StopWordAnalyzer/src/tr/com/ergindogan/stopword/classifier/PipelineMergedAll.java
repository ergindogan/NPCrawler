package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Dictionary.Pos;
import WordNet.Literal;
import WordNet.SynSet;
import WordNet.WordNet;
import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
import tr.com.ergindogan.stopword.classifier.feature.AvarageParagraphLengthFeature;
import tr.com.ergindogan.stopword.classifier.feature.AvarageWordLengthFeature;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.feature.PunctuationCountFeature;
import tr.com.ergindogan.stopword.classifier.feature.SentenceLengthAsWordCountFeature;
import tr.com.ergindogan.stopword.classifier.feature.VocublaryExtendFeature;
import tr.com.ergindogan.stopword.loader.DistinctAuthorLoader;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import tr.com.ergindogan.stopword.reader.stopword.StopWordReader;

public class PipelineMergedAll {

	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe");
		
		File myFrequencyFile = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe/wordFrequency.txt");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, 200, -1, true, 50);
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		printAuthorAndPassageCounts(myMap);
		
		//Select features
		List<Feature> features = new ArrayList<Feature>();
		features.add(new AvarageParagraphLengthFeature());
		features.add(new PunctuationCountFeature());
		features.add(new VocublaryExtendFeature());
		features.add(new AvarageWordLengthFeature(false));
		features.add(new SentenceLengthAsWordCountFeature());
		
		//Load nominal words...
		StopWordReader stopWordReader = new StopWordReader(myFrequencyFile);
		LinkedHashMap<String, Integer> nominalWordsMap = stopWordReader.readFileToMap(190,30);
		
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
		
		ClassifierMergedAll myClassifier = new ClassifierMergedAll(myMap, 
																	features, 
																	nominalWordsMap, 
																	synonymSetMap, 
																	synDic, 90, 10);
		myClassifier.classify();
	}

	private static void printAuthorAndPassageCounts(Map<String,List<Passage>> myMap){
		for(String authorName:myMap.keySet()){
			System.out.println("Author : " + authorName + " Passage Count : " + myMap.get(authorName).size());
		}
	}
}
