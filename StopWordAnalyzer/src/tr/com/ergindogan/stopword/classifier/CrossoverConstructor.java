package tr.com.ergindogan.stopword.classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tr.com.ergindogan.stopword.reader.passage.Passage;

/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CrossoverConstructor {
	
	private List<CrossoverItem> itemList;
	
	private int trainRatio;
	private int testRatio;
	
	private boolean random;
	
	
	public CrossoverConstructor(List<Passage> pasageList, int trainRatio, int testRatio, boolean random){
		itemList = new ArrayList<CrossoverItem>();
		
		for(Passage passage : pasageList){
			CrossoverItem crossoverItem = new CrossoverItem(passage);
			itemList.add(crossoverItem);
		}
		
		setTrainRatio(trainRatio);
		setTestRatio(testRatio);
		setRandom(random);
	}
	
	public void constructIterations(){
		List<Integer> testItemIndexes;
		List<Integer> trainItemIndexes;
		
		List<Integer> lastTestItemIndexes;
		List<Integer> lastTrainItemIndexes;
		
		int mainIterationTestSize = (getItemList().size() * getTestRatio()) / (getTrainRatio() + getTestRatio());
		
		
		int mainIterationTrainSize = (mainIterationTestSize * getTrainRatio()) / getTestRatio();
		
		int mainIterationCount = getItemList().size() / mainIterationTestSize;
		
		
		int lastIterationTestSize = getItemList().size() % getTestRatio();
		
		int lastIterationTrainSize = (lastIterationTestSize * getTrainRatio()) / getTestRatio();
		
		//Main iteration loop
		for(int i = 0; i < mainIterationCount; i++){
			if(isRandom()){
				testItemIndexes = getTestItemIndexesRandom(mainIterationTestSize);
				
				trainItemIndexes = getTrainItemIndexesRandom(mainIterationTrainSize, testItemIndexes);
			}else{
				testItemIndexes = getTestItemIndexes(mainIterationTestSize);
				
				trainItemIndexes = getTrainItemIndexes(mainIterationTrainSize, testItemIndexes);
			}
			
			//print each iteration.
			System.out.println("--------------------------------------------------------------------");
			System.out.println("Iteration number = " + i);
			printIterationIndexes(testItemIndexes, trainItemIndexes);
		}
		
		//Last iteration if any.
		if(lastIterationTestSize != 0 && !allPickedForTest()){
			if(isRandom()){
				lastTestItemIndexes = getTestItemIndexesRandom(lastIterationTestSize);
				
				lastTrainItemIndexes = getTrainItemIndexesRandom(lastIterationTrainSize, lastTestItemIndexes);
			}else{
				lastTestItemIndexes = getTestItemIndexes(lastIterationTestSize);
				
				lastTrainItemIndexes = getTrainItemIndexes(lastIterationTrainSize, lastTestItemIndexes);
			}
			
			//print iteration.
			System.out.println("--------------------------------------------------------------------");
			System.out.println("LAST ITERATION!!!");
			printIterationIndexes(lastTestItemIndexes, lastTrainItemIndexes);
		}
	}
	
	private void printIterationIndexes(List<Integer> testItemIndexes, List<Integer> trainItemIndexes){
		String testIndexString = "Test Indexes = ";
		String trainIndexString = "Train Indexes = ";
		
		for(Integer index: testItemIndexes){
			testIndexString = testIndexString + index + ",";
		}
		
		testIndexString = testIndexString.substring(0, testIndexString.length() - 1);
		testIndexString = testIndexString + " Item count = " + testItemIndexes.size();
		
		for(Integer index: trainItemIndexes){
			trainIndexString = trainIndexString + index + ",";
		}
		
		trainIndexString = trainIndexString.substring(0, trainIndexString.length() - 1);
		trainIndexString = trainIndexString + " Item count = " + trainItemIndexes.size();
		
		System.out.println(testIndexString);
		System.out.println(trainIndexString);
	}
	
	private List<Integer> getTestItemIndexes(int testItemCount){
		int addedItemCounter = 0;
		List<Integer> testItems = new ArrayList<Integer>();
		
		for(CrossoverItem item : getItemList()){
			if(!item.isPickedForTesting()){
				if(addedItemCounter >= testItemCount){
					break;
				}else{
					setPickedForTesting(item);
					testItems.add(getItemList().indexOf(item));
					addedItemCounter++;
				}
			}
		}
		
		return testItems;
	}
	
	private List<Integer> getTestItemIndexesRandom(int testItemCount){
		List<Integer> testItems = new ArrayList<Integer>();
		Random rand = new Random();
		
		List<Integer> tempIndexList = new ArrayList<Integer>();
		
		for(CrossoverItem item : getItemList()){
			if(!item.isPickedForTesting()){
				tempIndexList.add(getItemList().indexOf(item));
			}
		}
		
		for(int i = 0; i < testItemCount; i++){
			int randomNumber = rand.nextInt(tempIndexList.size());
			
			CrossoverItem item = getItemList().get(tempIndexList.get(randomNumber));
			setPickedForTesting(item);
			testItems.add(tempIndexList.get(randomNumber));
			
		}
		
		return testItems;
	}
	
	private List<Integer> getTrainItemIndexes(int trainItemCount, List<Integer> testItemIndexes){
		int addedItemCounter = 0;
		
		List<Integer> trainItems = new ArrayList<Integer>();
		
		for(CrossoverItem item : getItemList()){
			if(!testItemIndexes.contains(getItemList().indexOf(item))){
				if(addedItemCounter >= trainItemCount){
					break;
				}else{
					trainItems.add(getItemList().indexOf(item));
					addedItemCounter++;
				}
			}
		}
		
		return trainItems;
	}
	
	private List<Integer> getTrainItemIndexesRandom(int trainItemCount, List<Integer> testItemIndexes){
		List<Integer> trainItems = new ArrayList<Integer>();
		Random rand = new Random();
		
		List<Integer> tempIndexList = new ArrayList<Integer>();
		
		for(CrossoverItem item : getItemList()){
			if(!testItemIndexes.contains(getItemList().indexOf(item))){
				tempIndexList.add(getItemList().indexOf(item));
			}
		}
		
		for(int i = 0; i < trainItemCount; i++){
			int randomNumber = rand.nextInt(tempIndexList.size());
			
			trainItems.add(tempIndexList.get(randomNumber));
			tempIndexList.remove(randomNumber);
			
		}
		
		return trainItems;
	}

	public List<CrossoverItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<CrossoverItem> itemList) {
		this.itemList = itemList;
	}

	public int getTrainRatio() {
		return trainRatio;
	}

	public void setTrainRatio(int trainRatio) {
		this.trainRatio = trainRatio;
	}

	public int getTestRatio() {
		return testRatio;
	}

	public void setTestRatio(int testRatio) {
		this.testRatio = testRatio;
	}

	private void setPickedForTesting(CrossoverItem item){
		int index = getItemList().indexOf(item);
		
		if(index != -1){
			getItemList().get(index).setPickedForTesting(true);
		}
		
	}
	
	private boolean allPickedForTest(){
		for(CrossoverItem item:getItemList()){
			if(!item.isPickedForTesting()){
				return false;
			}
		}
		return true;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}
	
}
