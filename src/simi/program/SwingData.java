/*
 * Swing Data Coding Challenge
 * Simi Sasidharan
 */
package simi.program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		int ret = -1;
		if ((indexBegin <0 || indexBegin >= data.size()) || (indexEnd <0 || indexEnd >= data.size()) || 
				(indexEnd < indexBegin)){
			System.out.println("Invalid Index provided");
			return -1;
		}
		for (double dob : data.subList(indexBegin, indexEnd)) {
			//System.out.println(dob);
			
			if (dob > threshold) {
				count++;
				if (ret == -1) {
					ret = indexBegin + curIndex;
				}
			}
			if (count == winLength) {
				System.out.println("searchContinuityAboveValue return: " + ret);
				return ret;
			}
			curIndex++;
		}
			
		return -2;
		
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
		int ret = -1;
		if ((indexBegin <0 || indexBegin >= data.size()) || (indexEnd <0 || indexEnd >= data.size()) || 
				(indexEnd > indexBegin)){
			System.out.println("Invalid Index provided");
			return -1;
		}
		List<Double> temp = data.subList(indexEnd, indexBegin+1);
				
		Collections.reverse(temp);
		
		for (double dob : temp) {
			//System.out.println(dob);
			
			if (dob > thresholdLo && dob < thresholdHi) {
				count++;
				if (ret == -1) {
					ret = indexBegin - curIndex;
				}
			}
			if (count == winLength) {
				System.out.println("backSearchContinuityWithinRange return: " + ret);
				return ret;
			}
			curIndex++;
		}
			
		return -2;
		
		
	}

	/*
	 * searchContinuityAboveValueTwoSignals(data1, data2, indexBegin, indexEnd, threshold1, threshold2, winLength) - 
	 * from indexBegin to indexEnd, search data1 for values that are higher than threshold1 and also search data2 
	 * for values that are higher than threshold2. Return the first index where both data1 and data2 have values 
	 * that meet these criteria for at least winLength samples.
	 */
	public int searchContinuityAboveValueTwoSignals(List<Double> data1, List<Double> data2, int indexBegin, 
			int indexEnd, double threshold1, double threshold2, int winLength) {
		int count1 = 0;
		int count2 = 0;
		int curIndex = 0;
		int ret1 = -1;
		int ret2 = -1;
		//int ret = -1;
		if ((indexBegin <0 || indexBegin >= data1.size()) || (indexEnd <0 || indexEnd >= data1.size()) || 
				(indexEnd < indexBegin) ||
				(indexBegin <0 || indexBegin >= data2.size()) || (indexEnd <0 || indexEnd >= data2.size())){
			System.out.println("Invalid Index provided");
			return -1;
		}
		
		List<Double> data1t = data1.subList(indexBegin, indexEnd+1);
		List<Double> data2t = data2.subList(indexBegin, indexEnd+1);
		
		Iterator<Double> di1 = data1t.iterator();
		Iterator<Double> di2 = data2t.iterator();
		double dob1;
		double dob2;
		while (di1.hasNext() || di2.hasNext()) {
			if (di1.hasNext()) {
				dob1 = di1.next();
				if (dob1 > threshold1) {
					count1++;
					if (ret1 == -1 || ret2 != ret1) {
						ret1 = indexBegin + curIndex;
					}
				}
				if (count1 == winLength) {
					//System.out.println("ret1: " + ret1);
					//return ret;
				}

			}
			
			if (di2.hasNext()) {
				dob2 = di2.next();
				if (dob2 > threshold1) {
					count2++;
					if (ret2 == -1 || ret1 != ret2) {
						ret2 = indexBegin + curIndex;
					}
				}
				if (count2 == winLength) {
					//System.out.println("ret1: " + ret1);
					//return ret;
				}
				
			}
			

			if (count1 >= winLength && count2 >=winLength && ret1 == ret2) {
				System.out.println("searchContinuityAboveValueTwoSignals return " + ret1);
				return ret1;
			}
			curIndex++;
		}
			
		return -2;
	}

	/*
	 * searchMultiContinuityWithinRange(data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength) - from 
	 * indexBegin to indexEnd, search data for values that are higher than thresholdLo and lower than thresholdHi. 
	 * Return the the starting index and ending index of all continuous samples that meet this criteria for at least 
	 * winLength data points
	 */
	public int searchMultiContinuityWithinRange(List<Double> data, int indexBegin, int indexEnd, double thresholdLo, 
			double thresholdHi, int winLength) {
		int count = 0;
		int curIndex = 0;
		int ret = -1;
		boolean continuous = false;
		if ((indexBegin <0 || indexBegin >= data.size()) || (indexEnd <0 || indexEnd >= data.size()) || 
				(indexEnd < indexBegin) || (thresholdHi < thresholdLo)){
			System.out.println("Invalid Index or threshold provided");
			return -1;
		}
		for (double dob : data.subList(indexBegin, indexEnd+1)) {
			//System.out.println(dob);
			
			if (dob > thresholdLo && dob < thresholdHi) {
				count++;
				if (ret == -1 || continuous == false) {
					ret = indexBegin + curIndex;
					continuous = true;
				}
			} else {
				count = 0;
				continuous = false;
			}
			if (count == winLength && continuous == true) {
				System.out.println("searchMultiContinuityWithinRange return: " + ret);
				return ret;
			}
			curIndex++;
		}
			
		return -2;
		
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
