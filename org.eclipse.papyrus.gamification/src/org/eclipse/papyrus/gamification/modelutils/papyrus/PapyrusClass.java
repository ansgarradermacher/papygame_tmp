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
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassNameEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.OperationForClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PropertyForClassEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLCompartmentEditPart;

/**
 * @author lepallec
 *
 */
public class PapyrusClass extends PapyrusClassifier {


	// For read-only use

	Shape shape = null;

	// for read-write use

	ClassEditPart classEditPart = null;

	// for both uses

	PapyrusClassDiagram papyrusClassDiagram = null;
	PapyrusPackage papyrusPackage = null;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	PapyrusClass() {

	}
	// constructor for read only use

	public PapyrusClass(PapyrusClassDiagram papyrusClassDiagram, Shape shape) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.shape = shape;
	}

	public PapyrusClass(PapyrusPackage papyrusPackage, Shape shape) {
		this.papyrusPackage = papyrusPackage;
		this.shape = shape;
	}

	// constructor for read-write use

	public PapyrusClass(PapyrusClassDiagram papyrusClassDiagram, ClassEditPart classEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.classEditPart = classEditPart;
	}

	public PapyrusClass(PapyrusPackage papyrusPackage, ClassEditPart classEditPart) {
		this.papyrusPackage = papyrusPackage;
		this.classEditPart = classEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************


	@Override
	public boolean isReadOnly() {
		return classEditPart == null;
	}

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) ((EditPart) classEditPart).getModel();
	}

	@Override
	public EditPart getEditPart() {
		return (EditPart) classEditPart;
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
	public org.eclipse.uml2.uml.Class getSemanticElement() {
		return org.eclipse.uml2.uml.Class.class.cast(super.getSemanticElement());
	}

	public void hideNameCompartment() {
		EObject nameCompartment = EObject.class.cast(getNameCompartment().getModel());
		createSetCommand(new SetRequest(nameCompartment, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.VIEW__VISIBLE, false),
				nameCompartment).execute();
	}

	private ClassNameEditPart getNameCompartment() {
		for (Object child : ((EditPart) classEditPart).getChildren()) {
			if (child instanceof ClassNameEditPart) {
				return ClassNameEditPart.class.cast(child);
			}
		}
		return null;
	}


	// ************************
	//
	// ATTRIBUTES stuff
	//
	// ************************

	public PapyrusAttribute addAttribute(String attributeName) {
		if (!isReadOnly()) {
			if (papyrusClassDiagram != null) {
				return PapyrusNodeFactory.getInstance().createClassAttribute(this, attributeName);

			} else if (papyrusPackage != null) {
				return PapyrusNodeFactory.getInstance().createIncludedClassAttribute(this, attributeName);

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
			return shape.getType().equals(PropertyForClassEditPart.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected boolean isAttributeCompartment(Object compartment) {
		if (compartment instanceof BasicCompartment) {
			BasicCompartment attributeCompartment = BasicCompartment.class.cast(compartment);
			return attributeCompartment.getType().equals(ClassAttributeCompartmentEditPart.VISUAL_ID) ||
					attributeCompartment.getType().equals(ClassAttributeCompartmentEditPartCN.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected PapyrusAttribute createPapyrusAttribute(EditPart editPart) {
		return getPapyrusClassDiagram().getPapyrusAttribute(this, UMLCompartmentEditPart.class.cast(editPart));
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
				return PapyrusNodeFactory.getInstance().createClassOperation(this, operationName);

			} else if (papyrusPackage != null) {
				return PapyrusNodeFactory.getInstance().createIncludedClassOperation(this, operationName);

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
			return shape.getType().equals(OperationForClassEditPart.VISUAL_ID);
		}
		return false;
	}

	@Override
	protected boolean isOperationCompartment(Object compartment) {
		if (compartment instanceof BasicCompartment) {
			BasicCompartment operationCompartment = BasicCompartment.class.cast(compartment);
			return operationCompartment.getType().equals(ClassOperationCompartmentEditPart.VISUAL_ID) ||
					operationCompartment.getType().equals(ClassOperationCompartmentEditPartCN.VISUAL_ID);
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