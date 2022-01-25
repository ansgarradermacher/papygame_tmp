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
import org.eclipse.uml2.uml.Property;

/**
 * @author lepallec
 *
 */
public class PapyrusAssociationEnd extends PapyrusAttribute {

	PapyrusAssociation papyrusAssociation;
	Property associationEnd;

	public PapyrusAssociationEnd(PapyrusAssociation papyrusAssociation, Property associationEnd) {
		this.papyrusAssociation = papyrusAssociation;
		this.associationEnd = associationEnd;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************

	// TODO : manage graphic elements bound to association end

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusAssociation.getPapyrusClassDiagram();
	}

	@Override
	public Shape getNotationalElement() {
		return null;
	}

	@Override
	public EditPart getEditPart() {
		return null;
	}

	@Override
	public TransactionalEditingDomain getTransactionalEditingDomain() {
		return papyrusAssociation.getTransactionalEditingDomain();
	}

	@Override
	public DiagramEditPart getDiagramEditPart() {
		return papyrusAssociation.getDiagramEditPart();
	}

	@Override
	public Property getSemanticElement() {
		return associationEnd;
	}


	@Override
	public String toString() {
		return "PapyrusAssociationEnd [papyrusAssociation=" + papyrusAssociation + ", associationEnd=" + associationEnd + "]";
	}

	public void transferAssociationType(PapyrusAssociationEnd anotherEnd) {
		try {
			getSemanticElement().setIsNavigable(anotherEnd.getSemanticElement().isNavigable());
		} catch (IllegalStateException e) {
			System.out.println("Exception raised when modifying property");
		}
		try {
			getSemanticElement().setAggregation(anotherEnd.getSemanticElement().getAggregation());
		} catch (IllegalStateException e) {
			System.out.println("Exception raised when modifying property");
		}
	}

}
