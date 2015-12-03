import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalDisambiguation.HmmDisambiguation;


/**
 * @author ergindoganyildiz
 * 
 * Dec 3, 2015
 */
public abstract class BaseAnalyser {
	
	private static Map<String, Integer> WORD_MAP = new HashMap<String, Integer>();
	private static List<FileAnalyser> ANALYSER_LIST = new ArrayList<FileAnalyser>();
	private static boolean FINISHED_ANALYSING = false;
	private static AtomicInteger PROCESSED_SENTENCE_COUNT = new AtomicInteger(0);
	
	protected static HmmDisambiguation HMM_DISAMBIGUATION;
	protected static FsmMorphologicalAnalyzer FSM;
	
	protected static void addToAnalyserList(FileAnalyser fileAnalyser){
		ANALYSER_LIST.add(fileAnalyser);
	}

	protected static void updateAnalyseStatus(){
		boolean result = true;
		
		for(FileAnalyser fileAnalyser:ANALYSER_LIST){
			result = result && fileAnalyser.isFinishedAnalysing();
		}
		
		FINISHED_ANALYSING = result;
	}
	
	protected static void addToMap(String word){
		if(!WORD_MAP.containsKey(word)){
			WORD_MAP.put(word, 1);
		}else{
			WORD_MAP.put(word, WORD_MAP.get(word) + 1);
		}
	}
	
	public static Map<String, Integer> getWordMap(){
		return WORD_MAP;
	}

	public static boolean isFINISHED_ANALYSING() {
		return FINISHED_ANALYSING;
	}

	public static int getPROCESSED_SENTENCE_COUNT() {
		return PROCESSED_SENTENCE_COUNT.get();
	}
	
	protected static void increasePROCESSED_SENTENCE_COUNT(){
		PROCESSED_SENTENCE_COUNT.incrementAndGet();
	}

}
