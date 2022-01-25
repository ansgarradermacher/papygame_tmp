package org.eclipse.papyrus.gamification.data.jsonmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UmlClassDiagram {

	private List<UmlClass> classes;
	private Map<String, List<UmlRelationship>> relationships;
	private StatCounter stats;
	
	public UmlClassDiagram() {
		this.classes = new ArrayList();
		this.relationships = new HashMap<>();
		this.stats = new StatCounter();
	}
	public void addClass(UmlClass umlClass) {
		classes.add(umlClass);
		stats.numberOfClasses++;
		stats.numberOfAbstractClasses += umlClass.isAbstract() ? 1 : 0;
		stats.numberOfAttributes += umlClass.getNumberOfProperties();
		stats.numberOfOperations += umlClass.getNumberOfOperations();
	}
	
	public void addRelationship(UmlRelationship umlRelationship) {
		relationships.computeIfAbsent(umlRelationship.getType(), new Function<String, List<UmlRelationship>>(){
			@Override
			public List<UmlRelationship> apply(String t) {
				return new ArrayList<>();
			}
		});
		relationships.get(umlRelationship.getType()).add(umlRelationship);
		stats.numberOfRelationships++;
	}
}
