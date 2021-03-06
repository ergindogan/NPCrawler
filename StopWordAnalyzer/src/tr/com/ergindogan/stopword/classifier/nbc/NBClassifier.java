package tr.com.ergindogan.stopword.classifier.nbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.BaseClassifier;
import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationConstructor;
import tr.com.ergindogan.stopword.classifier.crossover.Iteration;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.test.TestResult;
import tr.com.ergindogan.stopword.classifier.test.Tester;
import tr.com.ergindogan.stopword.classifier.train.Trainer;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;
import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class NBClassifier extends BaseClassifier{
	
	private Map<String,List<FeatureVector>> featureVectorsToClassify;
	private List<Feature> features;

	public NBClassifier(Map<String,List<Passage>> myMap, List<Feature> features, int trainRatio, int testRatio){
		super(trainRatio, testRatio, new TestResult());
		setFeatures(features);
		setFeatureVectorsToClassify(myMap);
	}
	
	//We are assuming all authors have same amount of iterations.
	public void classify(){
		TestResult result;
		
		CrossValidationConstructor crossoverConstructor;
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
			crossoverConstructor = new CrossValidationConstructor(featureVectorList, getTrainRatio(), getTestRatio(), false);
			
			List<Iteration> iterationsForAuthor = crossoverConstructor.constructIterations();
			allIterations.put(authorName, iterationsForAuthor);
			iterationCount = iterationsForAuthor.size();
		}
		
		//With final iteration indexes ready, now we can construct each iterations author-feature vectors.
		for(int i = 0; i < iterationCount; i++){
			
			for(String authorName : allIterations.keySet()){
				trainFeatureVectorList = new ArrayList<FeatureVector>();
				testFeatureVectorList = new ArrayList<FeatureVector>();
				
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
			System.out.println("Iteration " + i + " correct : " + result.getCorrectGuessCounter() + " false : " + result.getFalseGuessCounter());
			
			//reinitialize maps after each iteration.
			trainMap = new HashMap<String,List<FeatureVector>>();
			testMap = new HashMap<String,List<FeatureVector>>();
		}
		
		System.out.println("Classification success rate : %" + getFinalResult().getSuccessRate());
		
//		printResultMatrix();
	}
	
	
	private void printResultMatrix(Tester tester){
		for(String authorName : getFeatureVectorsToClassify().keySet()){
			Map<String,Integer> guesses = tester.getChoices().get(authorName);
			String guessesString = "";
			for(String guessAuthorName : getFeatureVectorsToClassify().keySet()){
				if(!guesses.containsKey(guessAuthorName)){
					guessesString = guessesString + 0 + " ";
				}else{
					guessesString = guessesString + guesses.get(guessAuthorName) + " ";
				}
			}
			System.out.println(authorName + " : " + guessesString);
		}
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
		System.out.println("Feature Extraction Started!");
		long startTime = System.currentTimeMillis();
		
		Map<String,List<FeatureVector>> featureVectorMap = new HashMap<String,List<FeatureVector>>();
		List<FeatureVector> tempFeatureList;
		
		for(String authorName : myMap.keySet()){
			List<Passage> passages = myMap.get(authorName);
			tempFeatureList = new ArrayList<FeatureVector>();
			
			for(Passage passage : passages){
				if(!passage.getPassage().isEmpty()){
					FeatureVector featureVector = new FeatureVector(features, passage.getPassage(), passage.getParagraphs());
					tempFeatureList.add(featureVector);
				}
			}
			featureVectorMap.put(authorName, tempFeatureList);
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Feature Extraction Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to extract features.");
		
//		printVectorMap(featureVectorMap);
		
		return featureVectorMap;
	}
	
	private void printVectorMap(Map<String,List<FeatureVector>> featureVectorMap){
		for(String authorName : featureVectorMap.keySet()){
			System.out.println("Author : " + authorName);
			for(int i = 0; i < featureVectorMap.get(authorName).size(); i++){
				FeatureVector fv = featureVectorMap.get(authorName).get(i);
				System.out.println("Passage : " + i + " Vector : " + fv.getVectorString());
			}
		}
	}
	
}
