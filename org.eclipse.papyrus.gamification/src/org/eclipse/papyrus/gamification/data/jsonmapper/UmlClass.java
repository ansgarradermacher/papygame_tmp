package org.eclipse.papyrus.gamification.data.jsonmapper;

import java.util.ArrayList;
import java.util.List;

public class UmlClass extends UmlElement {
	private String name;
	private boolean isAbstract;
	private int numberOfProperties;
	private int numberOfOperations;
	
	private List<UmlProperty> properties;
	private List<UmlOperation> operations;
	
	
	public UmlClass(String name) {
		this.name = name;
		this.numberOfOperations = 0;
		this.numberOfProperties = 0;
		this.properties = new ArrayList<>();
		this.operations = new ArrayList<>();
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	public int getNumberOfProperties() {
		return numberOfProperties;
	}

	public int getNumberOfOperations() {
		return numberOfOperations;
	}

	public List<UmlProperty> getProperties() {
		return properties;
	}

	public List<UmlOperation> getOperations() {
		return operations;
	}

	public String getName() {
		return name;
	}

	public void addOperation(UmlOperation operation) {
		operations.add(operation);
		numberOfOperations++;
	}
	
	public void addProperty(UmlProperty property) {
		properties.add(property);
		numberOfProperties++;
	}

	@Override
	public String toString() {
		return "UmlClass [name=" + name
				+ ",\n isAbstract=" + isAbstract
				+ ",\n numberOfProperties=" + numberOfProperties
				+ ",\n numberOfOperations=" + numberOfOperations
				+ ",\n properties=" + properties
				+ ",\n operations=" + operations
				+ ",\n getNumberOfProperties()=" + getNumberOfProperties()
				+ ",\n getNumberOfOperations()=" + getNumberOfOperations()
				+ ",\n getAppliedStereotypes()=" + getAppliedStereotypes()
				+ "]";
	}

	
	
}
