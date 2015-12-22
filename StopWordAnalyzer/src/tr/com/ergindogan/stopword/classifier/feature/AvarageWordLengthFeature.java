package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

/**
 * @author ergindoganyildiz
 * 
 * Dec 15, 2015
 */
public class AvarageWordLengthFeature extends Feature {
	
	private boolean applyMorphologicalAnalysis;
	
	public AvarageWordLengthFeature(boolean applyMorphologicalAnalysis){
		super();
		setApplyMorphologicalAnalysis(applyMorphologicalAnalysis);
	}

	@Override
	public double extractFeatureResult(String passage) {
		double totalWordLength = 0.0;
		
		List<String> words;
		
		if(applyMorphologicalAnalysis()){
			words = splitPassageIntoWordsApplyHMM(passage);
		}else{
			words = splitPassageIntoWords(passage);
		}
		
		for(String word:words){
			totalWordLength += word.length();
		}
		return totalWordLength / words.size();
	}

	public boolean applyMorphologicalAnalysis() {
		return applyMorphologicalAnalysis;
	}

	public void setApplyMorphologicalAnalysis(boolean applyMorphologicalAnalysis) {
		this.applyMorphologicalAnalysis = applyMorphologicalAnalysis;
	}

}
