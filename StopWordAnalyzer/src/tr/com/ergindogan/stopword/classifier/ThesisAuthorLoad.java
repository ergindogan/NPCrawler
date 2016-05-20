package tr.com.ergindogan.stopword.classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.eval.Mean;
import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationType;
import tr.com.ergindogan.stopword.classifier.feature.AvarageParagraphLengthFeature;
import tr.com.ergindogan.stopword.classifier.feature.AvarageWordLengthFeature;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.feature.PunctuationCountFeature;
import tr.com.ergindogan.stopword.classifier.feature.SentenceLengthAsWordCountFeature;
import tr.com.ergindogan.stopword.classifier.feature.VocublaryExtendFeature;
import tr.com.ergindogan.stopword.classifier.feature.WordCountFeature;
import tr.com.ergindogan.stopword.loader.DistinctAuthorLoader;
import tr.com.ergindogan.stopword.reader.passage.Passage;

public class ThesisAuthorLoad {
	
	public static void main(String args[]){
		File folderToLoad = new File("/Users/ergindoganyildiz/Desktop/withParagraphs/safe");
		
		//Load data...
		DistinctAuthorLoader loader = new DistinctAuthorLoader(folderToLoad);
		
		
		//tokmak : 1000
		//emin çölaşan : 870
		//saygı öztürk : 610
		//abdülkadir selvi : 740
		//uğur dündar : 720
		//Burada yazarlarin en az 100 yazisi ve tam 100 ile bolunur sayida yazisini myMap icine koyduk.
		Map<String,List<Passage>> myMap = loader.loadAndSelectQualifiedAuthors(CrossValidationType._90_10, -1, -1, true, -1);
		
		
		
		Map<String,List<Double>> avarageParagraphMap = new HashMap<String,List<Double>>();
		
		Map<String,Double> avarageMap = new HashMap<String,Double>();
		Map<String,Double> sdMap = new HashMap<String,Double>();
		
		Map<String,Double> tookManyPassagesToGetClose = new HashMap<String,Double>();
		
		
		AvarageParagraphLengthFeature aplf = new AvarageParagraphLengthFeature();
		
		for(String authorName : myMap.keySet()){
//			System.out.println(authorName + ":" + myMap.get(authorName).size());
			List<Passage> passages = myMap.get(authorName);
			double total = 0.0;
			double mean = 0.0;
			List<Double> avarageList = new ArrayList<Double>();
			
			//loop for mean
			for(Passage passage:passages){
				double avarageParagraph = aplf.extractFeatureResult(passage.getParagraphs());
				total += avarageParagraph;
				avarageList.add(avarageParagraph);
			}
			
			mean = total / passages.size();
			
			avarageMap.put(authorName, mean);
			avarageParagraphMap.put(authorName, avarageList);
			
			//loop for sd
			double upside = 0.0;
			double sd = 0.0;
			for(Double val:avarageList){
				upside = upside + Math.pow(val-avarageMap.get(authorName), 2);
			}
			
			sd = Math.sqrt(upside / avarageList.size());
			
			sdMap.put(authorName, sd);
		}
		
		for(String authorName : myMap.keySet()){
			List<Double> paragraphAvarages = avarageParagraphMap.get(authorName);
			
			double counter = 0;
			for(Double paragraphAvarage:paragraphAvarages){
				if(paragraphAvarage > (avarageMap.get(authorName) - sdMap.get(authorName)) &&
						paragraphAvarage < (avarageMap.get(authorName) + sdMap.get(authorName))){
					counter++;
				}
			}
			System.out.println("There is " + counter + " passages that are within rage in " + paragraphAvarages.size() + " for " + authorName);
			
			tookManyPassagesToGetClose.put(authorName, (counter / paragraphAvarages.size()) * 100);
		}
		
		double finalTotal = 0.0;
		for(String author:tookManyPassagesToGetClose.keySet()){
			finalTotal += tookManyPassagesToGetClose.get(author); 
		}
		
		System.out.println("Percentage : " + finalTotal / tookManyPassagesToGetClose.keySet().size());
		System.out.println("Min bound = " + ((finalTotal / tookManyPassagesToGetClose.keySet().size()) * 156.68) / 100);
		
		//115,94
	}

	
	
//	for(String authorName : myMap.keySet()){
//	List<Passage> passages = myMap.get(authorName);
//	
//	for(int i = 1; i < passages.size(); i++){
//		List<Passage> innerPassages = new ArrayList<Passage>();
//		
//		for(int j = 0; j < i; j++){
//			innerPassages.add(passages.get(j));
//		}
//		
//		double total = 0.0;
//		double avarage = 0.0;
//		for(Passage passage:innerPassages){
//			total += aplf.extractFeatureResult(passage.getParagraphs());
//		}
//		
//		avarage = total / innerPassages.size();
//		
//		if(Math.abs(avarageMap.get(authorName) - avarage) <= ((avarageMap.get(authorName) * 10) / 100)){
//			tookManyPassagesToGetClose.put(authorName, i + 0.0);
////			System.out.println("Author name : " + authorName + " took " + i + " passages to reach the mean.");
//			break;
//		}
//	}
//}
//
//for(String authorName:tookManyPassagesToGetClose.keySet()){
//	double took = tookManyPassagesToGetClose.get(authorName);
//	
//}
	
	
	
}
