package com.quoori.levenshtein;

import java.util.Arrays;

/**
 * Main class.
 * 
 * @author Pablo Fernando Araoz Alves
 */
public class Main {

	/** Default Test cases **/
	private static final String[] TEST_CASES = { "Haus|Maus|2", "Haus|Mausi|2", "Haus|Häuser|2", "Kartoffelsalat|Runkelrüben|2" };

	public static void main(String[] args) {
		new Main().start(args);
	}
	
	/**
	 * Starts the test cases 
	 * @param args
	 */
	public void start(String[] args) {
		
		// Test cases from command line
		if (args != null && args.length > 0) {
			Arrays.stream(args).forEach(testCase -> {
				String[] tokens = testCase.split("\\|");
				calculateAndPrint(tokens[0],tokens[1], tokens.length == 2 ? Integer.valueOf(tokens[2]) : -1);
			});
		} 
		// Default test cases as described in document
		else {
			for (int i = 0; i < TEST_CASES.length; i++) {
				String[] tokens = TEST_CASES[i].split("\\|");
				calculateAndPrint(tokens[0],tokens[1], -1);
			}
			
			for (int i = 0; i < TEST_CASES.length; i++) {
				String[] tokens = TEST_CASES[i].split("\\|");
				calculateAndPrint(tokens[0],tokens[1], Integer.valueOf(tokens[2]));
			}
		}
	}
	
	/**
	 * Method that calls the levenshtein calculation method and prints the results.
	 * 
	 * @param token1
	 * @param token2
	 * @param maxDistance
	 */
	private void calculateAndPrint(String token1, String token2, int maxDistance) {
		long startTime = 0l;
		long endTime = 0l;
		int distance = 0;
		if (maxDistance > 0) {
			startTime = System.nanoTime();
			distance = LevenshteinAlgorithm.levenshtein(token1, token2, maxDistance);
			endTime = System.nanoTime();
			
			System.out.println(String.format("%s, %s, %d => %d.", token1, token2, maxDistance, distance));
		}
		else {
			startTime = System.nanoTime();
			distance = LevenshteinAlgorithm.levenshtein(token1, token2);
			endTime = System.nanoTime();
			
			System.out.println(String.format("%s, %s => %d.", token1, token2, distance));
		}
		
		this.profile(endTime - startTime);
	}
	
	/**
	 * Prints memory usage and duration of the calculation.
	 * 
	 * @param duration
	 */
	private void profile(long duration) {
		Runtime rt = Runtime.getRuntime();
		long usedMemory = rt.totalMemory() - rt.freeMemory();
		
		StringBuilder sb = new StringBuilder("\n* Performance Stats \n");
		sb.append("====> Used Memory: ").append(usedMemory).append(" KBytes \n");
		sb.append("====> Processing time: ").append(duration).append("ns \n");
		sb.append("------------------------------- END ------------------------------- \n");
		
		System.out.println(sb.toString());
		
		rt.gc();
	}
}
