/**
 * AnnotatedDependency.java
 * 
 * Natalie Parde
 * Last Updated: May 15, 2017
 * 
 * Class to hold information associated with a given dependency.
 */

import java.io.Serializable;

public class AnnotatedDependency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gov;
	private String dep;
	private String relation;
	private String govPOS;
	private String depPOS;
	private int govIdx;
	private int depIdx;
	private String govLemma;
	private String depLemma;
	private String govNER;
	private String depNER;
	private double metaphoricity;
	public AnnotatedDependency() {
		gov = "";
		dep = "";
		relation = "";
		govPOS = "";
		depPOS = "";
		govLemma = "";
		depLemma = "";
		metaphoricity = 0.0;
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
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getGovPOS() {
		return govPOS;
	}
	public void setGovPOS(String govPOS) {
		this.govPOS = govPOS;
	}
	public String getDepPOS() {
		return depPOS;
	}
	public void setDepPOS(String depPOS) {
		this.depPOS = depPOS;
	}
	public int getGovIdx() {
		return govIdx;
	}
	public void setGovIdx(int govIdx) {
		this.govIdx = govIdx;
	}
	public int getDepIdx() {
		return depIdx;
	}
	public void setDepIdx(int depIdx) {
		this.depIdx = depIdx;
	}
	public String getGovLemma() {
		return govLemma;
	}
	public void setGovLemma(String govLemma) {
		this.govLemma = govLemma;
	}
	public String getDepLemma() {
		return depLemma;
	}
	public void setDepLemma(String depLemma) {
		this.depLemma = depLemma;
	}
	public String getGovNER() {
		return govNER;
	}
	public void setGovNER(String govNER) {
		this.govNER = govNER;
	}
	public String getDepNER() {
		return depNER;
	}
	public void setDepNER(String depNER) {
		this.depNER = depNER;
	}
	public double getMetaphoricity() {
		return metaphoricity;
	}
	public void setMetaphoricity(double metaphoricity) {
		this.metaphoricity = metaphoricity;
	}

}
