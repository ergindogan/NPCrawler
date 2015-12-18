package tr.com.ergindogan.stopword.classifier.feature;

public class PunctuationCountFeature extends Feature {

	private String punctutations = ".,:;";
	
	public PunctuationCountFeature() {
		super();
	}

	@Override
	public double extractFeatureResult(String passage) {
		double counter = 0.0;
		for(int i = 0; i < passage.length(); i++){
			if(punctutations.contains(Character.toString(passage.charAt(i)))){
				counter++;
			}
		}
		
		return counter;
	}

}
