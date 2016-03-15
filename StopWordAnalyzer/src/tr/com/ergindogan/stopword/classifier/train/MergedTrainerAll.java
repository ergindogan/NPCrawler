package tr.com.ergindogan.stopword.classifier.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import WordNet.Literal;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.vector.BaseVector;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;
import tr.com.ergindogan.stopword.classifier.vector.NominalVector;
import tr.com.ergindogan.stopword.classifier.vector.PassageVector;
import tr.com.ergindogan.stopword.classifier.vector.SynonymVector;


public class MergedTrainerAll extends BaseTrainer {
	
	private List<Feature> features;
	private LinkedHashMap<String, Integer> nominalWordMap;
	private Map<String,List<PassageVector>> authorPassageVectorMap;
	
	
	private Map<String, NominalVector> nominalTrainMatrix;
	
	private Map<String,Vector<Double>> meanVector;
	private Map<String,Vector<Double>> standartDeviationVector;
	
	private LinkedHashMap<String, List<Literal>> mySynonymMap;
	private Map<String, SynonymVector> synonymTrainMatrix;

	public MergedTrainerAll(Map<String,List<PassageVector>> authorPassageVectorMap, 
			List<Feature> features, LinkedHashMap<String, Integer> nominalWordMap, 
			LinkedHashMap<String, List<Literal>> synonymSetMap){
		setAuthorPassageVectorMap(authorPassageVectorMap);
		setFeatures(features);
		setNominalWordMap(nominalWordMap);
		setMySynonymMap(synonymSetMap);
		
		setNominalTrainMatrix(new HashMap<String, NominalVector>());
		setMeanVector(new HashMap<String, Vector<Double>>());
		setStandartDeviationVector(new HashMap<String, Vector<Double>>());
		setSynonymTrainMatrix(new HashMap<String, SynonymVector>());
	}
	
	@Override
	public void train() {
		NominalVector nominalTempVector;
		SynonymVector synonymTempVector;
		List<FeatureVector> authorsFeatureVectorList;
		
		for(String authorName : getAuthorPassageVectorMap().keySet()){
			List<PassageVector> passageVectorList = getAuthorPassageVectorMap().get(authorName);
			
			nominalTempVector = new NominalVector(getNominalWordMap());
			synonymTempVector = new SynonymVector(getMySynonymMap());
			authorsFeatureVectorList = new ArrayList<FeatureVector>();
			
			for(PassageVector passageVector:passageVectorList){
				List<BaseVector> vectorRepresentations = passageVector.getVectorRepresentations();
				
				for(BaseVector vec:vectorRepresentations){
					if(vec instanceof NominalVector){
						NominalVector myNominalVector = (NominalVector) vec;
						
						nominalTempVector.addToVector(calculateConProb(myNominalVector.getVector()));
					}else if(vec instanceof SynonymVector){
						SynonymVector mySynonymVector = (SynonymVector) vec;
						
						synonymTempVector.addToVector(calculateConProb(mySynonymVector.getVector()));
					}else{
						//Add to feature vector list.
						FeatureVector myFeatureVector = (FeatureVector) vec;
						authorsFeatureVectorList.add(myFeatureVector);
					}
				}
			}
			
			//Set author's synonym vector
			getSynonymTrainMatrix().put(authorName, synonymTempVector);
			
			//Set author's nominal vector
			getNominalTrainMatrix().put(authorName, nominalTempVector);
			
			//Calculate and set author's mean feature vector
			Vector<Double> meanVector = calculateMeanVetor(authorsFeatureVectorList, getFeatures());
			getMeanVector().put(authorName, meanVector);
			
			//Calculate and set author's standard deviation feature vector
			Vector<Double> standardDeviationVector = calculateStandartDeviationVector(authorsFeatureVectorList, authorName, getFeatures() ,getMeanVector());
			getStandartDeviationVector().put(authorName, standardDeviationVector);
		}
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public LinkedHashMap<String, Integer> getNominalWordMap() {
		return nominalWordMap;
	}

	public void setNominalWordMap(LinkedHashMap<String, Integer> nominalWordMap) {
		this.nominalWordMap = nominalWordMap;
	}

	public Map<String, List<PassageVector>> getAuthorPassageVectorMap() {
		return authorPassageVectorMap;
	}

	public void setAuthorPassageVectorMap(
			Map<String, List<PassageVector>> authorPassageVectorMap) {
		this.authorPassageVectorMap = authorPassageVectorMap;
	}

	public Map<String, NominalVector> getNominalTrainMatrix() {
		return nominalTrainMatrix;
	}

	public void setNominalTrainMatrix(Map<String, NominalVector> nominalTrainMatrix) {
		this.nominalTrainMatrix = nominalTrainMatrix;
	}

	public Map<String, Vector<Double>> getMeanVector() {
		return meanVector;
	}

	public void setMeanVector(Map<String, Vector<Double>> meanVector) {
		this.meanVector = meanVector;
	}

	public Map<String, Vector<Double>> getStandartDeviationVector() {
		return standartDeviationVector;
	}

	public void setStandartDeviationVector(
			Map<String, Vector<Double>> standartDeviationVector) {
		this.standartDeviationVector = standartDeviationVector;
	}
	
	public LinkedHashMap<String, List<Literal>> getMySynonymMap() {
		return mySynonymMap;
	}

	public void setMySynonymMap(LinkedHashMap<String, List<Literal>> mySynonymMap) {
		this.mySynonymMap = mySynonymMap;
	}

	public Map<String, SynonymVector> getSynonymTrainMatrix() {
		return synonymTrainMatrix;
	}

	public void setSynonymTrainMatrix(Map<String, SynonymVector> synonymTrainMatrix) {
		this.synonymTrainMatrix = synonymTrainMatrix;
	}

	public double getMean(String authorName, int featureIndex){
		return getMeanVector().get(authorName).get(featureIndex);
	}
	
	public double getStandartDeviation(String authorName, int featureIndex){
		return getStandartDeviationVector().get(authorName).get(featureIndex);
	}
	
	private int getTotalVectors(String authorName){
		return getAuthorPassageVectorMap().get(authorName).size();
	}

	private double getTotalVectors(){
		double counter = 0;
		
		for(String authorName : getAuthorPassageVectorMap().keySet()){
			counter = counter + getAuthorPassageVectorMap().get(authorName).size();
		}
		
		return counter;
	}

	public double probabilityOfAuthor(String authorName){
		return getTotalVectors(authorName) / getTotalVectors();
	}
}
