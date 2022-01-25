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
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

/**
 * @author lepallec
 *
 */
public class InterfaceCopier {

	DiagramCopier diagramCopier;

	/**
	 * Constructor.
	 *
	 * @param diagramCopier
	 */
	public InterfaceCopier(DiagramCopier diagramCopier) {
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
	public org.eclipse.uml2.uml.Interface copy(Shape graphicSourceInterface) {

		org.eclipse.uml2.uml.Interface sourceInterface = getUmlInterface(graphicSourceInterface);
		if (sourceInterface == null) {
			return null;
		}

		Point interfaceLocation = PapyrusUtils.getINSTANCE().getShapeLocation(graphicSourceInterface);

		Interface targetInterface = PapyrusUtils.getINSTANCE().createGraphicalUmlInterface(getDiagramCopier().getTargetDiagram(), sourceInterface.getName(), interfaceLocation);

		GraphicalEditPart targetInterfaceEditPart = getDiagramCopier().getEditPart(targetInterface);

		PapyrusUtils.getINSTANCE().setElementBackgroundColor(EObject.class.cast(targetInterfaceEditPart.getModel()), 16777215);


		getDiagramCopier().getSourceIndexedMapping().put(sourceInterface, targetInterface);
		getDiagramCopier().getTargetIndexedMapping().put(targetInterface, sourceInterface);

		getDiagramCopier().elementHasBeenCopied(targetInterface);

		// copy all source attributes

		copyAttributes(sourceInterface, targetInterface);

		copyOperations(sourceInterface, targetInterface);

		return targetInterface;
	}


	/**
	 * @param sourceInterface
	 * @param targetInterface
	 */
	private void copyAttributes(org.eclipse.uml2.uml.Interface sourceInterface, Interface targetInterface) {
		InterfaceAttributeCopier attributeCopier = new InterfaceAttributeCopier(this, sourceInterface, targetInterface);
		EList<Property> attributes = sourceInterface.getAllAttributes();
		for (Property attribute : attributes) {
			if (getDiagramCopier().getCopierObserver().canICopyElement(attribute)) {
				attributeCopier.copyAttribute(attribute);
			}
		}
	}

	/**
	 * @param sourceInterface
	 * @param targetInterface
	 */
	private void copyOperations(org.eclipse.uml2.uml.Interface sourceInterface, Interface targetInterface) {
		InterfaceOperationCopier operationCopier = new InterfaceOperationCopier(this, sourceInterface, targetInterface);
		EList<Operation> operations = sourceInterface.getAllOperations();
		for (Operation operation : operations) {
			if (getDiagramCopier().getCopierObserver().canICopyElement(operation)) {
				operationCopier.copyOperation(operation);
			}
		}
	}

	/**
	 * @param graphicInterface
	 * @return
	 */
	private Interface getUmlInterface(Shape graphicInterface) {
		return org.eclipse.uml2.uml.Interface.class.cast(graphicInterface.getElement());
	}

}
