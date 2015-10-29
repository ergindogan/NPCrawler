import java.util.ArrayList;
import java.util.List;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.Parse;
import MorphologicalDisambiguation.HmmDisambiguation;


public abstract class Test {

	public static void main(String[] args) {
		TurkishSplitter ts = new TurkishSplitter();
		List<Sentence> sentenceList = ts.split("Siyaset meydanında kurusıkı atanlar da, siyaseti sadece Cumhurbaşkanı Erdoğan'a "
				+ "saldırmak biçiminde görenler de 'Yeni Türkiye' ile eskisi arasındaki farklardan habersizler. Eskiden en büyük ekonomik "
				+ "krizler bile nispeten hafif hasarlarla atlatılabilirdi. Köylü köyüne, kentli de kendi kabuğuna kapanırdı. "
				+ "Bugün ise, 300 milyar doları aşkın bir dış ticaret hacmi, milyonlarca kişinin tüketim kredisi kullandığı bir. ");
		
		HmmDisambiguation hmmDisambiguation = new HmmDisambiguation();
		hmmDisambiguation.loadModel();
		
		FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer("C:/Users/ergindogan/thesis/nlptoolkit/turkish_finite_state_machine.xml", 
				new TxtDictionary("C:/Users/ergindogan/thesis/nlptoolkit/turkish_dictionary.txt", 
						new TurkishWordComparator()));
				
		
		for (Sentence sentence : sentenceList) {
			
			Parse[][] myArray = fsm.morphologicalAnalysis(sentence, false);
			
			ArrayList<Parse> diambiguatedList = hmmDisambiguation.disambiguate(myArray);
			
			for (Parse parse : diambiguatedList) {
				System.out.print(parse.getWord() + " ");
			}
			System.out.println();

		}
	}
}
