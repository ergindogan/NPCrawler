package tr.com.ergindogan.stopword.classifier.nominal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationConstructor;
import tr.com.ergindogan.stopword.classifier.crossover.Iteration;
import tr.com.ergindogan.stopword.classifier.feature.NominalVector;
import tr.com.ergindogan.stopword.classifier.test.NominalTester;
import tr.com.ergindogan.stopword.classifier.test.TestResult;
import tr.com.ergindogan.stopword.classifier.train.NominalTrainer;
import tr.com.ergindogan.stopword.reader.passage.Passage;

public class NominalNBClassifier {
	
	private Map<String,List<NominalVector>> nominalVectorsToClassify;
	private LinkedHashMap<String, Integer> myWordMap;
	private int trainRatio;
	private int testRatio;
	
	private TestResult finalResult;

	public NominalNBClassifier(Map<String,List<Passage>> myMap, LinkedHashMap<String, Integer> myWordMap, int trainRatio, int testRatio){
		setTrainRatio(trainRatio);
		setTestRatio(testRatio);
		setMyWordMap(myWordMap);
		setNominalVectorsToClassify(myMap);
		setFinalResult(new TestResult());
	}
	
	public void classify(){
		TestResult result = null;
		
		CrossValidationConstructor crossoverConstructor;
		Map<String,List<Iteration>> allIterations = new HashMap<String,List<Iteration>>();
		
		Map<String,List<NominalVector>> trainMap = new HashMap<String,List<NominalVector>>();
		Map<String,List<NominalVector>> testMap = new HashMap<String,List<NominalVector>>();
		
		List<NominalVector> trainFeatureVectorList;
		List<NominalVector> testFeatureVectorList;
		
		int iterationCount = 0;
		
		//Construct final iteration Indexes.
		for(String authorName : getNominalVectorsToClassify().keySet()){
			//burada her bir loop her bir yazarin butun train test ikililerini dondurucek.
			List<NominalVector> nominalVectorList = getNominalVectorsToClassify().get(authorName);
			crossoverConstructor = new CrossValidationConstructor(nominalVectorList, getTrainRatio(), getTestRatio(), false);
			
			List<Iteration> iterationsForAuthor = crossoverConstructor.constructIterations();
			allIterations.put(authorName, iterationsForAuthor);
			iterationCount = iterationsForAuthor.size();
		}
		
		//With final iteration indexes ready, now we can construct each iterations author-feature vectors.
		for(int i = 0; i < iterationCount; i++){
			
			for(String authorName : allIterations.keySet()){
				trainFeatureVectorList = new ArrayList<NominalVector>();
				testFeatureVectorList = new ArrayList<NominalVector>();
				
				List<Iteration> iterations = allIterations.get(authorName);
				Iteration currentIterationToAdd = iterations.get(i);
				
				//put train feature vectors into train feature vector list
				for(int trainIndex:currentIterationToAdd.getTrainIndexes()){
					trainFeatureVectorList.add(getNominalVectorsToClassify().get(authorName).get(trainIndex));
				}
				
				//put test feature vectors into test feature vector list
				for(int testIndex:currentIterationToAdd.getTestIndexes()){
					testFeatureVectorList.add(getNominalVectorsToClassify().get(authorName).get(testIndex));
				}
				
				trainMap.put(authorName, trainFeatureVectorList);
				testMap.put(authorName, testFeatureVectorList);
			}
			
			//train the trainMap
			NominalTrainer trainer = new NominalTrainer(trainMap, getMyWordMap());
			trainer.train();
			
			//test the testMap
			NominalTester tester = new NominalTester(testMap, trainer);
			result = tester.test();
			
			//add mid results to the final result.
			addMidResultsToFinalResult(result);
			System.out.println("Iteration " + i + " correct : " + result.getCorrectGuessCounter() + " false : " + result.getFalseGuessCounter());
			
			//reinitialize maps after each iteration.
			trainMap = new HashMap<String,List<NominalVector>>();
			testMap = new HashMap<String,List<NominalVector>>();
		}
		
		System.out.println("Classification success rate : %" + getFinalResult().getSuccessRate());
	}
	
	private void addMidResultsToFinalResult(TestResult testResult){
		getFinalResult().increaseCorrectGuessCounter(testResult.getCorrectGuessCounter());
		getFinalResult().increaseFalseGuessCounter(testResult.getFalseGuessCounter());
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

	public Map<String, List<NominalVector>> getNominalVectorsToClassify() {
		return nominalVectorsToClassify;
	}

	public LinkedHashMap<String, Integer> getMyWordMap() {
		return myWordMap;
	}

	public void setMyWordMap(LinkedHashMap<String, Integer> myWordMap) {
		this.myWordMap = myWordMap;
	}

	public void setNominalVectorsToClassify(Map<String,List<Passage>> myMap) {
		this.nominalVectorsToClassify = getAuthorNominalVectorMap(myMap);
	}
	
	private Map<String,List<NominalVector>> getAuthorNominalVectorMap(Map<String,List<Passage>> myMap){
		System.out.println("Nominal Feature Extraction Started!");
		long startTime = System.currentTimeMillis();
		
		Map<String,List<NominalVector>> nominalVectorMap = new HashMap<String,List<NominalVector>>();
		List<NominalVector> tempFeatureList;
		
		for(String authorName : myMap.keySet()){
			List<Passage> passages = myMap.get(authorName);
			tempFeatureList = new ArrayList<NominalVector>();
			
			for(Passage passage : passages){
				if(!passage.getPassage().isEmpty()){
					NominalVector nominalVector = new NominalVector(getMyWordMap(), passage.getPassage());
					tempFeatureList.add(nominalVector);
				}
			}
			nominalVectorMap.put(authorName, tempFeatureList);
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Nominal Feature Extraction Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to extract nominal features.");
		
		return nominalVectorMap;
	}
}
