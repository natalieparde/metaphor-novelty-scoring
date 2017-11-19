/**
 * Note: This is almost identical to the code found here: 
 * http://sentiwordnet.isti.cnr.it/code/SentiWordNetDemoCode.java,
 * with only a few very slight modifications.
 * 
 * Last Updated: 11/16/2017
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SentiWordNet {

	private HashMap<String, Double> dictionary;
	
	public SentiWordNet(String sentiwordnetLocation) {
		dictionary = new HashMap<String, Double>();
		
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();
		
		try {
			@SuppressWarnings("resource")
			BufferedReader sentiwordnet_document = new BufferedReader(new FileReader(sentiwordnetLocation));
			String line = "";
			while ((line = sentiwordnet_document.readLine()) != null) {
				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];
					
					// Is it a valid line? Otherwise, throw exception.
					if (data.length != 6) {
						throw new IllegalArgumentException("Incorrect data length!");
					}
					
					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank, synsetScore);
					}
				}
			}
			sentiwordnet_document.close();
			
			// Go through all the terms.
			for (HashMap.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) {
				String word = entry.getKey();
				HashMap<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (HashMap.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Return the calculated SentiWordNet score for this word, for this part of 
	// speech.  Converts word to lowercase before checking.
	public String getScore(String word, String pos) {
		if(dictionary.containsKey(word.toLowerCase() + "#" + pos)) {
			return Double.toString(dictionary.get(word.toLowerCase() + "#" + pos));
		}
		else {
			return "0";
		}
	}
	
	// Calculate difference in scores for words 1 and 2.
	public String calculateDifference(String word1, String pos1, String word2, String pos2) {
		String score_w1 = getScore(word1, pos1);
		String score_w2 = getScore(word2, pos2);
		if(!score_w1.equals("?") && !score_w2.equals("?")) {
			double difference = Double.valueOf(score_w1) - Double.valueOf(score_w2);
			return Double.toString(difference);
		}
		else {
			return "?";
		}
	}
	
	// Calculate the absolute difference between scores for words 1 and 2.
	public String calculateAbsDifference(String word1, String pos1, String word2, String pos2) {
		String score_w1 = getScore(word1, pos1);
		String score_w2 = getScore(word2, pos2);
		if(!score_w1.equals("?") && !score_w2.equals("?")) {
			double difference = Double.valueOf(score_w1) - Double.valueOf(score_w2);
			return Double.toString(Math.abs(difference));
		}
		else {
			return "?";
		}
	}
	
	// Convert the specified Penn Treebank POS tag to its WordNet equivalent.
	public String convertPtbToPos(String pos) {
		if(pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS")) { // Check for noun.
			return "n";
		}
		
		// Check for verb.
		else if(pos.equals("VB") || pos.equals("VBD") || pos.equals("VBG") || pos.equals("VBN") || pos.equals("VBP") || pos.equals("VBZ")) {
			return "v";
		}
		
		// Check for adjective.
		else if(pos.equals("JJ") || pos.equals("JJR") || pos.equals("JJS")) {
			return "a";
		}
		
		// Check for adverb.
		else if(pos.equals("RB") || pos.equals("RBR") || pos.equals("RBS") || pos.equals("WRB")) {
			return "r";
		}
		
		else {
			System.out.println("Input POS " + pos + " is not a valid tag!");
			return "?";
		}
	}
}
