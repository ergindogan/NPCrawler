import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.ambiguity.AbstractDisambiguator.WordData;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Dictionary.Pos;
import Dictionary.Word;
import WordNet.SynSet;
import WordNet.WordNet;


public abstract class Test {
	
	private static TurkishMorphParser morphParser;
	private static Z3MarkovModelDisambiguator disambiguator;
	private static TurkishSentenceParser sentenceParser;
	private static TurkishSplitter ts;
	
	//No need to call load multiple times.
	static{
		try {
			ts = new TurkishSplitter();
			morphParser = TurkishMorphParser.createWithDefaults();
			disambiguator = new Z3MarkovModelDisambiguator();
			sentenceParser = new TurkishSentenceParser(morphParser, disambiguator);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		TurkishSplitter ts = new TurkishSplitter();
//		List<Sentence> sentenceList = ts.split("Siyaset meydanında kurusıkı atanlar da, siyaseti sadece Cumhurbaşkanı Erdoğan'a "
//				+ "saldırmak biçiminde görenler de 'Yeni Türkiye' ile eskisi arasındaki farklardan habersizler. Eskiden en büyük ekonomik "
//				+ "krizler bile nispeten hafif hasarlarla atlatılabilirdi. Köylü köyüne, kentli de kendi kabuğuna kapanırdı. "
//				+ "Bugün ise, 300 milyar doları aşkın bir dış ticaret hacmi, milyonlarca kişinin tüketim kredisi kullandığı bir. ");
//		
//		HmmDisambiguation hmmDisambiguation = new HmmDisambiguation();
//		hmmDisambiguation.loadModel();
//		
//		FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("C:/Users/ergindogan/thesis/nlptoolkit/turkish_finite_state_machine.xml", 
//				new TxtDictionary("C:/Users/ergindogan/thesis/nlptoolkit/turkish_dictionary.txt", 
//						new TurkishWordComparator()));
//				
//		
//		for (Sentence sentence : sentenceList) {
//			
//			Parse[][] myArray = fsm.morphologicalAnalysis(sentence, false);
//			
//			ArrayList<Parse> diambiguatedList = hmmDisambiguation.disambiguate(myArray);
//			
//			for (Parse parse : diambiguatedList) {
//				System.out.print(parse.getWord() + " ");
//			}
//			System.out.println();
//
//		}
		
		HashMap<String, String> myMap = new HashMap<String, String>();
		
		HashMap<String, Integer> myRep = new HashMap<String, Integer>();
		
		WordNet wordNet = new WordNet("/Users/ergindoganyildiz/Downloads/balkanet.xml", new Locale("tr"));
		for(SynSet ss:wordNet.getSynSets()){
			if(ss.getPos().equals(Pos.NOUN)){
//				System.out.println("ID : " + ss.getId() + "Synonym : " + ss.getSynonym().getLiteralString());
				myMap.put(ss.getId(), ss.getSynonym().getLiteralString());
				myRep.put(ss.getId(), 0);
			}
			
//			System.out.println(ss.getDefinition());
		}
		
		
//		printMap(myMap);
		
		String passage = "Cizre sokak çıkmak yasak ve devlet ilçe operasyon "
				+ "üzeri sürmek tartışmak insan hak duymak ve Hdp almak oy jakoben maske olmak kullanmak . "
				+ "önce insan hak boyut bakmak . devlet , kent özerk ilân etmek 120 bin nüfus bir ilçe "
				+ "esir almak birkaç 100 kişi terörist grup yönelik operasyon meşruiyet olmak iddia Ediliyor . "
				+ "yaşamak 90 yıl devlet refleks hatırlamak söylenmek .";
		
		List<String> words = splitPassageIntoWords(passage);
		
		String myString;
		String meanings[];
		
		for(String word:words){
			for(String key:myMap.keySet()){
				myString = myMap.get(key);
				meanings = myString.split(",");
				
				for(String meaning:meanings){
					if(meaning.equals(word)){
						myRep.put(key, myRep.get(key) + 1);
					}
				}
			}
		}
		
		for(String id:myRep.keySet()){
			if(myRep.get(id) >= 1){
				System.out.println(myMap.get(id) + " | counter : " +  myRep.get(id));
			}
		}
		
	}
	
	
	private static void printMap(HashMap<String, String> myMap){
		int counter = 0;
		String myString;
		int meanings = 0;
		
		for(String key:myMap.keySet()){
			myString = myMap.get(key);
			meanings = myString.split(",").length;
			
			if(counter < 100 && meanings >= 8){
				System.out.println("Id : " + key + " Sysnonms : " + myMap.get(key));
				counter++;
			}
		}
	}
	
	public static List<String> splitPassageIntoWords(String passage){
		List<String> wordList = new ArrayList<String>();
		
		List<Sentence> sentenceList = splitPassageIntoSentences(passage);
		
		for(Sentence sentence:sentenceList){
			for(Word word:sentence.getWords()){
				wordList.add(word.getName());
			}
		}
		
		return wordList;
	}
	
	public static List<Sentence> splitPassageIntoSentences(String passage){
		return ts.split(passage);
	}
}
