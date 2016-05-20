package tr.com.ergindogan.stopword.analyser;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tr.com.ergindogan.stopword.loader.NewsPaperLoader;
import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Dictionary.Word;

/**
 * @author ergindoganyildiz
 * 
 * Dec 10, 2015
 */
public class StopwordAnalyzer {
	
	private static Map<String, Integer> WORD_MAP = new HashMap<String, Integer>();
	
	private static final String[] punctuations = {".", ",", "’", "”", "“", "*", "?",
			"\"", "!", "'", "-", ":", ";", ")",
			"(", " ", "/", "\n", "ı", "\n\n", "  " };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/window_size_and_padding");
		
		NewsPaperLoader loader = new NewsPaperLoader(folderToLoad);
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData(true);
		
		System.out.println("");
		
		System.out.println("StopWord work Started!");
		long startTime = System.currentTimeMillis();
		
		TurkishSplitter ts = new TurkishSplitter();
		
		for(NewsPaper newsPaper : NewsPaper.values()){
			if(myMap.containsKey(newsPaper)){
				Map<String,List<Passage>> authorMap = myMap.get(newsPaper);
				for(String authorName : authorMap.keySet()){
					List<Passage> passages = authorMap.get(authorName);
					for(Passage passage:passages){
						List<Sentence> sentenceList = ts.split(passage.getPassage());
						
						for (Sentence sentence : sentenceList) {
							
							for(Word word:sentence.getWords()){
								addToMap(word.toString());
							}
						}
					}
				}
			}
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("StopWord work Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds find stopWords!");
		
		writeResultsToFile(folderToLoad.getAbsolutePath() + File.separator + "wordFrequency.txt");
		
	}
	
	private static void addToMap(String word){
		if(!isPunctuation(word)){
			if(!WORD_MAP.containsKey(word)){
				WORD_MAP.put(word, 1);
			}else{
				WORD_MAP.put(word, WORD_MAP.get(word) + 1);
			}
		}
	}
	
	private static boolean isPunctuation(String word){
		for(int i = 0; i < punctuations.length; i++){
			if(word.equals(punctuations[i])){
				return true;
			}
		}
		return false;
	}
	
	private static void writeResultsToFile(String filePath){
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
		    Map<String, Integer> mySortedMap = sortByValue(WORD_MAP);
		    for(String key:mySortedMap.keySet()){
		    	Integer value = WORD_MAP.get(key);
			       
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
