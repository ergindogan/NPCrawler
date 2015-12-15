package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

import Corpus.Sentence;

public class SentenceLengthAsWordCountFeature extends Feature {

	public SentenceLengthAsWordCountFeature() {
		super();
	}

	@Override
	public double extractFeatureResult(String passage) {
		double sentenceLengthAsWordCountTotal = 0.0;
		List<Sentence> sentences = splitPassageIntoSentences(passage);
		for(Sentence sentence:sentences){
			sentenceLengthAsWordCountTotal += sentence.getWords().size();
		}
		return sentenceLengthAsWordCountTotal / sentences.size();
	}

}
