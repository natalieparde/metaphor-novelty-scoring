/**
 * PMICalculator.java
 * 
 * Natalie Parde
 * Last Updated: 11/16/2017
 * 
 * Computes the pointwise mutual information (PMI) between word pairs, and
 * word-span pairs.  Functions in this file were also used for earlier work on
 * sarcasm detection; see the following paper for details about that project:
 *  Natalie Parde and Rodney D. Nielsen. (2017). #SarcasmDetection Is Soooo 
 *  General! Towards a Domain-Independent Approach for Detecting Sarcasm. In the
 *  Proceedings of the Thirtieth International Flairs Conference.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PMICalculator {

	private HashMap<String, Double> w1Freq_2gms;
	private HashMap<String, Double> w1Freq_3gms;
	private HashMap<String, Double> w1Freq_4gms;
	private HashMap<String, Double> w1Freq_5gms;
	
	private HashMap<String, Double> lastWordFreq2gms;
	private HashMap<String, Double> lastWordFreq3gms;
	private HashMap<String, Double> lastWordFreq4gms;
	private HashMap<String, Double> lastWordFreq5gms;
	
	private HashMap<String, Double> pmi_bigram;
	private HashMap<String, Double> pmi_trigram;
	private HashMap<String, Double> pmi_fourgram;
	private HashMap<String, Double> pmi_fivegram;
	
	// For spans where the metaphor occurs first:
	private HashMap<String, Double> freqW1_bigrams_span;
	private HashMap<String, Double> freqW1_trigrams_span;
	private HashMap<String, Double> freqW1_4grams_span;
	private HashMap<String, Double> freqW1_5grams_span;
	
	private HashMap<String, Double> freqTrailingWords_bigrams_span;
	private HashMap<String, Double> freqTrailingWords_trigrams_span;
	private HashMap<String, Double> freqTrailingWords_4grams_span;
	private HashMap<String, Double> freqTrailingWords_5grams_span;
	
	private HashMap<String, Double> pmi_metaphorFirst_bigrams_span;
	private HashMap<String, Double> pmi_metaphorFirst_trigrams_span;
	private HashMap<String, Double> pmi_metaphorFirst_4grams_span;
	private HashMap<String, Double> pmi_metaphorFirst_5grams_span;
	
	// For spans where the indicator occurs first:
	private HashMap<String, Double> freqW2_bigrams_span;
	private HashMap<String, Double> freqW3_trigrams_span;
	private HashMap<String, Double> freqW4_4grams_span;
	private HashMap<String, Double> freqW5_5grams_span;
	
	private HashMap<String, Double> freqStartingWords_bigrams_span;
	private HashMap<String, Double> freqStartingWords_trigrams_span;
	private HashMap<String, Double> freqStartingWords_4grams_span;
	private HashMap<String, Double> freqStartingWords_5grams_span;
	
	private HashMap<String, Double> pmi_indicatorFirst_bigrams_span;
	private HashMap<String, Double> pmi_indicatorFirst_trigrams_span;
	private HashMap<String, Double> pmi_indicatorFirst_4grams_span;
	private HashMap<String, Double> pmi_indicatorFirst_5grams_span;
	
	double twogms_total;
	double threegms_total;
	double fourgms_total;
	double fivegms_total;
	
	// Database variables.
	private Web1TDatabase web1TDatabase;
	private Connection connection;
	private PreparedStatement preparedStatement;
	
	public PMICalculator(String url, String user, String password) {
		// Serialized frequencies for word 1 in the Web1T bigrams, trigrams, 4-grams, and 5-grams.
		w1Freq_2gms = retrieveData(new HashMap<String, Double>(), "w1Freq_2gms");
		w1Freq_3gms = retrieveData(new HashMap<String, Double>(), "w1Freq_3gms");
		w1Freq_4gms = retrieveData(new HashMap<String, Double>(), "w1Freq_4gms");
		w1Freq_5gms = retrieveData(new HashMap<String, Double>(), "w1Freq_5gms");
		
		// Serialized frequencies of the last words in Web1T bigrams, trigrams, and so on.
		lastWordFreq2gms = retrieveData(new HashMap<String, Double>(), "lastWordFreq2gms");
		lastWordFreq3gms = retrieveData(new HashMap<String, Double>(), "lastWordFreq3gms");
		lastWordFreq4gms = retrieveData(new HashMap<String, Double>(), "lastWordFreq4gms");
		lastWordFreq5gms = retrieveData(new HashMap<String, Double>(), "lastWordFreq5gms");
		
		// Serialized PMIs of full bigrams, trigrams, 4-grams, and 5-grams.
		pmi_bigram = retrieveData(new HashMap<String, Double>(), "pmi_bigram");
		pmi_trigram = retrieveData(new HashMap<String, Double>(), "pmi_trigram");
		pmi_fourgram = retrieveData(new HashMap<String, Double>(), "pmi_fourgram");
		pmi_fivegram = retrieveData(new HashMap<String, Double>(), "pmi_fivegram");
		
		// For spans where the metaphor occurs first:
		freqW1_bigrams_span = retrieveData(new HashMap<String, Double>(), "freqW1_bigrams_span");
		freqW1_trigrams_span = retrieveData(new HashMap<String, Double>(), "freqW1_trigrams_span");
		freqW1_4grams_span = retrieveData(new HashMap<String, Double>(), "freqW1_4grams_span");
		freqW1_5grams_span = retrieveData(new HashMap<String, Double>(), "freqW1_5grams_span");
		
		freqTrailingWords_bigrams_span = retrieveData(new HashMap<String, Double>(), "freqTrailingWords_bigrams_span");
		freqTrailingWords_trigrams_span = retrieveData(new HashMap<String, Double>(), "freqTrailingWords_trigrams_span");
		freqTrailingWords_4grams_span = retrieveData(new HashMap<String, Double>(), "freqTrailingWords_4grams_span");
		freqTrailingWords_5grams_span = retrieveData(new HashMap<String, Double>(), "freqTrailingWords_5grams_span");
		
		pmi_metaphorFirst_bigrams_span = retrieveData(new HashMap<String, Double>(), "pmi_metaphorFirst_bigrams_span");
		pmi_metaphorFirst_trigrams_span = retrieveData(new HashMap<String, Double>(), "pmi_metaphorFirst_trigrams_span");
		pmi_metaphorFirst_4grams_span = retrieveData(new HashMap<String, Double>(), "pmi_metaphorFirst_4grams_span");
		pmi_metaphorFirst_5grams_span = retrieveData(new HashMap<String, Double>(), "pmi_metaphorFirst_5grams_span");
		
		// For spans where the indicator occurs first:
		freqW2_bigrams_span = retrieveData(new HashMap<String, Double>(), "freqW2_bigrams_span");
		freqW3_trigrams_span = retrieveData(new HashMap<String, Double>(), "freqW3_trigrams_span");
		freqW4_4grams_span = retrieveData(new HashMap<String, Double>(), "freqW4_4grams_span");
		freqW5_5grams_span = retrieveData(new HashMap<String, Double>(), "freqW5_5grams_span");
		
		freqStartingWords_bigrams_span = retrieveData(new HashMap<String, Double>(), "freqStartingWords_bigrams_span");
		freqStartingWords_trigrams_span = retrieveData(new HashMap<String, Double>(), "freqStartingWords_trigrams_span");
		freqStartingWords_4grams_span = retrieveData(new HashMap<String, Double>(), "freqStartingWords_4grams_span");
		freqStartingWords_5grams_span = retrieveData(new HashMap<String, Double>(), "freqStartingWords_5grams_span");
		
		pmi_indicatorFirst_bigrams_span = retrieveData(new HashMap<String, Double>(), "pmi_indicatorFirst_bigrams_span");
		pmi_indicatorFirst_trigrams_span = retrieveData(new HashMap<String, Double>(), "pmi_indicatorFirst_trigrams_span");
		pmi_indicatorFirst_4grams_span = retrieveData(new HashMap<String, Double>(), "pmi_indicatorFirst_4grams_span");
		pmi_indicatorFirst_5grams_span = retrieveData(new HashMap<String, Double>(), "pmi_indicatorFirst_5grams_span");
		
		twogms_total = 909808023212.0;
		threegms_total = 737755957564.0;
		fourgms_total = 507166847446.0;
		fivegms_total = 352338622536.0;

		web1TDatabase = new Web1TDatabase();
		connection = web1TDatabase.getConnection();
	}
	
	/**
	 * Retrieve the serialized frequencies for the specified unit.
	 * @param frequencies
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> retrieveData(HashMap<String, Double> frequencies, String filename) {
		FileInputStream fileInputStream;
		try {
			if(new File("resources/serialized_pmis/" + filename + ".ser").exists()) {
				fileInputStream = new FileInputStream("resources/serialized_pmis/" + filename + ".ser"); // Only partial path needed here.
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				frequencies = (HashMap<String, Double>) objectInputStream.readObject();
				objectInputStream.close();
				System.out.println("Successfully read data from: " + filename + ".ser");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return frequencies;
	}
	
	/**
	 * Store the serialized frequencies for the specified unit.
	 * @param frequencies
	 * @param filename
	 */
	public void storeData(HashMap<String, Double> frequencies, String filename) {
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream("resources/serialized_pmis/" + filename + ".ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(frequencies);
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Store all the frequency and PMI data at once.
	 */
	public void storeAllData() {
		// Store word 1 frequencies.
		storeData(w1Freq_2gms, "w1Freq_2gms");
		storeData(w1Freq_3gms, "w1Freq_3gms");
		storeData(w1Freq_4gms, "w1Freq_4gms");
		storeData(w1Freq_5gms, "w1Freq_5gms");
		
		// Store last word frequencies.
		storeData(lastWordFreq2gms, "lastWordFreq2gms");
		storeData(lastWordFreq3gms, "lastWordFreq3gms");
		storeData(lastWordFreq4gms, "lastWordFreq4gms");
		storeData(lastWordFreq5gms, "lastWordFreq5gms");
		
		// Store PMIs.
		storeData(pmi_bigram, "pmi_bigram");
		storeData(pmi_trigram, "pmi_trigram");
		storeData(pmi_fourgram, "pmi_fourgram");
		storeData(pmi_fivegram, "pmi_fivegram");
		
		// For when the metaphor occurs first in the span:
		storeData(freqW1_bigrams_span, "freqW1_bigrams_span");
		storeData(freqW1_trigrams_span, "freqW1_trigrams_span");
		storeData(freqW1_4grams_span, "freqW1_4grams_span");
		storeData(freqW1_5grams_span, "freqW1_5grams_span");
		
		storeData(freqTrailingWords_bigrams_span, "freqTrailingWords_bigrams_span");
		storeData(freqTrailingWords_trigrams_span, "freqTrailingWords_trigrams_span");
		storeData(freqTrailingWords_4grams_span, "freqTrailingWords_4grams_span");
		storeData(freqTrailingWords_5grams_span, "freqTrailingWords_5grams_span");
		
		storeData(pmi_metaphorFirst_bigrams_span, "pmi_metaphorFirst_bigrams_span");
		storeData(pmi_metaphorFirst_trigrams_span, "pmi_metaphorFirst_trigrams_span");
		storeData(pmi_metaphorFirst_4grams_span, "pmi_metaphorFirst_4grams_span");
		storeData(pmi_metaphorFirst_5grams_span, "pmi_metaphorFirst_5grams_span");
		
		// For when the indicator occurs first in the span:
		storeData(freqW2_bigrams_span, "freqW2_bigrams_span");
		storeData(freqW3_trigrams_span, "freqW3_trigrams_span");
		storeData(freqW4_4grams_span, "freqW4_4grams_span");
		storeData(freqW5_5grams_span, "freqW5_5grams_span");
		
		storeData(freqStartingWords_bigrams_span, "freqStartingWords_bigrams_span");
		storeData(freqStartingWords_trigrams_span, "freqStartingWords_trigrams_span");
		storeData(freqStartingWords_4grams_span, "freqStartingWords_4grams_span");
		storeData(freqStartingWords_5grams_span, "freqStartingWords_5grams_span");
		
		storeData(pmi_indicatorFirst_bigrams_span, "pmi_indicatorFirst_bigrams_span");
		storeData(pmi_indicatorFirst_trigrams_span, "pmi_indicatorFirst_trigrams_span");
		storeData(pmi_indicatorFirst_4grams_span, "pmi_indicatorFirst_4grams_span");
		storeData(pmi_indicatorFirst_5grams_span, "pmi_indicatorFirst_5grams_span");
	}
	
	/**
	 * Get the PMI for the specified bigram.
	 * @param w1
	 * @param w2
	 * @return
	 */
	public double getPMIBigram(String w1, String w2) {
		// Remove hashtag.
		if(w2.startsWith("#")) {
			w2 = w2.substring(1);
		}

		if(pmi_bigram.containsKey(w1 + " " + w2)) {
			return pmi_bigram.get(w1 + " " + w2);
		}
		else if(w1Freq_2gms.containsKey(w1) && lastWordFreq2gms.containsKey(w2)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w2);
			
			// Get the frequency for the last word.
			String query = "";
			ResultSet rs = null;
			double bothWordsFreq = 0.0;
			double pmi;
			
			// Get the frequency for both words occurring together.
			try {
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					bothWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (bothWordsFreq + 1) / (twogms_total + 1);
			double p_w1 = (w1Freq_2gms.get(w1) + 1) / (twogms_total + 1);
			double p_w2 = (lastWordFreq2gms.get(w2) + 1) / (twogms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_bigram.put(w1 + " " + w2, pmi);
			return pmi;
		}
		else if(w1Freq_2gms.containsKey(w1)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w2);
			
			// Get the frequency for the last word.
			String query = "";
			ResultSet rs = null;
			double lastWordFreq = 0.0;
			double bothWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 2gms where w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq2gms.put(w2, lastWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occuring together.
			try {
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					bothWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (bothWordsFreq + 1) / (twogms_total + 1);
			double p_w1 = (w1Freq_2gms.get(w1) + 1) / (twogms_total + 1);
			double p_w2 = (lastWordFreq + 1) / (twogms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_bigram.put(w1 + " " + w2, pmi);
			return pmi;
		}
		else if(lastWordFreq2gms.containsKey(w2)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w2);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double bothWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 2gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_2gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occurring together.
			try {
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					bothWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (bothWordsFreq + 1) / (twogms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (twogms_total + 1);
			double p_w2 = (lastWordFreq2gms.get(w2) + 1) / (twogms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_bigram.put(w1 + " " + w2, pmi);
			return pmi;
		}
		else {
			System.out.println("Calculating new PMI for: " + w1 + " " + w2);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double bothWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 2gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_2gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
						
			// Get the frequency for the last word.
			double lastWordFreq = 0.0;
			try {
				query = "select sum(frequency) from 2gms where w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq2gms.put(w2, lastWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occuring together.
			try {
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					bothWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (bothWordsFreq + 1) / (twogms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (twogms_total + 1);
			double p_w2 = (lastWordFreq + 1) / (twogms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_bigram.put(w1 + " " + w2, pmi);
			return pmi;
		}
	}
	
	/**
	 * Compute the PMI for the specified trigram.
	 * @param w1
	 * @param w2
	 * @param w3
	 * @return
	 */
	public double getPMITrigram(String w1, String w3) {
		if(w3.startsWith("@")) {
			w3 = "__@reply__";
		}
		
		// Remove hashtags.
		if(w3.startsWith("#")) {
			w3 = w3.substring(1);
		}

		if(pmi_trigram.containsKey(w1 + " " + w3)) {
			return pmi_trigram.get(w1 + " " + w3);
		}
		else if(w1Freq_3gms.containsKey(w1) && lastWordFreq3gms.containsKey(w3)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w3);

			String query = "";
			ResultSet rs = null;
			double allWordsFreq = 0.0;
			double pmi;			
			
			// Get the frequency for all words occurring together.
			try {
				// Wildcards are allowed, but all of the last words cannot be wildcards.
				if(w3.equals("__@reply__")) {
					query = "select sum(frequency) from 3gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 3gms where w1=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w3);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (threegms_total + 1);
			double p_w1 = (w1Freq_3gms.get(w1) + 1) / (threegms_total + 1);
			double p_w2 = (lastWordFreq3gms.get(w3) + 1) / (threegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_trigram.put(w1 + " " + w3, pmi);
			return pmi;
		}
		else if(w1Freq_3gms.containsKey(w1)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w3);
			
			// Get the frequency for the last word.
			String query = "";
			ResultSet rs = null;
			double lastWordsFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 3gms where w3=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w3);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq3gms.put(w3, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for all words occurring together.
			try {
				if(w3.equals("__@reply__")) { // Let @replies be wildcards.
					query = "select sum(frequency) from 3gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 3gms where w1=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w3);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (threegms_total + 1);
			double p_w1 = (w1Freq_3gms.get(w1) + 1) / (threegms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (threegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_trigram.put(w1 + " " + w3, pmi);
			return pmi;
		}
		else if(lastWordFreq3gms.containsKey(w3)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w3);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 3gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_3gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occurring together.
			try {
				if(w3.equals("__@reply__")) {
					query = "select sum(frequency) from 3gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 3gms where w1=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w3);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (threegms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (threegms_total + 1);
			double p_w2 = (lastWordFreq3gms.get(w3) + 1) / (threegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_trigram.put(w1 + " " + w3, pmi);
			return pmi;
		}
		else {
			System.out.println("Calculating new PMI for: " + w1 + " " + w3);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 3gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_3gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
						
			// Get the frequency for the last word.
			double lastWordsFreq = 0.0;
			try {
				query = "select sum(frequency) from 3gms where w3=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w3);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq3gms.put(w3, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occuring together.
			try {
				if(w3.equals("__@reply__")) {
					query = "select sum(frequency) from 3gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 3gms where w1=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w3);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (threegms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (threegms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (threegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_trigram.put(w1 + " " + w3, pmi);
			return pmi;
		}
	}
	
	/**
	 * Computes the PMI for the specified 4-gram.
	 * @param w1
	 * @param w2
	 * @param w3
	 * @param w4
	 * @return
	 */
	public double getPMIFourgram(String w1, String w4) {
		if(w4.startsWith("@")) {
			w4 = "__@reply__";
		}
		
		// Remove hashtags.
		if(w4.startsWith("#")) {
			w4 = w4.substring(1);
		}
				
		if(pmi_fourgram.containsKey(w1 + " " + w4)) {
			return pmi_fourgram.get(w1 + " " + w4);
		}
		else if(w1Freq_4gms.containsKey(w1)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w4);
			
			// Get the frequency for the last word.
			String query = "";
			ResultSet rs = null;
			double lastWordsFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 4gms where w4=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w4);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq4gms.put(w4, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for all words occurring together.
			try {
				if(w4.equals("__@reply__")) {
					query = "select sum(frequency) from 4gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 4gms where w1=? and w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w4);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fourgms_total + 1);
			double p_w1 = (w1Freq_4gms.get(w1) + 1) / (fourgms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (fourgms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fourgram.put(w1 + " " + w4, pmi);
			return pmi;
		}
		else if(lastWordFreq4gms.containsKey(w4)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w4);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 4gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_4gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occurring together.
			try {

				if(w4.equals("__@reply__")) {
					query = "select sum(frequency) from 4gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 4gms where w1=? and w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w4);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fourgms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (fourgms_total + 1);
			double p_w2 = (lastWordFreq4gms.get(w4) + 1) / (fourgms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fourgram.put(w1 + " " + w4, pmi);
			return pmi;
		}
		else {
			System.out.println("Calculating new PMI for: " + w1 + " " + w4);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 4gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_4gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
						
			// Get the frequency for the last word.
			double lastWordsFreq = 0.0;
			try {
				query = "select sum(frequency) from 4gms where w4=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w4);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq4gms.put(w4, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occuring together.
			try {
				if(w4.equals("__@reply__")) {
					query = "select sum(frequency) from 4gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 4gms where w1=? and w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w4);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fourgms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (fourgms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (fourgms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fourgram.put(w1 + " " + w4, pmi);
			return pmi;
		}
	}
	
	public double getPMIFivegram(String w1, String w5) {
		if(w5.startsWith("@")) {
			w5 = "__@reply__";
		}
		
		// Remove hashtags.
		if(w5.startsWith("#")) {
			w5 = w5.substring(1);
		}
		
		if(pmi_fivegram.containsKey(w1 + " " + w5)) {
			return pmi_fivegram.get(w1 + " " + w5);
		}
		else if(w1Freq_5gms.containsKey(w1)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w5);
			
			// Get the frequency for the last word.
			String query = "";
			ResultSet rs = null;
			double lastWordsFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 5gms where w5=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w5);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq5gms.put(w5, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for all words occurring together.
			try {
				if(w5.equals("__@reply__")) {
					query = "select sum(frequency) from 5gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 5gms where w1=? and w5=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w5);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fivegms_total + 1);
			double p_w1 = (w1Freq_5gms.get(w1) + 1) / (fivegms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (fivegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fivegram.put(w1 + " " + w5, pmi);
			return pmi;
		}
		else if(lastWordFreq5gms.containsKey(w5)) {
			System.out.println("Calculating new PMI for: " + w1 + " " + w5);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 5gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_5gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occurring together.
			try {
				if(w5.equals("__@reply__")) {
					query = "select sum(frequency) from 5gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 5gms where w1=? and w5=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w5);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fivegms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (fivegms_total + 1);
			double p_w2 = (lastWordFreq5gms.get(w5) + 1) / (fivegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fivegram.put(w1 + " " + w5, pmi);
			return pmi;
		}
		else {
			System.out.println("Calculating new PMI for: " + w1 + " " + w5);
			
			// Get the frequency for the first word.
			String query = "";
			ResultSet rs = null;
			double firstWordFreq = 0.0;
			double allWordsFreq = 0.0;
			double pmi;
			try {
				query = "select sum(frequency) from 5gms where w1=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					firstWordFreq = rs.getDouble("sum(frequency)");
				}
				
				w1Freq_5gms.put(w1, firstWordFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
						
			// Get the frequency for the last word.
			double lastWordsFreq = 0.0;
			try {
				query = "select sum(frequency) from 5gms where w5=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w5);
				System.out.println(preparedStatement);
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					lastWordsFreq = rs.getDouble("sum(frequency)");
				}
				
				lastWordFreq5gms.put(w5, lastWordsFreq);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Get the frequency for both words occurring together.
			try {
				if(w5.equals("__@reply__")) {
					query = "select sum(frequency) from 5gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					System.out.println(preparedStatement);
				}
				else {
					query = "select sum(frequency) from 5gms where w1=? and w5=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w5);
					System.out.println(preparedStatement);
				}
				rs = preparedStatement.executeQuery();

				while(rs.next()) {
					allWordsFreq = rs.getDouble("sum(frequency)");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			// Compute the necessary probabilities (plus-one smoothing used).
			double p_both = (allWordsFreq + 1) / (fivegms_total + 1);
			double p_w1 = (firstWordFreq + 1) / (fivegms_total + 1);
			double p_w2 = (lastWordsFreq + 1) / (fivegms_total + 1);
			
			// Compute the PMI from these probabilities.
			pmi = Math.log(p_both / (p_w1 * p_w2));
			pmi_fivegram.put(w1 + " " + w5, pmi);
			return pmi;
		}
	}
	
	/**
	 * Compute the PMI between a word and the span of words that follows it.
	 * @param w1
	 * @param followingWords
	 * @return
	 * @throws SQLException 
	 */
	public double getPMISpan_wordFirst(String w1, ArrayList<String> followingWords) throws SQLException {
		String query = "";
		ResultSet rs = null;
		
		if(followingWords.size() == 1) {
			String w2 = followingWords.get(0); // The first (and only) word in this list.
			double w1_freq = 0.0;
			double trailingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_metaphorFirst_bigrams_span.containsKey(w1 + " " + w2)) {
				return pmi_metaphorFirst_bigrams_span.get(w1 + " " + w2);
			}
			else {
				// Get the frequency of the first word.
				if(freqW1_bigrams_span.containsKey(w1)) {
					w1_freq = freqW1_bigrams_span.get(w1);
				}
				else {
					System.out.println("Calculating frequency for: " + w1);
					query = "select sum(frequency) from 2gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						w1_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW1_bigrams_span.put(w1, w1_freq);
				}
				
				// Get the frequency of the second word.
				if(freqTrailingWords_bigrams_span.containsKey(w2)) {
					trailingWords_freq = freqTrailingWords_bigrams_span.get(w2);
				}
				else {
					System.out.println("Calculating frequency for : " + w2);
					query = "select sum(frequency) from 2gms where w2=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w2);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						trailingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqTrailingWords_bigrams_span.put(w2, trailingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " and " + w2);
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (twogms_total + 1);
				double p_w1 = (freqW1_bigrams_span.get(w1) + 1) / (twogms_total + 1);
				double p_w2 = (freqTrailingWords_bigrams_span.get(w2) + 1) / (twogms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_metaphorFirst_bigrams_span.put(w1 + " " + w2, pmi);
				return pmi;
			}
		}
		else if(followingWords.size() == 2) {
			String w2 = followingWords.get(0);
			String w3 = followingWords.get(1);
			
			double w1_freq = 0.0;
			double trailingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_metaphorFirst_trigrams_span.containsKey(w1 + " " + w2 + " " + w3)) {
				return pmi_metaphorFirst_trigrams_span.get(w1 + " " + w2 + " " + w3);
			}
			else {
				// Get the frequency of the first word.
				if(freqW1_trigrams_span.containsKey(w1)) {
					w1_freq = freqW1_trigrams_span.get(w1);
				}
				else {
					System.out.println("Calculating frequency for: " + w1);
					query = "select sum(frequency) from 3gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						w1_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW1_trigrams_span.put(w1, w1_freq);
				}
				
				// Get the frequency of the second word.
				if(freqTrailingWords_trigrams_span.containsKey(w2 + " " + w3)) {
					trailingWords_freq = freqTrailingWords_trigrams_span.get(w2 + " " + w3);
				}
				else {
					System.out.println("Calculating frequency for : " + w2 + " " + w3);
					query = "select sum(frequency) from 3gms where w2=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w2);
					preparedStatement.setString(2, w3);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						trailingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqTrailingWords_trigrams_span.put(w2 + " " + w3, trailingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " and " + w2 + " and " + w3);
				query = "select sum(frequency) from 3gms where w1=? and w2=? and w3=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, w3);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (threegms_total + 1);
				double p_w1 = (freqW1_trigrams_span.get(w1) + 1) / (threegms_total + 1);
				double p_w2 = (freqTrailingWords_trigrams_span.get(w2 + " " + w3) + 1) / (threegms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_metaphorFirst_trigrams_span.put(w1 + " " + w2 + " " + w3, pmi);
				return pmi;
			}
		}
		else if(followingWords.size() == 3) {
			String w2 = followingWords.get(0);
			String w3 = followingWords.get(1);
			String w4 = followingWords.get(2);
			
			double w1_freq = 0.0;
			double trailingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_metaphorFirst_4grams_span.containsKey(w1 + " " + w2 + " " + w3 + " " + w4)) {
				return pmi_metaphorFirst_4grams_span.get(w1 + " " + w2 + " " + w3 + " " + w4);
			}
			else {
				// Get the frequency of the first word.
				if(freqW1_4grams_span.containsKey(w1)) {
					w1_freq = freqW1_4grams_span.get(w1);
				}
				else {
					System.out.println("Calculating frequency for: " + w1);
					query = "select sum(frequency) from 4gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						w1_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW1_4grams_span.put(w1, w1_freq);
				}
				
				// Get the frequency of the second word.
				if(freqTrailingWords_4grams_span.containsKey(w2 + " " + w3 + " " + w4)) {
					trailingWords_freq = freqTrailingWords_4grams_span.get(w2 + " " + w3 + " " + w4);
				}
				else {
					System.out.println("Calculating frequency for : " + w2 + " " + w3 + " " + w4);
					query = "select sum(frequency) from 4gms where w2=? and w3=? and w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w2);
					preparedStatement.setString(2, w3);
					preparedStatement.setString(3, w4);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						trailingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqTrailingWords_4grams_span.put(w2 + " " + w3 + " " + w4, trailingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " and " + w2 + " and " + w3 + " and " + w4);
				query = "select sum(frequency) from 4gms where w1=? and w2=? and w3=? and w4=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, w3);
				preparedStatement.setString(4, w4);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (fourgms_total + 1);
				double p_w1 = (freqW1_4grams_span.get(w1) + 1) / (fourgms_total + 1);
				double p_w2 = (freqTrailingWords_4grams_span.get(w2 + " " + w3 + " " + w4) + 1) / (fourgms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_metaphorFirst_4grams_span.put(w1 + " " + w2 + " " + w3 + " " + w4, pmi);
				return pmi;
			}
		}
		else { // This should be four, but we'll include an error check inside.
			if(followingWords.size() > 4) {
				System.out.println("NOOOOOOOOO how did this slip by??  You have more than four trailing words.");
			}
			
			String w2 = followingWords.get(0);
			String w3 = followingWords.get(1);
			String w4 = followingWords.get(2);
			String w5 = followingWords.get(3);
			
			double w1_freq = 0.0;
			double trailingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_metaphorFirst_5grams_span.containsKey(w1 + " " + w2 + " " + w3 + " " + w4 + " " + w5)) {
				return pmi_metaphorFirst_5grams_span.get(w1 + " " + w2 + " " + w3 + " " + w4 + " " + w5);
			}
			else {
				// Get the frequency of the first word.
				if(freqW1_5grams_span.containsKey(w1)) {
					w1_freq = freqW1_5grams_span.get(w1);
				}
				else {
					System.out.println("Calculating frequency for: " + w1);
					query = "select sum(frequency) from 5gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						w1_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW1_5grams_span.put(w1, w1_freq);
				}
				
				// Get the frequency of the second word.
				if(freqTrailingWords_5grams_span.containsKey(w2 + " " + w3 + " " + w4 + " " + w5)) {
					trailingWords_freq = freqTrailingWords_5grams_span.get(w2 + " " + w3 + " " + w4 + " " + w5);
				}
				else {
					System.out.println("Calculating frequency for : " + w2 + " " + w3 + " " + w4 + " " + w5);
					query = "select sum(frequency) from 5gms where w2=? and w3=? and w4=? and w5=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w2);
					preparedStatement.setString(2, w3);
					preparedStatement.setString(3, w4);
					preparedStatement.setString(4, w5);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						trailingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqTrailingWords_5grams_span.put(w2 + " " + w3 + " " + w4 + " " + w5, trailingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " and " + w2 + " and " + w3 + " and " + w4 + " and " + w5);
				query = "select sum(frequency) from 5gms where w1=? and w2=? and w3=? and w4=? and w5=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, w3);
				preparedStatement.setString(4, w4);
				preparedStatement.setString(5, w5);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (fivegms_total + 1);
				double p_w1 = (freqW1_5grams_span.get(w1) + 1) / (fivegms_total + 1);
				double p_w2 = (freqTrailingWords_5grams_span.get(w2 + " " + w3 + " " + w4 + " " + w5) + 1) / (fivegms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_metaphorFirst_5grams_span.put(w1 + " " + w2 + " " + w3 + " " + w4 + " " + w5, pmi);
				return pmi;
			}
		}
	}
	
	/**
	 * Computes the PMI between a word and the span of words that precedes it.
	 * @param firstWords
	 * @param lastWord
	 * @return
	 * @throws SQLException 
	 */
	public double getPMISpan_wordLast(ArrayList<String> firstWords, String lastWord) throws SQLException {
		String query = "";
		ResultSet rs = null;
		
		if(firstWords.size() == 1) {
			String w1 = firstWords.get(0); // The first (and only) word in this list.
			
			double lastWord_freq = 0.0;
			double precedingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_indicatorFirst_bigrams_span.containsKey(w1 + " " + lastWord)) {
				return pmi_indicatorFirst_bigrams_span.get(w1 + " " + lastWord);
			}
			else {
				// Get the frequency of the last word.
				if(freqW2_bigrams_span.containsKey(lastWord)) {
					lastWord_freq = freqW2_bigrams_span.get(lastWord);
				}
				else {
					System.out.println("Calculating frequency for: " + lastWord);
					query = "select sum(frequency) from 2gms where w2=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, lastWord);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						lastWord_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW2_bigrams_span.put(lastWord, lastWord_freq);
				}
				
				// Get the frequency of the preceding words.
				if(freqStartingWords_bigrams_span.containsKey(w1)) {
					precedingWords_freq = freqStartingWords_bigrams_span.get(w1);
				}
				else {
					System.out.println("Calculating frequency for : " + w1);
					query = "select sum(frequency) from 2gms where w1=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						precedingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqStartingWords_bigrams_span.put(w1, precedingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " and " + lastWord);
				query = "select sum(frequency) from 2gms where w1=? and w2=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, lastWord);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (twogms_total + 1);
				double p_w1 = (freqW2_bigrams_span.get(lastWord) + 1) / (twogms_total + 1);
				double p_w2 = (freqStartingWords_bigrams_span.get(w1) + 1) / (twogms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_indicatorFirst_bigrams_span.put(w1 + " " + lastWord, pmi);
				return pmi;
			}
		}
		else if(firstWords.size() == 2) {
			String w1 = firstWords.get(0);
			String w2 = firstWords.get(1);
			
			double lastWord_freq = 0.0;
			double precedingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_indicatorFirst_trigrams_span.containsKey(w1 + " " + w2 + " " + lastWord)) {
				return pmi_indicatorFirst_trigrams_span.get(w1 + " " + w2 + " " + lastWord);
			}
			else {
				// Get the frequency of the last word.
				if(freqW3_trigrams_span.containsKey(lastWord)) {
					lastWord_freq = freqW3_trigrams_span.get(lastWord);
				}
				else {
					System.out.println("Calculating frequency for: " + lastWord);
					query = "select sum(frequency) from 3gms where w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, lastWord);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						lastWord_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW3_trigrams_span.put(lastWord, lastWord_freq);
				}
				
				// Get the frequency of the preceding words.
				if(freqStartingWords_trigrams_span.containsKey(w1 + " " + w2)) {
					precedingWords_freq = freqStartingWords_trigrams_span.get(w1 + " " + w2);
				}
				else {
					System.out.println("Calculating frequency for : " + w1 + " " + w2);
					query = "select sum(frequency) from 3gms where w1=? and w2=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2,  w2);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						precedingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqStartingWords_trigrams_span.put(w1 + " " + w2, precedingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " " + w2 + " and " + lastWord);
				query = "select sum(frequency) from 3gms where w1=? and w2=? and w3=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, lastWord);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (threegms_total + 1);
				double p_w1 = (freqW3_trigrams_span.get(lastWord) + 1) / (threegms_total + 1);
				double p_w2 = (freqStartingWords_trigrams_span.get(w1 + " " + w2) + 1) / (threegms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_indicatorFirst_trigrams_span.put(w1 + " " + w2 + " " + lastWord, pmi);
				return pmi;
			}
		}
		else if(firstWords.size() == 3) {
			String w1 = firstWords.get(0);
			String w2 = firstWords.get(1);
			String w3 = firstWords.get(2);
			
			double lastWord_freq = 0.0;
			double precedingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_indicatorFirst_4grams_span.containsKey(w1 + " " + w2 + " " + w3 + " " + lastWord)) {
				return pmi_indicatorFirst_4grams_span.get(w1 + " " + w2 + " " + w3 + " " + lastWord);
			}
			else {
				// Get the frequency of the last word.
				if(freqW4_4grams_span.containsKey(lastWord)) {
					lastWord_freq = freqW4_4grams_span.get(lastWord);
				}
				else {
					System.out.println("Calculating frequency for: " + lastWord);
					query = "select sum(frequency) from 4gms where w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, lastWord);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						lastWord_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW4_4grams_span.put(lastWord, lastWord_freq);
				}
				
				// Get the frequency of the preceding words.
				if(freqStartingWords_4grams_span.containsKey(w1 + " " + w2 + " " + w3)) {
					precedingWords_freq = freqStartingWords_4grams_span.get(w1 + " " + w2 + " " + w3);
				}
				else {
					System.out.println("Calculating frequency for : " + w1 + " " + w2 + " " + w3);
					query = "select sum(frequency) from 4gms where w1=? and w2=? and w3=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w2);
					preparedStatement.setString(3, w3);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						precedingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqStartingWords_4grams_span.put(w1 + " " + w2 + " " + w3, precedingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " " + w2 + " " + w3 + " and " + lastWord);
				query = "select sum(frequency) from 4gms where w1=? and w2=? and w3=? and w4=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, w3);
				preparedStatement.setString(4, lastWord);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (fourgms_total + 1);
				double p_w1 = (freqW4_4grams_span.get(lastWord) + 1) / (fourgms_total + 1);
				double p_w2 = (freqStartingWords_4grams_span.get(w1 + " " + w2 + " " + w3) + 1) / (fourgms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_indicatorFirst_4grams_span.put(w1 + " " + w2 + " " + w3 + " " + lastWord, pmi);
				return pmi;
			}
		}
		else {
			if(firstWords.size() > 4) { // Obligatory error check.
				System.out.println("NOOOOOOOOO how did this slip by??  You have more than four preceding words.");
			}
			
			String w1 = firstWords.get(0);
			String w2 = firstWords.get(1);
			String w3 = firstWords.get(2);
			String w4 = firstWords.get(3);
			
			double lastWord_freq = 0.0;
			double precedingWords_freq = 0.0;
			double both_freq = 0.0;
			
			if(pmi_indicatorFirst_5grams_span.containsKey(w1 + " " + w2 + " " + w3 + " " + w4 + " " + lastWord)) {
				return pmi_indicatorFirst_5grams_span.get(w1 + " " + w2 + " " + w3 + " " + w4 + " " + lastWord);
			}
			else {
				// Get the frequency of the last word.
				if(freqW5_5grams_span.containsKey(lastWord)) {
					lastWord_freq = freqW5_5grams_span.get(lastWord);
				}
				else {
					System.out.println("Calculating frequency for: " + lastWord);
					query = "select sum(frequency) from 5gms where w5=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, lastWord);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						lastWord_freq = rs.getDouble("sum(frequency)");
					}
					
					freqW5_5grams_span.put(lastWord, lastWord_freq);
				}
				
				// Get the frequency of the preceding words.
				if(freqStartingWords_5grams_span.containsKey(w1 + " " + w2 + " " + w3 + " " + w4)) {
					precedingWords_freq = freqStartingWords_5grams_span.get(w1 + " " + w2 + " " + w3 + " " + w4);
				}
				else {
					System.out.println("Calculating frequency for : " + w1 + " " + w2 + " " + w3 + " " + w4);
					query = "select sum(frequency) from 5gms where w1=? and w2=? and w3=? and w4=?;";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, w1);
					preparedStatement.setString(2, w2);
					preparedStatement.setString(3, w3);
					preparedStatement.setString(4, w4);
					rs = preparedStatement.executeQuery();
	
					while(rs.next()) {
						precedingWords_freq = rs.getDouble("sum(frequency)");
					}
					
					freqStartingWords_5grams_span.put(w1 + " " + w2 + " " + w3 + " " + w4, precedingWords_freq);
				}
				
				// Get the frequency of both words together.
				System.out.println("Calculating frequency for: " + w1 + " " + w2 + " " + w3 + " " + w4 + " and " + lastWord);
				query = "select sum(frequency) from 5gms where w1=? and w2=? and w3=? and w4=? and w5=?;";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, w1);
				preparedStatement.setString(2, w2);
				preparedStatement.setString(3, w3);
				preparedStatement.setString(4, w4);
				preparedStatement.setString(5, lastWord);
				rs = preparedStatement.executeQuery();
	
				while(rs.next()) {
					both_freq = rs.getDouble("sum(frequency)");
				}
				
				// Compute the PMI.
				System.out.println("And now computing PMI....");
				// Compute the necessary probabilities (plus-one smoothing used).
				double p_both = (both_freq + 1) / (fivegms_total + 1);
				double p_w1 = (freqW5_5grams_span.get(lastWord) + 1) / (fivegms_total + 1);
				double p_w2 = (freqStartingWords_5grams_span.get(w1 + " " + w2 + " " + w3 + " " + w4) + 1) / (fivegms_total + 1);
				
				// Compute the PMI from these probabilities.
				double pmi = Math.log(p_both / (p_w1 * p_w2));
				pmi_indicatorFirst_5grams_span.put(w1 + " " + w2 + " " + w3 + " " + w4 + " " + lastWord, pmi);
				return pmi;
			}
		}
	}
}
