package tr.com.ergindogan.stopword.classifier.train;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.feature.FeatureVector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Trainer {
	
	private List<Feature> features;
	
	private Map<String,Vector<Double>> meanVector;
	private Map<String,Vector<Double>> standartDeviationVector;
	
	private Map<String,List<FeatureVector>> authorFeatureVectorMap;
	
	//Buraya yazarlara bolup featurelari cikarilmis halini vericez.
	public Trainer(Map<String,List<FeatureVector>> authorFeatureVectorMap, List<Feature> features){
		setAuthorFeatureVectorMap(authorFeatureVectorMap);
		setFeatures(features);
	}

	public void train(){
		for(String authorName : getAuthorFeatureVectorMap().keySet()){
			List<FeatureVector> vectorList = getAuthorFeatureVectorMap().get(authorName);
			
			Vector<Double> meanVector = calculateMeanVetor(vectorList);
			getMeanVector().put(authorName, meanVector);
			
			Vector<Double> standarDeviationVector = calculateStandartDeviationVector(vectorList, authorName);
			getStandartDeviationVector().put(authorName, standarDeviationVector);
		}
		
	}
	
	private Vector<Double> calculateMeanVetor(List<FeatureVector> featureVectors){
		Vector<Double> meanVector = new Vector<Double>();
		
		int size = featureVectors.size();
		int featureSize = getFeatures().size();
		
		double sum = 0.0;
		
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
	
	private Vector<Double> calculateStandartDeviationVector(List<FeatureVector> featureVectors, String authorName){
		Vector<Double> standartDeviationVector = new Vector<Double>();
		
		int size = featureVectors.size();
		int featureSize = getFeatures().size();
		
		double sum = 0.0;
		
		for(int i = 0; i < featureSize; i++){
			for(FeatureVector featureVector : featureVectors){
				double value = featureVector.getVector().get(i);
				sum = sum + Math.pow((value - getMeanVector().get(authorName).get(i)), 2);
			}
			standartDeviationVector.add(i, Math.sqrt(sum/size - 1));
			sum = 0.0;
		}
		
		return standartDeviationVector;
	}
	
	public double getMean(String authorName, int featureIndex){
		return getMeanVector().get(authorName).get(featureIndex);
	}
	
	public double getStandartDeviation(String authorName, int featureIndex){
		return getStandartDeviationVector().get(authorName).get(featureIndex);
	}
	
	private List<Feature> getFeatures() {
		return features;
	}

	private void setFeatures(List<Feature> features) {
		this.features = features;
	}

	private Map<String, Vector<Double>> getMeanVector() {
		return meanVector;
	}

	private Map<String, Vector<Double>> getStandartDeviationVector() {
		return standartDeviationVector;
	}

	public Map<String, List<FeatureVector>> getAuthorFeatureVectorMap() {
		return authorFeatureVectorMap;
	}

	private void setAuthorFeatureVectorMap(
			Map<String, List<FeatureVector>> authorFeatureVectorMap) {
		this.authorFeatureVectorMap = authorFeatureVectorMap;
	}
	
	public double probabilityOfAuthor(String authorName){
		return getTotalVectors(authorName) / getTotalVectors();
	}
	
	private int getTotalVectors(String authorName){
		return getAuthorFeatureVectorMap().get(authorName).size();
	}

	private int getTotalVectors(){
		int counter = 0;
		
		for(String authorName : getAuthorFeatureVectorMap().keySet()){
			counter = counter + getAuthorFeatureVectorMap().get(authorName).size();
		}
		
		return counter;
	}
}
