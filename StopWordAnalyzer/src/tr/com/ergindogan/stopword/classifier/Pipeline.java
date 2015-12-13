package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossoverType;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.feature.VocublaryExtendFeature;
import tr.com.ergindogan.stopword.classifier.feature.WordCountFeature;
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
		File folderToLoad = new File("C:/Users/ergindogan/Desktop/Loadtest");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		//Burada yazarlarin en az 100 yazisi ve tam 100 ile bolunur sayida yazisini myMap icine koyduk.
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossoverType._90_10);
		
		//Add features to feature list.
		List<Feature> features = new ArrayList<Feature>();
		features.add(new VocublaryExtendFeature());
		features.add(new WordCountFeature());
		
		//Classify...
		NBClassifier myClassifier = new NBClassifier(myMap, features, 90, 10);
		myClassifier.classify();
		
	}

}
