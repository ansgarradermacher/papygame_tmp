package org.eclipse.papyrus.gamification.data.jsonmapper;

public class UmlRelationship {
	private String label;
	private String source;
	private String target;
	private String type;
	private Integer sourceLowerMultiplicity;
	private Integer sourceUpperMultiplicity;
	private Integer targetLowerMultiplicity;
	private Integer targetUpperMultiplicity;
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSourceLowerMultiplicity() {
		return sourceLowerMultiplicity;
	}
	public void setSourceLowerMultiplicity(int sourceLowerMultiplicity) {
		this.sourceLowerMultiplicity = sourceLowerMultiplicity;
	}
	public int getSourceUpperMultiplicity() {
		return sourceUpperMultiplicity;
	}
	public void setSourceUpperMultiplicity(int sourceUpperMultiplicity) {
		this.sourceUpperMultiplicity = sourceUpperMultiplicity;
	}
	public int getTargetLowerMultiplicity() {
		return targetLowerMultiplicity;
	}
	public void setTargetLowerMultiplicity(int targetLowerMultiplicity) {
		this.targetLowerMultiplicity = targetLowerMultiplicity;
	}
	public int getTargetUpperMultiplicity() {
		return targetUpperMultiplicity;
	}
	public void setTargetUpperMultiplicity(int targetUpperMultiplicity) {
		this.targetUpperMultiplicity = targetUpperMultiplicity;
	}
	@Override
	public String toString() {
		return "UmlRelationship [label=" + label + ", source=" + source + ", target=" + target + ", type=" + type
				+ ", sourceLowerMultiplicity=" + sourceLowerMultiplicity + ", sourceUpperMultiplicity="
				+ sourceUpperMultiplicity + ", targetLowerMultiplicity=" + targetLowerMultiplicity
				+ ", targetUpperMultiplicity=" + targetUpperMultiplicity + "]";
	}

	
	
}
