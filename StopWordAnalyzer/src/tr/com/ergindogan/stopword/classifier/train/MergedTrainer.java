package tr.com.ergindogan.stopword.classifier.train;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.vector.BaseVector;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;
import tr.com.ergindogan.stopword.classifier.vector.NominalVector;
import tr.com.ergindogan.stopword.classifier.vector.PassageVector;


public class MergedTrainer extends BaseTrainer {
	
	private List<Feature> features;
	private LinkedHashMap<String, Integer> nominalWordMap;
	private Map<String,List<PassageVector>> authorPassageVectorMap;
	
	
	private Map<String, NominalVector> nominalTrainMatrix;
	
	private Map<String,Vector<Double>> meanVector;
	private Map<String,Vector<Double>> standartDeviationVector;

	public MergedTrainer(Map<String,List<PassageVector>> authorPassageVectorMap, 
			List<Feature> features, LinkedHashMap<String, Integer> nominalWordMap){
		setAuthorPassageVectorMap(authorPassageVectorMap);
		setFeatures(features);
		setNominalWordMap(nominalWordMap);
	}
	
	@Override
	public void train() {
		NominalVector nominalTempVector;
		List<FeatureVector> authorsFeatureVectorList;
		
		for(String authorName : getAuthorPassageVectorMap().keySet()){
			List<PassageVector> passageVectorList = getAuthorPassageVectorMap().get(authorName);
			
			nominalTempVector = new NominalVector(getNominalWordMap());
			authorsFeatureVectorList = new ArrayList<FeatureVector>();
			
			for(PassageVector passageVector:passageVectorList){
				List<BaseVector> vectorRepresentations = passageVector.getVectorRepresentations();
				
				for(BaseVector vec:vectorRepresentations){
					if(vec instanceof NominalVector){
						NominalVector myNominalVector = (NominalVector) vec;
						
						nominalTempVector.addToVector(calculateConProb(myNominalVector.getVector()));
					}else{
						//Add to feature vector list.
						FeatureVector myFeatureVector = (FeatureVector) vec;
						authorsFeatureVectorList.add(myFeatureVector);
					}
				}
			}
			
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

}
