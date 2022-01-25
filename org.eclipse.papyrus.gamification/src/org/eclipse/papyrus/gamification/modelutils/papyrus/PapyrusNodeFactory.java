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


import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPartCN;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.OperationForClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.OperationForInterfaceEditpart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PackageEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PrimitiveTypeEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PropertyForClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.PropertyForInterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

public class PapyrusNodeFactory {

	private static final PapyrusNodeFactory INSTANCE = new PapyrusNodeFactory();

	public static PapyrusNodeFactory getInstance() {
		return INSTANCE;
	}

	/*
	 * =======================
	 *
	 * CREATE PACKAGE
	 *
	 * =======================
	 */

	//
	// IN THE CLASS DIAGRAM
	//

	public PapyrusPackage createPackage(PapyrusClassDiagram papyrusClassDiagram, String name, int x, int y, int w, int h) {
		PapyrusGenericFactory<Package, PackageEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Package_Shape);
		PackageEditPart packageEditPart = factory.createElement(papyrusClassDiagram.getDiagramEditPart(), null, x, y, w, h);
		PapyrusPackage papyrusPackage = papyrusClassDiagram.getPapyrusPackage(packageEditPart);
		papyrusPackage.setName(name);
		return papyrusPackage;
	}

	/*
	 * =======================
	 *
	 * CREATE CLASS
	 *
	 * =======================
	 */

	//
	// IN THE CLASS DIAGRAM
	//

	public PapyrusClass createClass(PapyrusClassDiagram papyrusClassDiagram, String name, int x, int y, int w, int h) {
		PapyrusGenericFactory<Class, ClassEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Class_Shape);
		ClassEditPart classEditPart = factory.createElement(papyrusClassDiagram.getDiagramEditPart(), null, x, y, w, h);
		PapyrusClass papyrusClass = papyrusClassDiagram.getPapyrusClass(classEditPart);
		papyrusClass.setName(name);
		return papyrusClass;
	}

	//
	// IN A PACKAGE
	//

	public PapyrusClass createClass(PapyrusPackage papyrusPackage, String name, int x, int y, int w, int h) {
		PapyrusGenericFactory<Class, ClassEditPartCN> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Class_Shape_CN);
		ClassEditPartCN classEditPart = factory.createElement(papyrusPackage.getDiagramEditPart(), papyrusPackage.getContainerCompartmentEditPart(), x, y, w, h);
		PapyrusClass papyrusClass = papyrusPackage.getPapyrusClassDiagram().getPapyrusClass(papyrusPackage, classEditPart);
		papyrusClass.setName(name);
		return papyrusClass;
	}

	/*
	 * =======================
	 *
	 * CREATE INTERFACE
	 *
	 * =======================
	 */

	//
	// IN THE CLASS DIAGRAM
	//

	public PapyrusInterface createInterface(PapyrusClassDiagram papyrusClassDiagram, String name, int x, int y, int w, int h) {
		PapyrusGenericFactory<Interface, InterfaceEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Interface_Shape);
		InterfaceEditPart interfaceEditPart = factory.createElement(papyrusClassDiagram.getDiagramEditPart(), null, x, y, w, h);
		PapyrusInterface papyrusInterface = papyrusClassDiagram.getPapyrusInterface(interfaceEditPart);
		papyrusInterface.setName(name);
		return papyrusInterface;
	}

	//
	// IN A PACKAGE
	//

	public PapyrusInterface createInterface(PapyrusPackage papyrusPackage, String name, int x, int y, int w, int h) {
		PapyrusGenericFactory<Interface, InterfaceEditPartCN> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Interface_Shape_CN);
		InterfaceEditPartCN interfaceEditPart = factory.createElement(papyrusPackage.getDiagramEditPart(), papyrusPackage.getContainerCompartmentEditPart(), x, y, w, h);
		PapyrusInterface papyrusInterface = papyrusPackage.getPapyrusClassDiagram().getPapyrusInterface(papyrusPackage, interfaceEditPart);
		papyrusInterface.setName(name);
		return papyrusInterface;
	}

	/*
	 * =======================
	 *
	 * CREATE PRIMITIVE TYPE
	 *
	 * =======================
	 */

	//
	// IN THE CLASS DIAGRAM
	//

	public PapyrusPrimitiveType createPrimitiveType(PapyrusClassDiagram papyrusClassDiagram, String name) {
		PapyrusGenericFactory<PrimitiveType, PrimitiveTypeEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.PrimitiveType_Shape);
		PrimitiveTypeEditPart primitiveTypeEditPart = factory.createElement(papyrusClassDiagram.getDiagramEditPart(), null);
		PapyrusPrimitiveType papyrusPrimitiveType = papyrusClassDiagram.getPapyrusPrimitiveType(primitiveTypeEditPart);
		papyrusPrimitiveType.setName(name);

		try {
			papyrusPrimitiveType.getNotationalElement().setVisible(false);
		} catch (Exception ex) {

		}
		return papyrusPrimitiveType;
	}


	/*
	 * =======================
	 *
	 * CREATE ATTRIBUTE
	 *
	 * =======================
	 */

	//
	// IN A CLASS NON INCLUDED IN PACKAGE
	//

	public PapyrusAttribute createClassAttribute(PapyrusClass papyrusClass, String attributeName) {
		PapyrusGenericFactory<Property, PropertyForClassEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Property_ClassAttributeLabel, ClassAttributeCompartmentEditPart.class);
		PropertyForClassEditPart attributeEditPart = factory.createElement(papyrusClass.getDiagramEditPart(), (GraphicalEditPart) papyrusClass.getEditPart());
		PapyrusAttribute papyrusAttribute = papyrusClass.getPapyrusClassDiagram().getPapyrusAttribute(papyrusClass, attributeEditPart);
		papyrusAttribute.setName(attributeName);
		return papyrusAttribute;
	}

	//
	// IN A CLASS INCLUDED IN PACKAGE
	//

	public PapyrusAttribute createIncludedClassAttribute(PapyrusClass papyrusClass, String attributeName) {
		PapyrusGenericFactory<Property, PropertyForClassEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Property_ClassAttributeLabel, ClassAttributeCompartmentEditPartCN.class);
		PropertyForClassEditPart attributeEditPart = factory.createElement(papyrusClass.getDiagramEditPart(), (GraphicalEditPart) papyrusClass.getEditPart());
		PapyrusAttribute papyrusAttribute = papyrusClass.getPapyrusClassDiagram().getPapyrusAttribute(papyrusClass, attributeEditPart);
		papyrusAttribute.setName(attributeName);
		return papyrusAttribute;
	}

	//
	// IN AN INTERFACE NON INCLUDED IN PACKAGE
	//

	public PapyrusAttribute createInterfaceAttribute(PapyrusInterface papyrusInterface, String attributeName) {
		PapyrusGenericFactory<Property, PropertyForInterfaceEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Property_InterfaceAttributeLabel, InterfaceAttributeCompartmentEditPart.class);
		PropertyForInterfaceEditPart attributeEditPart = factory.createElement(papyrusInterface.getDiagramEditPart(), (GraphicalEditPart) papyrusInterface.getEditPart());
		PapyrusAttribute papyrusAttribute = papyrusInterface.getPapyrusClassDiagram().getPapyrusAttribute(papyrusInterface, attributeEditPart);
		papyrusAttribute.setName(attributeName);
		return papyrusAttribute;
	}

	//
	// IN AN INTERFACE INCLUDED IN PACKAGE
	//

	public PapyrusAttribute createIncludedInterfaceAttribute(PapyrusInterface papyrusInterface, String attributeName) {
		PapyrusGenericFactory<Property, PropertyForInterfaceEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Property_InterfaceAttributeLabel, InterfaceAttributeCompartmentEditPartCN.class);
		PropertyForInterfaceEditPart attributeEditPart = factory.createElement(papyrusInterface.getDiagramEditPart(), (GraphicalEditPart) papyrusInterface.getEditPart());
		PapyrusAttribute papyrusAttribute = papyrusInterface.getPapyrusClassDiagram().getPapyrusAttribute(papyrusInterface, attributeEditPart);
		papyrusAttribute.setName(attributeName);
		return papyrusAttribute;
	}
	/*
	 * =======================
	 *
	 * CREATE OPERATION
	 *
	 * =======================
	 */

	//
	// IN A CLASS NON INCLUDED IN PACKAGE
	//

	public PapyrusOperation createClassOperation(PapyrusClass papyrusClass, String operationName) {
		PapyrusGenericFactory<Operation, OperationForClassEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Operation_ClassOperationLabel, ClassOperationCompartmentEditPart.class);
		OperationForClassEditPart operationEditPart = factory.createElement(papyrusClass.getDiagramEditPart(), (GraphicalEditPart) papyrusClass.getEditPart());
		PapyrusOperation papyrusOperation = papyrusClass.getPapyrusClassDiagram().getPapyrusOperation(papyrusClass, operationEditPart);
		papyrusOperation.setName(operationName);
		return papyrusOperation;

	}

	//
	// IN A CLASS INCLUDED IN PACKAGE
	//

	public PapyrusOperation createIncludedClassOperation(PapyrusClass papyrusClass, String operationName) {
		PapyrusGenericFactory<Operation, OperationForClassEditPart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Operation_ClassOperationLabel, ClassOperationCompartmentEditPartCN.class);
		OperationForClassEditPart operationEditPart = factory.createElement(papyrusClass.getDiagramEditPart(), (GraphicalEditPart) papyrusClass.getEditPart());
		PapyrusOperation papyrusOperation = papyrusClass.getPapyrusClassDiagram().getPapyrusOperation(papyrusClass, operationEditPart);
		papyrusOperation.setName(operationName);
		return papyrusOperation;

	}

	//
	// IN AN INTERFACE NON INCLUDED IN PACKAGE
	//

	public PapyrusOperation createInterfaceOperation(PapyrusInterface papyrusInterface, String operationName) {
		PapyrusGenericFactory<Operation, OperationForInterfaceEditpart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Operation_InterfaceOperationLabel, InterfaceOperationCompartmentEditPart.class);
		OperationForInterfaceEditpart operationEditPart = factory.createElement(papyrusInterface.getDiagramEditPart(), (GraphicalEditPart) papyrusInterface.getEditPart());
		PapyrusOperation papyrusOperation = papyrusInterface.getPapyrusClassDiagram().getPapyrusOperation(papyrusInterface, operationEditPart);
		papyrusOperation.setName(operationName);
		return papyrusOperation;

	}

	//
	// IN AN INTERFACE INCLUDED IN PACKAGE
	//

	public PapyrusOperation createIncludedInterfaceOperation(PapyrusInterface papyrusInterface, String operationName) {
		PapyrusGenericFactory<Operation, OperationForInterfaceEditpart> factory = new PapyrusGenericFactory<>(
				UMLElementTypes.Operation_InterfaceOperationLabel, InterfaceOperationCompartmentEditPartCN.class);
		OperationForInterfaceEditpart operationEditPart = factory.createElement(papyrusInterface.getDiagramEditPart(), (GraphicalEditPart) papyrusInterface.getEditPart());
		PapyrusOperation papyrusOperation = papyrusInterface.getPapyrusClassDiagram().getPapyrusOperation(papyrusInterface, operationEditPart);
		papyrusOperation.setName(operationName);
		return papyrusOperation;

	}
	/*
	 * =======================
	 *
	 * CREATE PARAMETER
	 *
	 * =======================
	 */

	//
	// IN AN OPERATION
	//
	public PapyrusOperationParameter createOperationParameter(PapyrusOperation papyrusOperation, String parameterName) {

		Parameter parameter = UMLFactory.eINSTANCE.createParameter();

		org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest moveRequest = new MoveRequest(papyrusOperation.getSemanticElement(), parameter);
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(papyrusOperation.getSemanticElement());
		ICommand moveCommand = commandProvider.getEditCommand(moveRequest);
		GMFtoEMFCommandWrapper command = new GMFtoEMFCommandWrapper(moveCommand);
		command.execute();

		PapyrusOperationParameter papyrusOperationParameter = papyrusOperation.getPapyrusClassDiagram().getPapyrusOperationParameter(papyrusOperation, parameter);
		papyrusOperationParameter.setName(parameterName);
		return papyrusOperationParameter;
	}
}