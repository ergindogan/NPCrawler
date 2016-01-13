package tr.com.ergindogan.stopword.classifier.crossover;



/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CrossValidationItem<T> {
	
	private T item;
	private boolean tested;
	private boolean pickedForTesting;
	
	public CrossValidationItem(T item){
		setItem(item);
		setTested(false);
		setPickedForTesting(false);
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
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
