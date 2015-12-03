import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalDisambiguation.HmmDisambiguation;


/**
 * @author ergindoganyildiz
 * 
 * Dec 3, 2015
 */
public class ANALYZER {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		//Initialize HmmDiambiguator
		HmmDisambiguation hmmDisambiguation = new HmmDisambiguation();
		hmmDisambiguation.loadModel();
		
		FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("/Users/ergindoganyildiz/Documents/thesis/nlptoolkit/turkish_finite_state_machine.xml", 
				new TxtDictionary("/Users/ergindoganyildiz/Documents/thesis/nlptoolkit/turkish_dictionary.txt", 
						new TurkishWordComparator()));
		
		BaseAnalyser.HMM_DISAMBIGUATION = hmmDisambiguation;
		BaseAnalyser.FSM = fsm;
		
		File folderToAnalyse = new File("/Users/ergindoganyildiz/Desktop/MultiThread");
		FilenameFilter myFileter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.contains("Crawler")){
					return true;
				}else{
					return false;
				}
			}
		};
		
		//Start fileAnalysers.
		for(File fileToAnalyse:folderToAnalyse.listFiles(myFileter)){
			FileAnalyser myAnalayser = new FileAnalyser(fileToAnalyse);
			new Thread(myAnalayser).start();
		}
		
		//Check if analyse has finished.
		while(true){
			if(BaseAnalyser.isFINISHED_ANALYSING()){
				System.out.println("Analyse finished! Writing results to file.");
				//Analyse finished. Write results to a file.
//				writeResultsToFile(folderToAnalyse.getAbsolutePath() + File.separator + "wordFrequency.txt");
				break;
			}else{
				System.out.println("Processed sentence count : " + BaseAnalyser.getPROCESSED_SENTENCE_COUNT());
				if(BaseAnalyser.getPROCESSED_SENTENCE_COUNT() % 10 == 0){
					writeResultsToFile(folderToAnalyse.getAbsolutePath() + File.separator + "wordFrequency.txt");
				}
			}
			Thread.sleep(1000);
		}
	}
	
	private static void writeResultsToFile(String filePath){
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
		    Map<String, Integer> mySortedMap = sortByValue(BaseAnalyser.getWordMap());
		    for(String key:mySortedMap.keySet()){
		    	Integer value = BaseAnalyser.getWordMap().get(key);
			       
		    	writer.write(key + " __ " + value + "\n");
		    }
		    System.out.println("Results successfully written to report file!");
		} catch (IOException ex) {
			System.err.println("Error when writing to report file!");
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				System.err.println("Error when closing the report file!");
			}
		}
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ){
	    List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        @Override
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() ) * -1;
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}

}
