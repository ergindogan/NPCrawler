package tr.com.ergindogan.stopword.classifier.test;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class TestResult {
	
	private int correctGuessCounter;
	private int falseGuessCounter;
	
	public TestResult(){
		setCorrectGuessCounter(0);
		setFalseGuessCounter(0);
	}
	
	public double getSuccessRate(){
		return getCorrectGuessCounter() / (getCorrectGuessCounter() + getFalseGuessCounter());
	}
	
	public void increaseCorrectGuessCounter(int i){
		setCorrectGuessCounter(getCorrectGuessCounter() + i);
	}
	
	public void increaseFalseGuessCounter(int i){
		setFalseGuessCounter(getFalseGuessCounter() + i);
	}

	public int getCorrectGuessCounter() {
		return correctGuessCounter;
	}

	private void setCorrectGuessCounter(int correctGuessCounter) {
		this.correctGuessCounter = correctGuessCounter;
	}

	public int getFalseGuessCounter() {
		return falseGuessCounter;
	}

	private void setFalseGuessCounter(int falseGuessCounter) {
		this.falseGuessCounter = falseGuessCounter;
	}

}
