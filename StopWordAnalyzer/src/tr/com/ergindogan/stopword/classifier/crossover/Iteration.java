package tr.com.ergindogan.stopword.classifier.crossover;

import java.util.List;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Iteration {
	
	private List<Integer> trainIndexes;
	private List<Integer> testIndexes;
	
	public Iteration(List<Integer> trainIndexes, List<Integer> testIndexes){
		setTrainIndexes(trainIndexes);
		setTestIndexes(testIndexes);
	}

	public List<Integer> getTrainIndexes() {
		return trainIndexes;
	}

	public void setTrainIndexes(List<Integer> trainIndexes) {
		this.trainIndexes = trainIndexes;
	}

	public List<Integer> getTestIndexes() {
		return testIndexes;
	}

	public void setTestIndexes(List<Integer> testIndexes) {
		this.testIndexes = testIndexes;
	}
	
	public void addToTrainIndexes(int index){
		getTrainIndexes().add(index);
	}
	
	public void addToTestIndexes(int index){
		getTestIndexes().add(index);
	}

}
