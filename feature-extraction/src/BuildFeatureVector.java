/**
 *  BuildFeatureVector.java
 *  
 *  Natalie Parde
 *  Last Updated: 11/16/2017
 *  
 *  Contains functions to compute metaphor novelty features.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import edu.stanford.nlp.ling.IndexedWord;

public class BuildFeatureVector {
	
	/**
	 * Set the PMI for the word, based on the distance between the words (can only
	 * compute PMI for distances of up to four words), given a WordPair object.
	 * Reconfigured so that it can be called externally.
	 * @param pair
	 * @param pmiCalculator
	 * @return 
	 */
	public double setPmiRel(WordPair pair, PMICalculator pmiCalculator) {
		int distance = Math.abs(pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep());

		if(pair.getWordPositionCoreNLP_gov() < pair.getWordPositionCoreNLP_dep()) {
			if(distance == 1) {
				return pmiCalculator.getPMIBigram(pair.getGov(), pair.getDep());
			}
			else if(distance == 2) {
				return pmiCalculator.getPMITrigram(pair.getGov(), pair.getDep());
			}
			else if(distance == 3) {
				return pmiCalculator.getPMIFourgram(pair.getGov(), pair.getDep());
			}
			else if(distance == 4) {
				return pmiCalculator.getPMIFivegram(pair.getGov(), pair.getDep());
			}
			else {
				return 1.467;  // Average PMI in the VUAMC training set.
			}
		}
		else {
			if(distance == 1) {
				return pmiCalculator.getPMIBigram(pair.getDep(), pair.getGov());
			}
			else if(distance == 2) {
				return pmiCalculator.getPMITrigram(pair.getDep(), pair.getGov());
			}
			else if(distance == 3) {
				return pmiCalculator.getPMIFourgram(pair.getDep(), pair.getGov());
			}
			else if(distance == 4) {
				return pmiCalculator.getPMIFivegram(pair.getDep(), pair.getGov());
			}
			else {
				return 1.467;
			}
		}
	}
	
	/**
	 * Return the PMI with 0, 1, 2, or 3 words between the two words, based on
	 * word distance in the original sentence.
	 * @param gov
	 * @param dep
	 * @param wordDistance
	 * @param pmiCalculator
	 * @return
	 */
	public double getCorrectPMI(IndexedWord gov, IndexedWord dep, int wordDistance, PMICalculator pmiCalculator) {
		if(gov.index() < dep.index()) {
			if(wordDistance == 1) {
				return pmiCalculator.getPMIBigram(gov.word(), dep.word());
			}
			else if(wordDistance == 2) {
				return pmiCalculator.getPMITrigram(gov.word(), dep.word());
			}
			else if(wordDistance == 3) {
				return pmiCalculator.getPMIFourgram(gov.word(), dep.word());
			}
			else {
				return pmiCalculator.getPMIFivegram(gov.word(), dep.word());
			}
		}
		else {
			if(wordDistance == 1) {
				return pmiCalculator.getPMIBigram(dep.word(), gov.word());
			}
			else if(wordDistance == 2) {
				return pmiCalculator.getPMITrigram(dep.word(), gov.word());
			}
			else if(wordDistance == 3) {
				return pmiCalculator.getPMIFourgram(dep.word(), gov.word());
			}
			else {
				return pmiCalculator.getPMIFivegram(dep.word(), gov.word());
			}
		}
	}
	
	/**
	 * Similar to the above, but accepts words and their positions separately
	 * instead of as IndexedWords.
	 * @param w1
	 * @param w2
	 * @param wordDistance
	 * @param position1
	 * @param position2
	 * @param pmiCalculator
	 * @return
	 */
	public double getCorrectPMI(String w1, String w2, int wordDistance, int position1, int position2, PMICalculator pmiCalculator) {
		if(position1 < position2) {
			if(wordDistance == 1) {
				return pmiCalculator.getPMIBigram(w1, w2);
			}
			else if(wordDistance == 2) {
				return pmiCalculator.getPMITrigram(w1, w2);
			}
			else if(wordDistance == 3) {
				return pmiCalculator.getPMIFourgram(w1, w2);
			}
			else {
				return pmiCalculator.getPMIFivegram(w1, w2);
			}
		}
		else {
			if(wordDistance == 1) {
				return pmiCalculator.getPMIBigram(w2, w1);
			}
			else if(wordDistance == 2) {
				return pmiCalculator.getPMITrigram(w2, w1);
			}
			else if(wordDistance == 3) {
				return pmiCalculator.getPMIFourgram(w2, w1);
			}
			else {
				return pmiCalculator.getPMIFivegram(w2, w1);
			}
		}
	}
	
	/**
	 * Checks if the word is a content (noun, verb, adjective, or adverb) word.
	 * @param word
	 * @return
	 */
	public boolean isContentWord(IndexedWord word) {
		boolean contentWord = false;
		
		// If one part of the dependency is ROOT (and thus has no tag), there 
		// is not enough information to detect a metaphor (we're dealing with 
		// two-word relations).
		if(word.tag() != null && !word.value().replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", "").trim().equals("")) { // If a word only has non-alphanumeric characters, it is NOT a content word.
			// Check for noun.
			if(word.tag().equals("NN") || word.tag().equals("NNS") || word.tag().equals("NNP") || word.tag().equals("NNPS")) {
				contentWord = true;
			}
			
			// Check for verb.
			else if(word.tag().equals("VB") || word.tag().equals("VBD") || word.tag().equals("VBG") || word.tag().equals("VBN") || word.tag().equals("VBP") || word.tag().equals("VBZ")) {
				contentWord = true;
			}
			
			// Check for adjective.
			else if(word.tag().equals("JJ") || word.tag().equals("JJR") || word.tag().equals("JJS")) {
				contentWord = true;
			}
			
			// Check for adverb.
			else if(word.tag().equals("RB") || word.tag().equals("RBR") || word.tag().equals("RBS")) {
				contentWord = true;
			}
		}
		return contentWord;
	}
	
	/**
	 * Checks if the word is a content (noun, verb, adjective, or adverb) word.
	 * @param word
	 * @return
	 */
	public boolean isContentWord(String pos) {
		boolean contentWord = false;
		
		// Make sure the tag isn't empty.
		if(pos != null && !pos.equals("")) { // If a word only has non-alphanumeric characters, it is NOT a content word.
			// Check for noun.
			if(pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS")) {
				contentWord = true;
			}
			
			// Check for verb.
			else if(pos.equals("VB") || pos.equals("VBD") || pos.equals("VBG") || pos.equals("VBN") || pos.equals("VBP") || pos.equals("VBZ")) {
				contentWord = true;
			}
			
			// Check for adjective.
			else if(pos.equals("JJ") || pos.equals("JJR") || pos.equals("JJS")) {
				contentWord = true;
			}
			
			// Check for adverb.
			else if(pos.equals("RB") || pos.equals("RBR") || pos.equals("RBS")) {
				contentWord = true;
			}
		}
		return contentWord;
	}
	
	/**
	 * Uses functions from DataRetriever to check and make sure that the word:
	 * - Is a content word
	 * - Is not a stopword
	 * - Is not an auxiliary
	 * - Is not a proper noun
	 * - Contains at least one alphabet character
	 * @param word
	 * @return
	 */
	public boolean isValidWord(String word, String lemma, String pos) {
		DataRetriever dataRetriever = new DataRetriever(true);
		ArrayList<String> stopwords = dataRetriever.getStopwordsList();
		if(!stopwords.contains(word.toLowerCase())) {
			if(!dataRetriever.isAuxiliary(lemma)) {
				if(dataRetriever.isContentWord(pos)) {
					if(dataRetriever.containsAlphabetCharacter(word)) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Compute the average PMI between the governor and any other content
	 * words in the sentence (excluding the dependent), given a WordPair object.
	 * @param pmiCalculator
	 * @param wordPair
	 * @param otherWordsInSentence
	 * @return 
	 */
	public double setAvgPmiSentence_Gov(PMICalculator pmiCalculator, WordPair pair, ArrayList<String> otherWordsInSentence) {
		System.out.println("Computing average PMI (sentence + gov) for " + pair.getGov() + "....");
		
		double pmiCounter = 0.0;
		double pmiSum = 0.0;
		for(String word : otherWordsInSentence) {
			String[] components = word.split("___");
			String token = components[0];
			int idx = Integer.parseInt(components[2]);
			if(idx != pair.getWordPositionCoreNLP_dep()) {
				int distance = Math.abs(idx - pair.getWordPositionCoreNLP_gov());
				if(distance < 5) {
					pmiSum += getCorrectPMI(pair.getGov(), token, distance, pair.getWordPositionCoreNLP_gov(), idx, pmiCalculator);
					pmiCounter += 1.0;
				}
			}
		}
		
		// Finally compute the average PMI by dividing pmiSum by pmiCounter.
		if(pmiCounter > 0) {
			return (pmiSum / pmiCounter);
		}
		else {
			return 0.65; // Average for this feature with VUAMC pairs.
		}
	}
	
	/**
	 * Compute the average PMI between the dependent and any other content
	 * words in the sentence (excluding the governor), given a WordPair object.
	 * @param pmiCalculator
	 * @param wordPair
	 * @param otherWordsInSentence
	 * @return 
	 */
	public double setAvgPmiSentence_Dep(PMICalculator pmiCalculator, WordPair pair, ArrayList<String> otherWordsInSentence) {
		System.out.println("Computing average PMI (sentence + dep) for " + pair.getDep() + "....");

		double pmiCounter = 0.0;
		double pmiSum = 0.0;
		for(String word : otherWordsInSentence) {
			String[] components = word.split("___");
			String token = components[0];
			int idx = Integer.parseInt(components[2]);
			if(idx != pair.getWordPositionCoreNLP_gov()) {
				int distance = Math.abs(idx - pair.getWordPositionCoreNLP_dep());
				if(distance < 5) {
					pmiSum += getCorrectPMI(pair.getDep(), token, distance, pair.getWordPositionCoreNLP_dep(), idx, pmiCalculator);
					pmiCounter += 1.0;
				}
			}
		}
		
		// Finally compute the average PMI by dividing pmiSum by pmiCounter.
		if(pmiCounter > 0) {
			return (pmiSum / pmiCounter);
		}
		else {
			return 0.987; // Average for this feature with VUAMC pairs.
		}
	}
	
	/**
	 * Returns the PMI for the span of text preceding/proceeding the governor 
	 * through the dependent, with the governor.
	 * @param pmiCalculator
	 * @param pair
	 * @return
	 */
	public double setPmiSpanGov(PMICalculator pmiCalculator, WordPair pair, String tokenizedSentence) {
		if(Math.abs(pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 4) {
			return 5.121;
		}
		else {
			String[] tokens = tokenizedSentence.split(" ");
			if(tokens[pair.getWordPositionCoreNLP_gov()-1].equals(pair.getGov())) {	
				if(pair.getWordPositionCoreNLP_gov() < pair.getWordPositionCoreNLP_dep()) {
					int spanEnd = pair.getWordPositionCoreNLP_dep();
					ArrayList<String> trailingWords = new ArrayList<String>();
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 3) {
						trailingWords.add(tokens[spanEnd-4]);
					}
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 2) {
						trailingWords.add(tokens[spanEnd-3]);
					}
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 1) {
						trailingWords.add(tokens[spanEnd-2]);
					}
					trailingWords.add(tokens[spanEnd-1]);
					try {
						return pmiCalculator.getPMISpan_wordFirst(pair.getGov(), trailingWords);
					} catch (SQLException e) {
						e.printStackTrace();
						return 5.121;
					}
				}
				else {
					int spanStart = pair.getWordPositionCoreNLP_dep() - 1;
					ArrayList<String> startingWords = new ArrayList<String>();
					startingWords.add(tokens[spanStart]);
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 1) {
						startingWords.add(tokens[spanStart+1]);
					}
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 2) {
						startingWords.add(tokens[spanStart+2]);
					}
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 3) {
						startingWords.add(tokens[spanStart+3]);
					}
					try {
						return pmiCalculator.getPMISpan_wordLast(startingWords, pair.getGov());
					} catch (SQLException e) {
						e.printStackTrace();
						return 5.121;
					}
				}
			}
			else {
				System.out.println("Incorrectly aligned sentence!  Gov index should be " + pair.getWordPositionCoreNLP_gov());
				return 5.121;
			}
		}
	}
	
	/**
	 * A version of the above function that returns the PMI for the span of text 
	 * preceding/proceeding the dependent through the governor, with the dependent.
	 * @param pmiCalculator
	 * @param pair
	 * @return
	 */
	public double setPmiSpanDep(PMICalculator pmiCalculator, WordPair pair, String tokenizedSentence) {
		if(Math.abs(pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 4) {
			return 5.101;
		}
		else {
			String[] tokens = tokenizedSentence.split(" ");
			if(tokens[pair.getWordPositionCoreNLP_dep()-1].equals(pair.getDep())) {	
				if(pair.getWordPositionCoreNLP_gov() < pair.getWordPositionCoreNLP_dep()) {
					int spanEnd = pair.getWordPositionCoreNLP_dep()-1;
					ArrayList<String> trailingWords = new ArrayList<String>();
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 3) {
						trailingWords.add(tokens[spanEnd-4]);
					}
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 2) {
						trailingWords.add(tokens[spanEnd-3]);
					}
					if((pair.getWordPositionCoreNLP_dep() - pair.getWordPositionCoreNLP_gov()) > 1) {
						trailingWords.add(tokens[spanEnd-2]);
					}
					trailingWords.add(tokens[spanEnd-1]);
					try {
						return pmiCalculator.getPMISpan_wordLast(trailingWords, pair.getDep());
					} catch (SQLException e) {
						e.printStackTrace();
						return 5.101;
					}
				}
				else {
					int spanStart = pair.getWordPositionCoreNLP_dep();
					ArrayList<String> startingWords = new ArrayList<String>();
					startingWords.add(tokens[spanStart]);
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 1) {
						startingWords.add(tokens[spanStart+1]);
					}
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 2) {
						startingWords.add(tokens[spanStart+2]);
					}
					if((pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep()) > 3) {
						startingWords.add(tokens[spanStart+3]);
					}
					try {
						return pmiCalculator.getPMISpan_wordFirst(pair.getDep(), startingWords);
					} catch (SQLException e) {
						e.printStackTrace();
						return 5.101;
					}
				}
			}
			else {
				System.out.println("Incorrectly aligned sentence!  Gov index should be " + pair.getWordPositionCoreNLP_gov());
				return 5.101;
			}
		}
	}
	
	
	
	/**
	 * Computes two feature values:
	 * 1. The absolute value of the macro average of the absolute difference in 
	 *    concreteness between the governor and (1) the dependant, and 
	 *    (2) the rest of the content words in the sentence.
	 * 2. The absolute value of the macro average of the absolute difference in 
	 *    concreteness between the dependant and (1) the governor, and 
	 *    (2) the rest of the content words in the sentence.
	 * NOTE: (Normalized) Brysbaert concreteness ratings are checked first.  If
	 * the word isn't found, MRC+ concreteness ratings are also checked.
	 * @param concretenessRatings
	 * @param mrcPlus
	 * @param concGov
	 * @param concDep
	 * @param diffGovAndDep
	 * @param otherContentWordsInSentence
	 * @return
	 */
	public double[] setMacroAvgConcreteness(ConcretenessRatings concretenessRatings, MRCPlus mrcPlus, double concGov, double concDep, double diffGovAndDep, ArrayList<String> otherContentWordsInSentence) {
		int wordCounter = 0;
		double diffConcretenessSum_gov = 0.0;
		double diffConcretenessSum_dep = 0.0;
		
		for(String wordAndPOS : otherContentWordsInSentence) {
			String word = wordAndPOS.split("___")[0];
			String concreteness_currentWord = concretenessRatings.getMRCNormalizedConcreteness(word);
			double concreteness = 0.0;
			if(!concreteness_currentWord.equals("NA")) {
				concreteness = Double.parseDouble(concreteness_currentWord);
			}
			else {
				concreteness_currentWord = mrcPlus.getConcretenessScore(word);
				if(!concreteness_currentWord.equals("NA")) { // Check MRC+.
					concreteness = Double.parseDouble(concreteness_currentWord);
				}
				else {
					continue;
				}
			}
			
			// Calculate the difference in concreteness between this word and the metaphor word.
			double diff_concreteness_gov = Math.abs(concGov - concreteness);
			diffConcretenessSum_gov += diff_concreteness_gov;
			
			// Calculate the different in concreteness between this word and the indicator word.
			double diff_concreteness_dep = Math.abs(concDep - concreteness);
			diffConcretenessSum_dep += diff_concreteness_dep;
			
			wordCounter++;
		}
		
		if(wordCounter > 0) {
			double avg_govAndOtherWords = diffConcretenessSum_gov / wordCounter;
			double avg_depAndOtherWords = diffConcretenessSum_dep / wordCounter;
			
			// Average the concreteness difference between the metaphor and
			// indicator with the average concreteness between the metaphor
			// (or indicator) and other content words in the sentence.
			double macroAvg_gov = (diffGovAndDep + avg_govAndOtherWords) / 2;
			double macroAvg_dep = (diffGovAndDep + avg_depAndOtherWords) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
		else { // Can't compute what we want.  Assume concreteness of all other words was 0.
			double macroAvg_gov = (diffGovAndDep + concGov) / 2;
			double macroAvg_dep = (diffGovAndDep + concDep) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
	}
	
	/**
	 * Computes two feature values:
	 * 1. The macro average of the absolute difference in imageability between 
	 *    the governor and (1) the dependant, and (2) the rest of the content 
	 *    words in the sentence.
	 * 2. The macro average of the absolute difference in imageability between 
	 *    the dependant and (1) the governor, and (2) the rest of the content 
	 *    words in the sentence.
	 * @param mrcPlus
	 * @param imgGov
	 * @param imgDep
	 * @param diffGovAndDep
	 * @param otherContentWordsInSentence
	 * @return
	 */
	public double[] setMacroAvgImageability(MRCPlus mrcPlus, double imgGov, double imgDep, double diffGovAndDep, ArrayList<String> otherContentWordsInSentence) {
		int wordCounter = 0;
		double diffImageabilitySum_gov = 0.0;
		double diffImageabilitySum_dep = 0.0;
		
		for(String wordAndPOS : otherContentWordsInSentence) {
			String word = wordAndPOS.split("___")[0];
			String imageability_currentWord = mrcPlus.getImageabilityScore(word.toLowerCase());
			double imageability = 0.0;
			if(!imageability_currentWord.equals("NA")) {
				imageability = Double.parseDouble(imageability_currentWord);
			}
			else {
				continue;

			}
			
			// Calculate the difference in concreteness between this word and the metaphor word.
			double diff_imageability_gov = Math.abs(imgGov - imageability);
			diffImageabilitySum_gov += diff_imageability_gov;
			
			// Calculate the different in concreteness between this word and the indicator word.
			double diff_imageability_dep = Math.abs(imgDep - imageability);
			diffImageabilitySum_dep += diff_imageability_dep;
			
			wordCounter++;
		}
		
		if(wordCounter > 0) {
			double avg_govAndOtherWords = diffImageabilitySum_gov / wordCounter;
			double avg_depAndOtherWords = diffImageabilitySum_dep / wordCounter;
			
			// Average the concreteness difference between the metaphor and
			// indicator with the average concreteness between the metaphor
			// (or indicator) and other content words in the sentence.
			double macroAvg_gov = (diffGovAndDep + avg_govAndOtherWords) / 2;
			double macroAvg_dep = (diffGovAndDep + avg_depAndOtherWords) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
		else { // Can't compute what we want.  Assume concreteness of all other words was 0.
			double macroAvg_gov = (diffGovAndDep + imgGov) / 2;
			double macroAvg_dep = (diffGovAndDep + imgDep) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
	}
	
	/**
	 * Computes two feature values:
	 * 1. The macro average of the absolute difference in sentiment between 
	 *    the governor and (1) the dependant, and (2) the rest of the content 
	 *    content words in the sentence.
	 * 2. The macro average of the absolute difference in sentiment between 
	 *    the dependant and (1) the governor, and (2) the rest of the 
	 *    content words in the sentence.
	 * @param sentiWordNet
	 * @param senGov
	 * @param senDep
	 * @param diffGovAndDep
	 * @param otherContentWordsInSentence
	 * @return
	 */
	public double[] setMacroAvgSentiment(SentiWordNet sentiWordNet, double senGov, double senDep, double diffGovAndDep, ArrayList<String> otherContentWordsInSentence) {
		int wordCounter = 0;
		double diffSentimentSum_gov = 0.0;
		double diffSentimentSum_dep = 0.0;
		
		for(String wordAndPOS : otherContentWordsInSentence) {
			String word = wordAndPOS.split("___")[0];
			String pos = wordAndPOS.split("___")[1];
			String sentiment_currentWord = sentiWordNet.getScore(word, pos);
			double sentiment = 0.0;
			if(!sentiment_currentWord.equals("NA")) {
				sentiment = Double.parseDouble(sentiment_currentWord);
			}
			else {
				continue;

			}
			
			// Calculate the difference in sentiment between this word and the metaphor word.
			double diff_sentiment_gov = Math.abs(senGov - sentiment);
			diffSentimentSum_gov += diff_sentiment_gov;
			
			// Calculate the different in sentiment between this word and the indicator word.
			double diff_sentiment_dep = Math.abs(senDep - sentiment);
			diffSentimentSum_dep += diff_sentiment_dep;
			
			wordCounter++;
		}
		
		if(wordCounter > 0) {
			double avg_govAndOtherWords = diffSentimentSum_gov / wordCounter;
			double avg_depAndOtherWords = diffSentimentSum_dep / wordCounter;
			
			// Average the concreteness difference between the metaphor and
			// indicator with the average concreteness between the metaphor
			// (or indicator) and other content words in the sentence.
			double macroAvg_gov = (diffGovAndDep + avg_govAndOtherWords) / 2;
			double macroAvg_dep = (diffGovAndDep + avg_depAndOtherWords) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
		else { // Can't compute what we want.  Assume concreteness of all other words was 0.
			double macroAvg_gov = (diffGovAndDep + senGov) / 2;
			double macroAvg_dep = (diffGovAndDep + senDep) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
	}
	
	/**
	 * Computes two feature values:
	 * 1. The macro average of the absolute difference in ambiguity between 
	 *    the governor and (1) the dependant, and (2) the rest of the 
	 *    content words in the sentence.
	 * 2. The macro average of the absolute difference in ambiguity between 
	 *    the dependant and (1) the governor, and (2) the rest of the 
	 *    content words in the sentence.
	 * @param mrcpd
	 * @param ambGov
	 * @param ambDep
	 * @param diffGovAndDep
	 * @param otherContentWordsInSentence
	 * @return
	 */
	public double[] setMacroAvgAmbiguity(MRCPD mrcpd, double ambGov, double ambDep, double diffGovAndDep, ArrayList<String> otherContentWordsInSentence) {
		int wordCounter = 0;
		double diffAmbiguitySum_gov = 0.0;
		double diffAmbiguitySum_dep = 0.0;
		
		for(String wordAndPOS : otherContentWordsInSentence) {
			String word = wordAndPOS.split("___")[0];
			String ambiguity_currentWord = mrcpd.getAmbiguity(word.toLowerCase());
			double ambiguity = 0.0;
			if(!ambiguity_currentWord.equals("?")) {
				ambiguity = Double.parseDouble(ambiguity_currentWord);
			}
			else {
				continue;

			}
			
			// Calculate the difference in sentiment between this word and the metaphor word.
			double diff_ambiguity_gov = Math.abs(ambGov - ambiguity);
			diffAmbiguitySum_gov += diff_ambiguity_gov;
			
			// Calculate the different in sentiment between this word and the indicator word.
			double diff_ambiguity_dep = Math.abs(ambDep - ambiguity);
			diffAmbiguitySum_dep += diff_ambiguity_dep;
			
			wordCounter++;
		}
		
		if(wordCounter > 0) {
			double avg_govAndOtherWords = diffAmbiguitySum_gov / wordCounter;
			double avg_depAndOtherWords = diffAmbiguitySum_dep / wordCounter;
			
			// Average the concreteness difference between the metaphor and
			// indicator with the average concreteness between the metaphor
			// (or indicator) and other content words in the sentence.
			double macroAvg_gov = (diffGovAndDep + avg_govAndOtherWords) / 2;
			double macroAvg_dep = (diffGovAndDep + avg_depAndOtherWords) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
		else { // Can't compute what we want.  Assume concreteness of all other words was 0.
			double macroAvg_gov = (diffGovAndDep + ambGov) / 2;
			double macroAvg_dep = (diffGovAndDep + ambDep) / 2;
			double[] macroAvgs = new double[2];
			macroAvgs[0] = macroAvg_gov;
			macroAvgs[1] = macroAvg_dep;
			return macroAvgs;
		}
	}
}
