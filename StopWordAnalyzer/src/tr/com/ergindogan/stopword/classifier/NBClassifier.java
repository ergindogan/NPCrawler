package tr.com.ergindogan.stopword.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.crossover.CrossoverConstructor;
import tr.com.ergindogan.stopword.classifier.crossover.Iteration;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.feature.FeatureVector;
import tr.com.ergindogan.stopword.classifier.test.TestResult;
import tr.com.ergindogan.stopword.classifier.test.Tester;
import tr.com.ergindogan.stopword.classifier.train.Trainer;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class NBClassifier {
	
	private Map<String,List<FeatureVector>> featureVectorsToClassify;
	private List<Feature> features;
	private int trainRatio;
	private int testRatio;
	
	private TestResult finalResult;

	public NBClassifier(Map<String,List<Passage>> myMap, List<Feature> features, int trainRatio, int testRatio){
		setFeatureVectorsToClassify(myMap);
		setFeatures(features);
		setTrainRatio(trainRatio);
		setTestRatio(testRatio);
	}
	
	//We are assuming all authors have same amount of iterations.
	public void classify(){
		TestResult result;
		
		CrossoverConstructor crossoverConstructor;
		Map<String,List<Iteration>> allIterations = new HashMap<String,List<Iteration>>();
		
		Map<String,List<FeatureVector>> trainMap = new HashMap<String,List<FeatureVector>>();
		Map<String,List<FeatureVector>> testMap = new HashMap<String,List<FeatureVector>>();
		
		List<FeatureVector> trainFeatureVectorList;
		List<FeatureVector> testFeatureVectorList;
		
		int iterationCount = 0;
		
		//Construct final iteration Indexes.
		for(String authorName : getFeatureVectorsToClassify().keySet()){
			//burada her bir loop her bir yazarin butun train test ikililerini dondurucek.
			List<FeatureVector> featureVectorList = getFeatureVectorsToClassify().get(authorName);
			crossoverConstructor = new CrossoverConstructor(featureVectorList, getTrainRatio(), getTestRatio(), false);
			
			List<Iteration> iterationsForAuthor = crossoverConstructor.constructIterations();
			allIterations.put(authorName, iterationsForAuthor);
			iterationCount = iterationsForAuthor.size();
		}
		
		//With final iteration indexes now we can construct each iterations author-feature vectors.
		for(int i = 0; i < iterationCount; i++){
			trainFeatureVectorList = new ArrayList<FeatureVector>();
			testFeatureVectorList = new ArrayList<FeatureVector>();
			
			for(String authorName : allIterations.keySet()){
				List<Iteration> iterations = allIterations.get(authorName);
				Iteration currentIterationToAdd = iterations.get(i);
				
				//put train feature vectors into train feature vector list
				for(int trainIndex:currentIterationToAdd.getTrainIndexes()){
					trainFeatureVectorList.add(getFeatureVectorsToClassify().get(authorName).get(trainIndex));
				}
				
				//put test feature vectors into test feature vector list
				for(int testIndex:currentIterationToAdd.getTestIndexes()){
					testFeatureVectorList.add(getFeatureVectorsToClassify().get(authorName).get(testIndex));
				}
				
				trainMap.put(authorName, trainFeatureVectorList);
				testMap.put(authorName, testFeatureVectorList);
			}
			
			//train the trainMap
			Trainer trainer = new Trainer(trainMap, getFeatures());
			trainer.train();
			
			//test the testMap
			Tester tester = new Tester(testMap, trainer);
			result = tester.test();
			
			//add mid results to the final result.
			addMidResultsToFinalResult(result);
			
			//reinitialize maps after each iteration.
			trainMap = new HashMap<String,List<FeatureVector>>();
			testMap = new HashMap<String,List<FeatureVector>>();
		}
		
		System.out.println("Classification success rate : " + finalResult.getSuccessRate());
	}
	
	private void addMidResultsToFinalResult(TestResult testResult){
		finalResult.increaseCorrectGuessCounter(testResult.getCorrectGuessCounter());
		finalResult.increaseFalseGuessCounter(testResult.getFalseGuessCounter());
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public Map<String, List<FeatureVector>> getFeatureVectorsToClassify() {
		return featureVectorsToClassify;
	}

	public void setFeatureVectorsToClassify( Map<String,List<Passage>> myMap) {
		
		this.featureVectorsToClassify = getAuthorFeatureVectorMap(myMap, getFeatures());
	}
	
	private Map<String,List<FeatureVector>> getAuthorFeatureVectorMap(Map<String,List<Passage>> myMap, List<Feature> features){
		Map<String,List<FeatureVector>> featureVectorMap = new HashMap<String,List<FeatureVector>>();
		List<FeatureVector> tempFeatureList;
		
		for(String authorName : myMap.keySet()){
			List<Passage> passages = myMap.get(authorName);
			tempFeatureList = new ArrayList<FeatureVector>();
			
			for(Passage passage : passages){
				FeatureVector featureVector = new FeatureVector(features, passage.getPassage());
				tempFeatureList.add(featureVector);
			}
			featureVectorMap.put(authorName, tempFeatureList);
		}
		
		return featureVectorMap;
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
	
}
