package tr.com.ergindogan.stopword.classifier.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.SentenceMorphParse;
import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Dictionary.Word;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public abstract class Feature implements FeatureExtructable{
	
	private static TurkishMorphParser morphParser;
	private static Z3MarkovModelDisambiguator disambiguator;
	private static TurkishSentenceParser sentenceParser;
	private static TurkishSplitter ts;
	
	//No need to call load multiple times.
	static{
		try {
			ts = new TurkishSplitter();
			morphParser = TurkishMorphParser.createWithDefaults();
			disambiguator = new Z3MarkovModelDisambiguator();
			sentenceParser = new TurkishSentenceParser(morphParser, disambiguator);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Feature(){
		
	}
	
	public static List<Sentence> splitPassageIntoSentences(String passage){
		return ts.split(passage);
	}
	
	public static List<String> splitPassageIntoWords(String passage){
		List<String> wordList = new ArrayList<String>();
		
		List<Sentence> sentenceList = splitPassageIntoSentences(passage);
		
		for(Sentence sentence:sentenceList){
			for(Word word:sentence.getWords()){
				wordList.add(word.getName());
			}
		}
		
		return wordList;
	}
	
	protected List<String> splitPassageIntoWordsApplyHMM(String passage){
		List<String> wordList = new ArrayList<String>();
		
		List<Sentence> sentenceList = splitPassageIntoSentences(passage);
		
		for (Sentence sentence : sentenceList) {
			try {
				SentenceMorphParse sentenceParse = sentenceParser.parse(sentence.toString());
				sentenceParser.disambiguate(sentenceParse);
				
				for (SentenceMorphParse.Entry entry : sentenceParse) {
		            wordList.add(entry.parses.get(0).getLemma());
		        }
			} catch (Exception e) {
				System.out.println(sentence.toString());
				continue;
			}
		}
		
		return wordList;
	}
	
}
