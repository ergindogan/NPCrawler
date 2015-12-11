package tr.com.ergindogan.stopword.classifier.feature;

import java.util.ArrayList;
import java.util.List;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Dictionary.Word;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public abstract class Feature implements FeatureExtructable{
	
	private static TurkishSplitter ts;
	
	protected Feature(){
		ts = new TurkishSplitter();
	}
	
	protected List<Sentence> splitPassageIntoSentences(String passage){
		return ts.split(passage);
	}
	
	protected List<String> splitPassageIntoWords(String passage){
		List<String> wordList = new ArrayList<String>();
		
		List<Sentence> sentenceList = splitPassageIntoSentences(passage);
		
		for(Sentence sentence:sentenceList){
			for(Word word:sentence.getWords()){
				wordList.add(word.getName());
			}
		}
		
		return wordList;
	}
	
}
