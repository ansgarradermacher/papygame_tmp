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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLCompartmentEditPart;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class PapyrusOperation extends PapyrusNamedElement {

	// For read-only use

	Shape shape;

	// for read-write use

	PapyrusClassifier papyrusClassifier;
	UMLCompartmentEditPart operationEditPart;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	// constructor for read only use

	public PapyrusOperation(PapyrusClassifier papyrusClassifier, Shape shape) {
		this.papyrusClassifier = papyrusClassifier;
		this.shape = shape;
	}

	// constructor for read-write use

	public PapyrusOperation(PapyrusClassifier papyrusClassifier, UMLCompartmentEditPart operationEditPart) {
		this.papyrusClassifier = papyrusClassifier;
		this.operationEditPart = operationEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) operationEditPart.getModel();
	}

	@Override
	public EditPart getEditPart() {
		return operationEditPart;
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
	public Operation getSemanticElement() {
		return (Operation) super.getSemanticElement();
	}

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		return papyrusClassifier.getPapyrusClassDiagram();
	}

	// **************
	//
	// ABSTRACT
	//
	// **************


	public boolean isAbstract() {
		return getSemanticElement().isAbstract();
	}

	public void setAbstract(boolean value) {
		Operation element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.CLASSIFIER__IS_ABSTRACT, value), element).execute();
	}

	// ************************
	//
	// PARAMETERS stuff
	//
	// ************************

	public PapyrusOperationParameter[] getParameters() {
		Operation operation = getSemanticElement();
		EList<Parameter> parameters = operation.getOwnedParameters();
		PapyrusOperationParameter[] papyrusParameters = new PapyrusOperationParameter[parameters.size()];
		for (int i = 0; i < papyrusParameters.length; i++) {
			papyrusParameters[i] = getPapyrusClassDiagram().getPapyrusOperationParameter(this, parameters.get(i));
		}
		return papyrusParameters;
	}

	public PapyrusOperationParameter addParameter(String parameterName) {
		return PapyrusNodeFactory.getInstance().createOperationParameter(this, parameterName);
	}


}
