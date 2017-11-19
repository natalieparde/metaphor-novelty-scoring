

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {

	/**
	 * Just generated a random serialVersionUID.
	 */
	private static final long serialVersionUID = 1086481495507962916L;
	private String text;
	private boolean isMetaphoric;
	private String lemma;
	private String pos;
	private String function;
	private String subtype;
	private int sentencePosition;
	private int sentenceNumber;
	private String arg;
	private String fragmentName;
	private ArrayList<Dependency> dependencies;
	private FeatureVector featureVector;
	private String sentence;
	private String sentence_v2;
	private ArrayList<Word> allContentWordsInSentence;
	private int contentWordDependencies;
	private int parserIndex;

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setIsMetaphor(boolean isMetaphoric) {
		this.isMetaphoric = isMetaphoric;
	}
	
	public boolean getIsMetaphor() {
		return isMetaphoric;
	}
	
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	
	public String getLemma() {
		return lemma;
	}
	
	public void setPOS(String pos) {
		this.pos = pos;
	}
	
	public String getPOS() {
		return pos;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	public String getFunction() {
		return function;
	}
	
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	
	public String getSubtype() {
		return subtype;
	}
	
	public void setSentencePosition(int sentencePosition) {
		this.sentencePosition = sentencePosition;
	}
	
	public int getSentencePosition() {
		return sentencePosition;
	}
	
	public int getSentenceNumber() {
		return sentenceNumber;
	}

	public void setSentenceNumber(int sentenceNumber) {
		this.sentenceNumber = sentenceNumber;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}
	
	public String getArg() {
		return arg;
	}
	
	public void setFragmentName(String fragmentName){
		this.fragmentName = fragmentName;
	}
	
	public String getFragmentName() {
		return fragmentName;
	}
	
	public ArrayList<Dependency> getDependencies() {
		return dependencies;
	}
	
	public void setDependencies(ArrayList<Dependency> dependencies) {
		this.dependencies = dependencies;
	}
	
	public void addDependency(Dependency dependency) {
		dependencies.add(dependency);
	}
	
	public FeatureVector getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(FeatureVector featureVector) {
		this.featureVector = featureVector;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence_v2() {
		return sentence_v2;
	}

	public void setSentence_v2(String sentence_v2) {
		this.sentence_v2 = sentence_v2;
	}

	public ArrayList<Word> getAllContentWordsInSentence() {
		return allContentWordsInSentence;
	}

	public void setAllContentWordsInSentence(ArrayList<Word> allWordsInSentence) {
		this.allContentWordsInSentence = allWordsInSentence;
	}

	public int getContentWordDependencies() {
		return contentWordDependencies;
	}

	public void setContentWordDependencies(int contentWordDependencies) {
		this.contentWordDependencies = contentWordDependencies;
	}

	public int getParserIndex() {
		return parserIndex;
	}

	public void setParserIndex(int parserIndex) {
		this.parserIndex = parserIndex;
	}

	public Word() {
		text = "";
		isMetaphoric = false;
		lemma = "";
		pos = "";
		function = "";
		subtype = "";
		arg = "";
		fragmentName = "";
		dependencies = new ArrayList<Dependency>();
		featureVector = new FeatureVector();
		allContentWordsInSentence = new ArrayList<Word>();
	}

}
