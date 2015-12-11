package tr.com.ergindogan.stopword.classifier.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.feature.FeatureVector;
import tr.com.ergindogan.stopword.classifier.train.Trainer;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Tester {
	
	private Map<String,List<FeatureVector>> testMap;
	private Trainer trainer;
	
	public Tester(Map<String,List<FeatureVector>> testMap, Trainer trainer){
		setTestMap(testMap);
		setTrainer(trainer);
	}
	
	public TestResult test(){
		TestResult testResult = new TestResult();
		
		Map<String,Double> probabilityMap;
		
		for(String testAuthorName:getTestMap().keySet()){
			List<FeatureVector> authorsVectorList = getTestMap().get(testAuthorName);
			//Her bir yazi icin test edicez
			for(FeatureVector vector : authorsVectorList){
				probabilityMap = new HashMap<String,Double>();
				//test edicegimiz yazi = vector. Bu yaziyi icin butun olasi yazarlara ait olup olmadıgina bakmaliyiz.
				for(String possibleAuthorName:getTrainer().getAuthorFeatureVectorMap().keySet()){
					//vector un possibleAuthorName yazar a ait olma olasiligini hesaplayacagiz. 
					//Bu olasiliklari probabilityMap icine koyacaz.
					double probability = calculateProbability(vector, possibleAuthorName);
					probabilityMap.put(possibleAuthorName, probability);
				}
				
				//Map icindeki en buyuk olasiliga sahip keyi testAuthorName ile karsilastiricaz.
				String guessedAuthorName = getAuthorNameOfHighestProbability(probabilityMap);
				if(testAuthorName.equals(guessedAuthorName)){
					testResult.increaseCorrectGuessCounter(1);
				}else{
					testResult.increaseFalseGuessCounter(1);
				}
			}
		}
		
		return testResult;
	}
	
	private String getAuthorNameOfHighestProbability(Map<String,Double> probabilityMap){
		String highestProbabilityAuthorName = "";
		double highestProbability = -1;
		
		for(String authorName : probabilityMap.keySet()){
			double probability = probabilityMap.get(authorName);
			if(probability > highestProbability){
				highestProbability = probability;
				highestProbabilityAuthorName = authorName;
			}
		}
		
		return highestProbabilityAuthorName;
	}
	
	private double calculateProbability(FeatureVector vector, String possibleAuthorName){
		double result = 1;
		
		for(int i = 0; i < vector.getVector().size(); i++){
			double featureValue = vector.getVector().get(i);
			result = result * calculateNaiveBaesianProbability(featureValue, i, possibleAuthorName);
		}
		
		return result * getTrainer().probabilityOfAuthor(possibleAuthorName);
	}
	
	private double calculateNaiveBaesianProbability(double featureValue ,int featureIndex, String possibleAuthorName){
		double standartDeviation = getTrainer().getStandartDeviation(possibleAuthorName, featureIndex);
		double mean = getTrainer().getMean(possibleAuthorName, featureIndex);
		
		double firstPart = 1 / (Math.sqrt(2 * Math.PI * Math.pow(standartDeviation, 2)));
		double secondPart = Math.pow(Math.E, (-1 * Math.pow(featureValue - mean, 2)) / (2 * Math.pow(standartDeviation, 2)) );
		
		return firstPart * secondPart;
	}

	private Map<String, List<FeatureVector>> getTestMap() {
		return testMap;
	}

	private void setTestMap(Map<String, List<FeatureVector>> testMap) {
		this.testMap = testMap;
	}

	private Trainer getTrainer() {
		return trainer;
	}

	private void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

}
