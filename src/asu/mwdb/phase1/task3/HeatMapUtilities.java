package asu.mwdb.phase1.task3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import asu.mwdb.phase1.task1.Constants;

public class HeatMapUtilities {

	/**
	 * 
	 * @param inpFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static double[][] createInputDataForHeatMap(String inpFile) throws IOException, FileNotFoundException  {
		BufferedReader buffReader1 = new BufferedReader(new FileReader(inpFile));
		String line = "";
		String splitBy = ",";
		String entry[];
		int fileSize = 0;
		while((buffReader1.readLine() != null))
			fileSize++;
		System.out.println("fileSize:  " + fileSize);
		if(fileSize <= 1){
			System.out.println("Input data file is empty!!!\nExiting...");
			System.exit(0);
		}
		double result[][] = new double[51][fileSize-1];
		buffReader1.close();
		BufferedReader buffReader = new BufferedReader(new FileReader(inpFile));
		int j = 0;
		line = buffReader.readLine();
		while((line = buffReader.readLine()) != null){
			entry = line.split(splitBy);
			int state_index = 0;
			for(int i = 0; i < Constants.NUM_STATES; i++){
				result[state_index++][j] = Double.parseDouble(entry[i+2]);
			}
			j++;
		}
		buffReader.close();
		return result;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	public static double getStrength(String[] word) {
		double result = 0.0;
		for(int i = 0; i< word.length; i++){
			result = result + (Double.parseDouble(word[i]))*(Double.parseDouble(word[i]));
		}
		return Math.sqrt(result);
	}

	/**
	 * 
	 * @param strngth
	 * @param graphics
	 * @param hMapHght
	 * @param hMapWdth
	 * @param stateNum
	 * @param highest
	 * @param neighBoursOfHighest
	 */
	public static void drawWindow(Strength strngth, Graphics2D graphics,
			int hMapHght, int hMapWdth, int stateNum, boolean highest, List<Integer> neighBoursOfHighest) {
		int iteration = highest ? strngth.getIterationHighest() : strngth.getIterationLowest();
		if(!highest)
			graphics.setColor(Color.GREEN);
		else
			graphics.setColor(Color.BLUE);
        int x = hMapWdth * (iteration + 2) + hMapWdth/2;
        int y = hMapHght * stateNum;
        graphics.drawRect(x, y, hMapWdth * strngth.getWindLength(), hMapHght);
        graphics.drawString(Constants.stateMap.get(stateNum), x + (hMapWdth*strngth.getWindLength()) + 10,  y + hMapHght);
        graphics.fillOval(x + ((strngth.getWindLength()/2)*hMapWdth), y + 3, hMapWdth-5, hMapHght-5);
        
        for(int i = 0; i < neighBoursOfHighest.size(); i++){
        	//exclude the the highest state
        	if(neighBoursOfHighest.get(i) != stateNum){
        		y = hMapHght * neighBoursOfHighest.get(i);
        		graphics.drawRect(x, y, hMapWdth * strngth.getWindLength(), hMapHght);
        		graphics.drawString(Constants.stateMap.get(neighBoursOfHighest.get(i)), x + (hMapWdth*strngth.getWindLength()) + 10,  y + hMapHght);
        	}
        }
	}
}
