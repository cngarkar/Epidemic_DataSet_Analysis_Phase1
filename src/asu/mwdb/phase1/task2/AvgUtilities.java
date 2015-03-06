/**
 * 
 */
package asu.mwdb.phase1.task2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import asu.mwdb.phase1.task1.Constants;
import asu.mwdb.phase1.task1.Index;
import asu.mwdb.phase1.task1.StringOperations;

/**
 * @author Chandrashekhar
 *
 */
public class AvgUtilities {
	public static List<String> getAllDataFilesInDir(String dir){
		File directory = new File(dir);
		File[] allFiles = directory.listFiles();
		List<String> fileList = new ArrayList<String>();
		    for (int i = 0; i < allFiles.length; i++) {
		      if (allFiles[i].isFile()) {
		        fileList.add(allFiles[i].getName());
		      }
		    }
		return fileList;
	}
	
	public static List<String> getBuffer(String filePath){
		
		return null;
	}
	/**
	 * 
	 * @param adjacencyList
	 * @param graphFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void createGraph(Map<String, LinkedList<String>> adjacencyList, String graphFile) throws IOException, FileNotFoundException{
		BufferedReader buffRdr = new BufferedReader(new FileReader(graphFile));
		String line = "";
		String splitBy = ",";
		String entry[];
		int index = 2;
		//skip the header file
		line = buffRdr.readLine();
		while((line = buffRdr.readLine()) != null){
			entry = line.split(splitBy);
			LinkedList<String> neighbours = new LinkedList<String>();
			addNeighbours(neighbours, entry);
			adjacencyList.put(Constants.stateMap.get(index++), neighbours);
		}
		buffRdr.close();
	}

	/**
	 * 
	 * @param neighbours
	 * @param entry
	 */
	private static void addNeighbours(List<String> neighbours, String[] entry) {
		for(int i = 1; i < entry.length; i++){
			int isNeigbour = 0;
			isNeigbour = Integer.parseInt(entry[i]);
			if(isNeigbour == 1)
				neighbours.add(Constants.stateMap.get(i+1));
		}
	}

	/**
	 * 
	 * @param buffer
	 * @param buff
	 * @param adjecencyList
	 * @param alpha
	 * @return
	 */
	public static String[] getAverageAndDiff(Buffer buffer, List<Buffer> buff, Map<String, LinkedList<String>> adjecencyList, double alpha) {
		String splitBy = ";";
		Index idxObj = new Index();
		String wind = buffer.getWindow();
		String word[] = wind.split(splitBy);
		int wordLength = word.length;
		double[] winDiff = new double[wordLength];
		double[] winAvg = new double[wordLength];
		double[] avg = calculateAvgOfNeighbours(buffer, buff, adjecencyList, splitBy,
				wordLength);
		
		for(int i = 0; i< winAvg.length; i++){
			winAvg[i] = (alpha * Double.parseDouble(word[i])) + (avg[i] * (1 - alpha));
			winDiff[i] = (Double.parseDouble(word[i]) - avg[i]) / ((Double.parseDouble(word[i]) == 0)?Constants.EPSILON:Double.parseDouble(word[i]));
		}
		String resultAvg = StringOperations.createCSVForAvgAndDiff(buffer, idxObj, winAvg);
		String resultDiff = StringOperations.createCSVForAvgAndDiff(buffer, idxObj, winDiff);
		String result[] = new String[2];
		result[0] = resultAvg;
		result[1] = resultDiff;
		return result;
	}



	/**
	 * @param buffer
	 * @param buff
	 * @param adjecencyList
	 * @param splitBy
	 * @param wordLength
	 * @return
	 */
	public static double[] calculateAvgOfNeighbours(Buffer buffer, List<Buffer> buff,
			Map<String, LinkedList<String>> adjecencyList, String splitBy,
			int wordLength) {
		String currState;
		List<String[]> allWords = new ArrayList<String[]>();
		currState = buffer.getIndex().getStateName();
		int numNeighbours = (adjecencyList.get(currState).size() == 0) ? 1 : adjecencyList.get(currState).size();
		double[] avg = new double[wordLength];
		for(int i = 0; i < buff.size(); i++){
			if((buff.get(i).getIndex().getStateName() != currState) && (adjecencyList.get(currState).contains(buff.get(i).getIndex().getStateName()))){
				String[] valuesInWord = buff.get(i).getWindow().split(splitBy);
				allWords.add(valuesInWord);
			}
		}
		for(int i = 0; i < wordLength; i++){
			for(int j = 0; j < allWords.size(); j++){
				avg[i] = avg[i] + Double.parseDouble(allWords.get(j)[i]);
			}
		}
		for(int i = 0; i < avg.length; i++){
			avg[i] = avg[i]/ numNeighbours;
		}
		return avg;
	}
}
