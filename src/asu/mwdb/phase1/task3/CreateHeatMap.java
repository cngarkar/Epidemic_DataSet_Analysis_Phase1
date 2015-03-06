package asu.mwdb.phase1.task3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import asu.mwdb.phase1.task1.Constants;
import asu.mwdb.phase1.task1.Index;
import asu.mwdb.phase1.task2.AvgUtilities;
import org.tc33.jheatchart.HeatChart;
/**
 * 
 * @author Chandrashekhar
 *
 */
public class CreateHeatMap {
	public static void main(String args[])throws FileNotFoundException, IOException{
		boolean wrongArg = false;
		if(args.length != 4)
			wrongArg = true;

		if(wrongArg){
			System.err.println("Please enter correct command line arguments:");
			System.err.println("Usage: CreateHeatMap <Simulation File Path> <choice of result file path(word dic, avg or diff path)> <adjacency graph file path> <the output directory for heatMap>");
			System.exit(0);
		}
		CreateHeatMap hmObj = new CreateHeatMap();
		hmObj.drawHeatMap(args);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void drawHeatMap(String[] args) throws IOException,
			FileNotFoundException {
		File inputFile = new File(args[0]);
		String simFilePath = inputFile.getAbsolutePath();
		File adjGraph = new File(args[2]);
		String adjecencyGraph = adjGraph.getAbsolutePath();
		
		String resultDirectory = args[1];
		File heatMapDir = new File(args[3]);
		if(!heatMapDir.exists())
			heatMapDir.mkdir();
		String heatMapOutPath = heatMapDir.getAbsolutePath()+"\\";

		System.out.println("Executing...");
		String inpSimFile = inputFile.getName().trim();
		long startTime = System.currentTimeMillis();
		double[][] heatMapData= HeatMapUtilities.createInputDataForHeatMap(simFilePath);
		HeatChart heatMap = new HeatChart(heatMapData);
		Strength strngth = new Strength(resultDirectory+"\\"+inpSimFile);
		
		System.out.println((new Index()).toString(strngth.getIndexHighest()) + strngth.getWindowHighest());
		System.out.println((new Index()).toString(strngth.getIndexLowest()) + strngth.getWindowLowest());

		heatMap.setTitle("Epidemic DataSet HeatMap for "+resultDirectory+inpSimFile);
		heatMap.setXAxisLabel("Iterations");
		heatMap.setYAxisLabel("States");
		heatMap.setLowValueColour(Color.YELLOW);
		heatMap.setHighValueColour(Color.RED);
		heatMap.setYValues(Constants.states);
		heatMap.saveToFile(new File(heatMapOutPath+inpSimFile+"_heat-chart.png"));
		
		File inputHeatMap = new File(heatMapOutPath+inpSimFile+"_heat-chart.png");
        BufferedImage finalHeatMap = ImageIO.read(inputHeatMap);
        Graphics2D graphics = finalHeatMap.createGraphics();
        Dimension heatMapDim = heatMap.getCellSize();
        int hMapCellHght = (int)heatMapDim.getHeight();
        int hMapCellWdth = (int)heatMapDim.getWidth();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        // draw max window
        Map<String, LinkedList<String>> adjecencyList = new LinkedHashMap<String, LinkedList<String>>();
		AvgUtilities.createGraph(adjecencyList, adjecencyGraph);
        int stateNumLowest = 1;
        int stateNumHighest = 1;
        List<Integer> neighBoursOfHighest = new ArrayList<Integer>();
        List<Integer> neighBoursOfLowest = new ArrayList<Integer>();
        for(Entry<Integer, String> entry : Constants.stateMap.entrySet()){
        	if(entry.getValue().equals(strngth.getIndexHighest().getStateName()))
        		stateNumHighest = entry.getKey();
        	if(entry.getValue().equals(strngth.getIndexLowest().getStateName()))
        		stateNumLowest = entry.getKey();
        	if(adjecencyList.get(strngth.getIndexHighest().getStateName()).contains(entry.getValue()))
        		neighBoursOfHighest.add(entry.getKey());
        	if(adjecencyList.get(strngth.getIndexLowest().getStateName()).contains(entry.getValue()))
        		neighBoursOfLowest.add(entry.getKey());
        }

		//draw highest window
		boolean highest = true;
		HeatMapUtilities.drawWindow(strngth, graphics, hMapCellHght, hMapCellWdth, stateNumHighest, highest, neighBoursOfHighest);

		// draw lowest window
		HeatMapUtilities.drawWindow(strngth, graphics, hMapCellHght, hMapCellWdth, stateNumLowest, !highest, neighBoursOfLowest);
   
        graphics.dispose();
        ImageIO.write(finalHeatMap, "png", new File(heatMapOutPath+inpSimFile+"_heatmap.png"));
        System.out.println("Created HeatMap");
        long stopTime = System.currentTimeMillis();
		System.out.println("\nTime required for Task 1 is : "+ (stopTime - startTime) + " mSec");
	}

}
