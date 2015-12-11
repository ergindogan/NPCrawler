package tr.com.ergindogan.stopword.classifier.feature;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Corpus.Sentence;
import Dictionary.Word;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class VocublaryExtendFeature extends Feature{
	
	
	public VocublaryExtendFeature(){
		super();
	}

	/* (non-Javadoc)
	 * @see tr.com.ergindogan.stopword.classifier.FeatureExtructable#extractFeatureResult()
	 */
	@Override
	public double extractFeatureResult(String passage) {
		int totalWordCount = 0;
		Set<String> distinctWords = new HashSet<String>();
		List<Sentence> wordList = splitPassageIntoSentences(passage);
		
		for(Sentence sentence : wordList){
			for(Word word : sentence.getWords()){
				distinctWords.add(word.getName().toLowerCase());
				totalWordCount++;
			}
		}
		
		return distinctWords.size() / totalWordCount;
	}

}
