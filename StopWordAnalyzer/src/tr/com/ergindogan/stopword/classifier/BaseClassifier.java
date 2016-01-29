package tr.com.ergindogan.stopword.classifier;

import tr.com.ergindogan.stopword.classifier.test.TestResult;

public abstract class BaseClassifier {
	
	private int trainRatio;
	private int testRatio;
	
	private TestResult finalResult;
	
	public BaseClassifier(int trainRatio, int testRatio, TestResult finalResult){
		setTrainRatio(trainRatio);
		setTestRatio(testRatio);
		setFinalResult(finalResult);
	}

	public int getTrainRatio() {
		return trainRatio;
	}

	public void setTrainRatio(int trainRatio) {
		this.trainRatio = trainRatio;
	}

	public int getTestRatio() {
		return testRatio;
	}

	public void setTestRatio(int testRatio) {
		this.testRatio = testRatio;
	}

	public TestResult getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(TestResult finalResult) {
		this.finalResult = finalResult;
	}
	
	protected void addMidResultsToFinalResult(TestResult testResult){
		getFinalResult().increaseCorrectGuessCounter(testResult.getCorrectGuessCounter());
		getFinalResult().increaseFalseGuessCounter(testResult.getFalseGuessCounter());
	}

}
