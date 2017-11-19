/**
 * ConcretenessRatings.java
 * 
 * Natalie Parde
 * Last Updated: 11/16/2017
 * 
 * Stores and returns the concreteness scores for various words.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ConcretenessRatings {

	private BufferedReader bufferedReader;
	private HashMap<String, String> concretenessScores;
	
	public ConcretenessRatings(String brysbaertLocation) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(brysbaertLocation), "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		concretenessScores = new HashMap<String, String>();
	}
	
	// Process the concreteness scores document and store the scores in a hashmap
	// with key: lemma, value: concreteness rating.
	public void storeConcretenessScores() {
		try {
			bufferedReader.readLine(); // We don't need the headers right now.
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				String[] content = line.split("\t");
				concretenessScores.put(content[0], content[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Get the specified concreteness score.
	public String getConcreteness(String word) {
		if(concretenessScores.containsKey(word)) {
			return concretenessScores.get(word);
		}
		else {
			return "NA";
		}
	}
	
	/**
	 * Normalize these scores to a 0-5 range so they can be compared to MRC
	 * concreteness scores.
	 * @param word
	 * @return
	 */
	public String getMRCNormalizedConcreteness(String word) {
		if(concretenessScores.containsKey(word)) {
			double concreteness = Double.parseDouble(concretenessScores.get(word));
			
			double brysbaertMin = 1.0;
			double brysbaertMax = 5.0;
			double mrcNormalized = (concreteness - brysbaertMin) / (brysbaertMax - brysbaertMin);
			return String.valueOf(mrcNormalized);
		}
		else {
			return "NA";
		}
	}
}
