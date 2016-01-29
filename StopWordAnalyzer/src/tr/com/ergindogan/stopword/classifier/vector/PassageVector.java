package tr.com.ergindogan.stopword.classifier.vector;

import java.util.ArrayList;
import java.util.List;

public class PassageVector {
	
	private List<BaseVector> vectorRepresentations;
	
	public PassageVector(){
		vectorRepresentations = new ArrayList<BaseVector>();
	}

	public void addToRepresentationList(BaseVector vector){
		getVectorRepresentations().add(vector);
	}

	public List<BaseVector> getVectorRepresentations() {
		return vectorRepresentations;
	}

	public void setVectorRepresentations(List<BaseVector> vectorRepresentations) {
		this.vectorRepresentations = vectorRepresentations;
	}
	
}
