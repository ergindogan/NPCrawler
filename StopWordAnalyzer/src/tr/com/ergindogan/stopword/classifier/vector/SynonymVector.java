package tr.com.ergindogan.stopword.classifier.vector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;

public class SynonymVector extends BaseVector {
	
	public SynonymVector(LinkedHashMap<String, String> synsetMap){
		setVector(new Vector<Double>(synsetMap.size()));
		
		for(int i = 1; i < synsetMap.size() + 1; i++){
			getVector().add(0.0);
		}
	}
	
	public SynonymVector(LinkedHashMap<String, String> synsetMap, String passage){
		String myString;
		String meanings[];
		
		setVector(new Vector<Double>(synsetMap.size()));
		
		LinkedHashMap<String, Integer> tempsynSetMap = new LinkedHashMap<String, Integer>(synsetMap.size());
		
		List<String> words = Feature.splitPassageIntoWords(passage);
		
		for(String synId:synsetMap.keySet()){
			tempsynSetMap.put(synId, 0);
		}

		for(String word:words){
			for(String key:synsetMap.keySet()){
				myString = synsetMap.get(key);
				meanings = myString.split(",");
				
				for(String meaning:meanings){
					if(meaning.equals(word)){
						tempsynSetMap.put(key, tempsynSetMap.get(key) + 1);
					}
				}
			}
		}
		
		for(String synId:tempsynSetMap.keySet()){
			addToVector(tempsynSetMap.get(synId));
		}
	}

}