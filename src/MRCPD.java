

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MRCPD {

	private HashMap<String, HashMap<String, String>> entries = new HashMap<String, HashMap<String, String>>();
	public MRCPD(String mrcLocation) {
		readDataset(mrcLocation);
	}

	/**
	 * Read in the MRCPD complete dataset, and store the information in a
	 * structured hashmap.
	 */
	public void readDataset(String mrcLocation) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(mrcLocation), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				String[] columns = line.trim().split("\\s+"); // Columns are delimited by a varying number of spaces (not actual tabs).
				HashMap<String, String> entryDetails = new HashMap<String, String>();
				entryDetails.put("WORD", columns[0].toLowerCase().trim());
				entryDetails.put("IMAGERY_MEAN", columns[1].trim());
				entryDetails.put("IMAGERY_SD", columns[2].trim());
				entryDetails.put("AOA_MEAN", columns[3].trim());
				entryDetails.put("AOA_SD", columns[4].trim());
				entryDetails.put("FAMILIARITY_MEAN", columns[5].trim());
				entryDetails.put("FAMILIARITY_SD", columns[6].trim());
				entryDetails.put("CONCRETENESS_MEAN", columns[7].trim());
				entryDetails.put("CONCRETENESS_SD", columns[8].trim());
				entryDetails.put("AMBIGUITY", columns[9].trim());
				entryDetails.put("FREQUENCY", columns[10].trim());
				entries.put(columns[0].toLowerCase().trim(), entryDetails);
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
	 * Return the familiarity (mean) score for the given word, or a question
	 * mark if a score for that word does not exist.
	 * @param word
	 * @return
	 */
	public String getFamiliarity(String word) {
		if(entries.containsKey(word.toLowerCase().trim())) {
			HashMap<String, String> entry = entries.get(word.toLowerCase().trim());
			return entry.get("FAMILIARITY_MEAN");
		}
		else {
			return "0";
		}
	}
	
	/**
	 * Return the ambiguity score for the given word, or a question mark if a
	 * score for that word does not exist.
	 * @param word
	 * @return
	 */
	public String getAmbiguity(String word) {
		if(entries.containsKey(word.toLowerCase().trim())) {
			HashMap<String, String> entry = entries.get(word.toLowerCase().trim());
			return entry.get("AMBIGUITY");
		}
		else {
			return "?";
		}
	}
}
