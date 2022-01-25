package org.eclipse.papyrus.gamification.data.jsonmapper;

import java.util.ArrayList;
import java.util.List;

public class UmlOperation extends UmlElement {
	private String name;
	private String visibility;
	private int numberOfParameters;
	private List<UmlParameter> parameters;
	
	public UmlOperation(String name) {
		this.name = name;
		this.parameters = new ArrayList<>();
		this.numberOfParameters = 0;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public List<UmlParameter> getParameters() {
		return parameters;
	}

	public int getNumberOfParameters() {
		return numberOfParameters;
	}
	
	public void addParameter(UmlParameter parameter) {
		parameters.add(parameter);
		numberOfParameters++;
	}

	@Override
	public String toString() {
		return "UmlOperation [name=" + name
				+ ", visibility=" + visibility
				+ ", numberOfParameters=" + numberOfParameters
				+ ", parameters=" + parameters
				+ ", getNumberOfParameters()=" + getNumberOfParameters() 
				+ ",\n getAppliedStereotypes()=" + getAppliedStereotypes()
				+ "]";
	}
	
	
	
}
