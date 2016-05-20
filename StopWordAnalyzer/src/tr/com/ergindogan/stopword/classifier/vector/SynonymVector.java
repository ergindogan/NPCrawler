package tr.com.ergindogan.stopword.classifier.vector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;
import WordNet.Literal;

public class SynonymVector extends BaseVector {
	
	public SynonymVector(LinkedHashMap<String, List<Literal>> synsetMap){
		setVector(new Vector<Double>(synsetMap.size()));
		
		for(int i = 1; i < synsetMap.size() + 1; i++){
			getVector().add(0.0);
		}
	}
	
	public SynonymVector(LinkedHashMap<String, List<String>> synDic, LinkedHashMap<String, List<Literal>> synsetMap, String passage){
		List<String> ids = new ArrayList<String>();
//		String meaning;
		
		setVector(new Vector<Double>(synsetMap.size()));
		
		LinkedHashMap<String, Integer> tempsynSetMap = new LinkedHashMap<String, Integer>(synsetMap.size());
		
		List<String> words = Feature.splitPassageIntoWords(passage);
		
		for(String synId:synsetMap.keySet()){
			tempsynSetMap.put(synId, 0);
		}

		for(String word:words){
			if(word != null && !word.isEmpty()){
				ids = synDic.get(word);
				if(ids != null && !ids.isEmpty()){
					for(String id:ids){
						tempsynSetMap.put(id, tempsynSetMap.get(id) + 1);
					}
					foundSynsetCount++;
				}else{
					notFoundSynsetCount++;
				}
			}
		}
		
		for(String synId:tempsynSetMap.keySet()){
			addToVector(tempsynSetMap.get(synId));
		}
	}

}
