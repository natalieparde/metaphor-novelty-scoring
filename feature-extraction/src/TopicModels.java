/**
 * TopicModels.java
 * 
 * Natalie Parde
 * Last Updated: 11/16/2017
 * 
 * Reads in the pre-generated topic probabilities and stores them in a dictionary.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class TopicModels {

	private HashMap<String, ArrayList<String>> topicProbabilities;
	private String[] topicHeaders;

	/**
	 * Read the topic probabilities document into a hashmap in which the keys
	 * are unique words and the values are lists of topic probabilities for
	 * those words.
	 * @return topicProbabilities
	 */
	public HashMap<String, ArrayList<String>> readTopicProbabilities(String infileName) {
		topicProbabilities = new HashMap<String, ArrayList<String>>();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(infileName), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String header_line = bufferedReader.readLine(); 
			setTopicHeaders(header_line.split(",")); // Remember that topicHeaders[0] is "Word."
			
			// Each line contains a word, followed by its similarity to each topic.
			// All columns are tab-separated.
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				ArrayList<String> probabilities = new ArrayList<String>();
				String word = "";
				String[] columns = line.split(",");
				int counter = 0;
				for(String column : columns) {
					if(counter == 0) {
						if(!topicProbabilities.containsKey(column.trim())) {
							word = column.trim();
							topicProbabilities.put(word, new ArrayList<String>());
						}
						else {
							System.out.println("Duplicate word in wordToTopic probabilities list: " + column.trim());
						}
					}
					else { // All subsequent columns are similarity scores between that word and the topics, in order.
						probabilities.add(column.trim());
					}
					counter++;
				}
				topicProbabilities.put(word, probabilities);
			}
			bufferedReader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return topicProbabilities;
	}
	
	public HashMap<String, ArrayList<String>> getTopicProbabilities() {
		return topicProbabilities;
	}

	public void setTopicProbabilities(HashMap<String, ArrayList<String>> topicProbabilities) {
		this.topicProbabilities = topicProbabilities;
	}

	/**
	 * @return the topicHeaders
	 */
	public String[] getTopicHeaders() {
		return topicHeaders;
	}

	/**
	 * @param topicHeaders the topicHeaders to set
	 */
	public void setTopicHeaders(String[] topicHeaders) {
		this.topicHeaders = topicHeaders;
	}

}
