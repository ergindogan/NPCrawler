package tr.com.ergindogan.stopword.classifier.vector;

import java.util.Vector;

public abstract class BaseVector {

	private Vector<Double> vector;

	public Vector<Double> getVector() {
		return vector;
	}

	public void setVector(Vector<Double> vector) {
		this.vector = vector;
	}
	
	protected void addToVector(double value){
		getVector().add(value);
	}
	
	public void addToVector(Vector<Double> p1){
		double newValue = 0.0;
		
		for(int i = 0; i < p1.size(); i++){
			newValue = getVector().get(i) + p1.get(i);
			getVector().set(i, newValue);
		}
	}
	
	public String getVectorString(){
		String vectorString = "[";
		for(int i = 0; i < getVector().size(); i++){
			vectorString = vectorString + getVector().get(i) + ",";
		}
		return vectorString.substring(0,vectorString.length() - 1) + "]";
	}
	
	protected static String getVectorString(Vector<Double> vec){
		String vectorString = "[";
		for(int i = 0; i < vec.size(); i++){
			vectorString = vectorString + vec.get(i) + ",";
		}
		return vectorString.substring(0,vectorString.length() - 1) + "]";
	}
}
