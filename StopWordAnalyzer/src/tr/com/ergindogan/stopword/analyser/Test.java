package tr.com.ergindogan.stopword.analyser;
import Corpus.Corpus;
import Corpus.TurkishChecker;


/**
 * @author ergindoganyildiz
 * 
 * Dec 4, 2015
 */
public class Test {
	
	public static void main(String args[]){
		TurkishChecker turkishChecker = new TurkishChecker();
		Corpus myCorpus = new Corpus("/Users/ergindoganyildiz/Desktop/StopWords/CumhuriyetCrawler.txt",
				turkishChecker);
		myCorpus.printToFile("/Users/ergindoganyildiz/Desktop/StopWords/TurkishCheckerAdded.txt");
	}

}
