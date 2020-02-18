package com.quoori.levenshtein;

/**
 * 
 * @author Pablo Alves
 *
 */
public class LevenshteinAlgorithm {

	/**
	 * Levenshtein algorithm that uses an array as a buffer for calculation.
	 * 
	 * @param token1
	 * @param token2
	 * @return int- levenshtein distance for the tokens
	 */
	public static int levenshtein(String token1, String token2) {

		// Validate tokens
		validateTokens(token1, token2);

		// Order the tokens storing them in one array with longest at [0] and shortest
		// at[1]
		String[] orderedTokens = orderTokens(token1, token2);

		// Creates the buffer
		int[] buffer = createBuffer(orderedTokens[1].length());

		// Loops longest token
		for (int i = 1; i <= orderedTokens[0].length(); i++) {
			// First position in buffer is always as the row start index
			buffer[0] = i;

			// The substitution value starts as the last row start index
			int substitutionValue = i - 1;

			// Loops the shortest token
			for (int j = 1; j <= orderedTokens[1].length(); j++) {
				// Backups the current buffer value
				int temp = buffer[j];

				// Calculates minumum value for the operations
				buffer[j] = min(buffer, i, j, substitutionValue, orderedTokens);

				// The substitution value is equals to the last value in the buffer
				substitutionValue = temp;
			}
		}

		// Returns final distance
		return buffer[orderedTokens[1].length()];
	}

	/**
	 * Levenshtein algorithm that uses an array as a buffer for calculation 
	 * with early stop if the distance exceeds the value of the maxDistance parameter.
	 * 
	 * @param token1
	 * @param token2
	 * @param maxDistance
	 * @return int- levenshtein distance for the tokens
	 */
	public static int levenshtein(String token1, String token2, int maxDistance) {

		// Validate tokens
		validateTokens(token1, token2);

		// Validates maxDistance input
		if (maxDistance < 0) {
			throw new IllegalArgumentException("Max Distance threshold must be zero or higher!");
		}

		// Order the tokens storing them in one array with longest at [0] and shortest at[1]
		String[] orderedTokens = orderTokens(token1, token2);

		// Creates the buffer
		int[] buffer = createBuffer(orderedTokens[1].length());

		// Calculate the length difference between the tokens
		int diagDifference = orderedTokens[0].length() - orderedTokens[1].length();
		
		// Loops longest token
		for (int i = 1; i <= orderedTokens[0].length(); i++) {
			// First position in buffer is always as the row start index
			buffer[0] = i;

			// The substitution value starts as the last row start index
			int substitutionValue = i - 1;
			
			// Defines when the distance has been calculated for a row
			boolean distanceDefined = false;
			
			// Loops the shortest token
			for (int j = 1; j <= orderedTokens[1].length(); j++) {
				// Backups the current buffer value
				int temp = buffer[j];
				
				// Calculates minumum value for the operations
				buffer[j] = min(buffer, i, j, substitutionValue, orderedTokens);

				// The substitution value is equals to the last value in the buffer
				substitutionValue = temp;
				
				// Tests if the minimum value is a valid distance
				if (!distanceDefined && i >= j && (Math.abs(i - j) <= diagDifference)) {
					// if the minimum value is greater than the threshold, returns
					if (buffer[j] > maxDistance && i < (orderedTokens[0].length()-1)) {
						return buffer[j];
					}
					
					// The distance is defined if the index i and j are in a possible position for a 
					// distance value
					distanceDefined = true;
				}
			}
		}

		// Returns final distance
		return buffer[orderedTokens[1].length()];
	}

	/**
	 * Calculates the minimum value for the insertion, deletion and substitution
	 * operations.
	 * 
	 * @param buffer
	 * @param i
	 * @param j
	 * @param substitutionValue
	 * @param longestToken
	 * @param shortestToken
	 * @return int - the minimum value for the operations
	 */
	public static int min(int[] buffer, int i, int j, int substitutionValue, String[] orderedTokens) {
		int deletion = buffer[j] + 1;
		int insertion = buffer[j - 1] + 1;
		int substitution = substitutionValue + (isSubstitution(i, j, orderedTokens) ? 1 : 0);
		
		return Math.min(Math.min(deletion, insertion), substitution);
	}
	

	/**
	 * Checks if the characters differs from each other.
	 * 
	 * @param i
	 * @param j
	 * @param orderedTokens
	 * @return
	 */
	private static boolean isSubstitution(int i, int j, String[] orderedTokens) {
		return orderedTokens[0].charAt(i - 1) != orderedTokens[1].charAt(j - 1);
	}


	/**
	 * Stores the tokens in an array ordered by its lengths.
	 * 
	 * @param token1
	 * @param token2
	 * @return String[] - [0] longest length token | [1] shortest length token
	 */
	private static String[] orderTokens(String token1, String token2) {
		String[] orderedTokens = new String[] { token1, token2 };

		// Switch token2 as the longest token
		if (token1.length() < token2.length()) {
			orderedTokens[0] = token2;
			orderedTokens[1] = token1;
		}

		return orderedTokens;
	}

	/**
	 * Creates and initializes the calculation buffer.
	 * 
	 * @param size
	 * @return
	 */
	private static int[] createBuffer(int size) {
		// Creates the buffer array according to the longest string length
		int[] buffer = new int[size + 1];

		// Fill the buffer with the index values
		for (int i = 1; i <= size; i++) {
			buffer[i] = i;
		}

		return buffer;
	}

	/**
	 * Checks if the tokens are valid (not null).
	 * 
	 * @param token1
	 * @param token2
	 * @throws IllegalArgumentException
	 */
	private static void validateTokens(String token1, String token2) {
		if (token1 == null || token2 == null) {
			throw new IllegalArgumentException("Invalid token(s)!");
		}
	}
}