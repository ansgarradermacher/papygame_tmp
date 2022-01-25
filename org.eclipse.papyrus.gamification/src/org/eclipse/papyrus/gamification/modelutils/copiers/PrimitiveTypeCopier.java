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

import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.gamification.modelutils.papyrus.TypeHelper;
import org.eclipse.uml2.uml.PrimitiveType;

/**
 * @author lepallec
 *
 */
public class PrimitiveTypeCopier {

	DiagramCopier diagramCopier;
	TypeHelper typeHelper;

	/**
	 * Constructor.
	 *
	 */
	public PrimitiveTypeCopier(DiagramCopier diagramCopier) {
		this.diagramCopier = diagramCopier;
		this.typeHelper = new TypeHelper(diagramCopier.getSourceDiagram());
	}

	/**
	 * @param string
	 */
	public void copy(String name) {
		PapyrusUtils utils = PapyrusUtils.getINSTANCE();
		PrimitiveType targetPrimitiveType = utils.createPrimitiveType(getDiagramCopier().getTargetDiagram());
		Shape primitiveTypeShape = Shape.class.cast(utils.getEditPart(getDiagramCopier().getTargetDiagram(), targetPrimitiveType).getModel());

		utils.setElementName(targetPrimitiveType, name);
		try {
			primitiveTypeShape.setVisible(false);
		} catch (Exception ex) {

		}
		getDiagramCopier().getSourceIndexedMapping().put(getTypeHelper().getPrimitiveType(name), targetPrimitiveType);
		getDiagramCopier().getTargetIndexedMapping().put(targetPrimitiveType, getTypeHelper().getPrimitiveType(name));

	}

	/**
	 * @return the diagramCopier
	 */
	public DiagramCopier getDiagramCopier() {
		return diagramCopier;
	}

	/**
	 * @return the typeHelper
	 */
	public TypeHelper getTypeHelper() {
		return typeHelper;
	}

}
