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

package org.eclipse.papyrus.gamification.modelutils.copiers;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

/**
 * @author lepallec
 *
 */
public class ClassCopier {

	DiagramCopier diagramCopier;

	/**
	 * Constructor.
	 *
	 * @param diagramCopier
	 */
	public ClassCopier(DiagramCopier diagramCopier) {
		this.diagramCopier = diagramCopier;
	}


	/**
	 * @return the diagramCopier
	 */
	public DiagramCopier getDiagramCopier() {
		return diagramCopier;
	}

	/**
	 *
	 */
	public org.eclipse.uml2.uml.Class copy(Shape graphicSourceClass) {


		org.eclipse.uml2.uml.Class sourceClass = getUmlClass(graphicSourceClass);
		if (sourceClass == null) {
			return null;
		}

		Point classLocation = PapyrusUtils.getINSTANCE().getShapeLocation(graphicSourceClass);

		Class targetClass = PapyrusUtils.getINSTANCE().createGraphicalUmlClass(getDiagramCopier().getTargetDiagram(), sourceClass.getName(), classLocation);

		GraphicalEditPart targetClassEditPart = getDiagramCopier().getEditPart(targetClass);

		PapyrusUtils.getINSTANCE().setElementBackgroundColor(EObject.class.cast(targetClassEditPart.getModel()), 16777215);

		getDiagramCopier().getSourceIndexedMapping().put(sourceClass, targetClass);
		getDiagramCopier().getTargetIndexedMapping().put(targetClass, sourceClass);

		getDiagramCopier().elementHasBeenCopied(targetClass);

		// copy all source attributes

		copyAttributes(sourceClass, targetClass);

		copyOperations(sourceClass, targetClass);

		return targetClass;
	}


	/**
	 * @param sourceClass
	 * @param targetClass
	 */
	private void copyAttributes(org.eclipse.uml2.uml.Class sourceClass, Class targetClass) {
		ClassAttributeCopier attributeCopier = new ClassAttributeCopier(this, sourceClass, targetClass);
		EList<Property> attributes = sourceClass.getAllAttributes();
		for (Property attribute : attributes) {
			if (getDiagramCopier().getCopierObserver().canICopyElement(attribute)) {
				attributeCopier.copyAttribute(attribute);
			}
		}
	}

	/**
	 * @param sourceClass
	 * @param targetClass
	 */
	private void copyOperations(org.eclipse.uml2.uml.Class sourceClass, Class targetClass) {
		ClassOperationCopier operationCopier = new ClassOperationCopier(this, sourceClass, targetClass);
		EList<Operation> operations = sourceClass.getAllOperations();
		for (Operation operation : operations) {
			if (getDiagramCopier().getCopierObserver().canICopyElement(operation)) {
				operationCopier.copyOperation(operation);
			}
		}
	}

	/**
	 * @param graphicClass
	 * @return
	 */
	private Class getUmlClass(Shape graphicClass) {
		return org.eclipse.uml2.uml.Class.class.cast(graphicClass.getElement());
	}

}
