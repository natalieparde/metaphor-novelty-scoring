/**
 * NNFeatureGenerator.java
 * 
 * Natalie Parde
 * Last Updated: 11/19/2017
 * 
 * The main file for extracting metaphor novelty features.  Uses the functions
 * defined in the other files to compute various features, and outputs those
 * features to a CSV file.  Also outputs other supplementary information to
 * additional output files.
 */

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
import java.util.Properties;
import java.util.Map.Entry;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import net.didion.jwnl.data.Synset;

public class NNFeatureGenerator {

	private PrintWriter fileWriter;
	private ArrayList<String> header;
	private String filename;
	private HashMap<String, String> tokenizedSentences;
	private PrintWriter anFileWriter;
	private PrintWriter tsvetkovFileWriter;
	private PrintWriter dataAnalysisFileWriter;
	private PrintWriter qgInfoFileWriter;
	private String wordVectorFile;
	private String url;
	private String user;
	private String password;
	private String mrcPlusLocation;
	private String brysbaertLocation;
	private String sentiwordnetLocation;
	private String mrcLocation;
	
	/**
	 * Get the instances associated with the specified input file.
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<WordPair> getInstances(String fname) {
		String path = "";
		filename = fname;
		String outfileName = "output/" + filename.replace(".csv", ".out.csv");
		String anOutfileName = outfileName.replace("predictions_", "an_predictions_").replace("gold_", "an_gold_");
		String tsvetkovFileName = outfileName.replace("predictions_", "tsvetkov_predictions_").replace("gold_", "tsvetkov_gold_").replace("out.csv", ".txt");
		String dataAnalysisFileName = outfileName.replace("predictions_", "analysis_").replace("gold_", "analysis_gold_").replace(".csv", ".tsv");
		String qgInfoFileName = outfileName.replace("predictions_", "qg_info_").replace("gold_", "qg_info_gold_").replace(".csv", ".tsv");
		
		
		// Create an output CSV file that will contain the features.
		try {  
			fileWriter = new PrintWriter(outfileName, "UTF-8");
			System.out.println("New File Name: " + outfileName);
			
			anFileWriter = new PrintWriter(anOutfileName, "UTF-8");
			tsvetkovFileWriter = new PrintWriter(tsvetkovFileName, "UTF-8");
			dataAnalysisFileWriter = new PrintWriter(dataAnalysisFileName, "UTF-8");
			qgInfoFileWriter = new PrintWriter(qgInfoFileName, "UTF-8");
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
		String serializedFilename = "resources/" + filename.replace(".csv", ".ser");
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
	
	public HashMap<String, ArrayList<String>> identifyOtherWordsInSentences(ArrayList<WordPair> specifiedPairs) {
		// Load CoreNLP pipeline.
		Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, depparse, natlog, openie");
        props.setProperty("ssplit.isOneSentence", "true"); // COMMENT OUT IF PARSING FULL BOOK/DOCUMENT/ETC.
        props.setProperty("tokenize.whitespace", "true");  // So we stay as close to the VUAMC tokenization as possible.
        props.setProperty("openie.resolve_coref", "true"); // Replace pronouns with the nouns to which they refer.
        
        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        System.out.println("Building pipeline....");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        BuildFeatureVector buildFeatureVector = new BuildFeatureVector();  // Functions for building a lot of the features.
		HashMap<String, ArrayList<String>> otherWordMap = new HashMap<String, ArrayList<String>>();
        
		tokenizedSentences = new HashMap<String, String>();
		for(WordPair pair : specifiedPairs) {
			// Identify the other words in the sentence.
			// Annotate the sentence using Stanford CoreNLP.
			Annotation doc = new Annotation(pair.getSentence());
			pipeline.annotate(doc);
			ArrayList<String> otherContentWordsInSentence = new ArrayList<String>();
			String tokenizedSentence = "";
			for(CoreMap coreSentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) { // doc should've already been annotated in setAvgPmiDep.
				for(CoreLabel token : coreSentence.get(TokensAnnotation.class)) {
					String pos = token.tag();
					String lemma = token.lemma();
					if(buildFeatureVector.isValidWord(token.word(), lemma, pos) && token.index() != pair.getWordPositionCoreNLP_gov() && token.index() != pair.getWordPositionCoreNLP_dep()) {
						// Create a list of non-metaphor, non-indicator content words in 
						// the sentence that can be used across multiple functions.
						otherContentWordsInSentence.add(token.word() + "___" + pos + "___" + token.index());
					}
					tokenizedSentence += token.word() + " ";
				}
			} // End loop through CoreMap.
			otherWordMap.put(pair.getPairID(), otherContentWordsInSentence);
			tokenizedSentences.put(pair.getPairID(), tokenizedSentence.trim());
        }
		// Serialize this for easy future retrieval.
		try {
			FileOutputStream fos = new FileOutputStream("resources/" + filename + ".otherWordsInSentences");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(otherWordMap);
			oos.close();
			
			fos = new FileOutputStream("resources/" + filename + ".tokenizedSentences");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(tokenizedSentences);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return otherWordMap;
	}
	
	/**
	 * Get the feature vector for each instance.
	 * @param specifiedPairs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, double[]> getFeatures(ArrayList<WordPair> specifiedPairs) {
		// Load concreteness ratings.
		ConcretenessRatings concretenessRatings = new ConcretenessRatings(brysbaertLocation);
		concretenessRatings.storeConcretenessScores();
		MRCPlus mrcPlus = new MRCPlus(mrcPlusLocation);
		SentiWordNet sentiWordNet = new SentiWordNet(sentiwordnetLocation);
		MRCPD mrcpd = new MRCPD(mrcLocation);
		PMICalculator pmiCalculator = new PMICalculator(url, user, password);
		
		// Load topic models.
		TopicModels gutenbergTopicModels = new TopicModels();
		TopicModels wikiTopicModels = new TopicModels();
		gutenbergTopicModels.readTopicProbabilities("resources/vuamc_data/vuamc_topic_probs_gutenberg_tfidf_content.csv");
		wikiTopicModels.readTopicProbabilities("resources/vuamc_data/vuamc_topic_probs_wikipedia.csv");
		HashMap<String, ArrayList<String>> gutenbergTopicProbs = gutenbergTopicModels.getTopicProbabilities();
		HashMap<String, ArrayList<String>> wikiTopicProbs = wikiTopicModels.getTopicProbabilities();
		int topicProbLength = 100;
		
		// Load word embeddings.
		WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(wordVectorFile));
		
		// Load synsets.
		Synsets synsets = new Synsets();
		
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
		
		// Load all possible POS types for one-hot encoding.
		ArrayList<String> posTypes = new ArrayList<String>();
		posTypes.add("NN");
		posTypes.add("NNS");
		posTypes.add("VB");
		posTypes.add("VBD");
		posTypes.add("VBG");
		posTypes.add("VBN");
		posTypes.add("VBP");
		posTypes.add("VBZ");
		posTypes.add("JJ");
		posTypes.add("JJR");
		posTypes.add("JJS");
		posTypes.add("RB");
		posTypes.add("RBR");
		posTypes.add("RBS");
		posTypes.add("PRP");
		
		// Build a header row.
		header = new ArrayList<String>();
		header.add("instance_id");  // Instance ID (not a feature!)
		
		// Concreteness
		header.add("conc_gov");
		header.add("conc_dep");
		header.add("conc_diff");
		header.add("conc_macro_gov");
		header.add("conc_macro_dep");
		
		// Imageability
		header.add("img_gov");
		header.add("img_dep");
		header.add("img_diff");
		header.add("img_macro_gov");
		header.add("img_macro_dep");
		
		// Sentiment
		header.add("sent_gov");
		header.add("sent_dep");
		header.add("sent_diff");
		header.add("sent_macro_gov");
		header.add("sent_macro_dep");
		
		// Ambiguity
		header.add("amb_gov");
		header.add("amb_dep");
		header.add("amb_diff");
		header.add("amb_macro_gov");
		header.add("amb_macro_dep");
		
		// Co-Occurrence
		header.add("co_pmi");
		header.add("co_pmi_sentence_gov");
		header.add("co_pmi_sentence_dep");
		header.add("co_pmi_span_gov");
		header.add("co_pmi_span_dep");
		header.add("co_pmi_diff_sentence_gov");
		header.add("co_pmi_diff_sentence_dep");
		
		// Topic Model
		for(int i = 0; i < topicProbLength; i++) {
			header.add("govGutenbergTopic_" + i);
		}
		for(int i = 0; i < topicProbLength; i++) {
			header.add("depGutenbergTopic_" + i);
		}
		for(int i = 0; i < topicProbLength; i++) {
			header.add("govWikiTopic_" + i);
		}
		for(int i = 0; i < topicProbLength; i++) {
			header.add("depWikiTopic_" + i);
		}
		header.add("probGovInBestDepTopic_gutenberg");
		header.add("probDepInBestGovTopic_gutenberg");
		header.add("probGovInBestDepTopic_wiki");
		header.add("probDepInBestGovTopic_wiki");
		
		// Word Embedding
		for(int i = 0; i < wordVectors.getWordVector("word").length; i++) {  // Get the length of a word embedding by just getting the embedding for a sample word.
			header.add("gov_" + i);
		}
		for(int i = 0; i < wordVectors.getWordVector("word").length; i++) {
			header.add("dep_" + i);
		}
		header.add("embedding_cosineSim");
		
		// SynPos
		for(int i = 0; i < depTypes.size(); i++) {
			header.add("is_" + depTypes.get(i));
		}
		for(int i = 0; i < posTypes.size(); i++) {
			header.add("is_gov" + posTypes.get(i));
		}
		for(int i = 0; i < posTypes.size(); i++) {
			header.add("is_dep" + posTypes.get(i));
		}
		header.add("absDistance");
		
		// SynSet
		header.add("highestNumSynsets");
		header.add("lowestNumSynsets");
		header.add("avgNumSynsets");
		header.add("diffNumSynsets");

		// Label
		header.add("score");
		
		// For Tsvetkov comparison:
		for(String featName : header) {
			anFileWriter.print(featName);
			if(featName.equals("score")) {
				anFileWriter.print("\n");
			}
			else {
				anFileWriter.print(",");
			}
		}
		
		// Get the other words in each sentence (loaded from a serialized
		// file after running the first time).
		HashMap<String, ArrayList<String>> otherWordsMap = new HashMap<String, ArrayList<String>>();
		File serializedFile = new File("resources/" + filename + ".otherWordsInSentences");
		if(serializedFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream("resources/" + filename + ".otherWordsInSentences");
				ObjectInputStream ois = new ObjectInputStream(fis);
				otherWordsMap = (HashMap<String, ArrayList<String>>) ois.readObject();
				ois.close();
				
				fis = new FileInputStream("resources/" + filename + ".tokenizedSentences");
				ois = new ObjectInputStream(fis);
				tokenizedSentences = (HashMap<String, String>) ois.readObject();
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
			otherWordsMap = identifyOtherWordsInSentences(specifiedPairs);
		}
		
		// Iterate through each pair.
		HashMap<String, double[]> featureVectors = new HashMap<String, double[]>();
		int pairCounter = 0;
		for(WordPair pair : specifiedPairs) {
			// Print some basic info for corpus analysis purposes.
			String metaphorPOS = "";
			String indicatorPOS = "";
			if(pair.getMainWord().equals(pair.getGov())) {
				metaphorPOS = pair.getPos_gov();
				indicatorPOS = pair.getPos_dep();
			}
			else {
				metaphorPOS = pair.getPos_dep();
				indicatorPOS = pair.getPos_gov();
			}
			String dataAnalysisString = pair.getPairID() + "\t" + pair.getFragmentID() + "\t" + pair.getRelation() + "\t" + metaphorPOS + "\t" + indicatorPOS + "\t" + pair.getSentence() + "\t" + pair.getMetaphorNovelty() + "\n";
			dataAnalysisFileWriter.write(dataAnalysisString);
			dataAnalysisFileWriter.flush();
			
			String qgInfoString = pair.getPairID() + "\t" + pair.getFragmentID() + "\t" + pair.getRelation() + "\t" + pair.getGov() + "\t"  + pair.getPos_gov() + "\t" + pair.getDep() + "\t" + pair.getPos_dep() + "\t" + pair.getSentence() + "\n";
			qgInfoFileWriter.write(qgInfoString);
			qgInfoFileWriter.flush();
			
			
			if((pairCounter % 50) == 0) {
				System.out.println("Extracting features for instance " + pairCounter + " of " + specifiedPairs.size() + ".");
			}
			
			BuildFeatureVector buildFeatureVector = new BuildFeatureVector();  // Functions for building a lot of the features.			
			ArrayList<String> otherContentWordsInSentence = otherWordsMap.get(pair.getPairID());
			
			// Concreteness
			double[] concreteness = new double[5];
			String concretenessGov = concretenessRatings.getConcreteness(pair.getGov().toLowerCase());
			String concretenessDep = concretenessRatings.getConcreteness(pair.getDep().toLowerCase());
			if(concretenessGov.equals("NA")) {  // If concreteness wasn't found in Brysbaert, check MRC+.
				concretenessGov = mrcPlus.getConcretenessScore(pair.getGov().toLowerCase());
			}
			if(concretenessDep.equals("NA")) {
				concretenessDep = mrcPlus.getConcretenessScore(pair.getDep().toLowerCase());
			}
			
			// If no concreteness is found still, assume it is 0.
			concreteness[0] = (concretenessGov.equals("NA")) ? 0 : Double.parseDouble(concretenessGov);
			concreteness[1] = (concretenessDep.equals("NA")) ? 0 : Double.parseDouble(concretenessDep);
			concreteness[2] = Math.abs(concreteness[0] - concreteness[1]);  // Absolute difference in concreteness.
			
			// Compute concreteness macro-averages.
			double[] macroAvgs = buildFeatureVector.setMacroAvgConcreteness(concretenessRatings, mrcPlus, concreteness[0], concreteness[1], concreteness[2], otherContentWordsInSentence);
			concreteness[3] = macroAvgs[0];
			concreteness[4] = macroAvgs[1];
			
			// Imageability
			double[] imageability = new double[5];
			String imageabilityGov = mrcPlus.getImageabilityScore(pair.getGov().toLowerCase());
			String imageabilityDep = mrcPlus.getImageabilityScore(pair.getDep().toLowerCase());
			imageability[0] = (imageabilityGov.equals("NA")) ? 0 : Double.parseDouble(imageabilityGov);
			imageability[1] = (imageabilityDep.equals("NA")) ? 0 : Double.parseDouble(imageabilityDep);
			imageability[2] = Math.abs(imageability[0] - imageability[1]);
			
			// Compute imageability macro-averages.
			macroAvgs = buildFeatureVector.setMacroAvgImageability(mrcPlus, imageability[0], imageability[1], imageability[2], otherContentWordsInSentence);
			imageability[3] = macroAvgs[0];
			imageability[4] = macroAvgs[1];
			
			
			// Sentiment
			double[] sentiment = new double[5];
			String wnPosGov = sentiWordNet.convertPtbToPos(pair.getPos_gov());
			String wnPosDep = sentiWordNet.convertPtbToPos(pair.getPos_dep());
			String sentimentGov = sentiWordNet.getScore(pair.getGov(), wnPosGov);
			String sentimentDep = sentiWordNet.getScore(pair.getDep(), wnPosDep);
			sentiment[0] = (sentimentGov.equals("NA")) ? 0 : Double.parseDouble(sentimentGov);
			sentiment[1] = (sentimentDep.equals("NA")) ? 0 : Double.parseDouble(sentimentDep);
			sentiment[2] = Math.abs(sentiment[0] - sentiment[1]);
			
			// Compute sentiment macro-averages.
			macroAvgs = buildFeatureVector.setMacroAvgSentiment(sentiWordNet, sentiment[0], sentiment[1], sentiment[2], otherContentWordsInSentence);
			imageability[3] = macroAvgs[0];
			imageability[4] = macroAvgs[1];
			
			
			// Ambiguity
			double[] ambiguity = new double[5];
			String ambiguityGov = mrcpd.getAmbiguity(pair.getGov().toLowerCase());
			String ambiguityDep = mrcpd.getAmbiguity(pair.getDep().toLowerCase());
			ambiguity[0] = (ambiguityGov.equals("?")) ? 0 : Double.parseDouble(ambiguityGov);
			ambiguity[1] = (ambiguityDep.equals("?")) ? 0 : Double.parseDouble(ambiguityDep);
			ambiguity[2] = Math.abs(ambiguity[0] - ambiguity[1]);
			
			// Compute ambiguity macro-averages.
			macroAvgs = buildFeatureVector.setMacroAvgAmbiguity(mrcpd, ambiguity[0], ambiguity[1], ambiguity[2], otherContentWordsInSentence);
			ambiguity[3] = macroAvgs[0];
			ambiguity[4] = macroAvgs[1];
			
			
			// Co-Occurrence
			double[] co_occurrence = new double[7];
			co_occurrence[0] = buildFeatureVector.setPmiRel(pair, pmiCalculator);
			co_occurrence[1] = buildFeatureVector.setAvgPmiSentence_Gov(pmiCalculator, pair, otherContentWordsInSentence);
			co_occurrence[2] = buildFeatureVector.setAvgPmiSentence_Dep(pmiCalculator, pair, otherContentWordsInSentence);
			co_occurrence[3] = buildFeatureVector.setPmiSpanGov(pmiCalculator, pair, tokenizedSentences.get(pair.getPairID()));
			co_occurrence[4] = buildFeatureVector.setPmiSpanDep(pmiCalculator, pair, tokenizedSentences.get(pair.getPairID()));
			co_occurrence[5] = (co_occurrence[1] - co_occurrence[0]);
			co_occurrence[6] = (co_occurrence[2] - co_occurrence[0]);
			
			
			// Topic Models
			double[] govGutenbergTopicProbs = new double[topicProbLength];
			double[] depGutenbergTopicProbs = new double[topicProbLength];
			
			// Gutenberg, Governor
			if(gutenbergTopicProbs.containsKey(pair.getGov().toLowerCase())) {
				ArrayList<String> probs = gutenbergTopicProbs.get(pair.getGov().toLowerCase());
				for(int i = 0; i < govGutenbergTopicProbs.length; i++) {
					govGutenbergTopicProbs[i] = Double.parseDouble(probs.get(i));
				}
			}
			else {
				for(int i = 0; i < govGutenbergTopicProbs.length; i++) {
					govGutenbergTopicProbs[i] = 0.0;
				}
			}
			
			// Gutenberg, Dependent
			if(gutenbergTopicProbs.containsKey(pair.getDep().toLowerCase())) {
				ArrayList<String> probs = gutenbergTopicProbs.get(pair.getDep().toLowerCase());
				for(int i = 0; i < depGutenbergTopicProbs.length; i++) {
					depGutenbergTopicProbs[i] = Double.parseDouble(probs.get(i));
				}
			}
			else {
				for(int i = 0; i < depGutenbergTopicProbs.length; i++) {
					depGutenbergTopicProbs[i] = 0.0;
				}
			}
			
			double[] govWikiTopicProbs = new double[topicProbLength];
			double[] depWikiTopicProbs = new double[topicProbLength];
			
			// Wikipedia, Governor
			if(wikiTopicProbs.containsKey(pair.getGov().toLowerCase())) {
				ArrayList<String> probs = wikiTopicProbs.get(pair.getGov().toLowerCase());
				for(int i = 0; i < govWikiTopicProbs.length; i++) {
					govWikiTopicProbs[i] = Double.parseDouble(probs.get(i));
				}
			}
			else {
				for(int i = 0; i < govWikiTopicProbs.length; i++) {
					govWikiTopicProbs[i] = 0.0;
				}
			}
			
			// Wiki, Dependent
			if(wikiTopicProbs.containsKey(pair.getDep().toLowerCase())) {
				ArrayList<String> probs = wikiTopicProbs.get(pair.getDep().toLowerCase());
				for(int i = 0; i < depWikiTopicProbs.length; i++) {
					depWikiTopicProbs[i] = Double.parseDouble(probs.get(i));
				}
			}
			else {
				for(int i = 0; i < depWikiTopicProbs.length; i++) {
					depWikiTopicProbs[i] = 0.0;
				}
			}
			
			// Probability that the governor and dependent are
			// in one another's best-fitting topic.
			double[] extraTopicProbFeats = new double[4];
			int govGutenbergBest = 0;
			int depGutenbergBest = 0;
			int govWikiBest = 0;
			int depWikiBest = 0;
			for(int i = 0; i < topicProbLength; i++) {
				if(govGutenbergTopicProbs[i] > govGutenbergTopicProbs[govGutenbergBest]) {
					govGutenbergBest = i;
				}
			}
			for(int i = 0; i < topicProbLength; i++) {
				if(depGutenbergTopicProbs[i] > depGutenbergTopicProbs[depGutenbergBest]) {
					depGutenbergBest = i;
				}
			}
			for(int i = 0; i < topicProbLength; i++) {
				if(govWikiTopicProbs[i] > govWikiTopicProbs[govWikiBest]) {
					govWikiBest = i;
				}
			}
			for(int i = 0; i < topicProbLength; i++) {
				if(depWikiTopicProbs[i] > depWikiTopicProbs[depWikiBest]) {
					depWikiBest = i;
				}
			}
			if(gutenbergTopicProbs.containsKey(pair.getGov().toLowerCase())) {
				extraTopicProbFeats[0] = Double.parseDouble(gutenbergTopicProbs.get(pair.getGov().toLowerCase()).get(depGutenbergBest));
			}
			else {
				extraTopicProbFeats[0] = 0.0;
			}
			if(gutenbergTopicProbs.containsKey(pair.getDep().toLowerCase())) {
				extraTopicProbFeats[1] = Double.parseDouble(gutenbergTopicProbs.get(pair.getDep().toLowerCase()).get(govGutenbergBest));
			}
			else {
				extraTopicProbFeats[1] = 0.0;
			}
			if(wikiTopicProbs.containsKey(pair.getGov().toLowerCase())) {
				extraTopicProbFeats[2] = Double.parseDouble(wikiTopicProbs.get(pair.getGov().toLowerCase()).get(depWikiBest));
			}
			else {
				extraTopicProbFeats[2] = 0.0;
			}
			if(wikiTopicProbs.containsKey(pair.getDep().toLowerCase())) {
				extraTopicProbFeats[3] = Double.parseDouble(wikiTopicProbs.get(pair.getDep().toLowerCase()).get(govWikiBest));
			}
			else {
				extraTopicProbFeats[3] = 0.0;
			}
			
			// Word Embeddings
			double[] govVec = wordVectors.getWordVector(pair.getGov());
			double[] depVec = wordVectors.getWordVector(pair.getDep());
			double[] extraEmbeddingFeatures = new double[1];
			extraEmbeddingFeatures[0] = wordVectors.similarity(pair.getGov(), pair.getDep());  // Cosine similarity.
			if(Double.isNaN(extraEmbeddingFeatures[0])) { // Special case.
				extraEmbeddingFeatures[0] = 0;
			}
			
			// Dependency type.
			double[] oneHotRel = new double[depTypes.size()];
			for(int i = 0; i < depTypes.size(); i++) {
				if(pair.getRelation().equals(depTypes.get(i))) {
					oneHotRel[i] = 1;
				}
				else {
					oneHotRel[i] = 0;
				}
			}
			
			// POS
			double[] oneHotPOS = new double[posTypes.size()*2]; // A copy for the Gov and for the Dep.
			for(int i = 0; i < posTypes.size(); i++) {
				if(pair.getPos_gov().equals(posTypes.get(i))) {
					oneHotPOS[i] = 1;
				}
				else {
					oneHotPOS[i] = 0;
				}
			}
			for(int i = 0; i < posTypes.size(); i++) {
				if(pair.getPos_dep().equals(posTypes.get(i))) {
					oneHotPOS[i+posTypes.size()] = 1;
				}
				else {
					oneHotPOS[i+posTypes.size()] = 0;
				}
			}
			
			// Absolute distance between words.
			double[] absDistance = new double[1];
			absDistance[0] = Math.abs(pair.getWordPositionCoreNLP_gov() - pair.getWordPositionCoreNLP_dep());		
						
			// Synsets
			double[] synsetFeats = new double[4];
			Synset[] synsetsW1 = synsets.getSynsets(pair.getGov().toLowerCase(), pair.getPos_gov().toLowerCase());
			Synset[] synsetsW2 = synsets.getSynsets(pair.getDep().toLowerCase(), pair.getPos_dep().toLowerCase());
			int numSynsetsW1 = (synsetsW1 == null) ? 0 : synsetsW1.length;
			int numSynsetsW2 = (synsetsW2 == null) ? 0 : synsetsW2.length;
			
			// Get the highest and lowest number of synsets.
			int highest = numSynsetsW1;
			int lowest = numSynsetsW2;
			if(numSynsetsW1 < numSynsetsW2) {
				highest = numSynsetsW2;
				lowest = numSynsetsW1;
			}
			
			// Get the average number of synsets.
			double avg = (((double) highest) + ((double) lowest)) / 2.0;
			
			// Get the difference between highest and lowest number of synsets.
			int diff = Math.abs(highest - lowest);
			
			// Set feature values.
			synsetFeats[0] = highest;
			synsetFeats[1] = lowest;
			synsetFeats[2] = avg;
			synsetFeats[3] = diff;
			
			
			// The metaphor novelty score.
			double[] score = new double[1];
			score[0] = pair.getMetaphorNovelty();
			
			// Concatenate the features into a single feature vector.
			double[] fv = new double[concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length + synsetFeats.length + oneHotRel.length + oneHotPOS.length + 2];
			System.arraycopy(concreteness, 0, fv, 0, concreteness.length);
			System.arraycopy(imageability, 0, fv, concreteness.length, imageability.length);
			System.arraycopy(sentiment, 0, fv, concreteness.length + imageability.length, sentiment.length);
			System.arraycopy(ambiguity, 0, fv, concreteness.length + imageability.length + sentiment.length, ambiguity.length);
			System.arraycopy(co_occurrence, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length, co_occurrence.length);
			System.arraycopy(govGutenbergTopicProbs, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length, govGutenbergTopicProbs.length);
			System.arraycopy(depGutenbergTopicProbs, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length, depGutenbergTopicProbs.length);
			System.arraycopy(govWikiTopicProbs, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length, govWikiTopicProbs.length);
			System.arraycopy(depWikiTopicProbs, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length, depWikiTopicProbs.length);			
			System.arraycopy(extraTopicProbFeats, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length, extraTopicProbFeats.length);			
			System.arraycopy(govVec, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length, govVec.length);
			System.arraycopy(depVec, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length, depVec.length);
			System.arraycopy(extraEmbeddingFeatures, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length, extraEmbeddingFeatures.length);
			System.arraycopy(oneHotRel, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length + synsetFeats.length, oneHotRel.length);
			System.arraycopy(oneHotPOS, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length + synsetFeats.length + oneHotRel.length, oneHotPOS.length);
			System.arraycopy(absDistance, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length + synsetFeats.length + oneHotRel.length + oneHotPOS.length, absDistance.length);
			System.arraycopy(synsetFeats, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length, synsetFeats.length);
			System.arraycopy(score, 0, fv, concreteness.length + imageability.length + sentiment.length + ambiguity.length + co_occurrence.length + govGutenbergTopicProbs.length + depGutenbergTopicProbs.length + govWikiTopicProbs.length + depWikiTopicProbs.length + extraTopicProbFeats.length + govVec.length + depVec.length + extraEmbeddingFeatures.length + synsetFeats.length + oneHotRel.length + oneHotPOS.length + 1, score.length);	
			
			// Add this to the dictionary of feature vectors.
			featureVectors.put(pair.getPairID(), fv);
			
			// For Tsvetkov comparison:
			if(pair.getPos_gov().startsWith("J") && pair.getPos_dep().startsWith("N")) {
				String featureList = pair.getPairID();
				for(int i = 0; i < fv.length; i++) {
					featureList += "," + fv[i];
				}
				anFileWriter.println(featureList);
				anFileWriter.flush();
				
				tsvetkovFileWriter.println(pair.getGov() + " " + pair.getDep() + " " + pair.getMetaphorNovelty());
				tsvetkovFileWriter.flush();
			}
			else if(pair.getPos_gov().startsWith("N") && pair.getPos_dep().startsWith("J")) {
				String featureList = pair.getPairID();
				for(int i = 0; i < fv.length; i++) {
					featureList += "," + fv[i];
				}
				anFileWriter.println(featureList);
				anFileWriter.flush();
				
				tsvetkovFileWriter.println(pair.getDep() + " " + pair.getGov() + " " + pair.getMetaphorNovelty());
				tsvetkovFileWriter.flush();
			}
			
			pairCounter++;
		}
		pmiCalculator.storeAllData();
		qgInfoFileWriter.close();
		return featureVectors;
	}
	
	/**
	 * Prints the feature vectors to a CSV file.  The first column of
	 * the CSV file contains the instance ID.
	 * @param featureVectors
	 */
	public void printFeatures(HashMap<String, double[]> featureVectors) {
		System.out.println("Printing extracted features to CSV file....");
		for(String featName : header) {
			fileWriter.print(featName);
			if(featName.equals("score")) {
				fileWriter.print("\n");
			}
			else {
				fileWriter.print(",");
			}
		}
		for(Entry<String, double[]> instance : featureVectors.entrySet()) {
			String featureList = instance.getKey(); // The ID, not a feature.
			for(int i = 0; i < instance.getValue().length; i++) {
				featureList += "," + instance.getValue()[i];
			}
			fileWriter.println(featureList);
			fileWriter.flush();
		}
	}
	
	public void setBrysbaertLocation(String brysbaertLocation) {
		this.brysbaertLocation = brysbaertLocation;
	}
	
	public void setSentiwordnetLocation(String sentiwordnetLocation) {
		this.sentiwordnetLocation = sentiwordnetLocation;
	}
	
	public void setMrcLocation(String mrcLocation) {
		this.mrcLocation = mrcLocation;
	}
	
	public void setMrcPlusLocation(String mrcPlusLocation) {
		this.mrcPlusLocation = mrcPlusLocation;
	}
	
	public void setEmbeddingsLocation(String fileLocation) {
		wordVectorFile = fileLocation;
	}
	
	public static void main(String[] args) {
		// Set paths to resources not already included in resources/ and
		// computer-specific variables.
		String brysbaertLocation = "resources/brysbaert_concreteness_ratings/Concreteness_ratings_Brysbaert_et_al_BRM.txt";
		String sentiwordnetLocation = "resources/sentiwordnet/SentiWordNet_3.0.0_20130122.txt";
		String mrcLocation = "resources/mrc_plus/mrcpd_complete_dataset.wds";
		String mrcPlusLocation = "resources/mrc_plus/english_mrc.txt";
		String googleNewsEmbeddings = "resources/embeddings/GoogleNews-vectors-negative300.bin.gz";
		
		NNFeatureGenerator nnFeatureGenerator = new NNFeatureGenerator();
		nnFeatureGenerator.setBrysbaertLocation(brysbaertLocation);
		nnFeatureGenerator.setSentiwordnetLocation(sentiwordnetLocation);
		nnFeatureGenerator.setMrcLocation(mrcLocation);
		nnFeatureGenerator.setMrcPlusLocation(mrcPlusLocation);
		nnFeatureGenerator.setEmbeddingsLocation(googleNewsEmbeddings);
		
		// Indicate the file for which you would like to generate instances.
		String inputFile = "gold_metaphor_novelty.csv";
		
		ArrayList<WordPair> instances = nnFeatureGenerator.getInstances(inputFile);
		HashMap<String, double[]> featureVectors = nnFeatureGenerator.getFeatures(instances);
		nnFeatureGenerator.printFeatures(featureVectors);
	}

}
