package tr.com.ergindogan.stopword.classifier.train;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class Trainer extends BaseTrainer{
	
	private List<Feature> features;
	
	private Map<String,Vector<Double>> meanVector;
	private Map<String,Vector<Double>> standartDeviationVector;
	
	private Map<String,List<FeatureVector>> authorFeatureVectorMap;
	
	//Buraya yazarlara bolup featurelari cikarilmis halini vericez.
	public Trainer(Map<String,List<FeatureVector>> authorFeatureVectorMap, List<Feature> features){
		setAuthorFeatureVectorMap(authorFeatureVectorMap);
		setFeatures(features);
		setMeanVector(new HashMap<String, Vector<Double>>());
		setStandartDeviationVector(new HashMap<String, Vector<Double>>());
	}

	@Override
	public void train() {
		for(String authorName : getAuthorFeatureVectorMap().keySet()){
			List<FeatureVector> vectorList = getAuthorFeatureVectorMap().get(authorName);
			
			Vector<Double> meanVector = calculateMeanVetor(vectorList, getFeatures());
			getMeanVector().put(authorName, meanVector);
			
			Vector<Double> standardDeviationVector = calculateStandartDeviationVector(vectorList, authorName, getFeatures() ,getMeanVector());
			getStandartDeviationVector().put(authorName, standardDeviationVector);
		}
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

	private double getTotalVectors(){
		double counter = 0;
		
		for(String authorName : getAuthorFeatureVectorMap().keySet()){
			counter = counter + getAuthorFeatureVectorMap().get(authorName).size();
		}
		
		return counter;
	}

	private void setMeanVector(Map<String, Vector<Double>> meanVector) {
		this.meanVector = meanVector;
	}

	private void setStandartDeviationVector(
			Map<String, Vector<Double>> standartDeviationVector) {
		this.standartDeviationVector = standartDeviationVector;
	}

}
