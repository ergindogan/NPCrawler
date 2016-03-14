package tr.com.ergindogan.stopword.classifier.synonym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import WordNet.Literal;
import tr.com.ergindogan.stopword.classifier.BaseClassifier;
import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationConstructor;
import tr.com.ergindogan.stopword.classifier.crossover.Iteration;
import tr.com.ergindogan.stopword.classifier.test.SynonymTester;
import tr.com.ergindogan.stopword.classifier.test.TestResult;
import tr.com.ergindogan.stopword.classifier.train.SynonymTrainer;
import tr.com.ergindogan.stopword.classifier.vector.SynonymVector;
import tr.com.ergindogan.stopword.reader.passage.Passage;

public class SynonymNBClassifier extends BaseClassifier{
	
	private Map<String,List<SynonymVector>> synonymVectorsToClassify;
	private LinkedHashMap<String, List<Literal>> synonymSetMap;
	
	public SynonymNBClassifier(Map<String,List<Passage>> myMap, LinkedHashMap<String, List<Literal>> synonymSetMap, int trainRatio, int testRatio){
		super(trainRatio, testRatio, new TestResult());
		setSynonymSetMap(synonymSetMap);
		setSynonymVectorsToClassify(myMap);
	}
	
	public void classify(){
		TestResult result = null;
		
		CrossValidationConstructor crossoverConstructor;
		Map<String,List<Iteration>> allIterations = new HashMap<String,List<Iteration>>();
		
		Map<String,List<SynonymVector>> trainMap = new HashMap<String,List<SynonymVector>>();
		Map<String,List<SynonymVector>> testMap = new HashMap<String,List<SynonymVector>>();
		
		List<SynonymVector> trainSynonymVectorList;
		List<SynonymVector> testSynonymVectorList;
		
		int iterationCount = 0;
		
		//Construct final iteration Indexes.
		for(String authorName : getSynonymVectorsToClassify().keySet()){
			//burada her bir loop her bir yazarin butun train test ikililerini dondurucek.
			List<SynonymVector> nominalVectorList = getSynonymVectorsToClassify().get(authorName);
			crossoverConstructor = new CrossValidationConstructor(nominalVectorList, getTrainRatio(), getTestRatio(), false);
			
			List<Iteration> iterationsForAuthor = crossoverConstructor.constructIterations();
			allIterations.put(authorName, iterationsForAuthor);
			iterationCount = iterationsForAuthor.size();
		}
		
		//With final iteration indexes ready, now we can construct each iterations author-feature vectors.
		for(int i = 0; i < iterationCount; i++){
			
			for(String authorName : allIterations.keySet()){
				trainSynonymVectorList = new ArrayList<SynonymVector>();
				testSynonymVectorList = new ArrayList<SynonymVector>();
				
				List<Iteration> iterations = allIterations.get(authorName);
				Iteration currentIterationToAdd = iterations.get(i);
				
				//put train feature vectors into train feature vector list
				for(int trainIndex:currentIterationToAdd.getTrainIndexes()){
					trainSynonymVectorList.add(getSynonymVectorsToClassify().get(authorName).get(trainIndex));
				}
				
				//put test feature vectors into test feature vector list
				for(int testIndex:currentIterationToAdd.getTestIndexes()){
					testSynonymVectorList.add(getSynonymVectorsToClassify().get(authorName).get(testIndex));
				}
				
				trainMap.put(authorName, trainSynonymVectorList);
				testMap.put(authorName, testSynonymVectorList);
			}
			
			//train the trainMap
			SynonymTrainer trainer = new SynonymTrainer(trainMap, getSynonymSetMap());
			trainer.train();
			
			//test the testMap
			SynonymTester tester = new SynonymTester(testMap, trainer);
			result = tester.test();
			
			//add mid results to the final result.
			addMidResultsToFinalResult(result);
			System.out.println("Iteration " + i + " correct : " + result.getCorrectGuessCounter() + " false : " + result.getFalseGuessCounter());
			
			//reinitialize maps after each iteration.
			trainMap = new HashMap<String,List<SynonymVector>>();
			testMap = new HashMap<String,List<SynonymVector>>();
		}
		
		System.out.println("Classification success rate : %" + getFinalResult().getSuccessRate());
	}
	
	

	public Map<String, List<SynonymVector>> getSynonymVectorsToClassify() {
		return synonymVectorsToClassify;
	}

	public void setSynonymVectorsToClassify(Map<String, List<Passage>> myMap) {
		this.synonymVectorsToClassify = getAuthorNominalVectorMap(myMap);
	}
	
	private Map<String,List<SynonymVector>> getAuthorNominalVectorMap(Map<String,List<Passage>> myMap){
		System.out.println("Synonym Feature Extraction Started!");
		long startTime = System.currentTimeMillis();
		
		Map<String,List<SynonymVector>> synonymVectorMap = new HashMap<String,List<SynonymVector>>();
		List<SynonymVector> tempFeatureList;
		
		for(String authorName : myMap.keySet()){
			List<Passage> passages = myMap.get(authorName);
			tempFeatureList = new ArrayList<SynonymVector>();
			
			for(Passage passage : passages){
				if(!passage.getPassage().isEmpty()){
					SynonymVector synonymVector = new SynonymVector(getSynonymSetMap(), passage.getPassage());
					tempFeatureList.add(synonymVector);
					printVector(authorName, passage.getTitle(), synonymVector);
				}
			}
			synonymVectorMap.put(authorName, tempFeatureList);
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Synonym Feature Extraction Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to extract synonym features.");
		
		return synonymVectorMap;
	}

	public LinkedHashMap<String, List<Literal>> getSynonymSetMap() {
		return synonymSetMap;
	}

	public void setSynonymSetMap(LinkedHashMap<String, List<Literal>> synonymSetMap) {
		this.synonymSetMap = synonymSetMap;
	}
	
	private void printVector(String authorName, String passageTitle, SynonymVector vector){
		System.out.println("Author Name : " + authorName + " Title : " + passageTitle + " Vector Sum : " + vector.getVectorSum());
	}
}
