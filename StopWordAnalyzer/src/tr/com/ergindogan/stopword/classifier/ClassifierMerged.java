package tr.com.ergindogan.stopword.classifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Corpus.Sentence;
import Corpus.TurkishSplitter;
import tr.com.ergindogan.stopword.classifier.crossover.CrossValidationConstructor;
import tr.com.ergindogan.stopword.classifier.crossover.Iteration;
import tr.com.ergindogan.stopword.classifier.feature.Feature;
import tr.com.ergindogan.stopword.classifier.test.MergedTester;
import tr.com.ergindogan.stopword.classifier.test.TestResult;
import tr.com.ergindogan.stopword.classifier.train.MergedTrainer;
import tr.com.ergindogan.stopword.classifier.vector.FeatureVector;
import tr.com.ergindogan.stopword.classifier.vector.NominalVector;
import tr.com.ergindogan.stopword.classifier.vector.PassageVector;
import tr.com.ergindogan.stopword.reader.passage.Passage;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.apps.TurkishMorphParser;
import zemberek.morphology.apps.TurkishSentenceParser;
import zemberek.morphology.parser.SentenceMorphParse;

public class ClassifierMerged extends BaseClassifier{
	
	private Map<String,List<PassageVector>> passageVectorsToClassify;
	private LinkedHashMap<String, Integer> nominalWordMap;
	private List<Feature> features;
	
	private static TurkishMorphParser morphParser;
	private static Z3MarkovModelDisambiguator disambiguator;
	private static TurkishSentenceParser sentenceParser;
	private static TurkishSplitter ts;
	
	public ClassifierMerged(Map<String,List<Passage>> myMap, 
			List<Feature> features, LinkedHashMap<String, Integer> nominalWordMap, int trainRatio, int testRatio){
		super(trainRatio, testRatio, new TestResult());
		setFeatures(features);
		setNominalWordMap(nominalWordMap);
		setPassageVectorsToClassify(myMap);
		
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
	
	public void classify(){
		TestResult result;
		
		CrossValidationConstructor crossoverConstructor;
		Map<String,List<Iteration>> allIterations = new HashMap<String,List<Iteration>>();
		
		Map<String,List<PassageVector>> trainMap = new HashMap<String,List<PassageVector>>();
		Map<String,List<PassageVector>> testMap = new HashMap<String,List<PassageVector>>();
		
		List<PassageVector> trainFeatureVectorList;
		List<PassageVector> testFeatureVectorList;
		
		int iterationCount = 0;
		
		//Construct final iteration Indexes.
		for(String authorName : getPassageVectorsToClassify().keySet()){
			//burada her bir loop her bir yazarin butun train test ikililerini dondurucek.
			List<PassageVector> passageVectorList = getPassageVectorsToClassify().get(authorName);
			crossoverConstructor = new CrossValidationConstructor(passageVectorList, getTrainRatio(), getTestRatio(), false);
			
			List<Iteration> iterationsForAuthor = crossoverConstructor.constructIterations();
			allIterations.put(authorName, iterationsForAuthor);
			iterationCount = iterationsForAuthor.size();
		}
		
		//With final iteration indexes ready, now we can construct each iterations author-feature vectors.
		for(int i = 0; i < iterationCount; i++){
			
			for(String authorName : allIterations.keySet()){
				trainFeatureVectorList = new ArrayList<PassageVector>();
				testFeatureVectorList = new ArrayList<PassageVector>();
				
				List<Iteration> iterations = allIterations.get(authorName);
				Iteration currentIterationToAdd = iterations.get(i);
				
				//put train feature vectors into train feature vector list
				for(int trainIndex:currentIterationToAdd.getTrainIndexes()){
					trainFeatureVectorList.add(getPassageVectorsToClassify().get(authorName).get(trainIndex));
				}
				
				//put test feature vectors into test feature vector list
				for(int testIndex:currentIterationToAdd.getTestIndexes()){
					testFeatureVectorList.add(getPassageVectorsToClassify().get(authorName).get(testIndex));
				}
				
				trainMap.put(authorName, trainFeatureVectorList);
				testMap.put(authorName, testFeatureVectorList);
			}
			
			//train the trainMap
			MergedTrainer mergedTrainer = new MergedTrainer(trainMap, getFeatures(), getNominalWordMap());
			mergedTrainer.train();
			
			//test the testMap
			MergedTester mergedTester = new MergedTester(testMap, mergedTrainer);
			result = mergedTester.test();
			
			//add mid results to the final result.
			addMidResultsToFinalResult(result);
			System.out.println("Iteration " + i + " correct : " + result.getCorrectGuessCounter() + " false : " + result.getFalseGuessCounter());
			
			//reinitialize maps after each iteration.
			trainMap = new HashMap<String,List<PassageVector>>();
			testMap = new HashMap<String,List<PassageVector>>();
		}
		
		System.out.println("Classification success rate : %" + getFinalResult().getSuccessRate());
	}

	public Map<String, List<PassageVector>> getPassageVectorsToClassify() {
		return passageVectorsToClassify;
	}

	public void setPassageVectorsToClassify(Map<String,List<Passage>> myMap) {
		this.passageVectorsToClassify = extractVectors(myMap, getFeatures(), getNominalWordMap());
	}

	public LinkedHashMap<String, Integer> getNominalWordMap() {
		return nominalWordMap;
	}

	public void setNominalWordMap(LinkedHashMap<String, Integer> nominalWordMap) {
		this.nominalWordMap = nominalWordMap;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	
	private Map<String,List<PassageVector>> extractVectors(Map<String,List<Passage>> myMap, 
			List<Feature> features, LinkedHashMap<String, Integer> myWordMap){
		System.out.println("Vector Extraction Started!");
		long startTime = System.currentTimeMillis();
		
		Map<String,List<PassageVector>> passageVectorMap = new HashMap<String,List<PassageVector>>();
		List<PassageVector> tempPassageVectorList;
		PassageVector passageVector;
		
		for(String authorName : myMap.keySet()){
			List<Passage> passages = myMap.get(authorName);
			tempPassageVectorList = new ArrayList<PassageVector>();
			
			for(Passage passage : passages){
				if(!passage.getPassage().isEmpty()){
					FeatureVector featureVector = new FeatureVector(features, passage.getPassage(), passage.getParagraphs());
					NominalVector nominalVector = new NominalVector(getNominalWordMap(), getRootWordPassage(passage.getPassage()));
					
					passageVector = new PassageVector();
					passageVector.addToRepresentationList(featureVector);
					passageVector.addToRepresentationList(nominalVector);
					
					tempPassageVectorList.add(passageVector);
				}
			}
			passageVectorMap.put(authorName, tempPassageVectorList);
		}
		
		long endTime   = System.currentTimeMillis();
		System.out.println("Vector Extraction Finished!");
		
		long totalTime = endTime - startTime;
		System.out.println("It took " + totalTime + " miliseconds to extract vectors.");
		
		return passageVectorMap;
	}
	
	//We are finding the roots on the fly. This is repeated every time classifier does run.
	private String getRootWordPassage(String passage){
		List<Sentence> sentenceList = ts.split(passage);
		
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
		
		if(!tempSentence.isEmpty()){
			tempSentence = tempSentence.substring(0, tempSentence.length() - 1);
		}
		
		return tempSentence;
	}

}
