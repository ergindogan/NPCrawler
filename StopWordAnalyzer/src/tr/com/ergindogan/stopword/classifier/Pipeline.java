package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
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

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Pipeline {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		
		//tokmak : 1000
		//emin çölaşan : 870
		//saygı öztürk : 610
		//abdülkadir selvi : 740
		//uğur dündar : 720
		//Burada yazarlarin en az 100 yazisi ve tam 100 ile bolunur sayida yazisini myMap icine koyduk.
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, -1, true, -1);
		
		System.out.println(myMap.keySet().size() + " authors to test.");
		
//		for(String authorName : myMap.keySet()){
//			System.out.println(authorName + " : " + myMap.get(authorName).size());
//		}
		
		//Add features to feature list.
		List<Feature> features = new ArrayList<Feature>();
		features.add(new AvarageParagraphLengthFeature());
		features.add(new PunctuationCountFeature());
		features.add(new VocublaryExtendFeature());
		features.add(new AvarageWordLengthFeature());
		features.add(new SentenceLengthAsWordCountFeature());
//		features.add(new WordCountFeature());
		
		//Classify...
		NBClassifier myClassifier = new NBClassifier(myMap, features, 90, 10);
		myClassifier.classify();
		
	}

}
