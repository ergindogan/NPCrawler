package tr.com.ergindogan.stopword.classifier.test;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class TestResult {
	
	private double correctGuessCounter;
	private double falseGuessCounter;
	
	public TestResult(){
		setCorrectGuessCounter(0);
		setFalseGuessCounter(0);
	}
	
	public double getSuccessRate(){
		return getCorrectGuessCounter() / (getCorrectGuessCounter() + getFalseGuessCounter());
	}
	
	public void increaseCorrectGuessCounter(double i){
		setCorrectGuessCounter(getCorrectGuessCounter() + i);
	}
	
	public void increaseFalseGuessCounter(double i){
		setFalseGuessCounter(getFalseGuessCounter() + i);
	}

	public double getCorrectGuessCounter() {
		return correctGuessCounter;
	}

	private void setCorrectGuessCounter(double correctGuessCounter) {
		this.correctGuessCounter = correctGuessCounter;
	}

	public double getFalseGuessCounter() {
		return falseGuessCounter;
	}

	private void setFalseGuessCounter(double falseGuessCounter) {
		this.falseGuessCounter = falseGuessCounter;
	}

}
