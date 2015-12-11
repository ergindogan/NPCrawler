package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class WordCountFeature extends Feature{
	
	public WordCountFeature(){
		super();
	}

	/* (non-Javadoc)
	 * @see tr.com.ergindogan.stopword.classifier.feature.FeatureExtructable#extractFeatureResult(tr.com.ergindogan.stopword.reader.passage.Passage)
	 */
	@Override
	public double extractFeatureResult(String passage) {
		List<String> wordList = splitPassageIntoWords(passage);
		
		return wordList.size();
	}

}
