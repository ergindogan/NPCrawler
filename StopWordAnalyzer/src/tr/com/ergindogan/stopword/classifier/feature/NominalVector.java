package tr.com.ergindogan.stopword.classifier.feature;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

public class NominalVector extends BaseVector{
	
	public NominalVector(LinkedHashMap<String, Integer> myWordMap){
		setVector(new Vector<Double>(myWordMap.size()));
	}
	
	public NominalVector(LinkedHashMap<String, Integer> myWordMap, String passage){
		setVector(new Vector<Double>(myWordMap.size()));
		
		List<String> words = Feature.splitPassageIntoWords(passage);
		
		for(String word:myWordMap.keySet()){
			myWordMap.put(word, 0);
		}
		
		for(String word:words){
			boolean contains = myWordMap.containsKey(word);
			
			if(contains){
				myWordMap.put(word, myWordMap.get(word) + 1);
			}
		}
		
		for(String word:myWordMap.keySet()){
			addToVector(myWordMap.get(word));
		}
	}

}
