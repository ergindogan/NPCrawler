package tr.com.ergindogan.stopword.analyser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import MorphologicalAnalysis.Parse;


/**
 * @author ergindoganyildiz
 * 
 * Dec 3, 2015
 */
public class FileAnalyser extends BaseAnalyser implements Runnable{
	
	private File fileToAnalyse;
	private boolean finishedAnalysing;
	
	public FileAnalyser(File fileToAnalyse){
		setFileToAnalyse(fileToAnalyse);
		addToAnalyserList(this);
	}
	
	@Override
	public void run() {
		System.out.println("Starting File Analyser for file : " + getFileToAnalyse().getName());
		analyseFile();
	}
	
	public void analyseFile(){
		BufferedReader br = null;
		
		try {

			String sCurrentLine;
			int i = 0;
			Record myRecord = new Record();
			TurkishSplitter ts = new TurkishSplitter();

//			Reader reader = new InputStreamReader(new FileInputStream(getFileToAnalyse().getAbsolutePath()), "windows-1254");
//			br = new BufferedReader(reader);
			br = new BufferedReader(new FileReader(getFileToAnalyse().getAbsolutePath()));

			while ((sCurrentLine = br.readLine()) != null) {
				if(i % 5 == 0){
					myRecord.setWriterNameHeadlineDate(sCurrentLine);
				}else if(i % 5 == 1){
					myRecord.setPassage(sCurrentLine);
				}else if(i % 5 == 4){
					//Process the record
					List<Sentence> sentenceList = ts.split(myRecord.getPassage());
					
					for (Sentence sentence : sentenceList) {
						
						Parse[][] myArray = FSM.morphologicalAnalysis(sentence, false);
						
						ArrayList<Parse> diambiguatedList = HMM_DISAMBIGUATION.disambiguate(myArray);
						
						if(diambiguatedList != null){
							for (Parse parse : diambiguatedList) {
								addToMap(parse.getWord().toString());
							}
							//increase the processed passage count
							increasePROCESSED_SENTENCE_COUNT();
						}
					}
					myRecord = new Record();
				}
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				updateAnalyseStatus();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public File getFileToAnalyse() {
		return fileToAnalyse;
	}

	public void setFileToAnalyse(File fileToAnalyse) {
		this.fileToAnalyse = fileToAnalyse;
	}

	public boolean isFinishedAnalysing() {
		return finishedAnalysing;
	}

	public void setFinishedAnalysing(boolean finishedAnalysing) {
		this.finishedAnalysing = finishedAnalysing;
	}
}
