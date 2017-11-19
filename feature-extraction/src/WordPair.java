

import java.io.Serializable;

public class WordPair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sentence;
	private String metaphorAnnotation_mainWord;
	private String metaphorAnnotationSubtype_mainWord;
	private String gov;
	private String dep;
	private String mainWord;
	private String relation;
	private String pos_gov;
	private String pos_dep;
	private String lemma_gov;
	private String lemma_dep;
	private String ner_gov;
	private String ner_dep;
	private int wordPositionCoreNLP_gov;
	private int wordPositionCoreNLP_dep;
	private String fragmentID;
	private int sentenceNum;
	private String pairID;
	private double metaphorNovelty;
	
	public WordPair() {
		sentence = "";
		metaphorAnnotation_mainWord = "";
		metaphorAnnotationSubtype_mainWord = "";
		gov = "";
		dep = "";
		relation = "";
		pos_gov = "";
		pos_dep = "";
		lemma_gov = "";
		lemma_dep = "";
		ner_gov = "";
		ner_dep = "";
		fragmentID = "";
		pairID = "";
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getMetaphorAnnotation_mainWord() {
		return metaphorAnnotation_mainWord;
	}
	public void setMetaphorAnnotation_mainWord(String metaphorAnnotation_mainWord) {
		this.metaphorAnnotation_mainWord = metaphorAnnotation_mainWord;
	}
	public String getMetaphorAnnotationSubtype_mainWord() {
		return metaphorAnnotationSubtype_mainWord;
	}
	public void setMetaphorAnnotationSubtype_mainWord(
			String metaphorAnnotationSubtype_mainWord) {
		this.metaphorAnnotationSubtype_mainWord = metaphorAnnotationSubtype_mainWord;
	}
	public String getGov() {
		return gov;
	}
	public void setGov(String gov) {
		this.gov = gov;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getMainWord() {
		return mainWord;
	}
	public void setMainWord(String mainWord) {
		this.mainWord = mainWord;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getPos_gov() {
		return pos_gov;
	}
	public void setPos_gov(String pos_gov) {
		this.pos_gov = pos_gov;
	}
	public String getPos_dep() {
		return pos_dep;
	}
	public void setPos_dep(String pos_dep) {
		this.pos_dep = pos_dep;
	}
	public String getLemma_gov() {
		return lemma_gov;
	}
	public void setLemma_gov(String lemma_gov) {
		this.lemma_gov = lemma_gov;
	}
	public String getLemma_dep() {
		return lemma_dep;
	}
	public void setLemma_dep(String lemma_dep) {
		this.lemma_dep = lemma_dep;
	}
	public String getNer_gov() {
		return ner_gov;
	}
	public void setNer_gov(String ner_gov) {
		this.ner_gov = ner_gov;
	}
	public String getNer_dep() {
		return ner_dep;
	}
	public void setNer_dep(String ner_dep) {
		this.ner_dep = ner_dep;
	}
	public int getWordPositionCoreNLP_gov() {
		return wordPositionCoreNLP_gov;
	}
	public void setWordPositionCoreNLP_gov(int wordPositionCoreNLP_gov) {
		this.wordPositionCoreNLP_gov = wordPositionCoreNLP_gov;
	}
	public int getWordPositionCoreNLP_dep() {
		return wordPositionCoreNLP_dep;
	}
	public void setWordPositionCoreNLP_dep(int wordPositionCoreNLP_dep) {
		this.wordPositionCoreNLP_dep = wordPositionCoreNLP_dep;
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
	public String getPairID() {
		return pairID;
	}
	public void setPairID(String pairID) {
		this.pairID = pairID;
	}
	public double getMetaphorNovelty() {
		return metaphorNovelty;
	}
	public void setMetaphorNovelty(double metaphorNovelty) {
		this.metaphorNovelty = metaphorNovelty;
	}

}
