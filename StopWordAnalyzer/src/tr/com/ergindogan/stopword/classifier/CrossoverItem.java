package tr.com.ergindogan.stopword.classifier;

import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CrossoverItem {
	
	private Passage passage;
	private boolean tested;
	private boolean pickedForTesting;
	
	public CrossoverItem(Passage passage){
		setPassage(passage);
		setTested(false);
		setPickedForTesting(false);
	}

	public Passage getPassage() {
		return passage;
	}

	public void setPassage(Passage passage) {
		this.passage = passage;
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
