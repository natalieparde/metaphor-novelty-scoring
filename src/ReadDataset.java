

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ReadDataset {

	private String readPath;
	private String readFile;
	private ArrayList<String> data;
	private ArrayList<String> indicatorUnigrams;
	private ArrayList<String> metaphorUnigrams;
	
	public ReadDataset(String readPath, String readFile) {
		this.readPath = readPath;
		this.readFile = readFile;
		data = new ArrayList<String>();
		metaphorUnigrams = new ArrayList<String>();
		indicatorUnigrams = new ArrayList<String>();
	}
	
	/**
	 * Reads a predictions file, where the first column contains the ID for
	 * each instance and the last column contains that instance's metaphoricity.
	 * @return
	 */
	public void read_predictions() {
		try { // Open the file and create an XML Event Reader for the file.
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(readPath + readFile), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String line = bufferedReader.readLine(); // Discard header line.
			while((line = bufferedReader.readLine()) != null) {
				data.add(line);
				
				String[] id_parts = line.split("__");
				indicatorUnigrams.add(id_parts[1].split("_")[0]);
				metaphorUnigrams.add(id_parts[2].split("_")[0]);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getData() {
		return data;
	}
}
