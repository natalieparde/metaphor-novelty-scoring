

import java.io.Serializable;

public class Instance implements Serializable{
	
	/**
	 * Just generated a random serialVersionUID.
	 */
	private static final long serialVersionUID = -2655410475562849873L;
	private String sentence;
	private int index;
	private Word candidate;
	private String candidate_assignment;
	private String actual_class;
	
	public Instance(Word candidate) {
		sentence = candidate.getDependencies().get(0).getSentenceText();
		this.candidate = candidate;
		candidate_assignment = "";
		actual_class = "";
	}
	
	public String getSentence() {
		return sentence;
	}
	
	public void setCandidateAssignment(String candidate_assignment) {
		this.candidate_assignment = candidate_assignment;
	}
	
	public void setActualClass(String actual_class) {
		this.actual_class = actual_class;
	}
	
	public String getCandidateAssignment() {
		return candidate_assignment;
	}
	
	public String getActualClass() {
		return actual_class;
	}
	
	public Word getCandidate() {
		return candidate;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}
