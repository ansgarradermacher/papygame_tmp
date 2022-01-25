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

import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.ResizeableListCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPartCN;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


/**
 * @author lepallec
 *
 */
public class ClassAttributeCopier {

	ClassCopier classCopier;
	GraphicalEditPart targetClassEditPart;
	Class sourceClass;
	Class targetClass;
	ResizeableListCompartmentEditPart attributeCompartment = null;



	/**
	 * Constructor.
	 *
	 * @param classCopier
	 * @param sourceClass
	 * @param targetClass
	 */
	public ClassAttributeCopier(ClassCopier classCopier, Class sourceClass, Class targetClass) {
		this.classCopier = classCopier;
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
		setEditPartTargetClass();
		setAttributeCompartment();
	}

	/**
	 *
	 */
	private void setEditPartTargetClass() {
		this.targetClassEditPart = GraphicalEditPart.class.cast(getClassCopier().getDiagramCopier().getEditPart(this.targetClass));
	}

	/**
	 *
	 */
	private void setAttributeCompartment() {

		for (Object child : this.targetClassEditPart.getChildren()) {
			if (child instanceof ClassAttributeCompartmentEditPart ||
					child instanceof ClassAttributeCompartmentEditPartCN) {
				attributeCompartment = ResizeableListCompartmentEditPart.class.cast(child);
			}
		}
	}

	/**
	 * @return
	 *
	 */
	public Property copyAttribute(org.eclipse.uml2.uml.Property sourceAttribute) {

		String attributeName = sourceAttribute.getName();
		Type attributeType = Type.class.cast(getClassCopier().getDiagramCopier().getSourceIndexedMapping().get(sourceAttribute.getType()));

		PapyrusUtils utils = PapyrusUtils.getINSTANCE();

		Property targetAttribute = utils.createClassAttribute(getClassCopier().getDiagramCopier().getTargetDiagram(), getAttributeCompartment());
		utils.setElementName(targetAttribute, attributeName);
		utils.setElementType(targetAttribute, attributeType);
		utils.setElementVisibility(targetAttribute, sourceAttribute.getVisibility().getLiteral());
		// targetAttribute.setVisibility(sourceAttribute.getVisibility());
		// utils.setElementVisibility(targetAttribute, sourceAttribute.getVisibility().getLiteral());

		getClassCopier().getDiagramCopier().getTargetIndexedMapping().put(targetAttribute, sourceAttribute);

		getClassCopier().getDiagramCopier().elementHasBeenCopied(targetAttribute);
		return targetAttribute;
	}

	/**
	 * @return the classCopier
	 */
	public ClassCopier getClassCopier() {
		return classCopier;
	}

	/**
	 * @return the attributeCompartment
	 */
	public ResizeableListCompartmentEditPart getAttributeCompartment() {
		return attributeCompartment;
	}

}
