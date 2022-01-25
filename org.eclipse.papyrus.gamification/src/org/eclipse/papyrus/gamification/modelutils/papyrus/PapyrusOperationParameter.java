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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class PapyrusOperationParameter extends PapyrusTypedElement {

	PapyrusOperation papyrusOperation;
	Parameter parameter;



	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	public PapyrusOperationParameter(PapyrusOperation papyrusOperation, Parameter parameter) {
		super();
		this.papyrusOperation = papyrusOperation;
		this.parameter = parameter;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusOperation.getPapyrusClassDiagram();
	}

	@Override
	public DiagramEditPart getDiagramEditPart() {
		if (!isReadOnly()) {
			getPapyrusClassDiagram().getDiagramEditPart();
		}
		return null;
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
	public Parameter getSemanticElement() {
		return parameter;
	}

	@Override
	public TransactionalEditingDomain getTransactionalEditingDomain() {
		return papyrusOperation.getTransactionalEditingDomain();
	}

	// ************************
	//
	// VALUES stuff
	//
	// ************************

	//
	// LOWER
	//

	public int getLower() {
		return getSemanticElement().getLower();
	}

	public void setLower(int lower) {
		if (isReadOnly()) {
			return;
		}
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.MULTIPLICITY_ELEMENT__LOWER, lower), element).execute();
	}

	//
	// UPPER
	//

	public int getUpper() {
		return getSemanticElement().getUpper();
	}

	public void setUpper(int upper) {
		if (isReadOnly()) {
			return;
		}
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.MULTIPLICITY_ELEMENT__UPPER, upper), element).execute();
	}

	//
	// DIRECTION
	//

	public ParameterDirectionKind getDirection() {
		return getSemanticElement().getDirection();
	}

	public void setDirection(ParameterDirectionKind direction) {
		if (isReadOnly()) {
			return;
		}
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.PARAMETER__DIRECTION, direction), element).execute();
	}

	//
	// ORDERED
	//

	public boolean isOrdered() {
		return getSemanticElement().isOrdered();
	}

	public void setOrdered(boolean ordered) {
		if (isReadOnly()) {
			return;
		}
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.MULTIPLICITY_ELEMENT__IS_ORDERED, ordered), element).execute();
	}

	//
	// UNIQUE
	//

	public boolean isUnique() {
		return getSemanticElement().isUnique();
	}


	public void setUnique(boolean unique) {
		if (isReadOnly()) {
			return;
		}
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.MULTIPLICITY_ELEMENT__IS_UNIQUE, unique), element).execute();
	}

}
