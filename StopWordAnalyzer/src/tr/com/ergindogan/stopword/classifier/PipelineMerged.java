package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class PipelineMerged {

	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe");
		
		File myFrequencyFile = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe/wordFrequency.txt");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, 200, -1, true, -1);
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		
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
		
		ClassifierMerged myClassifier = new ClassifierMerged(myMap, features, nominalWordsMap, 90, 10);
		myClassifier.classify();
	}

}
