package tr.com.ergindogan.stopword.classifier.train;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.NominalVector;

public class NominalTrainer implements Trainable{
	
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
	
	private Vector<Double> calculateConProb(Vector<Double> vector){
		double sum = calculateTotal(vector);
		double value = 0.0;
		
		Vector<Double> resultVector = new Vector<Double>(vector.size());
		
		for(int i = 0; i < vector.size(); i++){
			value = (vector.get(i) + 1) / (sum + vector.size());
			resultVector.set(i, value);
		}
		return resultVector;
	}
	
	private double calculateTotal(Vector<Double> vector){
		double sum = 0.0;
		for(double value :vector){
			sum += value;
		}
		return sum;
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
