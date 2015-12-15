package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

/**
 * @author ergindoganyildiz
 * 
 * Dec 15, 2015
 */
public class AvarageWordLengthFeature extends Feature {
	
	public AvarageWordLengthFeature(){
		super();
	}

	@Override
	public double extractFeatureResult(String passage) {
		double totalWordLength = 0.0;
		List<String> words = splitPassageIntoWords(passage);
		for(String word:words){
			totalWordLength += word.length();
		}
		return totalWordLength / words.size();
	}

}
