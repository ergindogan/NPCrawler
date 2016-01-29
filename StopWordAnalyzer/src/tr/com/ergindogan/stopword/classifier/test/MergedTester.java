package tr.com.ergindogan.stopword.classifier.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.train.MergedTrainer;
import tr.com.ergindogan.stopword.classifier.train.NominalTrainer;
import tr.com.ergindogan.stopword.classifier.train.Trainer;
import tr.com.ergindogan.stopword.classifier.vector.BaseVector;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;
import tr.com.ergindogan.stopword.classifier.vector.NominalVector;
import tr.com.ergindogan.stopword.classifier.vector.PassageVector;

public class MergedTester extends BaseTester<PassageVector> {

	public MergedTester(Map<String,List<PassageVector>> testMap, MergedTrainer trainer){
		setTestMap(testMap);
		setTrainer(trainer);
	}
	
	@Override
	public TestResult test() {
		TestResult testResult = new TestResult();
		
		Map<String,Double> probabilityMap;
		
		Map<String,Integer> guesses;
		
		for(String testAuthorName:getTestMap().keySet()){
			List<PassageVector> authorsPassageVectorList = getTestMap().get(testAuthorName);
			//Her bir yazi icin test edicez
			for(PassageVector passageVector : authorsPassageVectorList){
				probabilityMap = new HashMap<String,Double>();
				//test edicegimiz yazi = vector. Bu yaziyi icin butun olasi yazarlara ait olup olmadıgina bakmaliyiz.
				for(String possibleAuthorName: ((MergedTrainer) getTrainer()).getAuthorPassageVectorMap().keySet()){
					//vector un possibleAuthorName yazar a ait olma olasiligini hesaplayacagiz. 
					//Bu olasiliklari probabilityMap icine koyacaz.
					double probability = calculateProbability(passageVector, possibleAuthorName);
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
	public double calculateProbability(PassageVector vector, String possibleAuthorName) {
		double finalProbability = 1.0;
		
		List<BaseVector> vectorRepresentations = vector.getVectorRepresentations();
		
		for(BaseVector vec:vectorRepresentations){
			if(vec instanceof FeatureVector){
				FeatureVector myFV = (FeatureVector) vec;
				finalProbability = finalProbability * calculateNBTestProb(myFV, possibleAuthorName);
			}else{
				NominalVector myNV = (NominalVector) vec;
				finalProbability = finalProbability * calculateNominalTestProb(myNV, possibleAuthorName);
			}
		}
		return finalProbability;
	}
	
	//TODO: Under here it is all redundant code. I need to remove this redundancy.
	
	private double calculateNominalTestProb(NominalVector vector, String possibleAuthorName){
		double value = 0.0;
		double propability = getPriorProbability(possibleAuthorName);
		double condProb = 0.0;
		double logCondProb = 0.0;
		
		for(int i = 0; i < vector.getVector().size(); i++){
			value = vector.getVector().get(i);
			//En fazla gecen X kelime (nominal) listesinde olup bu yazıda geçenler için.
			if(value != 0){
				condProb = ((NominalTrainer) getTrainer()).getTrainMatrix().get(possibleAuthorName).getVector().get(i);
				//Burada log negatif deger donduruyor.
				logCondProb = Math.log(condProb);
						
				propability = propability + logCondProb;
			}
		}
		return propability;
	}
	
	private double calculateNBTestProb(FeatureVector vector, String possibleAuthorName){
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
