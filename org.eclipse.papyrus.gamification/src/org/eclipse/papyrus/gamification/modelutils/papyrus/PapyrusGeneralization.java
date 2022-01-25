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
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.GeneralizationEditPart;
import org.eclipse.uml2.uml.Generalization;

/**
 * @author lepallec
 *
 */
public class PapyrusGeneralization extends PapyrusLink {

	Connector connector;

	PapyrusClassDiagram papyrusClassDiagram;
	GeneralizationEditPart generalizationEditPart;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************


	public PapyrusGeneralization(PapyrusClassDiagram papyrusClassDiagram, Connector connector) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.connector = connector;
	}

	public PapyrusGeneralization(PapyrusClassDiagram papyrusClassDiagram, GeneralizationEditPart generalizationEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.generalizationEditPart = generalizationEditPart;
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
	public Generalization getSemanticElement() {
		return Generalization.class.cast(super.getSemanticElement());
	}

	@Override
	public Connector getNotationalElement() {
		if (isReadOnly()) {
			return connector;
		}
		return (Connector) generalizationEditPart.getModel();
	}


	@Override
	public EditPart getEditPart() {
		return generalizationEditPart;
	}

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusClassDiagram;
	}

	public String getName() {
		return null;
	}

	public void setName(String name) {

	}

	// ************************
	//
	// ASSOCIATION ENDS stuff
	//
	// ************************

	// TODO : implement graphical concerns and memorizing mechanisms

	public PapyrusClassifier getSubclass() {
		return PapyrusClassifier.class.cast(getPapyrusClassDiagram().getCorrespondingPapyrusNode(getSemanticElement().getSpecific()));
	}

	public PapyrusClassifier getSuperclass() {
		return PapyrusClassifier.class.cast(getPapyrusClassDiagram().getCorrespondingPapyrusNode(getSemanticElement().getGeneral()));
	}

	@Override
	public String toString() {
		return "PapyrusGeneralization [connector=" + connector + ", papyrusClassDiagram=" + papyrusClassDiagram + ", generalizationEditPart=" + generalizationEditPart + "]";
	}


}
