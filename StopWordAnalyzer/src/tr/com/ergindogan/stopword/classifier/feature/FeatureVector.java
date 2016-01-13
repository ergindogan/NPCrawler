package tr.com.ergindogan.stopword.classifier.feature;

import java.util.List;
import java.util.Vector;

/**
 * @author ergindoganyildiz
 * 
 * Dec 11, 2015
 */
public class FeatureVector extends BaseVector{
	
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

}
