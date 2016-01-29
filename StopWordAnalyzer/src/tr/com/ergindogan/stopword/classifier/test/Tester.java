package tr.com.ergindogan.stopword.classifier.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.train.Trainer;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Tester extends BaseTester<FeatureVector>{
	
	public Tester(Map<String,List<FeatureVector>> testMap, Trainer trainer){
		setTestMap(testMap);
		setTrainer(trainer);
	}
	
	public TestResult test(){
		TestResult testResult = new TestResult();
		
		Map<String,Double> probabilityMap;
		
		Map<String,Integer> guesses;
		
		for(String testAuthorName:getTestMap().keySet()){
			List<FeatureVector> authorsVectorList = getTestMap().get(testAuthorName);
			//Her bir yazi icin test edicez
			for(FeatureVector vector : authorsVectorList){
				probabilityMap = new HashMap<String,Double>();
				//test edicegimiz yazi = vector. Bu yaziyi icin butun olasi yazarlara ait olup olmadıgina bakmaliyiz.
				for(String possibleAuthorName: ((Trainer) getTrainer()).getAuthorFeatureVectorMap().keySet()){
					//vector un possibleAuthorName yazar a ait olma olasiligini hesaplayacagiz. 
					//Bu olasiliklari probabilityMap icine koyacaz.
					double probability = calculateProbability(vector, possibleAuthorName);
					probabilityMap.put(possibleAuthorName, probability);
				}
				
				//Map icindeki en buyuk olasiliga sahip keyi testAuthorName ile karsilastiricaz.
				String guessedAuthorName = getAuthorNameOfHighestProbability(probabilityMap);
				
				if(getChoices().get(testAuthorName) == null){
					guesses = new HashMap<String, Integer>();
					guesses.put(guessedAuthorName, 1);
				}else{
					guesses = getChoices().get(testAuthorName);
					if(guesses.get(guessedAuthorName) == null){
						guesses.put(guessedAuthorName, 1);
					}else{
						guesses.put(guessedAuthorName, guesses.get(guessedAuthorName) + 1);
					}
				}
				getChoices().put(testAuthorName, guesses);
				
				if(testAuthorName.equals(guessedAuthorName)){
					testResult.increaseCorrectGuessCounter(1);
				}else{
					testResult.increaseFalseGuessCounter(1);
				}
			}
		}
		
		return testResult;
	}
	
	@Override
	public double calculateProbability(FeatureVector vector, String possibleAuthorName){
		double result = 1;
		
		for(int i = 0; i < vector.getVector().size(); i++){
			double featureValue = vector.getVector().get(i);
			result = result * calculateNaiveBaesianProbability(featureValue, i, possibleAuthorName);
		}
		
		return result * ((Trainer) getTrainer()).probabilityOfAuthor(possibleAuthorName);
	}
	
	private double calculateNaiveBaesianProbability(double featureValue ,int featureIndex, String possibleAuthorName){
		double standartDeviation = ((Trainer) getTrainer()).getStandartDeviation(possibleAuthorName, featureIndex);
		double mean = ((Trainer) getTrainer()).getMean(possibleAuthorName, featureIndex);
		
		double firstPart = 1 / (Math.sqrt(2 * Math.PI * Math.pow(standartDeviation, 2)));
		double secondPart = Math.pow(Math.E, (-1 * Math.pow(featureValue - mean, 2)) / (2 * Math.pow(standartDeviation, 2)) );
		
		return firstPart * secondPart;
	}

}
