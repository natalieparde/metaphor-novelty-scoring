package metaphorDetectionPairClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

public class LRECBaseline {

	private PrintWriter fileWriter;
	private ArrayList<String> header;

	/**
	 * Get the instances associated with the specified input file.
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<WordPair> getInstances() {
		String path = "";
		String filename = "gold_metaphor_novelty.csv";
		String outfileName = "src/main/output/" + filename.replace(".csv", ".out.csv");
		
		// Create an output CSV file that will contain the features.
		try {  
			fileWriter = new PrintWriter(outfileName, "UTF-8");
			System.out.println("New File Name: " + outfileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// Check to see if a serialized instance file already exists
		// for this input file.  If so, read that in.  If not, find
		// all the instances from this input file.
		ReadDataset readDataset = new ReadDataset(path, filename);
		readDataset.read_predictions();
		
		DataRetriever dataRetriever = new DataRetriever();
		HashMap<Integer, ArrayList<VUAMCWord>> folds = dataRetriever.splitDataIntoFolds(dataRetriever.retrieveFullDataset());
		
		System.out.println("Getting candidates!");
		ArrayList<WordPair> specifiedPairs = new ArrayList<WordPair>();
		String serializedFilename = "src/main/resources/" + filename.replace(".csv", ".ser");
		File serializedFile = new File(serializedFilename);
		if(serializedFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(serializedFilename);
				ObjectInputStream ois = new ObjectInputStream(fis);
				specifiedPairs = (ArrayList<WordPair>) ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			ArrayList<WordPair> wordPairs = dataRetriever.getPairCandidates(folds.get(1));
			wordPairs.addAll(dataRetriever.getPairCandidates(folds.get(2)));
			wordPairs.addAll(dataRetriever.getPairCandidates(folds.get(3)));
			wordPairs.addAll(dataRetriever.getPairCandidates(folds.get(4)));
			wordPairs.addAll(dataRetriever.getPairCandidates(folds.get(5)));
			
			// Get the WordPairs associated with this instance.
			for(WordPair wordPair : wordPairs) {
				for(String line : readDataset.getData()) {
					String[] columns = line.split(",");
					String id = columns[0];
					double metaphorNovelty = Double.parseDouble(columns[1]);
					
					String[] id_parts = id.split("__");
					int sentence_num = Integer.parseInt(id_parts[0]);
					String indicator = id_parts[1].split("_")[0];
					int indicator_position = Integer.parseInt(id_parts[1].split("_")[1]);
					String metaphor = id_parts[2].split("_")[0];
					int metaphor_position = Integer.parseInt(id_parts[2].split("_")[1]);
					
					if(wordPair.getSentenceNum() == sentence_num) {
						if(((wordPair.getGov().equals(indicator) && wordPair.getWordPositionCoreNLP_gov() == indicator_position) &&
								(wordPair.getDep().equals(metaphor) && wordPair.getWordPositionCoreNLP_dep() == metaphor_position)) || 
								((wordPair.getGov().equals(metaphor) && wordPair.getWordPositionCoreNLP_gov() == metaphor_position) &&
								(wordPair.getDep().equals(indicator) && wordPair.getWordPositionCoreNLP_dep() == indicator_position))) {
							if(wordPair.getMainWord().equals(metaphor)) {
								if((specifiedPairs.size() % 100) == 0) {
									System.out.println("Found pair: " + id);
								}
								wordPair.setPairID(id);
								wordPair.setMetaphorNovelty(metaphorNovelty);
								specifiedPairs.add(wordPair);
							}

						}
					}
				}
			}
			// Serialize this for easy future retrieval.
			try {
				FileOutputStream fos = new FileOutputStream(serializedFilename);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(specifiedPairs);
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return specifiedPairs;
	}
	
	/**
	 * Get the feature vector for each instance.
	 * @param specifiedPairs
	 * @return
	 */
	public HashMap<String, double[]> getFeatures(ArrayList<WordPair> specifiedPairs) {
		// Load word embeddings.
		String word_vector_file = "/local2/workspace/metaphorDetection/src/main/resources/GoogleNewsVectors/GoogleNews-vectors-negative300.bin.gz";
		WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(word_vector_file));
		
		// Load all possible dependency types for one-hot encoding.
		ArrayList<String> depTypes = new ArrayList<String>();
		depTypes.add("nsubj");
		depTypes.add("nsubjpass");
		depTypes.add("dobj");
		depTypes.add("iobj");
		depTypes.add("csubj");
		depTypes.add("csubjpass");
		depTypes.add("xcomp");
		depTypes.add("appos");
		depTypes.add("nmod");
		depTypes.add("nmod:npmod");
		depTypes.add("nmod:poss");
		depTypes.add("nmod:tmod");
		depTypes.add("acl");
		depTypes.add("acl:relcl");
		depTypes.add("amod");
		depTypes.add("advcl");
		depTypes.add("advmod");
		depTypes.add("compound");
		depTypes.add("dep");
		depTypes.add("subj-obj");
		depTypes.add("subj-reln");
		depTypes.add("obj-reln");
		depTypes.add("reln-subj");
		depTypes.add("reln-obj");
		
		// Build a header row.
		header = new ArrayList<String>();
		for(int i = 0; i < wordVectors.getWordVector("word").length; i++) {  // Get the length of a word embedding by just getting the embedding for a sample word.
			header.add("gov_" + i);
		}
		for(int i = 0; i < wordVectors.getWordVector("word").length; i++) {
			header.add("dep_" + i);
		}
		for(int i = 0; i < depTypes.size(); i++) {
			header.add("is_" + depTypes.get(i));
		}
		header.add("absDistance");
		header.add("score");
		
		// Iterate through each pair.
		HashMap<String, double[]> featureVectors = new HashMap<String, double[]>();
		int pairCounter = 0;
		for(WordPair pair : specifiedPairs) {
			if((pairCounter % 50) == 0) {
				System.out.println("Extracting features for instance " + pairCounter + " of " + specifiedPairs.size() + ".");
			}
			
			// Word embeddings.
			double[] govVec = wordVectors.getWordVector(pair.getGov());
			double[] depVec = wordVectors.getWordVector(pair.getDep());
			
			// Dependency type.
			double[] oneHot = new double[depTypes.size()];
			for(int i = 0; i < depTypes.size(); i++) {
				if(pair.getRelation().equals(depTypes.get(i))) {
					oneHot[i] = 1;
				}
				else {
					oneHot[i] = 0;
				}
			}
			
			// Absolute distance between words.
			double[] absDistance = new double[1];
			absDistance[0] = Math.abs(pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep());
			
			// The metaphor novelty score.
			double[] score = new double[1];
			score[0] = pair.getMetaphorNovelty();
			
			// Concatenate the features into a single feature vector.
			double[] fv = new double[govVec.length + depVec.length + oneHot.length + 2];
			System.arraycopy(govVec, 0, fv, 0, govVec.length);
			System.arraycopy(depVec, 0, fv, govVec.length, depVec.length);
			System.arraycopy(oneHot, 0, fv, govVec.length + depVec.length, oneHot.length);
			System.arraycopy(absDistance, 0, fv, govVec.length + depVec.length + oneHot.length, absDistance.length);
			System.arraycopy(score, 0, fv, govVec.length + depVec.length + oneHot.length + 1, score.length);
			
			// Add this to the dictionary of feature vectors.
			featureVectors.put(pair.getPairID(), fv);
			
			pairCounter++;
		}
		return featureVectors;
	}
	
	/**
	 * Prints the feature vectors to a CSV file.  The first column of
	 * the CSV file contains the instance ID.
	 * @param featureVectors
	 */
	public void printFeatures(HashMap<String, double[]> featureVectors) {
		for(Entry<String, double[]> instance : featureVectors.entrySet()) {
			String featureList = instance.getKey(); // The ID, not a feature.
			for(int i = 0; i < instance.getValue().length; i++) {
				featureList += "," + instance.getValue()[i];
			}
			
			fileWriter.println(featureList);
			fileWriter.flush();
		}
	}
	
	public static void main(String[] args) {
		LRECBaseline lrecBaseline = new LRECBaseline();
		ArrayList<WordPair> instances = lrecBaseline.getInstances();
		HashMap<String, double[]> featureVectors = lrecBaseline.getFeatures(instances);
		lrecBaseline.printFeatures(featureVectors);
	}

}
