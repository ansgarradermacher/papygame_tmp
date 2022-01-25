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
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPartCN;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * @author lepallec
 *
 */
public class InterfaceAttributeCopier {

	InterfaceCopier interfaceCopier;
	GraphicalEditPart targetInterfaceEditPart;
	Interface sourceInterface;
	Interface targetInterface;
	ResizeableListCompartmentEditPart attributeCompartment = null;



	/**
	 * Constructor.
	 *
	 * @param interfaceCopier
	 * @param sourceInterface
	 * @param targetInterface
	 */
	public InterfaceAttributeCopier(InterfaceCopier interfaceCopier, Interface sourceInterface, Interface targetInterface) {
		this.interfaceCopier = interfaceCopier;
		this.sourceInterface = sourceInterface;
		this.targetInterface = targetInterface;
		setEditPartTargetInterface();
		setAttributeCompartment();
	}

	/**
	 *
	 */
	private void setEditPartTargetInterface() {
		this.targetInterfaceEditPart = GraphicalEditPart.class.cast(getInterfaceCopier().getDiagramCopier().getEditPart(this.targetInterface));
	}

	/**
	 *
	 */
	private void setAttributeCompartment() {
		for (Object child : this.targetInterfaceEditPart.getChildren()) {
			if (child instanceof InterfaceAttributeCompartmentEditPart ||
					child instanceof InterfaceAttributeCompartmentEditPartCN) {
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
		Type attributeType = Type.class.cast(getInterfaceCopier().getDiagramCopier().getSourceIndexedMapping().get(sourceAttribute.getType()));

		PapyrusUtils utils = PapyrusUtils.getINSTANCE();

		Property targetAttribute = utils.createInterfaceAttribute(getInterfaceCopier().getDiagramCopier().getTargetDiagram(), getAttributeCompartment());
		utils.setElementName(targetAttribute, attributeName);
		utils.setElementType(targetAttribute, attributeType);
		utils.setElementVisibility(targetAttribute, sourceAttribute.getVisibility().getLiteral());
		// targetAttribute.setVisibility(sourceAttribute.getVisibility());

		getInterfaceCopier().getDiagramCopier().getTargetIndexedMapping().put(targetAttribute, sourceAttribute);

		getInterfaceCopier().getDiagramCopier().elementHasBeenCopied(targetAttribute);
		return targetAttribute;
	}

	/**
	 * @return the interfaceCopier
	 */
	public InterfaceCopier getInterfaceCopier() {
		return interfaceCopier;
	}

	/**
	 * @return the attributeCompartment
	 */
	public ResizeableListCompartmentEditPart getAttributeCompartment() {
		return attributeCompartment;
	}

}
