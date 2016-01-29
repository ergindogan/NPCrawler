package tr.com.ergindogan.stopword.classifier.train;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;

public abstract class BaseTrainer implements Trainable{
	
	//--------------------------------- FEATURE TRAINER METHODS ---------------------------------
	
	protected Vector<Double> calculateMeanVetor(List<FeatureVector> featureVectors, List<Feature> features){
		Vector<Double> meanVector = new Vector<Double>(features.size());
		
		double size = featureVectors.size();
		int featureSize = features.size();
		
		Double sum = 0.0;
		
		for(int i = 0; i < featureSize; i++){
			for(FeatureVector featureVector : featureVectors){
				double value = featureVector.getVector().get(i);
				sum += value;
			}
			meanVector.add(i, sum/size);
			sum = 0.0;
		}
		
		return meanVector;
	}
	
	protected Vector<Double> calculateStandartDeviationVector(List<FeatureVector> featureVectors, 
			String authorName, List<Feature> features, Map<String,Vector<Double>> meanVector){
		Vector<Double> standartDeviationVector = new Vector<Double>(features.size());
		
		int size = featureVectors.size();
		int featureSize = features.size();
		
		double sum = 0.0;
		
		for(int i = 0; i < featureSize; i++){
			for(FeatureVector featureVector : featureVectors){
				double value = featureVector.getVector().get(i);
				sum = sum + Math.pow((value - meanVector.get(authorName).get(i)), 2);
			}
			standartDeviationVector.add(i, Math.sqrt(sum/(size - 1)));
			sum = 0.0;
		}
		
		return standartDeviationVector;
	}
	
	//--------------------------------- NOMINAL TRAINER METHODS ---------------------------------
	
	protected Vector<Double> calculateConProb(Vector<Double> vector){
		double sum = calculateTotal(vector);
		double value = 0.0;
		
		Vector<Double> resultVector = new Vector<Double>(vector.size());
		
		for(double vectorValue:vector){
			value = (vectorValue + 1) / (sum + vector.size());
			resultVector.add(value);
		}

		return resultVector;
	}
	
	protected double calculateTotal(Vector<Double> vector){
		double sum = 0.0;
		for(double value :vector){
			sum += value;
		}
		return sum;
	}
}
