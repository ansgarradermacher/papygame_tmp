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
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLCompartmentEditPart;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage.Literals;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * @author lepallec
 *
 */
public class PapyrusAttribute extends PapyrusTypedElement {

	// For read-only use

	Shape shape;

	// for read-write use

	PapyrusClassifier papyrusClassifier;
	UMLCompartmentEditPart attributeEditPart;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	public PapyrusAttribute() {

	}

	// constructor for read only use

	public PapyrusAttribute(PapyrusClassifier papyrusClassifier, Shape shape) {
		this.papyrusClassifier = papyrusClassifier;
		this.shape = shape;
	}

	// constructor for read-write use

	public PapyrusAttribute(PapyrusClassifier papyrusClassifier, UMLCompartmentEditPart attributeEditPart) {
		this.papyrusClassifier = papyrusClassifier;
		this.attributeEditPart = attributeEditPart;
	}


	// ************************
	//
	// BASIC stuff
	//
	// ************************

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusClassifier.getPapyrusClassDiagram();
	}

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) attributeEditPart.getModel();
	}

	@Override
	public EditPart getEditPart() {
		return attributeEditPart;
	}

	@Override
	public TransactionalEditingDomain getTransactionalEditingDomain() {
		return papyrusClassifier.getTransactionalEditingDomain();
	}

	@Override
	public DiagramEditPart getDiagramEditPart() {
		return papyrusClassifier.getDiagramEditPart();
	}

	@Override
	public Property getSemanticElement() {
		return Property.class.cast(super.getSemanticElement());
	}

	// **************
	//
	// VISIBILITY
	//
	// **************

	public String getVisibility() {
		return getSemanticElement().getVisibility().getLiteral();
	}

	public void setVisibility(String visibility) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		VisibilityKind visibilityKind = VisibilityKind.get(visibility);
		createSetCommand(new SetRequest(attribute, Literals.NAMED_ELEMENT__VISIBILITY, visibilityKind),
				attribute).execute();
	}

	// **************
	//
	// LOWER
	//
	// **************

	public int getLower() {
		return getSemanticElement().getLower();
	}

	public void setLower(int lower) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		createSetCommand(new SetRequest(attribute, Literals.MULTIPLICITY_ELEMENT__LOWER, lower),
				attribute).execute();
	}

	// **************
	//
	// UPPER
	//
	// **************

	public int getUpper() {
		return getSemanticElement().getUpper();
	}

	public void setUpper(int upper) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		createSetCommand(new SetRequest(attribute, Literals.MULTIPLICITY_ELEMENT__UPPER, upper),
				attribute).execute();
	}

	// **************
	//
	// ORDERED
	//
	// **************

	public boolean isDerived() {
		return getSemanticElement().isDerived();
	}

	public void setDerived(boolean derived) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		createSetCommand(new SetRequest(attribute, Literals.PROPERTY__IS_DERIVED, derived),
				attribute).execute();
	}

	// **************
	//
	// ORDERED
	//
	// **************

	public boolean isOrdered() {
		return getSemanticElement().isOrdered();
	}

	public void setOrdered(boolean ordered) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		createSetCommand(new SetRequest(attribute, Literals.MULTIPLICITY_ELEMENT__IS_ORDERED, ordered),
				attribute).execute();
	}

	// **************
	//
	// UNIQUE
	//
	// **************

	public boolean isUnique() {
		return getSemanticElement().isUnique();
	}

	public void setUnique(boolean unique) {
		if (isReadOnly()) {
			return;
		}
		Property attribute = getSemanticElement();
		createSetCommand(new SetRequest(attribute, Literals.MULTIPLICITY_ELEMENT__IS_UNIQUE, unique),
				attribute).execute();
	}
}
