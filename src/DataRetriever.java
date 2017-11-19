/**
 * DataRetriever.java
 * 
 * Natalie Parde
 * 11/16/2017
 * 
 * Retrieve the candidate word pairs from the VUAMC data.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataRetriever {

	private ArrayList<VUAMCWord> full_dataset;
	private ArrayList<String> stopwords;
	private HashMap<Integer, ArrayList<VUAMCWord>> dataFolds;

	public DataRetriever() {
		ArrayList<VUAMCWord> dataset = retrieveFullDataset();
		splitDataIntoFolds(dataset);
		buildStopwordsList(); // We'll need this later.
	}
	
	/**
	 * Constructor for it only the helper functions (i.e., isContentWord) are
	 * needed.
	 * @param justNeedHelperFunctions
	 */
	public DataRetriever(boolean justNeedHelperFunctions) {
		buildStopwordsList();
	}
	
	/**
	 * Return the list of stopwords.
	 * @return
	 */
	public ArrayList<String> getStopwordsList() {
		return stopwords;
	}
	
	/**
	 * Retrieve the full dataset of VUAMC words.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<VUAMCWord> retrieveFullDataset() {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream("resources/vuamc_words.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			full_dataset = (ArrayList<VUAMCWord>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return full_dataset;
	}
	
	/**
	 * Splits the data into five folds based on fragment IDs, and returns a
	 * hashmap indicating which instances are part of each fold.
	 * @param dataset
	 * @return
	 */
	public HashMap<Integer, ArrayList<VUAMCWord>> splitDataIntoFolds(ArrayList<VUAMCWord> dataset) {
		dataFolds = new HashMap<Integer, ArrayList<VUAMCWord>>();
		ArrayList<VUAMCWord> fold1 = new ArrayList<VUAMCWord>();
		ArrayList<VUAMCWord> fold2 = new ArrayList<VUAMCWord>();
		ArrayList<VUAMCWord> fold3 = new ArrayList<VUAMCWord>();
		ArrayList<VUAMCWord> fold4 = new ArrayList<VUAMCWord>();
		ArrayList<VUAMCWord> fold5 = new ArrayList<VUAMCWord>();
		for(VUAMCWord word : dataset) {
			if(word.getFragmentID().equals("a3k-fragment11") || word.getFragmentID().equals("aa3-fragment08")
				|| word.getFragmentID().equals("ahe-fragment03") || word.getFragmentID().equals("ahf-fragment24")
				|| word.getFragmentID().equals("ahf-fragment63") || word.getFragmentID().equals("a8n-fragment19")
				|| word.getFragmentID().equals("a3m-fragment02") || word.getFragmentID().equals("al5-fragment03")
				|| word.getFragmentID().equals("ajf-fragment07") || word.getFragmentID().equals("a1p-fragment01")
				|| word.getFragmentID().equals("a1p-fragment03") || word.getFragmentID().equals("faj-fragment17")
				|| word.getFragmentID().equals("ab9-fragment03") || word.getFragmentID().equals("kcv-fragment42")
				|| word.getFragmentID().equals("kbd-fragment07") || word.getFragmentID().equals("kbd-fragment21")
				|| word.getFragmentID().equals("b1g-fragment02")) {
				fold1.add(word);
			}
			else if(word.getFragmentID().equals("a1n-fragment09") || word.getFragmentID().equals("a1n-fragment18")
					|| word.getFragmentID().equals("ahc-fragment60") || word.getFragmentID().equals("ahc-fragment61")
					|| word.getFragmentID().equals("a36-fragment07") || word.getFragmentID().equals("a1x-fragment03")
					|| word.getFragmentID().equals("a1x-fragment04") || word.getFragmentID().equals("a1x-fragment05")
					|| word.getFragmentID().equals("a8u-fragment14") || word.getFragmentID().equals("a31-fragment03")
					|| word.getFragmentID().equals("a1g-fragment26") || word.getFragmentID().equals("a1g-fragment27")
					|| word.getFragmentID().equals("a7w-fragment01") || word.getFragmentID().equals("c8t-fragment01")
					|| word.getFragmentID().equals("fpb-fragment01") || word.getFragmentID().equals("cb5-fragment02")
					|| word.getFragmentID().equals("kbw-fragment04") || word.getFragmentID().equals("kbw-fragment09")
					|| word.getFragmentID().equals("kbw-fragment11") || word.getFragmentID().equals("kbw-fragment17")
					|| word.getFragmentID().equals("kbw-fragment42") || word.getFragmentID().equals("clp-fragment01")
					|| word.getFragmentID().equals("alp-fragment01") || word.getFragmentID().equals("ea7-fragment03")
					|| word.getFragmentID().equals("b17-fragment02") || word.getFragmentID().equals("ew1-fragment01")) {
					fold2.add(word);
			}
			else if(word.getFragmentID().equals("a4d-fragment02") || word.getFragmentID().equals("a9j-fragment01")
					|| word.getFragmentID().equals("a3c-fragment05") || word.getFragmentID().equals("al2-fragment16")
					|| word.getFragmentID().equals("al2-fragment23") || word.getFragmentID().equals("a80-fragment15")
					|| word.getFragmentID().equals("a39-fragment01") || word.getFragmentID().equals("a2d-fragment05")
					|| word.getFragmentID().equals("a8r-fragment02") || word.getFragmentID().equals("a7y-fragment03")
					|| word.getFragmentID().equals("bpa-fragment14") || word.getFragmentID().equals("bmw-fragment09")
					|| word.getFragmentID().equals("kbp-fragment09") || word.getFragmentID().equals("kbh-fragment01")
					|| word.getFragmentID().equals("kbh-fragment02") || word.getFragmentID().equals("kbh-fragment03")
					|| word.getFragmentID().equals("kbh-fragment04") || word.getFragmentID().equals("kbh-fragment09")
					|| word.getFragmentID().equals("kbh-fragment41") || word.getFragmentID().equals("kcf-fragment14")
					|| word.getFragmentID().equals("clw-fragment01") || word.getFragmentID().equals("cty-fragment03")
					|| word.getFragmentID().equals("a6u-fragment02") || word.getFragmentID().equals("acj-fragment01")) {
					fold3.add(word);
			}
			else if(word.getFragmentID().equals("ahb-fragment51") || word.getFragmentID().equals("a1h-fragment05")
					|| word.getFragmentID().equals("a1h-fragment06") || word.getFragmentID().equals("a1u-fragment04")
					|| word.getFragmentID().equals("a3e-fragment02") || word.getFragmentID().equals("a3e-fragment03")
					|| word.getFragmentID().equals("a7t-fragment01") || word.getFragmentID().equals("a5e-fragment06")
					|| word.getFragmentID().equals("ahl-fragment02") || word.getFragmentID().equals("a38-fragment01")
					|| word.getFragmentID().equals("a8m-fragment02") || word.getFragmentID().equals("ccw-fragment03")
					|| word.getFragmentID().equals("ccw-fragment04") || word.getFragmentID().equals("g0l-fragment01")
					|| word.getFragmentID().equals("kb7-fragment10") || word.getFragmentID().equals("kb7-fragment31")
					|| word.getFragmentID().equals("kb7-fragment45") || word.getFragmentID().equals("kb7-fragment48")
					|| word.getFragmentID().equals("as6-fragment01") || word.getFragmentID().equals("as6-fragment02")
					|| word.getFragmentID().equals("ecv-fragment05") || word.getFragmentID().equals("crs-fragment01")) {
					fold4.add(word);
			}
			else if(word.getFragmentID().equals("a1k-fragment02") || word.getFragmentID().equals("al0-fragment06")
					|| word.getFragmentID().equals("a1f-fragment06") || word.getFragmentID().equals("a1f-fragment07")
					|| word.getFragmentID().equals("a1f-fragment08") || word.getFragmentID().equals("a1f-fragment09")
					|| word.getFragmentID().equals("a1f-fragment10") || word.getFragmentID().equals("a1f-fragment11")
					|| word.getFragmentID().equals("a1f-fragment12") || word.getFragmentID().equals("a3p-fragment09")
					|| word.getFragmentID().equals("a7s-fragment03") || word.getFragmentID().equals("a1e-fragment01")
					|| word.getFragmentID().equals("a98-fragment03") || word.getFragmentID().equals("ahd-fragment06")
					|| word.getFragmentID().equals("a1m-fragment01") || word.getFragmentID().equals("a1l-fragment01")
					|| word.getFragmentID().equals("a1j-fragment33") || word.getFragmentID().equals("a1j-fragment34")
					|| word.getFragmentID().equals("fet-fragment01") || word.getFragmentID().equals("cdb-fragment02")
					|| word.getFragmentID().equals("cdb-fragment04") || word.getFragmentID().equals("ac2-fragment06")
					|| word.getFragmentID().equals("kbc-fragment13") || word.getFragmentID().equals("kcu-fragment02")
					|| word.getFragmentID().equals("kcc-fragment02") || word.getFragmentID().equals("kbj-fragment17")
					|| word.getFragmentID().equals("amm-fragment02") || word.getFragmentID().equals("fef-fragment03")) {
					fold5.add(word);
			}
			else {
				System.out.println("Error!  Fragment: " + word.getFragmentID() + " is not in any of the folds.");
			}
		}
		dataFolds.put(1, fold1);
		dataFolds.put(2, fold2);
		dataFolds.put(3, fold3);
		dataFolds.put(4, fold4);
		dataFolds.put(5, fold5);
		return dataFolds;
	}
	
	/**
	 * Return the instances corresponding to the specified folds.
	 * @param foldNumber
	 * @param dataFolds
	 * @return
	 */
	public ArrayList<VUAMCWord> getFold(int foldNumber, HashMap<Integer, ArrayList<VUAMCWord>> dataFolds) {
		if(dataFolds.containsKey(foldNumber)) {
			return dataFolds.get(foldNumber);
		}
		else {
			System.out.println("This fold doesn't exist!");
			return new ArrayList<VUAMCWord>();
		}
	}
	
	/**
	 * Build a list of stopwords from the list provided at 
	 * http://www.lextek.com/manuals/onix/stopwords1.html, plus some common
	 * contraction endings.
	 */
	public void buildStopwordsList() {
		stopwords = new ArrayList<String>();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("resources/lextek_stopwords.txt"), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				stopwords.add(line.trim().toLowerCase());
			}
			bufferedReader.close();
			
			stopwords.add("n't");
			stopwords.add("'ll");
			stopwords.add("'s");
			stopwords.add("'ve");
			stopwords.add("'d");
			stopwords.add("'m");
			stopwords.add("'re");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns true if the input pos-tag is a content word (no proper nouns!),
	 * or false otherwise.
	 * @param pos
	 * @return
	 */
	public boolean isContentWord(String pos) {
		if(pos == null) {
		//	System.out.println("Null POS!");
			return false;
		}
		if(pos.equals("NN") || pos.equals("NNS")) { // Noun
			return true;
		}
		if(pos.equals("VB") || pos.equals("VBD") || pos.equals("VBG") || pos.equals("VBN") || pos.equals("VBP") || pos.equals("VBZ")) { // Verb
			return true;
		}
		if(pos.equals("JJ") || pos.equals("JJR") || pos.equals("JJS")) { // Adjective
			return true;
		}
		if(pos.equals("RB") || pos.equals("RBR") || pos.equals("RBS")) { // Adverb
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns true if the input lemma is one of the specified auxiliaries, or
	 * false otherwise.
	 * @param lemma
	 * @return
	 */
	public boolean isAuxiliary(String lemma) {
		if(lemma.equals("be") || lemma.equals("can") || lemma.equals("could")
				|| lemma.equals("do") || lemma.equals("have") || lemma.equals("may")
				|| lemma.equals("might") || lemma.equals("must") || lemma.equals("need")
				|| lemma.equals("ought") || lemma.equals("shall") || lemma.equals("should")
				|| lemma.equals("will") || lemma.equals("would")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Return true if the relation is one of the permitted relations, and false
	 * otherwise.
	 * @param relation
	 * @return
	 */
	public boolean isValidRelation(String relation) {
		if(relation.equals("nsubj") || relation.equals("nsubjpass") || relation.equals("dobj")
				|| relation.equals("iobj") || relation.equals("csubj") || relation.equals("csubjpass")
				|| relation.equals("xcomp") || relation.equals("appos") || relation.equals("nmod")
				|| relation.equals("acl") || relation.equals("amod") || relation.equals("advcl")
				|| relation.equals("advmod") || relation.equals("compound") || relation.equals("dep")
				|| relation.equals("subj-obj") || relation.equals("reln-obj") || relation.equals("reln-subj")) {
			return true;
		}
		else if(relation.startsWith("nsubj:") || relation.startsWith("nsubjpass:") || relation.startsWith("dobj:")
				|| relation.startsWith("iobj:") || relation.startsWith("csubj:") || relation.startsWith("csubjpass:")
				|| relation.startsWith("xcomp:") || relation.startsWith("appos:") || relation.startsWith("nmod:")
				|| relation.startsWith("acl:") || relation.startsWith("amod:") || relation.startsWith("advcl:")
				|| relation.startsWith("advmod:") || relation.startsWith("compound:") || relation.startsWith("dep:")) {
			return true;
		}
		else {
		//	System.out.println("Not allowing relation: " + relation);
			return false;
		}
	}
	
	/**
	 * Check to make sure the word contains at least one alphabet character.
	 * @param word
	 * @return
	 */
	public boolean containsAlphabetCharacter(String word) {
		if(word.matches(".*[a-zA-Z]+.*")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Creates a list of syntactically related candidate pairwise metaphors, 
	 * stored as WordPair objects, from the list of input VUAMCWords.  Criteria 
	 * for inclusion as a candidate include:
	 * - At least one of the words must be a content word (noun, verb, adj., or
	 *   adv.), and the other word must be either a content word or a personal
	 *   pronoun.
	 * - Neither of the words can be a stopword.
	 * - Neither of the words' lemmas can be the auxiliaries: be, can, could,
	 *   do, have, may, might, must, need, ought, shall, should, will, or would.
	 * - Neither of the words can be a proper noun.
	 * - The words must be related using one of the following universal
	 *   dependencies: nsubj, nsubjpass, dobj, iobj, csubj, csubjpass, xcomp,
	 *   appos, nmod, acl, amod, nmod, advcl, advmod, compound, dep.
	 * - The words must contain at least one alphabet character (a-zA-Z).
	 * @param dataset
	 * @return
	 */
	public ArrayList<WordPair> getPairCandidates(ArrayList<VUAMCWord> dataset) {
		ArrayList<WordPair> pairs = new ArrayList<WordPair>();
		ArrayList<String> pairIndex = new ArrayList<String>();
		
		// Loop through each word.
		for(VUAMCWord word : dataset) {
			// Check the "main" word first.
			if(isContentWord(word.getPos())) { // First make sure this word is a content word.
				if(!isAuxiliary(word.getLemma().toLowerCase())) { // Next make sure this word is not an auxiliary.
					if(!stopwords.contains(word.getWord().toLowerCase())) { // Then make sure the word is not a stopword.
						
						// Stage 2: Now examine the possible pairings for this word.
						ArrayList<AnnotatedDependency> dependencies = word.getDependencies();
						
						for(AnnotatedDependency dependency : dependencies) {
							if((isContentWord(dependency.getGovPOS()) && isContentWord(dependency.getDepPOS()))
									|| (isContentWord(dependency.getGovPOS()) && dependency.getDepPOS() != null && dependency.getDepPOS().equals("PRP"))
									|| (dependency.getGovPOS() != null && dependency.getGovPOS().equals("PRP") && isContentWord(dependency.getDepPOS()))) { // Either both are content words, or one is a content word and one is a personal pronoun.
								if(!isAuxiliary(dependency.getGovLemma().toLowerCase()) && !isAuxiliary(dependency.getDepLemma().toLowerCase())) { // Make sure neither is an auxiliary.
									if(!stopwords.contains(dependency.getGov().toLowerCase()) && !stopwords.contains(dependency.getDep().toLowerCase())) { // Make sure neither is a stopword.
										if(containsAlphabetCharacter(dependency.getGov()) && containsAlphabetCharacter(dependency.getDep())) { // Make sure neither word is only punctuation (should've been caught earlier anyway, but sometimes the pos-tagger messes up).
											if(isValidRelation(dependency.getRelation())) { // Passed the last test!  We have a candidate!
												String possibleIndex1 = word.getFragmentID() + "_" + word.getSentenceNum() + "_" + word.getWord() + "_" + dependency.getGovIdx() + "_" + dependency.getDepIdx();
												String possibleIndex2 = word.getFragmentID() + "_" + word.getSentenceNum() + "_" + word.getWord() + "_" + dependency.getDepIdx() + "_" + dependency.getGovIdx();

												if(!pairIndex.contains(possibleIndex1) && !pairIndex.contains(possibleIndex2)) { // Let's just make sure we haven't already added this exact pair.
													WordPair pair = new WordPair();
													pair.setGov(dependency.getGov());
													pair.setDep(dependency.getDep());
													pair.setMainWord(word.getWord());
													
													pair.setFragmentID(word.getFragmentID());
													pair.setSentence(word.getSentence());
													pair.setSentenceNum(word.getSentenceNum());
													pair.setWordPositionCoreNLP_gov(dependency.getGovIdx());
													pair.setWordPositionCoreNLP_dep(dependency.getDepIdx());
													
													pair.setLemma_gov(dependency.getGovLemma());
													pair.setLemma_dep(dependency.getDepLemma());
													pair.setNer_gov(dependency.getGovNER());
													pair.setNer_dep(dependency.getDepNER());
													pair.setPos_gov(dependency.getGovPOS());
													pair.setPos_dep(dependency.getDepPOS());
													pair.setRelation(dependency.getRelation());
													
													pair.setMetaphorAnnotation_mainWord(word.getMetaphorAnnotation());
													pair.setMetaphorAnnotationSubtype_mainWord(word.getMetaphorAnnotationSubtype());
													pair.setMetaphorNovelty(dependency.getMetaphoricity());
													
													pairs.add(pair);
													pairIndex.add(pair.getFragmentID() + "_" + pair.getSentenceNum() + "_" + word.getWord() + "_" + dependency.getGovIdx() + "_" + dependency.getDepIdx());
												}
											}
										} // End if contains alphabet character.
									} // End stopword check.
								} // End auxiliary check.
							} // End content word check.
						} // End for loop through annotated dependencies.
					} // End stopword check (for the focus word).
				} // End auxiliary check (for the focus word).
			} // End content word check (for the focus word).
		} // End loop through all VUAMC words in the dataset.
		return pairs;
	}
}
