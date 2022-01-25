package org.eclipse.papyrus.gamification.data.jsonmapper;

public class UmlParameter extends UmlElement {
	private String name;
	private String visibility;
	private String direction;
	private String type;
	private int lowerMultiplicity;
	private int upperMultiplicity;
	
	
	public UmlParameter(String name) {
		this.name = name;
	}
	
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public int getLowerMultiplicity() {
		return lowerMultiplicity;
	}
	public void setLowerMultiplicity(int lowerMultiplicity) {
		this.lowerMultiplicity = lowerMultiplicity;
	}
	public int getUpperMultiplicity() {
		return upperMultiplicity;
	}
	public void setUpperMultiplicity(int upperMultiplicity) {
		this.upperMultiplicity = upperMultiplicity;
	}
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "UmlParameter [name=" + name
				+ ", visibility=" + visibility
				+ ", direction=" + direction
				+ ", type="	+ type
				+ ", lowerMultiplicity=" + lowerMultiplicity
				+ ", upperMultiplicity=" + upperMultiplicity
				+ ",\n getAppliedStereotypes()=" + getAppliedStereotypes()
				+ "]";
	}
}
