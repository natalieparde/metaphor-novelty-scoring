

import java.io.Serializable;
import java.util.ArrayList;

import edu.stanford.nlp.ling.IndexedWord;

public class Dependency implements Serializable{

	/**
	 * Just generated a random serialVersionUID.
	 */
	private static final long serialVersionUID = 9180250190560476146L;
	private String arg1;
	private String arg2;
	private int arg1_position;
	private int arg2_position;
	private String arg1_pos;
	private String arg2_pos;
	private String arg1_lemma;
	private String arg2_lemma;
	private String dependency;
	private boolean containsMetaphor;
	private ArrayList<Word> componentWords;
	private String sentence_text;
	private String formal_arg1;
	private String formal_arg2;
	private int parserIndex_arg1;
	private int parserIndex_arg2;
	
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	
	public String getArg1() {
		return arg1;
	}
	
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	
	public String getArg2() {
		return arg2;
	}
	
	public void setArg1Position(int arg1_position) {
		this.arg1_position = arg1_position;
	}
	
	public int getArg1Position() {
		return arg1_position;
	}
	
	public void setArg2Position(int arg2_position) {
		this.arg2_position = arg2_position;
	}
	
	public int getArg2Position() {
		return arg2_position;
	}
	
	public void setDependency(String dependency) {
		this.dependency = dependency;
	}
	
	public String getDependency() {
		return dependency;
	}
	
	public void setContainsMetaphor(boolean containsMetaphor) {
		this.containsMetaphor = containsMetaphor;
	}
	
	public boolean getContainsMetaphor() {
		return containsMetaphor;
	}
	
	public void setComponentWords(ArrayList<Word> componentWords) {
		this.componentWords = componentWords;
	}
	
	public ArrayList<Word> getComponentWords() {
		return componentWords;
	}
	
	public void setSentenceText(String sentence_text) {
		this.sentence_text = sentence_text;
	}
	
	public String getSentenceText() {
		return sentence_text;
	}
	
	public void setFormalArg1(String formal_arg1) {
		this.formal_arg1 = formal_arg1;
	}
	
	public String getFormalArg1() {
		return formal_arg1;
	}
	
	public void setFormalArg2(String formal_arg2) {
		this.formal_arg2 = formal_arg2;
	}
	
	public String getFormalArg2() {
		return formal_arg2;
	}
	
	public String getArg1_pos() {
		return arg1_pos;
	}

	public void setArg1_pos(String arg1_pos) {
		this.arg1_pos = arg1_pos;
	}

	public String getArg2_pos() {
		return arg2_pos;
	}

	public void setArg2_pos(String arg2_pos) {
		this.arg2_pos = arg2_pos;
	}

	public String getArg1_lemma() {
		return arg1_lemma;
	}

	public void setArg1_lemma(String arg1_lemma) {
		this.arg1_lemma = arg1_lemma;
	}

	public String getArg2_lemma() {
		return arg2_lemma;
	}

	public void setArg2_lemma(String arg2_lemma) {
		this.arg2_lemma = arg2_lemma;
	}

	public Dependency() {
		componentWords = new ArrayList<Word>();
		arg1 = "";
		arg2 = "";
		dependency = "";
		containsMetaphor = false;
		sentence_text = "";
		formal_arg1 = "";
		formal_arg2 = "";
	}
	
	public Dependency(IndexedWord gov, IndexedWord dep, String reln) {
		componentWords = new ArrayList<Word>();
		containsMetaphor = false;
		sentence_text = "";
		formal_arg1 = "";
		formal_arg2 = "";
		setArg1(gov.word());
		setArg2(dep.word());
		setDependency(reln);
	}

	public int getParserIndex_arg2() {
		return parserIndex_arg2;
	}

	public void setParserIndex_arg2(int parserIndex_arg2) {
		this.parserIndex_arg2 = parserIndex_arg2;
	}

	public int getParserIndex_arg1() {
		return parserIndex_arg1;
	}

	public void setParserIndex_arg1(int parserIndex_arg1) {
		this.parserIndex_arg1 = parserIndex_arg1;
	}
}
