/*
 * Swing Data Coding Challenge
 * Simi Sasidharan
 */
package simi.program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SwingData {

	public static final String FILE_PATH = "C:\\Users\\simi\\Downloads\\latestSwing.csv";

	/*
	 * searchContinuityAboveValue(data, indexBegin, indexEnd, threshold, winLength) - from indexBegin to indexEnd, 
	 * search data for values that are higher than threshold. Return the first index where data has values that meet 
	 * this criteria for at least winLength samples.
	 */
	public int searchContinuityAboveValue(List<Double> data, int indexBegin, int indexEnd, double threshold, 
			int winLength) {
		int count = 0;
		int curIndex = 0;

		checkIndexLimits(indexBegin, indexEnd, data.size());

		for (double dob : data.subList(indexBegin, indexEnd+1)) {

			count = checkContinuity(1, dob, threshold, 0.0, 0.0, count);
			if (count == winLength) {
				System.out.println("searchContinuityAboveValue return " + (indexBegin + curIndex - winLength +1));
				return (indexBegin + curIndex - winLength +1);
			}
			curIndex++;
		}
			
		return -1;
		
	}

	/*
	 * backSearchContinuityWithinRange(data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength) - from 
	 * indexBegin to indexEnd (where indexBegin is larger than indexEnd), search data for values that are higher than 
	 * thresholdLo and lower than thresholdHi. Return the first index where data has values that meet this criteria 
	 * for at least winLength samples
	 */
	public int backSearchContinuityWithinRange(List<Double> data, int indexBegin, int indexEnd, double thresholdLo, 
			double thresholdHi, int winLength) {
		
		int count = 0;
		int curIndex = 0;

		checkBackIndexLimits(indexBegin, indexEnd, data.size());
		checkThreholdLimits(thresholdLo, thresholdHi);

		List<Double> temp = data.subList(indexEnd, indexBegin+1);
				
		Collections.reverse(temp);
		
		for (double dob : temp) {
			count = checkContinuity(2, dob, thresholdLo, 0.0, thresholdHi, count);
			if (count == winLength) {
				System.out.println("backSearchContinuityWithinRange return " + (indexBegin - curIndex + winLength -1));
				return (indexBegin - curIndex + winLength -1);
			}
			curIndex++;
		}

		return -1;

	}

	/*
	 * searchContinuityAboveValueTwoSignals(data1, data2, indexBegin, indexEnd, threshold1, threshold2, winLength) - 
	 * from indexBegin to indexEnd, search data1 for values that are higher than threshold1 and also search data2 
	 * for values that are higher than threshold2. Return the first index where both data1 and data2 have values 
	 * that meet these criteria for at least winLength samples.
	 */
	public int searchContinuityAboveValueTwoSignals(List<Double> data1, List<Double> data2, int indexBegin, 
													int indexEnd, double threshold1, double threshold2, int winLength) {
		int count = 0;
		int curIndex = 0;

		checkIndexLimits(indexBegin, indexEnd, data1.size());
		checkIndexLimits(indexBegin, indexEnd, data2.size());

		List<Double> data1t = data1.subList(indexBegin, indexEnd+1);
		List<Double> data2t = data2.subList(indexBegin, indexEnd+1);
		
		Iterator<Double> di1 = data1t.iterator();
		Iterator<Double> di2 = data2t.iterator();

		while (di1.hasNext() && di2.hasNext()) {

			double dob1;
			double dob2;
			dob1 = di1.next();
			dob2 = di2.next();
			count = checkContinuity(3, dob1, threshold1, dob2, threshold2, count);
			if (count == winLength) {
				System.out.println("searchContinuityAboveValueTwoSignals return " + (indexBegin + curIndex - winLength +1));
				return indexBegin + curIndex - winLength;
			}
			curIndex++;
		}
			
		return -1;
	}

	/*
	 * searchMultiContinuityWithinRange(data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength) - from 
	 * indexBegin to indexEnd, search data for values that are higher than thresholdLo and lower than thresholdHi. 
	 * Return the the starting index and ending index of all continuous samples that meet this criteria for at least 
	 * winLength data points
	 */
	public ArrayList<Integer> searchMultiContinuityWithinRange(List<Double> data, int indexBegin, int indexEnd, double thresholdLo, 
			double thresholdHi, int winLength) {
		int count = 0;
		int curIndex = 0;

		checkIndexLimits(indexBegin, indexEnd, data.size());
		checkThreholdLimits(thresholdLo, thresholdHi);

		for (double dob : data.subList(indexBegin, indexEnd+1)) {
			//System.out.println(dob);
			count = checkContinuity(4, dob, thresholdLo, 0.0, thresholdHi, count);
			if (count == winLength) {
				ArrayList<Integer> r = new ArrayList<Integer>();
				r.add(indexBegin + curIndex - winLength +1);
				r.add(indexBegin + curIndex);
				System.out.println("searchMultiContinuityWithinRange return: " + r);
				return r;
			}

			curIndex++;
		}
		return null;
		
	}

	public void checkIndexLimits(int indexBegin, int indexEnd, int size) {
		if ((indexBegin <0 || indexBegin >= size) || (indexEnd <0 || indexEnd >= size) || (indexEnd < indexBegin)){
			throw new IllegalArgumentException("Invalid Index provided");
		}
	}

	public void checkBackIndexLimits(int indexBegin, int indexEnd, int size) {
		if ((indexBegin <0 || indexBegin >= size) || (indexEnd <0 || indexEnd >= size) || (indexEnd > indexBegin)){
			throw new IllegalArgumentException("Invalid Index provided");
		}
	}

	public void checkThreholdLimits(double thresholdLo, double thresholdHi) {
		if (thresholdLo > thresholdHi){
			throw new IllegalArgumentException("Invalid thresholds provided");
		}
	}

	/*type is the API number*/
	public int checkContinuity(int type, double dob1, double threshold1, double dob2, double threshold2, int count) {
		if ((type == 4 && dob1 > threshold1 && dob1 < threshold2) ||
			(type == 3 && dob1 > threshold1 && dob2 > threshold2) ||
			(type == 2 && dob1 > threshold1 && dob1 < threshold2) ||
			(type == 1 && dob1 > threshold1)) {
			return ++count;
		}
		return 0; //reset count
	}

	public static void main(String[] args) {
		SwingData sd;
		// 6 column lists
		List<Double> records1 = new LinkedList<>();
		List<Double> records2 = new LinkedList<>();
		List<Double> records3 = new LinkedList<>();
		List<Double> records4 = new LinkedList<>();
		List<Double> records5 = new LinkedList<>();
		List<Double> records6 = new LinkedList<>();


		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
		        
		        /*Add each records into 6 column lists*/
		        records1.add(row[1]);
		        records2.add(row[2]);
		        records3.add(row[3]);
		        records4.add(row[4]);
		        records5.add(row[5]);
		        records6.add(row[6]);
		    }

		    sd  = new SwingData();
		    //Test Code is given below
		    sd.searchContinuityAboveValue(records1, 500, 1000, 0.876953, 10 );
		    sd.backSearchContinuityWithinRange(records2, 1200, 700, 0, 0.2, 25);
		    sd.searchContinuityAboveValueTwoSignals(records1, records2, 33, 400, 0.85, 0.90, 10);
		    sd.searchMultiContinuityWithinRange(records3, 10, 1000, 0.1, 0.2, 32);

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
