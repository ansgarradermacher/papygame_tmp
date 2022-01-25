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

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;

/**
 * @author lepallec
 *
 */
public class AssociationCopier {

	private DiagramCopier diagramCopier;

	/**
	 * Constructor.
	 *
	 */
	public AssociationCopier(DiagramCopier diagramCopier) {
		this.diagramCopier = diagramCopier;
	}

	/**
	 * @param connector
	 */
	public void copy(Connector connector) {

		// TODO : copier observer notifications

		Association originalAssociation = Association.class.cast(connector.getElement());
		EList<Property> memberEnds = originalAssociation.getMemberEnds();

		Property originalSource = originalAssociation.getMemberEnds().get(0);
		Property originalTarget = originalAssociation.getMemberEnds().get(1);

		Classifier source = Classifier.class.cast(getDiagramCopier().getSourceIndexedMapping().get(originalSource.getType()));
		Classifier target = Classifier.class.cast(getDiagramCopier().getSourceIndexedMapping().get(originalTarget.getType()));

		GraphicalEditPart sourceEditPart = getDiagramCopier().getEditPart(source);
		GraphicalEditPart targetEditPart = getDiagramCopier().getEditPart(target);

		PapyrusUtils.getINSTANCE().createAssociation(getDiagramCopier().getTargetDiagram(),
				getDiagramCopier().getCurrentTransactionalEditingDomain(), sourceEditPart, targetEditPart);
	}



	/**
	 * @return the diagramCopier
	 */
	public DiagramCopier getDiagramCopier() {
		return diagramCopier;
	}


}
