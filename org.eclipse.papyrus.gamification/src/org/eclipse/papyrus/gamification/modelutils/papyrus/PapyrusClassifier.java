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

import java.util.ArrayList;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public abstract class PapyrusClassifier extends PapyrusType {

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusNode#getSemanticElement()
	 *
	 * @return
	 */
	@Override
	public Classifier getSemanticElement() {
		return Classifier.class.cast(super.getSemanticElement());
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
		Classifier element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.CLASSIFIER__IS_ABSTRACT, value), element).execute();
	}

	// *********************
	//
	// ATTRIBUTES
	//
	// *********************

	public PapyrusAttribute[] getAttributes() {
		if (isReadOnly()) {
			return getAttributesReadOnlyMode();
		} else {
			return getAttributesReadWriteMode();
		}
	}

	//
	// READ ONLY MODE
	//

	public PapyrusAttribute[] getAttributesReadOnlyMode() {
		ArrayList<PapyrusAttribute> attributes = new ArrayList<>();
		for (Object child : getNotationalElement().getChildren()) {
			if (isAttributeCompartment(child)) {
				BasicCompartment compartment = BasicCompartment.class.cast(child);
				for (Object grandchild : compartment.getChildren()) {
					if (isNotationalAttribute(grandchild)) {
						attributes.add(createPapyrusAttribute(Shape.class.cast(grandchild)));
					}
				}
				return attributes.toArray(new PapyrusAttribute[attributes.size()]);
			}
		}
		return new PapyrusAttribute[0];
	}

	protected abstract PapyrusAttribute createPapyrusAttribute(Shape element);

	protected abstract boolean isNotationalAttribute(Object element);

	protected abstract boolean isAttributeCompartment(Object compartment);

	//
	// READ WRITE MODE
	//

	public PapyrusAttribute[] getAttributesReadWriteMode() {
		ArrayList<PapyrusAttribute> attributes = new ArrayList<>();
		EditPart editPart = getEditPart();
		for (Object child : editPart.getChildren()) {
			if (isAttributeCompartmentEditPart(child)) {
				CompartmentEditPart compartment = CompartmentEditPart.class.cast(child);
				for (Object grandChild : compartment.getChildren()) {
					if (isAttributeEditPart(grandChild)) {
						attributes.add(createPapyrusAttribute(EditPart.class.cast(grandChild)));
					}
				}
				return attributes.toArray(new PapyrusAttribute[attributes.size()]);
			}
		}
		return new PapyrusAttribute[0];
	}

	protected abstract PapyrusAttribute createPapyrusAttribute(EditPart cast);

	protected abstract boolean isAttributeEditPart(Object grandChild);

	protected abstract boolean isAttributeCompartmentEditPart(Object child);

	// *********************
	//
	// OPERATIONS
	//
	// *********************

	public PapyrusOperation[] getOperations() {
		if (isReadOnly()) {
			return getOperationsReadOnlyMode();
		} else {
			return getOperationsReadWriteMode();
		}
	}

	//
	// READ ONLY MODE
	//

	public PapyrusOperation[] getOperationsReadOnlyMode() {
		ArrayList<PapyrusOperation> operations = new ArrayList<>();
		for (Object child : getNotationalElement().getChildren()) {
			if (isOperationCompartment(child)) {
				BasicCompartment compartment = BasicCompartment.class.cast(child);
				for (Object grandchild : compartment.getChildren()) {
					if (isNotationalOperation(grandchild)) {
						operations.add(createPapyrusOperation(Shape.class.cast(grandchild)));
					}
				}
				return operations.toArray(new PapyrusOperation[operations.size()]);
			}
		}
		return new PapyrusOperation[0];
	}

	protected abstract PapyrusOperation createPapyrusOperation(Shape element);

	protected abstract boolean isNotationalOperation(Object element);

	protected abstract boolean isOperationCompartment(Object compartment);

	//
	// READ WRITE MODE
	//

	public PapyrusOperation[] getOperationsReadWriteMode() {
		ArrayList<PapyrusOperation> operations = new ArrayList<>();
		EditPart editPart = getEditPart();
		for (Object child : editPart.getChildren()) {
			if (isOperationCompartmentEditPart(child)) {
				CompartmentEditPart compartment = CompartmentEditPart.class.cast(child);
				for (Object grandChild : compartment.getChildren()) {
					if (isOperationEditPart(grandChild)) {
						operations.add(createPapyrusOperation(EditPart.class.cast(grandChild)));
					}
				}
				return operations.toArray(new PapyrusOperation[operations.size()]);
			}
		}
		return new PapyrusOperation[0];
	}

	protected abstract PapyrusOperation createPapyrusOperation(EditPart cast);

	protected abstract boolean isOperationEditPart(Object grandChild);

	protected abstract boolean isOperationCompartmentEditPart(Object child);

}
