

import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable {

	/**
	 *  Just generated a random serialVersionUID.
	 */
	private static final long serialVersionUID = 2568937687841968687L;
	private String sentenceText;
	private String sentence_v2;
	private ArrayList<Word> words;
	private boolean containsMetaphor;
	private String fragmentName;
	private String documentName;
	
	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}
	
	public String getSentenceText() {
		return sentenceText;
	}
	
	public String getSentence_v2() {
		return sentence_v2;
	}

	public void setSentence_v2(String sentence_v2) {
		this.sentence_v2 = sentence_v2;
	}

	public void addWord(Word word) {
		words.add(word);
	}
	
	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	public ArrayList<Word> getWords() {
		return words;
	}
	
	public void setContainsMetaphor(boolean containsMetaphor) {
		this.containsMetaphor = containsMetaphor;
	}
	
	public boolean getContainsMetaphor() {
		return containsMetaphor;
	}
	
	public void setFragmentName(String fragmentName){
		this.fragmentName = fragmentName;
	}
	
	public String getFragmentName() {
		return fragmentName;
	}
	
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	public String getDocumentName() {
		return documentName;
	}
	
	public Sentence() {
		sentenceText = "";
		words = new ArrayList<Word>();
		containsMetaphor = false;
		fragmentName = "";
		documentName = "";
	}

}
