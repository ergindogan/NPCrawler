package tr.com.ergindogan.stopword.disambiguator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import tr.com.ergindogan.stopword.loader.NewsPaperLoader;
import tr.com.ergindogan.stopword.reader.BaseReader;
import tr.com.ergindogan.stopword.reader.passage.NewsPaper;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.SentenceMorphParse;

public class DisambiguatedDataCreator extends BaseReader{
	
	private static TurkishMorphParser morphParser;
	private static Z3MarkovModelDisambiguator disambiguator;
	private static TurkishSentenceParser sentenceParser;
	private static TurkishSplitter ts;
	
	private boolean paragragh;
	
	private static final Logger logger = Logger.getLogger(DisambiguatedDataCreator.class);

	public DisambiguatedDataCreator(File fileToRead, boolean paragraph) {
		super(fileToRead);
		setParagragh(paragraph);
	}
	
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
	
	public void createDisambiguatedFiles(){
		NewsPaperLoader loader = new NewsPaperLoader(getFileToRead());
		Map<NewsPaper,Map<String,List<Passage>>> myMap = loader.loadData(isParagragh());
		
		for(NewsPaper newsPaper:myMap.keySet()){
			OutputController.configureLogger(new File(getFileToRead().getAbsolutePath() + "/" + newsPaper.toString() + "CrawlerKOK.txt"), false);
			Map<String,List<Passage>> authorMap = myMap.get(newsPaper);
			for(String authorName : authorMap.keySet()){
				List<Passage> passages = authorMap.get(authorName);
				for(Passage passage:passages){
					List<String> newParagraphs = new ArrayList<String>();
					String newSentence = "";
					
					for(String paragraph:passage.getParagraphs()){
						List<Sentence> sentenceList = ts.split(paragraph);
						
						List<String> wordList = new ArrayList<String>();
						String tempSentence = "";
						
						for (Sentence sentence : sentenceList) {
							try {
								SentenceMorphParse sentenceParse = sentenceParser.parse(sentence.toString());
								sentenceParser.disambiguate(sentenceParse);
								
								for (SentenceMorphParse.Entry entry : sentenceParse) {
						            wordList.add(entry.parses.get(0).getLemma());
						        }
							} catch (Exception e) {
								System.out.println(sentence.toString());
								continue;
							}
						}
						
						for(String word:wordList){
							tempSentence = tempSentence + word + " ";
						}
						tempSentence = tempSentence.substring(0, tempSentence.length() - 1);
						
						newParagraphs.add(tempSentence);
						newSentence = newSentence + tempSentence;
					}
					
					KoseYazisi koseYazisi = new KoseYazisi(
							passage.getAuthor(), 
							passage.getTitle(), 
							newSentence, 
							passage.getPublishDate());
					koseYazisi.setParagraphs(newParagraphs);
					
					String oneRecord = koseYazisi.getOneRecord();
					logger.warn(oneRecord);
					logger.warn("------------------------------------------------------------------------------------------ \n");
				}
			}
		}
	}

	public boolean isParagragh() {
		return paragragh;
	}

	public void setParagragh(boolean paragragh) {
		this.paragragh = paragragh;
	}

}
