/**
 * 
 */
package asu.mwdb.phase1.task2;


/**
 * @author Chandrashekhar
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import asu.mwdb.phase1.task1.Constants;

public class CreateAvgAndDiffFiles {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException {
		
		boolean wrongArg = false;
		if(args.length != 3)
			wrongArg = true;
		
		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: CreateEpidemicWordDic <alpha> <Adjacency Graph path> <path of the epidemic simulation word dictionary files i.e path of the epidemic_word_file folder obtained in task1.>");
			System.exit(0);
		}

		CreateAvgAndDiffFiles dicObj = new CreateAvgAndDiffFiles();
		dicObj.createAvgDiffDic(args);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void createAvgDiffDic(String[] args) throws IOException,
			FileNotFoundException {
		List<String> allFileNames = new ArrayList<String>();
		allFileNames = AvgUtilities.getAllDataFilesInDir(args[2]+"\\");
		String filePath = args[2] + "\\";
		
		File avgOutputDir = new File(Constants.EPIDIEMIC_DICTIONARY_AVG);
		if(!avgOutputDir.exists())
			avgOutputDir.mkdir();
		
		File diffOutputDir = new File(Constants.EPIDIEMIC_DICTIONARY_DIFF);
		if(!diffOutputDir.exists())
			diffOutputDir.mkdir();
		
		String avgOutputFilePath = avgOutputDir + "\\";
		String diffOutputFilePath = diffOutputDir + "\\";
		double alpha = Double.parseDouble(args[0]);
		File adjGraph = new File(args[1]);
		String neighborGraph = adjGraph.getAbsolutePath();
		Map<String, LinkedList<String>> adjecencyList = new LinkedHashMap<String, LinkedList<String>>();
		System.out.println("Executing...");
		long startTime = System.currentTimeMillis();
		AvgUtilities.createGraph(adjecencyList, neighborGraph);
		for(int k=0; k < allFileNames.size(); k++){
			String inpFile = filePath + allFileNames.get(k);
			String avgOutFile = avgOutputFilePath + allFileNames.get(k);
			String diffOutFile = diffOutputFilePath + allFileNames.get(k);
			BufferedReader buffRdr = new BufferedReader(new FileReader(inpFile));
			BufferedWriter buffWrtrAvg = new BufferedWriter(new FileWriter(avgOutFile));
			BufferedWriter buffWrtrDiff = new BufferedWriter(new FileWriter(diffOutFile));
			String line = "";

			while((line = buffRdr.readLine()) != null){
				List<Buffer> buff = new ArrayList<Buffer>();
				for(int i = 0; i < Constants.NUM_STATES; i ++){
					Buffer lineBuffer = new Buffer(line);
					buff.add(lineBuffer);
					if(i == Constants.NUM_STATES-1)
						break;
					if((line = buffRdr.readLine()) == null) break;
				}
				String avgAndDiffWin[];
				for(int i = 0; i < buff.size(); i++){
					avgAndDiffWin = AvgUtilities.getAverageAndDiff(buff.get(i), buff, adjecencyList, alpha);
					buffWrtrAvg.write(avgAndDiffWin[0]);
					buffWrtrDiff.write(avgAndDiffWin[1]);
				}

				// the buff is created for a time stamp
			}

			buffRdr.close();
			buffWrtrAvg.close();
			buffWrtrDiff.close();
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("\nTime required for Task 2 is : "+ (stopTime - startTime) + " mSec");
	}

}
