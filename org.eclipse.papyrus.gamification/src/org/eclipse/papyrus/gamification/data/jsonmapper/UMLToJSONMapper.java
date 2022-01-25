package org.eclipse.papyrus.gamification.data.jsonmapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.internal.impl.DependencyImpl;
import org.eclipse.uml2.uml.internal.impl.InterfaceRealizationImpl;
import org.eclipse.uml2.uml.internal.impl.UsageImpl;


public class UMLToJSONMapper {

	public static Set<Element> retrieveElements(Element e) {
		Set<Element> elementSet = new HashSet<>();
		List<Element> ownedElementsList = e.getOwnedElements();
		elementSet.addAll(ownedElementsList);
		for (Element newE : ownedElementsList) {
			elementSet.addAll(retrieveElements(newE));
		}
		return elementSet;
	}

	public static UmlClassDiagram map(Model m) {
		UmlClassDiagram classDiagram = new UmlClassDiagram();

		HashMap<String, Integer> counterMap = new HashMap();

		Set<Relationship> relationSet = new HashSet<>();
		HashMap<Relationship, String> associationTypeMap = new HashMap<>();
		HashMap<Relationship, Boolean> isDirectedMap = new HashMap<>();
		HashMap<Relationship, HashMap<String, String>> multiplicityMap = new HashMap<>();

		for (Element e : retrieveElements(m)) {
			if (e instanceof Class) {
				classDiagram.addClass(mapClass(e));
				// System.out.println(mapClass(e).toString());
			}
			if (e instanceof Relationship) {
				relationSet.add((Relationship) e);
			}
			if (e instanceof Property) {
				computeRelationInformation(relationSet, associationTypeMap, isDirectedMap, multiplicityMap, e);
			}
		}

		// Now that information is collected for relationships,
		// Let's map them
		for (Relationship r : relationSet) {
			if (r instanceof Relationship) {
				UmlRelationship r2 = mapRelationship(r, associationTypeMap, multiplicityMap, isDirectedMap);
				if (r2 != null) {
					classDiagram.addRelationship(r2);
					// System.out.println("---> " + r2.toString());
				} else {
					System.out.println("Could not map relationship: " + r2);
				}
			}
		}

		return classDiagram;
		// System.out.println((new Gson()).toJson(classDiagram));
	}

	private static void computeRelationInformation(Set<Relationship> relationSet,
			HashMap<Relationship, String> associationTypeMap, HashMap<Relationship, Boolean> isDirectedMap,
			HashMap<Relationship, HashMap<String, String>> multiplicityMap, Element e) {
		Property p = (Property) e;

		// This is a relationship, not an attribute in a Class
		if (p.getRelationships().size() > 0) {
			relationSet.addAll(p.getRelationships());
			for (Relationship r : p.getRelationships()) {
				if (r instanceof Association) {
					Association association = (Association) r;

					multiplicityMap.computeIfAbsent(r, new Function<Relationship, HashMap<String, String>>() {
						@Override
						public HashMap<String, String> apply(Relationship t) {
							return new HashMap<>();
						}
					});

					Class owningClass = null;

					for (Property relationProperty : association.getAllAttributes()) {

						// If owner is class then it is the source
						if (relationProperty.getOwner() instanceof Class) {
							owningClass = (Class) relationProperty.getOwner();

							multiplicityMap.get(r).computeIfAbsent(owningClass.getName(),
									new Function<String, String>() {
										@Override
										public String apply(String t) {
											return relationProperty.getLower() + ";"
													+ relationProperty.getUpper();
										}
									});
						}
					}

					// Now find the target class
					for (Property relationProperty : association.getAllAttributes()) {
						if (relationProperty.getOwner() instanceof Association) {
							for (Element associationRelated : association.getRelatedElements()) {
								if (!associationRelated.equals(owningClass)) {
									Class targetClass = (Class) associationRelated;
									multiplicityMap.get(r).computeIfAbsent(targetClass.getName(),
											new Function<String, String>() {
												@Override
												public String apply(String t) {
													return relationProperty.getLower() + ";"
															+ relationProperty.getUpper();
												}
											});
								}
							}
						}

					}

					// Identify if relation is directed
					isDirectedMap.computeIfAbsent(r, new Function<Relationship, Boolean>() {
						@Override
						public Boolean apply(Relationship t) {
							boolean isDirected = false;
							for (Property prop : association.getAllAttributes()) {
								isDirected = isDirected || prop.isNavigable();
								System.out.println("%% " + prop.isNavigable());

							}
							return isDirected;
						}
					});

					// Identify if association has an aggregation type;
					associationTypeMap.computeIfAbsent(r, new Function<Relationship, String>() {
						@Override
						public String apply(Relationship t) {
							String aggregation = "none";
							for (Property prop : association.getAllAttributes()) {
								String currentAggregation = prop.getAggregation().getLiteral().toString();
								System.out.println("%% " + currentAggregation);
								if (!("none".equals(currentAggregation))) {
									aggregation = currentAggregation;
								}
							}
							return aggregation;
						}
					});

				}

			}
		}
	}

	private static UmlClass mapClass(Element classElement) {
		UmlClass umlClass = new UmlClass(((Class) classElement).getName());
		umlClass.setAbstract(((Class) classElement).isAbstract());
		umlClass.setStereotypeApplications(classElement);
		for (Element element : classElement.getOwnedElements()) {
			if (element instanceof Property) {
				umlClass.addProperty(mapProperty(element));
			}
			if (element instanceof Operation) {
				umlClass.addOperation(mapOperation(element));
			}
		}
		return umlClass;
	}

	private static UmlProperty mapProperty(Element element) {
		Property propertyElement = (Property) element;
		UmlProperty umlProperty = new UmlProperty(propertyElement.getName());
		umlProperty.setStereotypeApplications(element);
		if (propertyElement.getType() == null) {
			umlProperty.setType("null");
		} else {
			umlProperty.setType(propertyElement.getType().getName());
		}
		umlProperty.setVisibility(propertyElement.getVisibility().toString());
		umlProperty.setUpperMultiplicity(propertyElement.getUpper());
		umlProperty.setLowerMultiplicity(propertyElement.getLower());
		return umlProperty;
	}

	private static UmlOperation mapOperation(Element element) {
		Operation operationElement = (Operation) element;
		UmlOperation umlOperation = new UmlOperation(operationElement.getName());
		umlOperation.setStereotypeApplications(element);
		umlOperation.setVisibility(operationElement.getVisibility().toString());

		for (Element e : operationElement.allOwnedElements()) {
			if (e instanceof Parameter) {
				umlOperation.addParameter(mapParameter(e));
			}
		}
		return umlOperation;
	}

	private static UmlParameter mapParameter(Element element) {
		Parameter parameterElement = (Parameter) element;
		UmlParameter umlParameter = new UmlParameter(parameterElement.getName());
		umlParameter.setStereotypeApplications(element);
		if (parameterElement.getType() == null) {
			umlParameter.setType("null");
		} else {
			umlParameter.setType(parameterElement.getType().getName());
		}
		umlParameter.setVisibility(parameterElement.getVisibility().toString());
		umlParameter.setUpperMultiplicity(parameterElement.getUpper());
		umlParameter.setLowerMultiplicity(parameterElement.getLower());
		umlParameter.setDirection(parameterElement.getDirection().toString());
		return umlParameter;
	}

	/*

	 */
	private static UmlRelationship mapRelationship(Relationship relationship,
			HashMap<Relationship, String> aggregationMap,
			HashMap<Relationship, HashMap<String, String>> multiplicityMap,
			HashMap<Relationship, Boolean> isDirectedMap) {

		UmlRelationType relationType = getRelationType(relationship, aggregationMap.get(relationship),
				isDirectedMap.get(relationship));

		if (relationType == UmlRelationType.UNKNOWN) {
			return null;
		}

		UmlRelationship umlRelationship = new UmlRelationship();

		if (relationship instanceof DirectedRelationship) {

			DirectedRelationship directedRelationship = (DirectedRelationship) relationship;
			List<Element> sourcesList = directedRelationship.getSources();
			List<Element> targetsList = directedRelationship.getTargets();
			for (Element source : sourcesList) {
				for (Element target : targetsList) {

					if (source instanceof Class) {
						if (target instanceof Class) {
							Class sourceClass = (Class) source;
							Class targetClass = (Class) target;
							umlRelationship.setLabel(getRelationName(directedRelationship));
							umlRelationship.setSource(sourceClass.getName());
							umlRelationship.setTarget(targetClass.getName());
							umlRelationship.setType(relationType.toString());
						}
					}
				}
			}
		} else {
			List<Element> relatedElements = relationship.getRelatedElements();
			if (relatedElements.size() == 2) {
				if (relatedElements.get(0) instanceof Class) {
					if (relatedElements.get(1) instanceof Class) {

						Class sourceClass = (Class) relatedElements.get(0);
						Class targetClass = (Class) relatedElements.get(1);
						umlRelationship.setLabel(getRelationName(relationship));
						umlRelationship.setSource(sourceClass.getName());
						umlRelationship.setTarget(targetClass.getName());
						umlRelationship.setType(relationType.toString());
					}
				}
			} else {
				umlRelationship = null;
				System.out.println("Could not map relationship: " + relationship);
			}
		}

		if (multiplicityMap.containsKey(relationship)) {
			String sourceMultiplicity = multiplicityMap.get(relationship).get(umlRelationship.getSource());
			int sourceLower = Integer.valueOf(sourceMultiplicity.split(";")[0]);
			int sourceUpper = Integer.valueOf(sourceMultiplicity.split(";")[1]);
			String targetMultiplicity = multiplicityMap.get(relationship).get(umlRelationship.getTarget());
			int targetLower = Integer.valueOf(targetMultiplicity.split(";")[0]);
			int targetUpper = Integer.valueOf(targetMultiplicity.split(";")[1]);

			umlRelationship.setSourceLowerMultiplicity(sourceLower);
			umlRelationship.setSourceUpperMultiplicity(sourceUpper);
			umlRelationship.setTargetLowerMultiplicity(targetLower);
			umlRelationship.setTargetUpperMultiplicity(targetUpper);
		}

		return umlRelationship;
	}

	private static UmlRelationType getRelationType(Relationship relationship, String aggregationValue,
			Boolean isDirected) {

		if (aggregationValue != null) {
			if ((isDirected != null) && isDirected) {
				if ("none".equals(aggregationValue)) {
					return UmlRelationType.DIRECTED_ASSOCIATION;
				}
				if (("composite").equals(aggregationValue)) {
					return UmlRelationType.COMPOSITE_ASSOCIATION;
				}
				if (("shared").equals(aggregationValue)) {
					return UmlRelationType.SHARED_ASSOCIATION;
				}
			} else {
				if (relationship instanceof Association) {
					return UmlRelationType.UNDIRECTED_ASSOCIATION;
				}
			}
		}

		if (relationship instanceof Generalization) {
			return UmlRelationType.GENERALIZATION;
		}
		if (relationship instanceof InterfaceRealizationImpl) {
			return UmlRelationType.INTERFACE_REALIZATION;
		}
		if (relationship instanceof UsageImpl) {
			return UmlRelationType.USAGE;
		}
		if (relationship instanceof DependencyImpl) {
			return UmlRelationType.DEPENDENCY;
		}
		return UmlRelationType.UNKNOWN;
	}

	private static String getRelationName(Relationship relationship) {
		if (relationship instanceof Generalization) {
			return null;
		}
		if (relationship instanceof InterfaceRealizationImpl) {
			InterfaceRealizationImpl i = (InterfaceRealizationImpl) relationship;
			return i.getName();
		}
		if (relationship instanceof UsageImpl) {
			UsageImpl u = (UsageImpl) relationship;
			return u.getName();
		}
		if (relationship instanceof DependencyImpl) {
			DependencyImpl d = (DependencyImpl) relationship;
			return d.getName();
		}
		return null;
	}

}
