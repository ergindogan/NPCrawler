package tr.com.ergindogan.stopword.classifier.train;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.classifier.vector.NominalVector;

public class NominalTrainer extends BaseTrainer{
	
	private LinkedHashMap<String, Integer> myWordMap;
	private Map<String, List<NominalVector>> nominalVectorsToClassify;
	
	private Map<String, NominalVector> trainMatrix;
	
	public NominalTrainer(Map<String,List<NominalVector>> nominalVectorsToClassify, LinkedHashMap<String, Integer> myWordMap){
		setNominalVectorsToClassify(nominalVectorsToClassify);
		setMyWordMap(myWordMap);
		setTrainMatrix(new HashMap<String, NominalVector>());
	}
	
	@Override
	public void train() {
		NominalVector tempVector;
		for(String authorName : getNominalVectorsToClassify().keySet()){
			List<NominalVector> vectorList = getNominalVectorsToClassify().get(authorName);
			tempVector = new NominalVector(getMyWordMap());
			for(NominalVector nominalVector:vectorList){
				//authorName yazarinin, vector yazisini train ederken
				tempVector.addToVector(calculateConProb(nominalVector.getVector()));
			}
			
			getTrainMatrix().put(authorName, tempVector);
		}
	}

	public LinkedHashMap<String, Integer> getMyWordMap() {
		return myWordMap;
	}

	private void setMyWordMap(LinkedHashMap<String, Integer> myWordMap) {
		this.myWordMap = myWordMap;
	}

	public Map<String, List<NominalVector>> getNominalVectorsToClassify() {
		return nominalVectorsToClassify;
	}

	private void setNominalVectorsToClassify(
			Map<String, List<NominalVector>> nominalVectorsToClassify) {
		this.nominalVectorsToClassify = nominalVectorsToClassify;
	}

	public Map<String, NominalVector> getTrainMatrix() {
		return trainMatrix;
	}

	private void setTrainMatrix(Map<String, NominalVector> trainMatrix) {
		this.trainMatrix = trainMatrix;
	}
	
}
