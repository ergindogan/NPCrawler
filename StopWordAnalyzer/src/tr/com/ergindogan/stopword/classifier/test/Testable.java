package tr.com.ergindogan.stopword.classifier.test;


public interface Testable<T> {

	TestResult test();
	
	double calculateProbability(T vector, String possibleAuthorName);
}
