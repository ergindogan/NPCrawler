package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;
import java.util.Vector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class FeatureVector {
	
	private Vector<Double> vector;
	
	public FeatureVector(List<Feature> features, String passage){
		for(Feature feature : features){
			double value = feature.extractFeatureResult(passage);
			addToVector(value);
		}
	}

	public Vector<Double> getVector() {
		return vector;
	}

	public void setVector(Vector<Double> vector) {
		this.vector = vector;
	}
	
	private void addToVector(double value){
		getVector().add(value);
	}

}
