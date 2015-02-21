import java.util.*;
import java.util.Map.Entry;

class hashObject{
	
	 private int count=0;
	 private double probability;
	
	public void setCount(int count){
		this.count=count;
	}
	public void setProbability(double probability){
		this.probability=probability;
	}
	public int getCount(){
		return count;
		
	}
	public double getProbability(){
		return probability;
	}
}

public class nlp{
		
	private final static String START_TRAINING = "no";
	
	public static void main(String[] args)
	{
		
		List<List<String>> testData= new ArrayList<List<String>>();	// Presumed test data
	
		HashMap<String,hashObject> uniGramMap = new HashMap<String,hashObject>();
		
		double perplexity=0;
		Integer totalWords=0;
		
		nlp Object = new nlp();

		if(START_TRAINING.equals("yes"))
		{
		    // get test Data from training.txt
	        System.out.println("Reading File from Java code");
	        String fileName="training.txt";
	        String fileNameUpspeak="fileUpspeak.txt";
	        String fileNameDownspeak="fileDownspeak.txt";

	        FileSplit fs= new FileSplit(fileName,fileNameUpspeak,fileNameDownspeak);
	        fs.Split();
	        testData = fs.getSentencesFromString("fileUpspeak.txt");
		}
		else
		{
			// sample testing data
			testData= Object.sampleData();
		}
		
		for(int i=0; i<testData.size(); i++)
		{
			totalWords = totalWords + testData.get(i).size();
		}
		
		System.out.println("totalWords: " + totalWords);
		
		// Train Unigram Model for test data
		uniGramMap = Object.calculateUnigram(testData, totalWords);
		
		// do laplace Smoothing
		HashMap<String,hashObject> emptyMap = new HashMap<String,hashObject>();
		HashMap<String,hashObject> uniGramSmoothedMap = Object.laplaceSmoothing(uniGramMap, emptyMap, totalWords, uniGramMap.size());
	
		// Compute perplexity
		perplexity = Object.ComputePerplexity(uniGramSmoothedMap, totalWords);
		System.out.println("perplexity: " + perplexity);
	 
	}

	// Brenda's sample data
	List<List<String>> sampleData() {
		List<String> l1=new ArrayList<String>();
		l1.add("hi");
		l1.add("How");
		l1.add("are");
		List<List<String>> test= new ArrayList<List<String>>();
		test.add(l1);
		
		l1=new ArrayList<String>();
		l1.add("hifi");
		l1.add("How");
		l1.add("is");
		test.add(l1);
		
		
		l1=new ArrayList<String>();
		l1.add("hifi");
		l1.add("How");
		l1.add("How1");
		test.add(l1);
		
		l1=new ArrayList<String>();
		l1.add("hifi");
		l1.add("How");
		l1.add("are");
		test.add(l1);
		
		return test;
		
	}
    
    HashMap<String,hashObject> calculateUnigram(List<List<String>> acceptVal, Integer nbWords){
		//hashObject h1=new hashObject();
		//System.out.println("acceptVal:" + acceptVal);
		HashMap<String,hashObject> uniHash = new HashMap<String, hashObject>();
	
		for(List<String> rowList:acceptVal){
			System.out.println("################rowList:" + rowList);	
			for(String columnList:rowList){
				if(uniHash.containsKey(columnList)){
					//System.out.println("****Word:*****" + columnList);
					//System.out.println("Before update unihash is" + uniHash);
				    hashObject h1=new hashObject();
					h1=uniHash.get(columnList);
					int uniCount=h1.getCount();
					//System.out.println("get uniCount:"+uniCount);
					//System.out.println("In if the count is " + uniCount+ ":" +columnList);
					h1.setCount(++uniCount);
					//System.out.println("Incremented Count:" + h1.getCount());
					uniHash.put(columnList, h1);
					//System.out.println("After update unihash is" + uniHash);
					
					
				}
				else{
				//hashObject h1=new hashObject();
				//System.out.println("*****Word:*****" + columnList);		
				hashObject h1=new hashObject();
				h1.setCount(1);
				//System.out.println("in else part");
				uniHash.put(columnList, h1);
				//System.out.println("unihash is" + uniHash);
				}
			}
		
		
		}
		
		double probability;

		for(Entry<String,hashObject> entry: uniHash.entrySet())
		{
			probability = (double)((entry.getValue().getCount())/ (double)nbWords );
			entry.getValue().setProbability(probability);	
		}
		
		return uniHash;

	}
    
    HashMap<String, hashObject> laplaceSmoothing (HashMap<String, hashObject> unsmoothedMap1, HashMap<String, hashObject> unsmoothedMap2, Integer totalWords, Integer totalWordTypes)
    {
    	System.out.println("Doing laplace smoothing");
    	HashMap<String, hashObject> smoothedMap = new HashMap<String, hashObject>();
    	if(unsmoothedMap2.isEmpty())	// Case of Unigram
    	{
    		System.out.println("Unigram Case");
	    	for(Entry<String,hashObject> entry: unsmoothedMap1.entrySet())
	    	{
	    		hashObject temp = new hashObject();
	    		int count = entry.getValue().getCount() + 1;
	    		temp.setCount(count);
	    		double probability = (double) count / (totalWords+totalWordTypes);
	    		temp.setProbability(probability);
	    		smoothedMap.put(entry.getKey(), temp);
	    	}
    	}
    	else
    	{
    		System.out.println("Higher than Unigram"); //Not tested since no bigrams/trigrams available.
    		for(Entry<String,hashObject> entry: unsmoothedMap1.entrySet())
	    	{
    			hashObject temp = new hashObject();
	    		int countNr = entry.getValue().getCount() + 1; // Increment the count of Numerator
	    		temp.setCount(countNr);
	    		String words = entry.getKey();
	    		//String refKey = words.split(" ",1).(0);
	    		String[] arraywords = words.split(" ");
	    		String subword ="";
	    		for(int i=0; i< (arraywords.length-1); i++)
	    		{
	    			subword = subword + " " + arraywords[i];
	    		}
	    		int countDr = unsmoothedMap2.get(subword).getCount() + totalWordTypes; // Increment the count of Denominator by Word Types
	    		double probability = (double)  countNr/ countDr;
	    		temp.setProbability(probability);
	    		smoothedMap.put(entry.getKey(), temp);
	    	}
    	}
    	
    	return smoothedMap;
    }
    
	double ComputePerplexity(HashMap<String,hashObject>inputMap, Integer totalWords)
	{
		double perplexity;
		double totalLogProbability=0;
		double totalProbability=0; // calculate log probabailities.
		
		for(Entry<String,hashObject> entry: inputMap.entrySet())
		{
			hashObject Value = entry.getValue();
			/*System.out.println("Entry : " + entry);
			System.out.println("Count : " + Value.getCount());
			System.out.println("Probability : " + Value.getProbability());*/
			totalLogProbability = totalLogProbability + Math.log(Value.getProbability()); 
			//multProbability = multProbability * Value.getProbability();
		}
		
		totalProbability = Math.exp(totalLogProbability);
		
		/*System.out.println("totalLogProbability : " + totalLogProbability);
		System.out.println("totalProbability : " + totalProbability);
		System.out.println("multProbability : " + multProbability);
		System.out.println("1/multProbability : " + 1/multProbability);
		*/
		
		double root = (double)1/totalWords; 
		perplexity = Math.pow((1/totalProbability),root);
		return perplexity;
	}
	
}
