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

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;

/**
 * @author lepallec
 *
 */
public class InterfaceRealizationCopier {

	private DiagramCopier diagramCopier;

	/**
	 * Constructor.
	 *
	 */
	public InterfaceRealizationCopier(DiagramCopier diagramCopier) {
		this.diagramCopier = diagramCopier;
	}

	/**
	 * @param connector
	 */
	public void copy(Connector connector) {

		// TODO : copier observer notifications

		InterfaceRealization originalRealization = InterfaceRealization.class.cast(connector.getElement());
		Class sourceClass = Class.class.cast(originalRealization.getClients().get(0));
		Interface targetInterface = Interface.class.cast(originalRealization.getSuppliers().get(0));

		Classifier source = Classifier.class.cast(getDiagramCopier().getSourceIndexedMapping().get(sourceClass));
		Classifier target = Classifier.class.cast(getDiagramCopier().getSourceIndexedMapping().get(targetInterface));

		GraphicalEditPart sourceEditPart = getDiagramCopier().getEditPart(source);
		GraphicalEditPart targetEditPart = getDiagramCopier().getEditPart(target);

		PapyrusUtils.getINSTANCE().createConnector(UMLDIElementTypes.INTERFACE_REALIZATION_EDGE, getDiagramCopier().getTargetDiagram(),
				getDiagramCopier().getCurrentTransactionalEditingDomain(), sourceEditPart, targetEditPart);

	}



	/**
	 * @return the diagramCopier
	 */
	public DiagramCopier getDiagramCopier() {
		return diagramCopier;
	}


}
