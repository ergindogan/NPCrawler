package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;
import java.util.Vector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class FeatureVector {
	
	private Vector<Double> vector;
	
	public FeatureVector(List<Feature> features, String passage, List<String> paragraphs){
		double value = 0.0;
		setVector(new Vector<Double>(features.size()));
		for(Feature feature : features){
			if(feature instanceof AvarageParagraphLengthFeature){
				AvarageParagraphLengthFeature myFeature = (AvarageParagraphLengthFeature) feature;
				value = myFeature.extractFeatureResult(paragraphs);
			}else{
				value = feature.extractFeatureResult(passage);
			}
			if(Double.isNaN(value)){
				System.out.println("NAN!");
				value = 1;
			}
			addToVector(value);
		}
	}

	public Vector<Double> getVector() {
		return vector;
	}

	public void setVector(Vector<Double> vector) {
		this.vector = vector;
	}
	
	private void addToVector(double value){
		getVector().add(value);
	}
	
	public String getVectorString(){
		String vectorString = "[";
		for(int i = 0; i < getVector().size(); i++){
			vectorString = vectorString + getVector().get(i) + ",";
		}
		return vectorString.substring(0,vectorString.length() - 1) + "]";
	}
	
	public static String getVectorString(Vector<Double> vec){
		String vectorString = "[";
		for(int i = 0; i < vec.size(); i++){
			vectorString = vectorString + vec.get(i) + ",";
		}
		return vectorString.substring(0,vectorString.length() - 1) + "]";
	}

}
