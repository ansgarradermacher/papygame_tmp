/*****************************************************************************
 * Copyright (c) 2020 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.games.oyo;

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAssociation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAssociationEnd;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAttribute;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClass;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusGeneralization;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.AssociationEndMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.AssociationMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.AttributeMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassMask;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.OperationMask;

/**
 * @author lepallec
 *
 */
public class DiagramComparator {

	private static final boolean SHOULD_LOG = true;
	PapyrusClassDiagram originalClassDiagram;
	PapyrusClassDiagram playerClassDiagram;

	public DiagramComparator(PapyrusClassDiagram originalClassDiagram, PapyrusClassDiagram playerClassDiagram) {
		super();
		this.originalClassDiagram = originalClassDiagram;
		this.playerClassDiagram = playerClassDiagram;
	}

	public boolean compareDiagram() {
		boolean isError = false;
		boolean log = SHOULD_LOG;

		for (PapyrusClass originalClass : originalClassDiagram.getClasses()) {
			System.out.println(log ? "Assessing class : " + originalClass.getName() : "");
			isError = isError || assessClass(originalClass);
			if (isError) {
				return isError;
			}
		}

		for (PapyrusAssociation originalAssociation : originalClassDiagram.getAssociations()) {
			System.out.println(log ? "Assessing association : " + originalAssociation.getName() : "");
			isError = isError || assessAssociation(originalAssociation);
			if (isError) {
				return isError;
			}
		}

		for (PapyrusGeneralization originalGeneralization : originalClassDiagram.getGeneralizations()) {
			System.out.println(log ? "Assessing generalization : " + originalGeneralization.getName() : "");
			System.out.println(log ? "Assessing generalization (full) : " + originalGeneralization.toString() : "");
			isError = isError || assessGeneralization(originalGeneralization);
			if (isError) {
				return isError;
			}
		}

		return false;
	}

	private boolean assessClass(PapyrusClass originalClass) {
		boolean isError = false;

		boolean log = SHOULD_LOG;

		ClassMask classMask = new ClassMask(originalClass);

		String className = classMask.getFinalName();
		System.out.println(log ? "Class unmasked name : " + className : "");


		PapyrusClass correspondingClass = findClass(classMask.getFinalName());

		if (correspondingClass != null) {

			// Check abstract
			if (classMask.isAbstractMentioned()) {
				if (originalClass.isAbstract() != correspondingClass.isAbstract()) {
					System.out.println(log ? "Class abstract different" : "");
					return true;
				}
			}

			// Check attributes
			for (PapyrusAttribute originalAttribute : originalClass.getAttributes()) {
				System.out.println(log ? "Assessing attribute : " + originalAttribute.getName() : "");
				isError = isError || assessClassAttribute(originalAttribute, correspondingClass, originalClass);
			}

			// Check operations
			for (PapyrusOperation originalOperation : originalClass.getOperations()) {
				System.out.println(log ? "Assessing operation : " + originalOperation.getName() : "");
				isError = isError || assessClassOperation(originalOperation, correspondingClass, originalClass);
			}
		} else {
			isError = true;
			System.out.println(log ? "Class does not exist in player model" : "");
		}

		System.out.println(log ? "---> Class is correct" : "");

		return isError;
	}

	private boolean assessAssociation(PapyrusAssociation papyrusAssociation) {
		boolean log = SHOULD_LOG;

		String source = getUnmaskedName(papyrusAssociation.getSourceEnd().getType().getName());
		String target = getUnmaskedName(papyrusAssociation.getTargetEnd().getType().getName());

		PapyrusAssociation correspondingAssociation = findAssociation(source, target);

		if (correspondingAssociation == null) {
			System.out.println(log ? "Looking for reversed association model" : "");
			correspondingAssociation = findAssociation(target, source);
		}

		if (correspondingAssociation == null) {
			System.out.println(log ? "Association not exist in player model" : "");
			return true;
		}

		AssociationMask associationMask = new AssociationMask(papyrusAssociation);
		String associationName = associationMask.getFinalName();

		if (associationMask.isNameMentioned()) {
			if (!associationName.equals(correspondingAssociation.getName())) {
				System.out.println(log ? "Association name is different " : "");
				return true;
			}
		}

		PapyrusAssociationEnd[] ends = { papyrusAssociation.getSourceEnd(), papyrusAssociation.getTargetEnd() };
		PapyrusAssociationEnd[] newEnds = { correspondingAssociation.getSourceEnd(), correspondingAssociation.getTargetEnd() };

		if (correspondingAssociation.getSourceEnd() == null) {
			return true;
		}
		if (correspondingAssociation.getTargetEnd() == null) {
			return true;
		}

		for (int i = 0; i < ends.length; i++) {

			PapyrusAssociationEnd end = ends[i];
			PapyrusAssociationEnd newEnd = newEnds[i];

			System.out.println(log ? "Analyzing end : " + end.getName() : "");

			AssociationEndMask mask = new AssociationEndMask(end);
			String associationEndName = mask.getFinalName();

			System.out.println(log ? "Association End unmasked name : " + associationEndName : "");

			if (mask.isNameMentioned()) {
				if (!associationEndName.equals(newEnd.getName())) {
					System.out.println(log ? "Association End name is different" : "");
					return true;
				}
			}

			if (mask.isMultiplicityMentioned()) {
				if (end.getUpper() != newEnd.getUpper()) {
					System.out.println(log ? "Association End multiplicity upper is different" : "");
					return true;
				}
				if (end.getLower() != newEnd.getLower()) {
					System.out.println(log ? "Association End multiplicity lower is different" : "");
					return true;
				}
			}

			if (mask.isDerivedMentioned()) {
				if (end.isDerived() != newEnd.isDerived()) {
					System.out.println(log ? "Association End derived is different" : "");
					return true;
				}
			}

			if (mask.isOrderedMentioned()) {
				if (end.isOrdered() != newEnd.isOrdered()) {
					System.out.println(log ? "Association End ordered is different" : "");
					return true;
				}
			}

			if (mask.isUniqueMentioned()) {
				if (end.isUnique() != newEnd.isUnique()) {
					System.out.println(log ? "Association End unique is different" : "");
					return true;
				}
			}

			if (mask.isVisibilityMentioned()) {
				if (end.getVisibility() != newEnd.getVisibility()) {
					System.out.println(log ? "Association End visibility is different" : "");
					return true;
				}
			}

		}
		System.out.println(log ? "---> Association is correct" : "");
		return false;
	}

	private boolean assessClassAttribute(PapyrusAttribute originalAttribute, PapyrusClass targetClass, PapyrusClass originalClass) {

		boolean log = SHOULD_LOG;
		AttributeMask mask = new AttributeMask(originalAttribute);

		String name = mask.getFinalName();
		System.out.println(log ? "Attribute unmasked name : " + name : "");

		PapyrusAttribute correspondingAttribute = findAttribute(name, targetClass);

		if (correspondingAttribute == null) {
			System.out.println(log ? "Attribute does not exist in player model" : "");
			return true;
		}

		if (mask.isMultiplicityMentioned()) {
			if (originalAttribute.getUpper() != correspondingAttribute.getUpper()) {
				System.out.println(log ? "Attribute multiplicity upper is different" : "");
				return true;
			}
			if (originalAttribute.getLower() != correspondingAttribute.getLower()) {
				System.out.println(log ? "Attribute multiplicity lower is different" : "");
				return true;
			}
		}

		if (mask.isDerivedMentioned()) {
			if (originalAttribute.isDerived() != correspondingAttribute.isDerived()) {
				System.out.println(log ? "Attribute derived is different" : "");
				return true;
			}
		}

		if (mask.isOrderedMentioned()) {
			if (originalAttribute.isOrdered() != correspondingAttribute.isOrdered()) {
				System.out.println(log ? "Attribute ordered is different" : "");
				return true;
			}
		}

		if (mask.isUniqueMentioned()) {
			if (originalAttribute.isUnique() != correspondingAttribute.isUnique()) {
				System.out.println(log ? "Attribute unique is different" : "");
				return true;
			}
		}

		if (mask.isVisibilityMentioned()) {
			if (originalAttribute.getVisibility() != correspondingAttribute.getVisibility()) {
				System.out.println(log ? "Attribute visibility is different" : "");
				return true;
			}
		}

		if (mask.isTypeMentioned()) {
			// TODO: check
			if ((originalAttribute.getType() == null) && (correspondingAttribute.getType() == null)) {
				// DO Nothing
			}
			if (originalAttribute.getType() != null) {
				if (correspondingAttribute.getType() == null) {
					System.out.println(log ? "Attribute type is Undefined and different from original" : "");
					return true;
				} else {
					if (!originalAttribute.getType().getName().equals(correspondingAttribute.getType().getName())) {
						System.out.println(log ? "Attribute type is different" : "");
						return true;
					}
				}
			}
		}

		System.out.println(log ? "---> Attribute is correct" : "");

		return false;
	}

	private boolean assessClassOperation(PapyrusOperation originalOperation, PapyrusClass targetClass, PapyrusClass originalClass) {
		OperationMask mask = new OperationMask(originalOperation);
		boolean log = SHOULD_LOG;

		String name = mask.getFinalName();

		PapyrusOperation correspondingOperation = findOperation(name, targetClass);

		if (correspondingOperation == null) {
			System.out.println(log ? "Operation does not exist in player model" : "");
			return true;
		}

		return false;
	}

	private boolean assessGeneralization(PapyrusGeneralization originalGeneralization) {

		boolean log = SHOULD_LOG;

		PapyrusGeneralization correspondingGenralization = findGeneralization(
				getUnmaskedName(originalGeneralization.getSuperclass().getName()),
				getUnmaskedName(originalGeneralization.getSubclass().getName()));

		if (correspondingGenralization == null) {
			System.out.println(log ? "Generalization does not exist in player model" : "");
			return true;
		}
		return false;
	}

	private PapyrusGeneralization findGeneralization(String superclass, String subclass) {
		System.out.println("Serching generalization between " + superclass + " and " + subclass);
		for (PapyrusGeneralization papyrusGeneralization : playerClassDiagram.getGeneralizations()) {
			System.out.println("------> Looking in generalization between " + papyrusGeneralization.getSuperclass().getName() + " and " + papyrusGeneralization.getSubclass().getName());
			if (papyrusGeneralization.getSubclass().getName().equals(subclass)
					&& papyrusGeneralization.getSuperclass().getName().equals(superclass)) {
				return papyrusGeneralization;
			}
		}
		return null;
	}

	private PapyrusAssociation findAssociation(String source, String target) {
		System.out.println("Serching generalization between " + source + " and " + target);
		for (PapyrusAssociation papyrusAssociation : playerClassDiagram.getAssociations()) {
			if (papyrusAssociation.getSourceEnd().getType().getName().equals(source)
					&& papyrusAssociation.getTargetEnd().getType().getName().equals(target)) {
				return papyrusAssociation;
			}
		}
		return null;
	}

	private PapyrusClass findClass(String classToFind) {
		for (PapyrusClass papyrusClass : playerClassDiagram.getClasses()) {
			if (papyrusClass.getName().equals(classToFind)) {
				return papyrusClass;
			}
		}
		return null;
	}

	private PapyrusAttribute findAttribute(String attributeToFind, PapyrusClass targetClass) {
		for (PapyrusAttribute papyrusAttribute : targetClass.getAttributes()) {
			if (papyrusAttribute.getName().equals(attributeToFind)) {
				return papyrusAttribute;
			}
		}
		return null;
	}

	private PapyrusOperation findOperation(String operationToFind, PapyrusClass targetClass) {
		for (PapyrusOperation papyrusOperation : targetClass.getOperations()) {
			if (papyrusOperation.getName().equals(operationToFind)) {
				return papyrusOperation;
			}
		}
		return null;
	}

	private String getUnmaskedName(String maskedName) {
		if (maskedName.contains("@")) {
			String[] split = maskedName.split("@");
			return split[0];
		}
		return maskedName;
	}
}