package tr.com.ergindogan.stopword.classifier.crossover;

import tr.com.ergindogan.stopword.classifier.feature.FeatureVector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CrossValidationItem {
	
	private FeatureVector vector;
	private boolean tested;
	private boolean pickedForTesting;
	
	public CrossValidationItem(FeatureVector vector){
		setVector(vector);
		setTested(false);
		setPickedForTesting(false);
	}

	public FeatureVector getVector() {
		return vector;
	}

	public void setVector(FeatureVector vector) {
		this.vector = vector;
	}

	public boolean isTested() {
		return tested;
	}

	public void setTested(boolean tested) {
		this.tested = tested;
	}

	public boolean isPickedForTesting() {
		return pickedForTesting;
	}

	public void setPickedForTesting(boolean pickedForTesting) {
		this.pickedForTesting = pickedForTesting;
	}

}
