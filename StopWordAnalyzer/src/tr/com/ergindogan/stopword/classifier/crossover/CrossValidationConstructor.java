package tr.com.ergindogan.stopword.classifier.crossover;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ergindoganyildiz
 * 
 * Dec 6, 2015
 */
public class CrossValidationConstructor {
	
	private List<CrossValidationItem> itemList;
	
	private int trainRatio;
	private int testRatio;
	
	private boolean random;
	
	
	public CrossValidationConstructor(List<?> vectorList, int trainRatio, int testRatio, boolean random){
		itemList = new ArrayList<CrossValidationItem>();
		
		for(Object item : vectorList){
			CrossValidationItem crossoverItem = new CrossValidationItem(item);
			itemList.add(crossoverItem);
		}
		
		setTrainRatio(trainRatio);
		setTestRatio(testRatio);
		setRandom(random);
	}
	
	public List<Iteration> constructIterations(){
		List<Iteration> iterationList = new ArrayList<Iteration>();
		
		List<Integer> testItemIndexes;
		List<Integer> trainItemIndexes;
		
		List<Integer> lastTestItemIndexes;
		List<Integer> lastTrainItemIndexes;
		
		int mainIterationTestSize = getItemList().size() / getTestRatio();
		
		int mainIterationTrainSize = getItemList().size() - mainIterationTestSize;
		
		int mainIterationCount = getTestRatio();
		
		
		int lastIterationTestSize = getItemList().size() % getTestRatio();
		
		int lastIterationTrainSize = getItemList().size() - lastIterationTestSize;
		
		//Main iteration loop
		for(int i = 0; i < mainIterationCount; i++){
			testItemIndexes = getTestItemIndexesRandom(mainIterationTestSize);
			
			trainItemIndexes = getTrainItemIndexes(mainIterationTrainSize, testItemIndexes);
			
			iterationList.add(new Iteration(trainItemIndexes, testItemIndexes));
			
//			printIterationIndexes(testItemIndexes, trainItemIndexes);
		}
		
		//Last iteration if any.
		if(lastIterationTestSize != 0 && !allPickedForTest()){
			lastTestItemIndexes = getTestItemIndexesRandom(lastIterationTestSize);
			
			lastTrainItemIndexes = getTrainItemIndexes(lastIterationTrainSize, lastTestItemIndexes);
			
			iterationList.add(new Iteration(lastTrainItemIndexes, lastTestItemIndexes));
			
//			printIterationIndexes(lastTestItemIndexes, lastTrainItemIndexes);
		}
		
		return iterationList;
	}
	
//	private void printIterationIndexes(List<Integer> testItemIndexes, List<Integer> trainItemIndexes){
//		String testIndexString = "Test Indexes = ";
//		String trainIndexString = "Train Indexes = ";
//		
//		for(Integer index: testItemIndexes){
//			testIndexString = testIndexString + index + ",";
//		}
//		
//		testIndexString = testIndexString.substring(0, testIndexString.length() - 1);
//		testIndexString = testIndexString + " Item count = " + testItemIndexes.size();
//		
//		for(Integer index: trainItemIndexes){
//			trainIndexString = trainIndexString + index + ",";
//		}
//		
//		trainIndexString = trainIndexString.substring(0, trainIndexString.length() - 1);
//		trainIndexString = trainIndexString + " Item count = " + trainItemIndexes.size();
//		
//		System.out.println(testIndexString);
//		System.out.println(trainIndexString);
//	}
	
//	private List<Integer> getTestItemIndexes(int testItemCount){
//		int addedItemCounter = 0;
//		List<Integer> testItems = new ArrayList<Integer>();
//		
//		for(CrossoverItem item : getItemList()){
//			if(!item.isPickedForTesting()){
//				if(addedItemCounter >= testItemCount){
//					break;
//				}else{
//					setPickedForTesting(item);
//					testItems.add(getItemList().indexOf(item));
//					addedItemCounter++;
//				}
//			}
//		}
//		
//		return testItems;
//	}
	
	private List<Integer> getTestItemIndexesRandom(int testItemCount){
		List<Integer> testItems = new ArrayList<Integer>();
		Random rand = new Random();
		
		List<Integer> tempIndexList = new ArrayList<Integer>();
		
		for(CrossValidationItem item : getItemList()){
			if(!item.isPickedForTesting()){
				tempIndexList.add(getItemList().indexOf(item));
			}
		}
		
		for(int i = 0; i < testItemCount; i++){
			int randomNumber = rand.nextInt(tempIndexList.size());
			
			CrossValidationItem item = getItemList().get(tempIndexList.get(randomNumber));
			setPickedForTesting(item);
			testItems.add(tempIndexList.get(randomNumber));
			
			tempIndexList.remove(randomNumber);
		}
		
		return testItems;
	}
	
	private List<Integer> getTrainItemIndexes(int trainItemCount, List<Integer> testItemIndexes){
		int addedItemCounter = 0;
		
		List<Integer> trainItems = new ArrayList<Integer>();
		
		for(CrossValidationItem item : getItemList()){
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
	
//	private List<Integer> getTrainItemIndexesRandom(int trainItemCount, List<Integer> testItemIndexes){
//		List<Integer> trainItems = new ArrayList<Integer>();
//		Random rand = new Random();
//		
//		List<Integer> tempIndexList = new ArrayList<Integer>();
//		
//		for(CrossoverItem item : getItemList()){
//			if(!testItemIndexes.contains(getItemList().indexOf(item))){
//				tempIndexList.add(getItemList().indexOf(item));
//			}
//		}
//		
//		for(int i = 0; i < trainItemCount; i++){
//			int randomNumber = rand.nextInt(tempIndexList.size());
//			
//			trainItems.add(tempIndexList.get(randomNumber));
//			tempIndexList.remove(randomNumber);
//			
//		}
//		
//		return trainItems;
//	}

	public List<CrossValidationItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<CrossValidationItem> itemList) {
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

	private void setPickedForTesting(CrossValidationItem item){
		int index = getItemList().indexOf(item);
		
		if(index != -1){
			getItemList().get(index).setPickedForTesting(true);
		}
		
	}
	
	private boolean allPickedForTest(){
		for(CrossValidationItem item:getItemList()){
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
