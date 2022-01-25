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

package org.eclipse.papyrus.gamification.modelutils.papyrus;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PrimitiveTypeEditPart;
import org.eclipse.uml2.uml.PrimitiveType;

/**
 * @author lepallec
 *
 */
public class PapyrusPrimitiveType extends PapyrusType {

	// For read-only use

	PrimitiveType semanticElement = null;
	Shape shape = null;

	// for read-write use

	PrimitiveTypeEditPart primitiveTypeEditPart = null;

	// for both uses
	PapyrusClassDiagram papyrusClassDiagram = null;


	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************


	public PapyrusPrimitiveType(PapyrusClassDiagram papyrusClassDiagram, PrimitiveType semanticElement) {
		super();
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.semanticElement = semanticElement;
	}

	public PapyrusPrimitiveType(PapyrusClassDiagram papyrusClassDiagram, Shape shape) {
		super();
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.shape = shape;
	}

	public PapyrusPrimitiveType(PapyrusClassDiagram papyrusClassDiagram, PrimitiveTypeEditPart primitiveTypeEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.primitiveTypeEditPart = primitiveTypeEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************

	@Override
	public boolean isReadOnly() {
		return primitiveTypeEditPart == null;
	}

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) ((EditPart) primitiveTypeEditPart).getModel();
	}

	@Override
	public EditPart getEditPart() {
		return (EditPart) primitiveTypeEditPart;
	}

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusClassDiagram;
	}

	@Override
	public TransactionalEditingDomain getTransactionalEditingDomain() {

		return getPapyrusClassDiagram().getTransactionalEditingDomain();
	}

	@Override
	public DiagramEditPart getDiagramEditPart() {
		return getPapyrusClassDiagram().getDiagramEditPart();
	}

	@Override
	public org.eclipse.uml2.uml.PrimitiveType getSemanticElement() {
		if (semanticElement != null) {
			return semanticElement;
		} else {
			return org.eclipse.uml2.uml.PrimitiveType.class.cast(super.getSemanticElement());
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "PapyrusPrimitiveType [semanticElement=" + semanticElement + ", shape=" + shape + ", primitiveTypeEditPart=" + primitiveTypeEditPart + ", papyrusClassDiagram=" + papyrusClassDiagram + "]";
	}


}
