/**
 * VUAMCWord.java
 * 
 * Natalie Parde
 * 11/16/2017
 * 
 * Class to hold the information associated with each word from the VUAMC.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class VUAMCWord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sentence;
	private String metaphorAnnotation;
	private String metaphorAnnotationSubtype;
	private String word; // Can be word or phrase.
	private int termPositionVUAMC; // Term position according to VUAMC tokenization.
	private int wordPositionVUAMC; // Word position according to VUAMC if multi-word terms are whitespace-tokenized.
	private int wordPositionCoreNLP; // Word position according to CoreNLP tokenization (in case it is different for some reason).
	private String fragmentID;
	private int sentenceNum;
	private ArrayList<AnnotatedDependency> dependencies;
	private String pos; // From CoreNLP, instead of the original VUAMC parts of speech.
	private String lemma; // Also from CoreNLP.
	private String ner;
	private boolean partOfMultiwordPhrase; // Was this part of a VUAMC term that included multiple words?
	private boolean isInteresting; // Do we want to ask a question about this instance?
	
	public VUAMCWord() {
		setSentence("");
		setMetaphorAnnotation("");
		setMetaphorAnnotationSubtype("");
		setWord("");
		setFragmentID("");
		setDependencies(new ArrayList<AnnotatedDependency>());
		setPos("");
		setLemma("");
		setIsPartOfMultiwordPhrase(false);
		setInteresting(false);
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getMetaphorAnnotation() {
		return metaphorAnnotation;
	}

	public void setMetaphorAnnotation(String metaphorAnnotation) {
		this.metaphorAnnotation = metaphorAnnotation;
	}

	public String getMetaphorAnnotationSubtype() {
		return metaphorAnnotationSubtype;
	}

	public void setMetaphorAnnotationSubtype(String metaphorAnnotationSubtype) {
		this.metaphorAnnotationSubtype = metaphorAnnotationSubtype;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String term) {
		this.word = term;
	}

	public int getTermPositionVUAMC() {
		return termPositionVUAMC;
	}

	public void setTermPositionVUAMC(int termPositionVUAMC) {
		this.termPositionVUAMC = termPositionVUAMC;
	}

	public int getWordPositionVUAMC() {
		return wordPositionVUAMC;
	}

	public void setWordPositionVUAMC(int wordPositionVUAMC) {
		this.wordPositionVUAMC = wordPositionVUAMC;
	}

	public int getWordPositionCoreNLP() {
		return wordPositionCoreNLP;
	}

	public void setWordPositionCoreNLP(int wordPositionCoreNLP) {
		this.wordPositionCoreNLP = wordPositionCoreNLP;
	}

	public String getFragmentID() {
		return fragmentID;
	}

	public void setFragmentID(String fragmentID) {
		this.fragmentID = fragmentID;
	}

	public int getSentenceNum() {
		return sentenceNum;
	}

	public void setSentenceNum(int sentenceNum) {
		this.sentenceNum = sentenceNum;
	}

	public ArrayList<AnnotatedDependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(ArrayList<AnnotatedDependency> dependencies) {
		this.dependencies = dependencies;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getNer() {
		return ner;
	}

	public void setNer(String ner) {
		this.ner = ner;
	}

	public boolean isInteresting() {
		return isInteresting;
	}

	public boolean isPartOfMultiwordPhrase() {
		return partOfMultiwordPhrase;
	}

	public void setIsPartOfMultiwordPhrase(boolean partOfMultiwordPhrase) {
		this.partOfMultiwordPhrase = partOfMultiwordPhrase;
	}

	public void setInteresting(boolean isInteresting) {
		this.isInteresting = isInteresting;
	}

}
