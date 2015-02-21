import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.process.DocumentPreprocessor;

class FileSplit{
	
	private String fileName;
	private String fileNameUpspeak;
	private String fileNameDownspeak;
	private final static String SENTENCE_START = "<s>";
	private final static String SENTENCE_END = "</s>";
	
	
	public FileSplit(String f1,String f2,String f3)
	{
		fileName=f1;
		fileNameUpspeak=f2;
		fileNameDownspeak=f3;
	}
	
	public void Split()
	{
		 try{
	          System.out.println("fileName " + fileName);
	          FileReader inputFile = new FileReader(fileName);   
	          BufferedReader bufferReader = new BufferedReader(inputFile);
        
	          String line;
	          
	          File fileUp = new File(fileNameUpspeak);
	          File fileDown = new File(fileNameDownspeak);
	          
	          FileWriter fwUp = new FileWriter(fileUp);
     		  BufferedWriter bw = new BufferedWriter(fwUp);
     		  
     		  FileWriter fwDown = new FileWriter(fileDown);
     		  

	          while ((line = bufferReader.readLine()) != null) 
	         {
	          try {
	        			        	
	            	if (line.contains("UPSPEAK")== true){
	            		if (!fileUp.exists()) {
		      				fileUp.createNewFile();
		      				
		      			}
	            		fwUp = new FileWriter(fileUp,true);
	            		bw = new BufferedWriter(fwUp);
	            	
	            		continue;
	            	}
	            	else if (line.contains("DOWNSPEAK")== true){
	            		if (!fileDown.exists()) {
		      				fileUp.createNewFile();
		      				
		      			}
	            	    fwDown = new FileWriter(fileDown,true);
	            		bw = new BufferedWriter(fwDown);
	            		continue;
	            	}
	            	else if(line.contains("**START**")== true){
	               		line = line.replace("**START**", "");
	            	}
	            	else if (line.contains("**EOM**")== true){
	            		line = line.replace("**EOM**", " ");
	            	}
	            	else if (line.contains("--")==true){
	            		line=line.replace("-","");
	            	}
	            	else if (line.contains("..")==true){
	            		line=line.replace(".","");
	            	}
	        
	          /*  	System.out.println("DONE");*/
	      			bw.write(line);
	      			bw.flush();
	      		} 
	           catch (IOException e) {
	      			e.printStackTrace();
	      		}	        		      	  
//	            System.out.println(line);
	          }
	          bufferReader.close();
	          bw.close();
	       } catch(Exception e){
	          System.out.println("Error while reading file line by line:" + e.getMessage());                      
       }	       
	}

	/**
	 * 
	 * @param filePath
	 * @return List of Lists
	 * 			sublists represent sentences as tokens including <s> and </s> 
	 * 	Ex: "How are you. I am fine"
	 * 	Return: A List with two elements. Each element itself being a list
	 * 			with words as its elements
	 */
	public List<List<String>> getSentencesFromString(String filePath) {
		// TODO Auto-generated method stub
		List<List<String>> returnVal = new ArrayList<List<String>>(); 
		 DocumentPreprocessor dp = new DocumentPreprocessor(filePath);
	      for (List sentence : dp) {

	    	  sentence.add(0, SENTENCE_START);
	    	  sentence.remove(sentence.size() - 1);
	    	  sentence.add(SENTENCE_END);
	    	  //System.out.println(sentence);
	    	  returnVal.add(sentence);
	      }
		
		return returnVal;
	}
	
	/*public static void main(String[] args) {	

		       System.out.println("Reading File from Java code");
		       String fileName="training.txt";
		       String fileNameUpspeak="fileUpspeak.txt";
		       String fileNameDownspeak="fileDownspeak.txt";
	
		       FileSplit fs= new FileSplit(fileName,fileNameUpspeak,fileNameDownspeak);
		       fs.Split(); 
				
		       List<List<String>> returnToken=getSentencesFromString("fileUpspeak.txt");
		       System.out.println("returnToken" + returnToken);
	
	}*/
	
}

