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
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.OperationForClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.OperationForInterfaceEditpart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PropertyForClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PropertyForInterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.ClassifierEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLCompartmentEditPart;

/**
 * @author lepallec
 *
 */
public class PapyrusInterface extends PapyrusClassifier {


	// For read-only use

	Shape shape = null;

	// for read-write use

	ClassifierEditPart interfaceEditPart = null;


	// for both uses

	PapyrusClassDiagram papyrusClassDiagram = null;
	PapyrusPackage papyrusPackage = null;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	PapyrusInterface() {

	}
	// constructor for read only use

	public PapyrusInterface(PapyrusClassDiagram papyrusClassDiagram, Shape shape) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.shape = shape;
	}

	public PapyrusInterface(PapyrusPackage papyrusPackage, Shape shape) {
		this.papyrusPackage = papyrusPackage;
		this.shape = shape;
	}

	// constructor for read-write use

	public PapyrusInterface(PapyrusClassDiagram papyrusClassDiagram, ClassifierEditPart interfaceEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.interfaceEditPart = interfaceEditPart;
	}

	public PapyrusInterface(PapyrusPackage papyrusPackage, ClassifierEditPart interfaceEditPart) {
		this.papyrusPackage = papyrusPackage;
		this.interfaceEditPart = interfaceEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************


	@Override
	public boolean isReadOnly() {
		return interfaceEditPart == null;
	}

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) ((EditPart) interfaceEditPart).getModel();
	}

	@Override
	public EditPart getEditPart() {
		return (EditPart) interfaceEditPart;
	}

	@Override
	public PapyrusClassDiagram getPapyrusClassDiagram() {
		if (papyrusClassDiagram != null) {
			return papyrusClassDiagram;

		} else if (papyrusPackage != null) {
			return papyrusPackage.getPapyrusClassDiagram();

		} else {
			return null;
		}
	}

	@Override
	public TransactionalEditingDomain getTransactionalEditingDomain() {

		if (papyrusClassDiagram != null) {
			return papyrusClassDiagram.getTransactionalEditingDomain();

		} else if (papyrusPackage != null) {
			return papyrusPackage.getTransactionalEditingDomain();

		} else {
			return null;
		}
	}

	@Override
	public DiagramEditPart getDiagramEditPart() {
		if (papyrusClassDiagram != null) {
			return papyrusClassDiagram.getDiagramEditPart();

		} else if (papyrusPackage != null) {
			return papyrusPackage.getDiagramEditPart();

		} else {
			return null;
		}
	}

	@Override
	public org.eclipse.uml2.uml.Interface getSemanticElement() {
		return org.eclipse.uml2.uml.Interface.class.cast(super.getSemanticElement());
	}


	// ************************
	//
	// ATTRIBUTES stuff
	//
	// ************************

	public PapyrusAttribute addAttribute(String attributeName) {
		if (!isReadOnly()) {
			if (papyrusClassDiagram != null) {
				return PapyrusNodeFactory.getInstance().createInterfaceAttribute(this, attributeName);

			} else if (papyrusPackage != null) {
				return PapyrusNodeFactory.getInstance().createIncludedInterfaceAttribute(this, attributeName);

			}
		}
		return null;
	}

	@Override
	protected PapyrusAttribute createPapyrusAttribute(Shape element) {
		return getPapyrusClassDiagram().getPapyrusAttribute(this, element);
	}

	@Override
	protected boolean isNotationalAttribute(Object element) {
		if (element instanceof Shape) {
			Shape shape = Shape.class.cast(element);
			return shape.getType().equals(PropertyForInterfaceEditPart.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected boolean isAttributeCompartment(Object compartment) {
		if (compartment instanceof Shape) {
			Shape shape = Shape.class.cast(compartment);
			return shape.getType().equals(InterfaceAttributeCompartmentEditPart.VISUAL_ID) ||
					shape.getType().equals(InterfaceAttributeCompartmentEditPartCN.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected PapyrusAttribute createPapyrusAttribute(EditPart editPart) {
		return new PapyrusAttribute(this, UMLCompartmentEditPart.class.cast(editPart));
	}

	@Override
	protected boolean isAttributeEditPart(Object object) {
		return (object instanceof PropertyForClassEditPart);
	}

	@Override
	protected boolean isAttributeCompartmentEditPart(Object compartment) {
		return (compartment instanceof ClassAttributeCompartmentEditPart) ||
				(compartment instanceof ClassAttributeCompartmentEditPartCN);
	}

	// ************************
	//
	// OPERATIONS stuff
	//
	// ************************

	public PapyrusOperation addOperation(String operationName) {
		if (!isReadOnly()) {
			if (papyrusClassDiagram != null) {
				return PapyrusNodeFactory.getInstance().createInterfaceOperation(this, operationName);

			} else if (papyrusPackage != null) {
				return PapyrusNodeFactory.getInstance().createIncludedInterfaceOperation(this, operationName);

			}
		}
		return null;
	}

	@Override
	protected PapyrusOperation createPapyrusOperation(Shape element) {
		return getPapyrusClassDiagram().getPapyrusOperation(this, element);
	}

	@Override
	protected boolean isNotationalOperation(Object element) {
		if (element instanceof Shape) {
			Shape shape = Shape.class.cast(element);
			return shape.getType().equals(OperationForInterfaceEditpart.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected boolean isOperationCompartment(Object compartment) {
		if (compartment instanceof Shape) {
			Shape shape = Shape.class.cast(compartment);
			return shape.getType().equals(InterfaceOperationCompartmentEditPart.VISUAL_ID) ||
					shape.getType().equals(InterfaceOperationCompartmentEditPartCN.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected PapyrusOperation createPapyrusOperation(EditPart editPart) {
		return new PapyrusOperation(this, UMLCompartmentEditPart.class.cast(editPart));
	}

	@Override
	protected boolean isOperationEditPart(Object object) {
		return (object instanceof OperationForClassEditPart);
	}

	@Override
	protected boolean isOperationCompartmentEditPart(Object compartment) {
		return (compartment instanceof ClassOperationCompartmentEditPart) ||
				(compartment instanceof ClassOperationCompartmentEditPartCN);
	}


}