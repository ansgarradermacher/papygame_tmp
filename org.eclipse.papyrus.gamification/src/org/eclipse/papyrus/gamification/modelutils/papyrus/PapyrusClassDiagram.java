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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.GeneralizationEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PackageEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PrimitiveTypeEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.ClassifierEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLCompartmentEditPart;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * @author lepallec
 *
 */
public class PapyrusClassDiagram {

	// For read-only use

	Diagram diagram = null;

	// for read-write use

	DiagramEditPart diagramEditPart = null;
	TransactionalEditingDomain transactionalEditingDomain = null;

	// for both uses

	Map<EObject, PapyrusNode> papyrusNodeMapping = new HashMap<>();
	Map<EObject, PapyrusLink> papyrusLinkMapping = new HashMap<>();

	// Constructor for read-only use

	public PapyrusClassDiagram(Diagram diagram) {
		super();
		this.diagram = diagram;
		loadDefaultPrimitiveTypes();
	}

	// Constructor for read-write use

	public PapyrusClassDiagram(TransactionalEditingDomain transactionalEditingDomain, DiagramEditPart diagramEditPart) {
		super();
		this.transactionalEditingDomain = transactionalEditingDomain;
		this.diagramEditPart = diagramEditPart;
	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************


	public boolean isReadOnly() {
		return transactionalEditingDomain == null;
	}


	public Diagram getNotationalDiagram() {
		if (diagramEditPart != null) {
			return Diagram.class.cast(diagramEditPart.getModel());
		} else {
			return diagram;
		}
	}

	public DiagramEditPart getDiagramEditPart() {
		return diagramEditPart;
	}

	public TransactionalEditingDomain getTransactionalEditingDomain() {
		return transactionalEditingDomain;
	}

	public Collection<PapyrusNode> allNodes() {
		return papyrusNodeMapping.values();
	}

	// *************
	//
	// ASSOCIATIONS stuff
	//
	// *************

	public PapyrusAssociation addAssociation(String associationName, PapyrusNode source, PapyrusNode target) {
		if (!isReadOnly()) {
			return PapyrusLinkFactory.getInstance().createAssociation(this, associationName, source, target);
		}
		return null;
	}

	public PapyrusAssociation[] getAssociations() {
		if (isReadOnly()) {
			return getAssociationsReadOnlyMode();
		} else {
			return getAssociationsReadWriteMode();
		}
	}

	protected PapyrusAssociation[] getAssociationsReadWriteMode() {
		ArrayList<PapyrusAssociation> associations = new ArrayList<>();
		for (Object child : diagramEditPart.getConnections()) {
			if (child instanceof AssociationEditPart) {
				associations.add(getPapyrusAssociation(AssociationEditPart.class.cast(child)));
			}
		}
		return associations.toArray(new PapyrusAssociation[associations.size()]);
	}

	protected PapyrusAssociation[] getAssociationsReadOnlyMode() {
		ArrayList<PapyrusAssociation> associations = new ArrayList<>();
		for (Object originalObject : diagram.getEdges()) {
			if (originalObject instanceof Connector) {
				Connector connector = Connector.class.cast(originalObject);
				if (connector.getElement() instanceof org.eclipse.uml2.uml.Association) {
					associations.add(getPapyrusAssociation(connector));
				}
			}
		}
		return associations.toArray(new PapyrusAssociation[associations.size()]);
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

	protected PapyrusClass[] getClassesReadWriteMode() {
		ArrayList<PapyrusClass> classes = new ArrayList<>();
		for (Object child : diagramEditPart.getChildren()) {
			if (child instanceof ClassEditPart) {
				classes.add(getPapyrusClass(ClassEditPart.class.cast(child)));
			}
		}
		return classes.toArray(new PapyrusClass[classes.size()]);
	}

	protected PapyrusClass[] getClassesReadOnlyMode() {
		ArrayList<PapyrusClass> classes = new ArrayList<>();
		for (Object originalObject : diagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Class) {
					classes.add(getPapyrusClass(shape));
				}
			}
		}
		return classes.toArray(new PapyrusClass[classes.size()]);
	}

	// *************
	//
	// GENERALIZATIONS stuff
	//
	// *************

	public PapyrusGeneralization addGeneralization(PapyrusClassifier subclass, PapyrusClassifier superclass) {
		if (!isReadOnly()) {
			return PapyrusLinkFactory.getInstance().createGeneralization(this, subclass, superclass);
		}
		return null;
	}

	public PapyrusGeneralization[] getGeneralizations() {
		if (isReadOnly()) {
			return getGeneralizationsReadOnlyMode();
		} else {
			return getGeneralizationsReadWriteMode();
		}
	}

	protected PapyrusGeneralization[] getGeneralizationsReadWriteMode() {
		ArrayList<PapyrusGeneralization> generalizations = new ArrayList<>();
		for (Object child : diagramEditPart.getConnections()) {
			if (child instanceof GeneralizationEditPart) {
				generalizations.add(getPapyrusGeneralization(GeneralizationEditPart.class.cast(child)));
			}
		}
		return generalizations.toArray(new PapyrusGeneralization[generalizations.size()]);
	}

	protected PapyrusGeneralization[] getGeneralizationsReadOnlyMode() {
		ArrayList<PapyrusGeneralization> generalizations = new ArrayList<>();
		for (Object originalObject : diagram.getEdges()) {
			if (originalObject instanceof Connector) {
				Connector connector = Connector.class.cast(originalObject);
				if (connector.getElement() instanceof org.eclipse.uml2.uml.Generalization) {
					generalizations.add(getPapyrusGeneralization(connector));
				}
			}
		}
		return generalizations.toArray(new PapyrusGeneralization[generalizations.size()]);
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
		for (Object child : diagramEditPart.getChildren()) {
			if (child instanceof InterfaceEditPart) {
				interfaces.add(getPapyrusInterface(ClassEditPart.class.cast(child)));
			}
		}
		return interfaces.toArray(new PapyrusInterface[interfaces.size()]);
	}

	protected PapyrusInterface[] getInterfacesReadOnlyMode() {
		ArrayList<PapyrusInterface> interfaces = new ArrayList<>();
		for (Object originalObject : diagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Interface) {
					interfaces.add(getPapyrusInterface(shape));
				}
			}
		}
		return interfaces.toArray(new PapyrusInterface[interfaces.size()]);
	}

	// *************
	//
	// PACKAGES stuff
	//
	// *************

	public PapyrusPackage addPackage(String packageName, int x, int y, int w, int h) {
		if (!isReadOnly()) {
			return PapyrusNodeFactory.getInstance().createPackage(this, packageName, x, y, w, h);
		}
		return null;
	}

	public PapyrusPackage[] getPackages() {
		if (isReadOnly()) {
			return getPackagesReadOnlyMode();
		} else {
			return getPackagesReadWriteMode();
		}
	}

	protected PapyrusPackage[] getPackagesReadWriteMode() {
		ArrayList<PapyrusPackage> packages = new ArrayList<>();
		for (Object child : diagramEditPart.getChildren()) {
			if (child instanceof PackageEditPart) {
				packages.add(getPapyrusPackage(PackageEditPart.class.cast(child)));
			}
		}
		return packages.toArray(new PapyrusPackage[packages.size()]);
	}

	protected PapyrusPackage[] getPackagesReadOnlyMode() {
		ArrayList<PapyrusPackage> packages = new ArrayList<>();
		for (Object originalObject : diagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Package) {
					packages.add(getPapyrusPackage(shape));
				}
			}
		}
		return packages.toArray(new PapyrusPackage[packages.size()]);
	}

	// *************
	//
	// PRIMITIVE TYPES stuff
	//
	// *************

	public PapyrusPrimitiveType addPrimitive(String primitiveTypeName) {
		if (!isReadOnly()) {
			return PapyrusNodeFactory.getInstance().createPrimitiveType(this, primitiveTypeName);
		}
		return null;
	}

	public PapyrusPrimitiveType[] getPrimitiveTypes() {
		if (isReadOnly()) {
			return getPrimitiveTypesReadOnlyMode();
		} else {
			return getPrimitiveTypesReadWriteMode();
		}
	}

	protected PapyrusPrimitiveType[] getPrimitiveTypesReadWriteMode() {
		ArrayList<PapyrusPrimitiveType> primitiveTypes = new ArrayList<>();
		for (Object child : diagramEditPart.getChildren()) {
			if (child instanceof PrimitiveTypeEditPart) {
				primitiveTypes.add(getPapyrusPrimitiveType(PrimitiveTypeEditPart.class.cast(child)));
			}
		}
		return primitiveTypes.toArray(new PapyrusPrimitiveType[primitiveTypes.size()]);
	}

	protected PapyrusPrimitiveType[] getPrimitiveTypesReadOnlyMode() {
		ArrayList<PapyrusPrimitiveType> primitiveTypes = new ArrayList<>();
		for (Object originalObject : diagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.PrimitiveType) {
					primitiveTypes.add(getPapyrusPrimitiveType(shape));
				}
			}
		}
		for (PapyrusNode node : papyrusNodeMapping.values()) {
			if (node instanceof PapyrusPrimitiveType && !primitiveTypes.contains(node)) {
				primitiveTypes.add(PapyrusPrimitiveType.class.cast(node));
			}
		}
		return primitiveTypes.toArray(new PapyrusPrimitiveType[primitiveTypes.size()]);
	}

	private void loadDefaultPrimitiveTypes() {
		TypeHelper typeHelper = new TypeHelper(diagram);
		String[] primitiveTypeNames = { "String", "Boolean", "Integer", "Real" };

		for (String primitiveTypeName : primitiveTypeNames) {
			PrimitiveType primitiveType = typeHelper.getPrimitiveType(primitiveTypeName);
			PapyrusPrimitiveType papyrusPrimitiveType = new PapyrusPrimitiveType(this, primitiveType);
			memorizeNode(papyrusPrimitiveType);
		}
	}

	// ************************
	//
	// MAPPING / MEMORIZE stuff
	//
	// ************************

	public void memorizeNode(PapyrusNode papyrusNode) {
		papyrusNodeMapping.put(papyrusNode.getSemanticElement(), papyrusNode);
	}

	public void memorizeNode(EObject element, PapyrusNode papyrusNode) {
		papyrusNodeMapping.put(element, papyrusNode);
	}

	public PapyrusNode getCorrespondingPapyrusNode(EObject element) {
		return papyrusNodeMapping.get(element);
	}

	public void memorizeLink(PapyrusLink papyrusLink) {
		papyrusLinkMapping.put(papyrusLink.getSemanticElement(), papyrusLink);
	}

	public void memorizeLink(EObject link, PapyrusLink papyrusLink) {
		papyrusLinkMapping.put(link, papyrusLink);
	}

	public PapyrusLink getCorrespondingPapyrusLink(EObject link) {
		return papyrusLinkMapping.get(link);
	}

	// ************************
	//
	// GETTING PAPYRUS ELEMENTS (instantiation or already mapped elements)
	//
	// ************************





	public <T> T getCorrespondingPapyrusNode(PackageEditPart shape) {
		PapyrusNode papyrusElement = getCorrespondingPapyrusNode(((View) shape).getElement());
		if (papyrusElement != null) {
			return (T) papyrusElement;
		}
		return null;
	}


	public <T> T getCorrespondingPapyrusNode(ClassifierEditPart shape) {
		PapyrusNode papyrusElement = getCorrespondingPapyrusNode(shape.getNamedElement());
		if (papyrusElement != null) {
			return (T) papyrusElement;
		}
		return null;
	}

	public <T> T getCorrespondingPapyrusNode(ClassEditPart shape) {
		PapyrusNode papyrusElement = getCorrespondingPapyrusNode(((View) shape).getElement());
		if (papyrusElement != null) {
			return (T) papyrusElement;
		}
		return null;
	}

	public <T> T getCorrespondingPapyrusNode(Shape shape) {
		PapyrusNode papyrusElement = getCorrespondingPapyrusNode(shape.getElement());
		if (papyrusElement != null) {
			return (T) papyrusElement;
		}
		return null;
	}

	public <T> T getCorrespondingPapyrusNode(EditPart editPart) {
		Shape shape = Shape.class.cast(editPart.getModel());
		PapyrusNode papyrusElement = getCorrespondingPapyrusNode(shape.getElement());
		if (papyrusElement != null) {
			return (T) papyrusElement;
		}
		return null;
	}

	public <T> T getCorrespondingPapyrusLink(Connector connector) {
		PapyrusLink papyrusLink = getCorrespondingPapyrusLink(connector.getElement());
		if (papyrusLink != null) {
			return (T) papyrusLink;
		}
		return null;
	}

	public <T> T getCorrespondingPapyrusLink(EditPart editPart) {
		Connector connector = Connector.class.cast(editPart.getModel());
		PapyrusLink papyrusLink = getCorrespondingPapyrusLink(connector.getElement());
		if (papyrusLink != null) {
			return (T) papyrusLink;
		}
		return null;
	}
	//
	// ATTRIBUTE
	//

	public PapyrusAttribute getPapyrusAttribute(PapyrusClassifier papyrusClassifier, Shape notationalAttribute) {
		PapyrusAttribute element = this.<PapyrusAttribute> getCorrespondingPapyrusNode(notationalAttribute);
		if (element != null) {
			return element;
		}
		element = new PapyrusAttribute(papyrusClassifier, notationalAttribute);
		memorizeNode(element);
		return element;

	}

	public PapyrusAttribute getPapyrusAttribute(PapyrusClassifier papyrusClassifier, UMLCompartmentEditPart attributeEditPart) {

		PapyrusAttribute element = this.<PapyrusAttribute> getCorrespondingPapyrusNode(attributeEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusAttribute(papyrusClassifier, attributeEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// CLASS
	//

	public PapyrusClass getPapyrusClass(Shape notationalClass) {
		PapyrusClass element = this.<PapyrusClass> getCorrespondingPapyrusNode(notationalClass);
		if (element != null) {
			return element;
		}
		element = new PapyrusClass(this, notationalClass);
		memorizeNode(element);
		return element;
	}

	public PapyrusClass getPapyrusClass(PapyrusPackage papyrusContainer, Shape notationalClass) {
		PapyrusClass element = this.<PapyrusClass> getCorrespondingPapyrusNode(notationalClass);
		if (element != null) {
			return element;
		}
		element = new PapyrusClass(papyrusContainer, notationalClass);
		memorizeNode(element);
		return element;
	}

	public PapyrusClass getPapyrusClass(org.eclipse.papyrus.uml.diagram.common.editparts.ClassEditPart classEditPart) {

		PapyrusClass element = this.<PapyrusClass> getCorrespondingPapyrusNode(classEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusClass(this, classEditPart);
		memorizeNode(element);
		return element;
	}

	public PapyrusClass getPapyrusClass(PapyrusPackage papyrusContainer, org.eclipse.papyrus.uml.diagram.common.editparts.ClassEditPart classEditPart) {

		PapyrusClass element = this.<PapyrusClass> getCorrespondingPapyrusNode(classEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusClass(papyrusContainer, classEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// INTERFACE
	//

	public PapyrusInterface getPapyrusInterface(Shape notationalInterface) {
		PapyrusInterface element = this.<PapyrusInterface> getCorrespondingPapyrusNode(notationalInterface);
		if (element != null) {
			return element;
		}
		element = new PapyrusInterface(this, notationalInterface);
		memorizeNode(element);
		return element;
	}

	public PapyrusInterface getPapyrusInterface(PapyrusPackage papyrusContainer, Shape notationalInterface) {
		PapyrusInterface element = this.<PapyrusInterface> getCorrespondingPapyrusNode(notationalInterface);
		if (element != null) {
			return element;
		}
		element = new PapyrusInterface(papyrusContainer, notationalInterface);
		memorizeNode(element);
		return element;
	}

	public PapyrusInterface getPapyrusInterface(ClassifierEditPart interfaceEditPart) {

		PapyrusInterface element = this.<PapyrusInterface> getCorrespondingPapyrusNode(interfaceEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusInterface(this, interfaceEditPart);
		memorizeNode(element);
		return element;
	}

	public PapyrusInterface getPapyrusInterface(PapyrusPackage papyrusContainer, ClassifierEditPart interfaceEditPart) {

		PapyrusInterface element = this.<PapyrusInterface> getCorrespondingPapyrusNode(interfaceEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusInterface(papyrusContainer, interfaceEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// OPERATION
	//

	public PapyrusOperation getPapyrusOperation(PapyrusClassifier papyrusContainer, Shape notationalOperation) {
		PapyrusOperation element = this.<PapyrusOperation> getCorrespondingPapyrusNode(notationalOperation);
		if (element != null) {
			return element;
		}
		element = new PapyrusOperation(papyrusContainer, notationalOperation);
		memorizeNode(element);
		return element;

	}

	public PapyrusOperation getPapyrusOperation(PapyrusClassifier papyrusContainer, UMLCompartmentEditPart operationEditPart) {

		PapyrusOperation element = this.<PapyrusOperation> getCorrespondingPapyrusNode(operationEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusOperation(papyrusContainer, operationEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// OPERATION PARAMETER
	//

	public PapyrusOperationParameter getPapyrusOperationParameter(PapyrusOperation papyrusContainer, Parameter parameter) {
		PapyrusNode element = this.getCorrespondingPapyrusNode(parameter);
		if (element != null) {
			return PapyrusOperationParameter.class.cast(element);
		}
		element = new PapyrusOperationParameter(papyrusContainer, parameter);
		memorizeNode(element);
		return PapyrusOperationParameter.class.cast(element);
	}

	//
	// PACKAGE
	//

	public PapyrusPackage getPapyrusPackage(Shape notationalPackage) {
		PapyrusPackage element = this.<PapyrusPackage> getCorrespondingPapyrusNode(notationalPackage);
		if (element != null) {
			return element;
		}
		element = new PapyrusPackage(this, notationalPackage);
		memorizeNode(element);
		return element;
	}

	public PapyrusPackage getPapyrusPackage(PapyrusPackage papyrusContainer, Shape notationalPackage) {
		PapyrusPackage element = this.<PapyrusPackage> getCorrespondingPapyrusNode(notationalPackage);
		if (element != null) {
			return element;
		}
		element = new PapyrusPackage(papyrusContainer, notationalPackage);
		memorizeNode(element);
		return element;
	}

	public PapyrusPackage getPapyrusPackage(PackageEditPart packageEditPart) {

		PapyrusPackage element = this.<PapyrusPackage> getCorrespondingPapyrusNode(packageEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusPackage(this, packageEditPart);
		memorizeNode(element);
		return element;
	}

	public PapyrusPackage getPapyrusPackage(PapyrusPackage papyrusContainer, PackageEditPart packageEditPart) {

		PapyrusPackage element = this.<PapyrusPackage> getCorrespondingPapyrusNode(packageEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusPackage(papyrusContainer, packageEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// PRIMITIVE TYPE
	//

	public PapyrusPrimitiveType getPapyrusPrimitiveType(PrimitiveType primitiveType) {
		PapyrusNode element = this.getCorrespondingPapyrusNode(primitiveType);
		if (element != null) {
			return PapyrusPrimitiveType.class.cast(element);
		}
		PapyrusPrimitiveType newPrimitiveType = new PapyrusPrimitiveType(this, primitiveType);
		memorizeNode(newPrimitiveType);
		return newPrimitiveType;
	}

	public PapyrusPrimitiveType getPapyrusPrimitiveType(Shape notationalPrimitiveType) {
		PapyrusPrimitiveType element = this.<PapyrusPrimitiveType> getCorrespondingPapyrusNode(notationalPrimitiveType);
		if (element != null) {
			return element;
		}
		element = new PapyrusPrimitiveType(this, notationalPrimitiveType);
		memorizeNode(element);
		return element;
	}

	public PapyrusPrimitiveType getPapyrusPrimitiveType(PrimitiveTypeEditPart primitiveTypeEditPart) {

		PapyrusPrimitiveType element = this.<PapyrusPrimitiveType> getCorrespondingPapyrusNode(primitiveTypeEditPart);
		if (element != null) {
			return element;
		}
		element = new PapyrusPrimitiveType(this, primitiveTypeEditPart);
		memorizeNode(element);
		return element;
	}

	//
	// TYPE
	//

	public PapyrusType getPapyrusType(Type type) {
		PapyrusNode element = this.getCorrespondingPapyrusNode(type);
		if (element != null) {
			return PapyrusType.class.cast(element);
		}
		return new PapyrusType() {

			@Override
			public TransactionalEditingDomain getTransactionalEditingDomain() {
				return getTransactionalEditingDomain();
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
			public DiagramEditPart getDiagramEditPart() {
				return this.getDiagramEditPart();
			}

			@Override
			public Type getSemanticElement() {
				return type;
			}

			@Override
			public PapyrusClassDiagram getPapyrusClassDiagram() {
				return PapyrusClassDiagram.this;
			}
		};
	}


	//
	// ASSOCIATION
	//

	public PapyrusAssociation getPapyrusAssociation(Connector notationalLink) {
		PapyrusAssociation link = this.<PapyrusAssociation> getCorrespondingPapyrusLink(notationalLink);
		if (link != null) {
			return link;
		}
		link = new PapyrusAssociation(this, notationalLink);
		memorizeLink(link);
		return link;

	}

	public PapyrusAssociation getPapyrusAssociation(AssociationEditPart linkEditPart) {

		PapyrusAssociation link = this.<PapyrusAssociation> getCorrespondingPapyrusLink(linkEditPart);
		if (link != null) {
			return link;
		}
		link = new PapyrusAssociation(this, linkEditPart);
		memorizeLink(link);
		return link;
	}

	public PapyrusAssociation getPapyrusAssociation(PapyrusNode source, PapyrusNode target) {

		for (EObject link : papyrusLinkMapping.keySet()) {
			if (link instanceof Association) {
				Association association = Association.class.cast(link);
				EList<Property> members = association.getMemberEnds();
				if (members.get(0).getType().equals(source.getSemanticElement()) &&
						members.get(1).getType().equals(target.getSemanticElement())) {
					return PapyrusAssociation.class.cast(papyrusLinkMapping.get(association));
				}
			}
		}

		return null;

	}

	//
	// GENERALIZATION
	//

	public PapyrusGeneralization getPapyrusGeneralization(Connector notationalLink) {
		PapyrusGeneralization link = this.<PapyrusGeneralization> getCorrespondingPapyrusLink(notationalLink);
		if (link != null) {
			return link;
		}
		link = new PapyrusGeneralization(this, notationalLink);
		memorizeLink(link);
		return link;

	}

	public PapyrusGeneralization getPapyrusGeneralization(GeneralizationEditPart linkEditPart) {

		PapyrusGeneralization link = this.<PapyrusGeneralization> getCorrespondingPapyrusLink(linkEditPart);
		if (link != null) {
			return link;
		}
		link = new PapyrusGeneralization(this, linkEditPart);
		memorizeLink(link);
		return link;
	}

	public PapyrusGeneralization getPapyrusGeneralization(PapyrusClassifier subclass, PapyrusClassifier superclass) {

		for (EObject link : papyrusLinkMapping.keySet()) {
			if (link instanceof Generalization) {
				Generalization generalization = Generalization.class.cast(link);
				if (generalization.getSpecific().equals(subclass.getSemanticElement()) &&
						generalization.getGeneral().equals(superclass.getSemanticElement())) {
					return PapyrusGeneralization.class.cast(papyrusLinkMapping.get(generalization));
				}
			}
		}

		return null;

	}
}
