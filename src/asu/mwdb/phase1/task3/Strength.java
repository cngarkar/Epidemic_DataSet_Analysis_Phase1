package asu.mwdb.phase1.task3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import asu.mwdb.phase1.task1.Index;
import asu.mwdb.phase1.task1.StringOperations;
/**
 * 
 * @author Chandrashekhar
 *
 */
public class Strength {
	private double strengthLowest;
	private double strengthHighest;
	private String windowLowest;
	private String windowHighest;
	private Index indexLowest;
	private Index indexHighest;
	private int iterationLowest;
	private int iterationHighest;
	private int windLength;
	/**
	 * @return the iterationLowest
	 */
	public int getIterationLowest() {
		return iterationLowest;
	}
	/**
	 * @param iterationLowest the iterationLowest to set
	 */
	public void setIterationLowest(int iterationLowest) {
		this.iterationLowest = iterationLowest;
	}
	/**
	 * @return the iterationHighest
	 */
	public int getIterationHighest() {
		return iterationHighest;
	}
	/**
	 * @param iterationHighest the iterationHighest to set
	 */
	public void setIterationHighest(int iterationHighest) {
		this.iterationHighest = iterationHighest;
	}
	/**
	 * @return the strengthLowest
	 */
	public double getStrengthLowest() {
		return strengthLowest;
	}
	/**
	 * @param strengthLowest the strengthLowest to set
	 */
	public void setStrengthLowest(double strengthLowest) {
		this.strengthLowest = strengthLowest;
	}
	/**
	 * @return the strengthHighest
	 */
	public double getStrengthHighest() {
		return strengthHighest;
	}
	/**
	 * @param strengthHighest the strengthHighest to set
	 */
	public void setStrengthHighest(double strengthHighest) {
		this.strengthHighest = strengthHighest;
	}
	/**
	 * @return the windowLowest
	 */
	public String getWindowLowest() {
		return windowLowest;
	}
	/**
	 * @param windowLowest the windowLowest to set
	 */
	public void setWindowLowest(String windowLowest) {
		this.windowLowest = windowLowest;
	}
	/**
	 * @return the windowHighest
	 */
	public String getWindowHighest() {
		return windowHighest;
	}
	/**
	 * @param windowHighest the windowHighest to set
	 */
	public void setWindowHighest(String windowHighest) {
		this.windowHighest = windowHighest;
	}
	/**
	 * @return the indexLowest
	 */
	public Index getIndexLowest() {
		return indexLowest;
	}
	/**
	 * @param indexLowest the indexLowest to set
	 */
	public void setIndexLowest(Index indexLowest) {
		this.indexLowest = indexLowest;
	}
	/**
	 * @return the indexHighest
	 */
	public Index getIndexHighest() {
		return indexHighest;
	}
	/**
	 * @param indexHighest the indexHighest to set
	 */
	public void setIndexHighest(Index indexHighest) {
		this.indexHighest = indexHighest;
	}
	
	/**
	 * @return the windLength
	 */
	public int getWindLength() {
		return windLength;
	}
	/**
	 * @param windLength the windLength to set
	 */
	public void setWindLength(int windLength) {
		this.windLength = windLength;
	}

	public Strength(String fileName) throws IOException{
		 BufferedReader buffRdr = new BufferedReader(new FileReader(fileName));
		 String splitByComma = ",";
		 String splitBySemiColon = ";";
		 String line = "";
		 String entry[], index[], word[];
		 double minStrength = Double.MAX_VALUE, local;
		 double maxStrength = 0.00000000000000001;
		while ((line = buffRdr.readLine()) != null) {
			if(line != null){
				entry = line.split(splitByComma);
				index = StringOperations.trimTriangularBrackets(entry[0]).split(splitBySemiColon);
				word = StringOperations.trimTriangularBrackets(entry[1]).split(splitBySemiColon);
				local = HeatMapUtilities.getStrength(word);
				if(local > maxStrength){
					maxStrength = local;
					this.setStrengthHighest(maxStrength);
					this.setIndexHighest(new Index(index[0],index[1],index[2]));
					this.setWindowHighest(entry[1]);
					this.setIterationHighest(StringOperations.getIterationNumber(index[2]));
				}
				if(local < minStrength){
					minStrength = local;
					this.setStrengthLowest(minStrength);
					this.setIndexLowest(new Index(index[0], index[1], index[2]));
					this.setWindowLowest(entry[1]);
					this.setIterationLowest(StringOperations.getIterationNumber(index[2]));
				}
				this.setWindLength(word.length);
			}
		}
		if(this.getIndexHighest() == null){
			this.setStrengthHighest(this.getStrengthLowest());;
			this.setIndexHighest(this.getIndexLowest());
			this.setWindowHighest(this.getWindowLowest());
			this.setIterationHighest(this.getIterationLowest());
		}
		buffRdr.close();
	}
}
