package tr.com.ergindogan.stopword.classifier.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.feature.NominalVector;
import tr.com.ergindogan.stopword.classifier.train.NominalTrainer;

public class NominalTester extends BaseTester<NominalVector>{
	
	public NominalTester(Map<String,List<NominalVector>> testMap, NominalTrainer trainer){
		setTestMap(testMap);
		setTrainer(trainer);
	}

	@Override
	public TestResult test() {
		TestResult testResult = new TestResult();
		
		Map<String,Double> probabilityMap;
		
		Map<String,Integer> guesses;
		
		for(String testAuthorName:getTestMap().keySet()){
			List<NominalVector> authorsVectorList = getTestMap().get(testAuthorName);
			//Her bir yazi icin test edicez
			for(NominalVector vector : authorsVectorList){
				probabilityMap = new HashMap<String,Double>();
				//test edicegimiz yazi = vector. Bu yaziyi icin butun olasi yazarlara ait olup olmadıgina bakmaliyiz.
				for(String possibleAuthorName : ((NominalTrainer) getTrainer()).getNominalVectorsToClassify().keySet()){
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
		
		return null;
	}

	@Override
	public double calculateProbability(NominalVector vector, String possibleAuthorName) {
		double value = 0.0;
		double propability = getPriorProbability(possibleAuthorName);
		
		for(int i = 0; i < vector.getVector().size(); i++){
			value = vector.getVector().get(i);
			//En fazla gecen X kelime (nominal) listesinde olup bu yazıda geçenler için.
			if(value != 0){
				propability += Math.log(
							((NominalTrainer) getTrainer()).
								getTrainMatrix().
								get(possibleAuthorName).
								getVector().
								get(i));
			}
		}
		return propability;
	}
	
}
