package tr.com.ergindogan.stopword.classifier.vector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;

public class NominalVector extends BaseVector{
	
	public NominalVector(LinkedHashMap<String, Integer> myWordMap){
		setVector(new Vector<Double>(myWordMap.size()));
		
		for(int i = 1; i < myWordMap.size() + 1; i++){
			getVector().add(0.0);
		}
	}
	
	public NominalVector(LinkedHashMap<String, Integer> myWordMap, String passage){
		setVector(new Vector<Double>(myWordMap.size()));
		
		LinkedHashMap<String, Integer> tempWordMap = new LinkedHashMap<String, Integer>(myWordMap.size());
		
		List<String> words = Feature.splitPassageIntoWords(passage);
		
		for(String word:myWordMap.keySet()){
			tempWordMap.put(word, 0);
		}
		
		for(String word:words){
			boolean contains = tempWordMap.containsKey(word);
			
			if(contains){
				tempWordMap.put(word, tempWordMap.get(word) + 1);
			}
		}
		
		for(String word:tempWordMap.keySet()){
			addToVector(tempWordMap.get(word));
		}
	}

}
