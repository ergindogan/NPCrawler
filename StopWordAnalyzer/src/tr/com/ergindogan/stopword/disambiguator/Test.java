package tr.com.ergindogan.stopword.disambiguator;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		System.out.println("Load Started!");
		long startTime = System.currentTimeMillis();
		
		DisambiguatedDataCreator ddc = new DisambiguatedDataCreator(
				new File("/Users/ergindoganyildiz/Desktop/withParagraphs"), 
				true);
		
		ddc.createDisambiguatedFiles();
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Load Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to create disambiguated files.");
	}

}
