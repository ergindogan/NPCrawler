package tr.com.ergindogan.stopword.classifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CLASSIFY {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int dataSize = 190;
		
		List<Passage> passages = new ArrayList<Passage>();
		
		//Produce data.
		for(int i = 0; i < dataSize; i++){
			Passage passage = new Passage("Author " + i, "Title " + i, Calendar.getInstance().getTime(), "Passage " + i);
			passages.add(passage);
		}
		
		CrossoverConstructor myConstructor = new CrossoverConstructor(passages, 90, 10, true);
		myConstructor.constructIterations();
	}

}
