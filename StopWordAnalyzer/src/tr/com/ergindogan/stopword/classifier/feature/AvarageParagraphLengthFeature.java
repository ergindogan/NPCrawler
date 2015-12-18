package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

public class AvarageParagraphLengthFeature  extends Feature {
	
	public AvarageParagraphLengthFeature(){
		super();
	}

	@Override
	public double extractFeatureResult(String passage) {
		return splitPassageIntoWords(passage).size();
	}
	
	public double extractFeatureResult(List<String> paragraphs){
		double totalWords = 0.0;
		
		for(int i = 0; i < paragraphs.size(); i++){
			totalWords += splitPassageIntoWords(paragraphs.get(i)).size();
		}
		
		return totalWords/paragraphs.size();
	}

}
