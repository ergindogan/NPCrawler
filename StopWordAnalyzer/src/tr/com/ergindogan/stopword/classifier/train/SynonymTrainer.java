package tr.com.ergindogan.stopword.classifier.train;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.vector.SynonymVector;
import WordNet.Literal;

public class SynonymTrainer extends BaseTrainer {
	
	private LinkedHashMap<String, List<Literal>> mySynonymMap;
	private Map<String, List<SynonymVector>> synonymVectorsToClassify;
	
	private Map<String, SynonymVector> trainMatrix;
	
	public SynonymTrainer(Map<String,List<SynonymVector>> synonymVectorsToClassify, LinkedHashMap<String, List<Literal>> mySynonymMap){
		setSynonymVectorsToClassify(synonymVectorsToClassify);
		setMySynonymMap(mySynonymMap);
		setTrainMatrix(new HashMap<String, SynonymVector>());
	}

	@Override
	public void train() {
		SynonymVector tempVector;
		for(String authorName : getSynonymVectorsToClassify().keySet()){
			List<SynonymVector> vectorList = getSynonymVectorsToClassify().get(authorName);
			tempVector = new SynonymVector(getMySynonymMap());
			for(SynonymVector synonymVector:vectorList){
				//authorName yazarinin, vector yazisini train ederken
				tempVector.addToVector(calculateConProb(synonymVector.getVector()));
			}
			
			getTrainMatrix().put(authorName, tempVector);
		}
	}

	public LinkedHashMap<String, List<Literal>> getMySynonymMap() {
		return mySynonymMap;
	}

	public void setMySynonymMap(LinkedHashMap<String, List<Literal>> mySynonymMap) {
		this.mySynonymMap = mySynonymMap;
	}

	public Map<String, List<SynonymVector>> getSynonymVectorsToClassify() {
		return synonymVectorsToClassify;
	}

	public void setSynonymVectorsToClassify(
			Map<String, List<SynonymVector>> synonymVectorsToClassify) {
		this.synonymVectorsToClassify = synonymVectorsToClassify;
	}

	public Map<String, SynonymVector> getTrainMatrix() {
		return trainMatrix;
	}

	public void setTrainMatrix(Map<String, SynonymVector> trainMatrix) {
		this.trainMatrix = trainMatrix;
	}

}
