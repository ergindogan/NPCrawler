package tr.com.ergindogan.stopword.reader.stopword;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class StopWordReaderTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File myFrequencyFile = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/"
				+ "koklerine_ayrilmis/wordFrequency.txt");
		
		StopWordReader stopWordReader = new StopWordReader(myFrequencyFile);
		LinkedHashMap<String, Integer> myWordMap = stopWordReader.readFileToMap(10,10);
		
		for(String key:myWordMap.keySet()){
			System.out.println("Word :" + key + " Frequency : " + myWordMap.get(key));
		}
	}

}
