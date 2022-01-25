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

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationEditPart;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class PapyrusAssociation extends PapyrusLink {

	Connector connector;

	PapyrusClassDiagram papyrusClassDiagram;
	AssociationEditPart associationEditPart;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************


	public PapyrusAssociation(PapyrusClassDiagram papyrusClassDiagram, Connector connector) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.connector = connector;
	}

	public PapyrusAssociation(PapyrusClassDiagram papyrusClassDiagram, AssociationEditPart associationEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.associationEditPart = associationEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************


	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusLink#getSemanticElement()
	 *
	 * @return
	 */
	@Override
	public Association getSemanticElement() {
		return Association.class.cast(super.getSemanticElement());
	}

	@Override
	public Connector getNotationalElement() {
		if (isReadOnly()) {
			return connector;
		}
		return (Connector) associationEditPart.getModel();
	}


	@Override
	public EditPart getEditPart() {
		return associationEditPart;
	}

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusClassDiagram;
	}

	public String getName() {
		return getSemanticElement().getName();
	}

	public void setName(String name) {
		Association association = getSemanticElement();
		createSetCommand(new SetRequest(association, Literals.NAMED_ELEMENT__NAME, name), association).execute();
	}

	// ************************
	//
	// ASSOCIATION ENDS stuff
	//
	// ************************

	// TODO : implement graphical concerns and memorizing mechanisms

	public PapyrusAssociationEnd getSourceEnd() {
		if (getSemanticElement().getOwnedEnds().size() > 1) {
			return new PapyrusAssociationEnd(this, getSemanticElement().getOwnedEnds().get(0));
		}
		for (Property relationProperty : getSemanticElement().getAllAttributes()) {
			System.out.println("Current property is : " + relationProperty);
			System.out.println("Current owner is : " + relationProperty.getOwner());

			// If owner is class then it is the source
			if (relationProperty.getOwner() instanceof Class) {
				return new PapyrusAssociationEnd(this, relationProperty);
			}
		}

		return null;
	}

	public PapyrusAssociationEnd getTargetEnd() {
		if (getSemanticElement().getOwnedEnds().size() > 1) {
			return new PapyrusAssociationEnd(this, getSemanticElement().getOwnedEnds().get(1));
		}

		Class owningClass = null;
		for (Property relationProperty : getSemanticElement().getAllAttributes()) {
			// If owner is class then it is the source
			if (relationProperty.getOwner() instanceof Class) {
				owningClass = (Class) relationProperty.getOwner();
			}
		}


		// Now find the target class
		for (Property relationProperty : getSemanticElement().getAllAttributes()) {
			if (relationProperty.getOwner() instanceof Association) {
				for (Element associationRelated : getSemanticElement().getRelatedElements()) {
					if (!associationRelated.equals(owningClass)) {
						Class targetClass = (Class) associationRelated;
						return new PapyrusAssociationEnd(this, relationProperty);
					}
				}
			}

		}
		return null;
		// return new PapyrusAssociationEnd(this, getSemanticElement().getOwnedEnds().get(1));
	}

	@Override
	public String toString() {
		return "PapyrusAssociation [connector=" + connector + ", papyrusClassDiagram=" + papyrusClassDiagram + ", associationEditPart=" + associationEditPart + "]";
	}


}
