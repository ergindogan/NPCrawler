package tr.com.ergindogan.stopword.classifier.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.train.BaseTrainer;

public abstract class BaseTester<T> implements Testable<T>{
	
	private Map<String,List<T>> testMap;
	private BaseTrainer trainer;

	private Map<String,Map<String,Integer>> choices = new HashMap<String, Map<String,Integer>>();

	
	protected Map<String, List<T>> getTestMap() {
		return testMap;
	}

	protected void setTestMap(Map<String, List<T>> testMap) {
		this.testMap = testMap;
	}

	protected BaseTrainer getTrainer() {
		return trainer;
	}

	protected void setTrainer(BaseTrainer trainer) {
		this.trainer = trainer;
	}

	public Map<String, Map<String, Integer>> getChoices() {
		return choices;
	}

	public void setChoices(Map<String, Map<String, Integer>> choices) {
		this.choices = choices;
	}
	
	protected double getPriorProbability(String index){
		return getTestMap().get(index).size() / getTotalTestDataCount();
	}
	
	private double getTotalTestDataCount(){
		double sum = 0.0;
		
		for(String key:getTestMap().keySet()){
			sum += getTestMap().get(key).size();
		}
		return sum;
	}
	
	protected String getAuthorNameOfHighestProbability(Map<String,Double> probabilityMap){
		String highestProbabilityAuthorName = "";
		double highestProbability = -1;
		
		for(String authorName : probabilityMap.keySet()){
			highestProbabilityAuthorName = authorName;
			highestProbability = probabilityMap.get(authorName);
			break;
		}
		
		for(String authorName : probabilityMap.keySet()){

			if(probabilityMap.get(authorName) > highestProbability){
				highestProbability = probabilityMap.get(authorName);
				highestProbabilityAuthorName = authorName;
			}
		}
		
		return highestProbabilityAuthorName;
	}
}
