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

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PackageEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PackagePackageableElementCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PackagePackageableElementCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.common.editparts.ClassEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

/**
 * @author lepallec
 *
 */
public class PapyrusPackage extends PapyrusNamedElement {

	// For read-only use

	Shape shape = null;

	// for read-write use

	PackageEditPart packageEditPart = null;

	// for both uses

	PapyrusClassDiagram papyrusClassDiagram = null;
	PapyrusPackage papyrusPackage = null;

	// ************************
	//
	// CONSTRUCTORS
	//
	// ************************

	// constructor for read only use

	public PapyrusPackage(PapyrusClassDiagram papyrusClassDiagram, Shape shape) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.shape = shape;
	}

	public PapyrusPackage(PapyrusPackage papyrusPackage, Shape shape) {
		this.papyrusPackage = papyrusPackage;
		this.shape = shape;
	}

	// constructor for read-write use

	public PapyrusPackage(PapyrusClassDiagram papyrusClassDiagram, PackageEditPart packageEditPart) {
		this.papyrusClassDiagram = papyrusClassDiagram;
		this.packageEditPart = packageEditPart;
	}

	public PapyrusPackage(PapyrusPackage papyrusPackage, PackageEditPart packageEditPart) {
		this.papyrusPackage = papyrusPackage;
		this.packageEditPart = packageEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************


	@Override
	public boolean isReadOnly() {
		return packageEditPart == null;
	}

	@Override
	public Shape getNotationalElement() {
		if (isReadOnly()) {
			return shape;
		}
		return (Shape) ((Element) packageEditPart).getModel();
	}

	@Override
	public EditPart getEditPart() {
		return (EditPart) packageEditPart;
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
	public Package getSemanticElement() {
		return Package.class.cast(super.getSemanticElement());
	}


	// *************
	//
	// CONTAINER COMPARTMENT stuff
	//
	// *************

	public CompartmentEditPart getContainerCompartmentEditPart() {
		for (Object child : ((EditPart) packageEditPart).getChildren()) {
			if (child instanceof PackagePackageableElementCompartmentEditPart ||
					child instanceof PackagePackageableElementCompartmentEditPartCN) {
				return CompartmentEditPart.class.cast(child);
			}
		}
		return null;
	}

	// *************
	//
	// CLASSES stuff
	//
	// *************

	public PapyrusClass addClass(String className, int x, int y, int w, int h) {
		if (!isReadOnly()) {
			return PapyrusNodeFactory.getInstance().createClass(this, className, x, y, w, h);
		}
		return null;
	}

	public PapyrusClass[] getClasses() {
		if (isReadOnly()) {
			return getClassesReadOnlyMode();
		} else {
			return getClassesReadWriteMode();
		}
	}

	protected PapyrusClass[] getClassesReadOnlyMode() {
		ArrayList<PapyrusClass> classes = new ArrayList<>();
		for (Object originalObject : shape.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Class) {
					classes.add(getPapyrusClassDiagram().getPapyrusClass(this, shape));
				}
			}
		}
		return classes.toArray(new PapyrusClass[classes.size()]);
	}

	protected PapyrusClass[] getClassesReadWriteMode() {
		ArrayList<PapyrusClass> classes = new ArrayList<>();
		for (Object child : getContainerCompartmentEditPart().getChildren()) {
			if (child instanceof ClassEditPartCN) {
				classes.add(getPapyrusClassDiagram().getPapyrusClass(this, ClassEditPart.class.cast(child)));
			}
		}
		return classes.toArray(new PapyrusClass[classes.size()]);
	}

	// *************
	//
	// INTERFACES stuff
	//
	// *************


	public PapyrusInterface addInterface(String interfaceName, int x, int y, int w, int h) {
		if (!isReadOnly()) {
			return PapyrusNodeFactory.getInstance().createInterface(this, interfaceName, x, y, w, h);
		}
		return null;
	}

	public PapyrusInterface[] getInterfaces() {
		if (isReadOnly()) {
			return getInterfacesReadOnlyMode();
		} else {
			return getInterfacesReadWriteMode();
		}
	}

	protected PapyrusInterface[] getInterfacesReadWriteMode() {
		ArrayList<PapyrusInterface> interfaces = new ArrayList<>();
		for (Object child : getContainerCompartmentEditPart().getChildren()) {
			if (child instanceof InterfaceEditPartCN) {
				interfaces.add(getPapyrusClassDiagram().getPapyrusInterface(this, ClassEditPart.class.cast(child)));
			}
		}
		return interfaces.toArray(new PapyrusInterface[interfaces.size()]);
	}

	protected PapyrusInterface[] getInterfacesReadOnlyMode() {
		ArrayList<PapyrusInterface> interfaces = new ArrayList<>();
		for (Object originalObject : shape.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Interface) {
					interfaces.add(getPapyrusClassDiagram().getPapyrusInterface(this, shape));
				}
			}
		}
		return interfaces.toArray(new PapyrusInterface[interfaces.size()]);
	}
}
