

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MRCPlus {
	
	private HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	public MRCPlus(String mrcLocation) {
		readDataset(mrcLocation);
	}
	
	/**
	 * Read in the MRC+ English dataset, and store the information in a
	 * structured hashmap.
	 */
	public void readDataset(String mrcLocation) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(mrcLocation), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				if(line.startsWith("0") || line.startsWith("1")) {
					String[] columns = line.split("\t");
					HashMap<String, String> entryDetails = new HashMap<String, String>();
					entryDetails.put("IMAGEABILITY", columns[0]);
					entryDetails.put("CONCRETENESS", columns[1]);
					entryDetails.put("POS", columns[2]);
					entryDetails.put("CLASSIFIER", columns[4]);
					entryDetails.put("SOURCE", columns[5]);
					entries.put(columns[3], entryDetails); // Word -> Details; there don't appear to be multiple entries for homonyms, cases of polysemy, etc.
				}
			}
			bufferedReader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the imageability score associated with this word, or a question
	 * mark if there is no imageability score associated with this word.
	 * @param word
	 * @return
	 */
	public String getImageabilityScore(String word) {
		if(entries.containsKey(word)) {
			return entries.get(word).get("IMAGEABILITY");
		}
		else {
			return "0";
		}
	}
	
	/**
	 * Return the concreteness score associated with this word, or a question
	 * mark if there is no imageability score associated with this word.
	 * @param word
	 * @return
	 */
	public String getConcretenessScore(String word) {
		if(entries.containsKey(word)) {
			return entries.get(word).get("CONCRETENESS");
		}
		else {
			return "NA";
		}
	}
}
