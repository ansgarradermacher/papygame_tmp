package org.eclipse.papyrus.gamification.data.jsonmapper;

public class UmlProperty extends UmlElement {
	private String name;
	private String visibility;
	private int upperMultiplicity;
	private int lowerMultiplicity;
	private String type;
	
	public UmlProperty(String name) {
		this.name = name;
	}
	
	public String getVisiblity() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public int getUpperMultiplicity() {
		return upperMultiplicity;
	}
	public void setUpperMultiplicity(int upperMultiplicity) {
		this.upperMultiplicity = upperMultiplicity;
	}
	public int getLowerMultiplicity() {
		return lowerMultiplicity;
	}
	public void setLowerMultiplicity(int lowerMultiplicity) {
		this.lowerMultiplicity = lowerMultiplicity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "UmlProperty [name=" + name
				+ ", visibility=" + visibility
				+ ", upperMultiplicity=" + upperMultiplicity
				+ ", lowerMultiplicity=" + lowerMultiplicity
				+ ", type=" + type
				+ ",\n getAppliedStereotypes()=" + getAppliedStereotypes()
				+ "]";
	}
}
