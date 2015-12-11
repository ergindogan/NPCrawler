package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;

import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class FeatureExtractor {
	
	private List<Feature> features;
	private Passage passage;
	
	public FeatureExtractor(List<Feature> features, Passage passage){
		setFeatures(features);
		setPassage(passage);
	}
	
	public FeatureVector extractFeatureVector(){
		FeatureVector featureVector = new FeatureVector(getFeatures(), getPassage().getPassage());
		return featureVector;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public Passage getPassage() {
		return passage;
	}

	public void setPassage(Passage passage) {
		this.passage = passage;
	}

}
