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

package org.eclipse.papyrus.gamification.modelutils.copiers;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.commands.wrappers.GEFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassNameEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage.Literals;
import org.eclipse.uml2.uml.VisibilityKind;


/**
 * @author lepallec
 *
 */
public class PapyrusUtils {

	static PapyrusUtils INSTANCE = new PapyrusUtils();

	/**
	 * @return the iNSTANCE
	 */

	public static PapyrusUtils getINSTANCE() {
		return INSTANCE;
	}

	//
	// CREATE ASSOCIATION
	//

	/*
	 * from org.eclipse.papyrus.uml.diagram.tests.canonical.TestLink
	 *
	 * public CreateConnectionViewRequest createConnectionViewRequest(IElementType type, EditPart source, EditPart target) {
	 * CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type, ((IGraphicalEditPart) getDiagramEditPart()).getDiagramPreferencesHint());
	 * connectionRequest.setSourceEditPart(null);
	 * connectionRequest.setTargetEditPart(source);
	 * connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
	 * source.getCommand(connectionRequest);
	 * // Now, setup the request in preparation to get the
	 * // connection end
	 * // command.
	 * connectionRequest.setSourceEditPart(source);
	 * connectionRequest.setTargetEditPart(target);
	 * connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
	 * return connectionRequest;
	 * }
	 */
	/**
	 * @param sourceEditPart
	 * @param targetEditPart
	 */
	public void createAssociation(DiagramEditPart diagramEditPart, TransactionalEditingDomain transactionalEditingDomain, GraphicalEditPart sourceEditPart, GraphicalEditPart targetEditPart) {
		IElementType type = UMLDIElementTypes.ASSOCIATION_NON_DIRECTED_EDGE;

		CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type,
				diagramEditPart.getDiagramPreferencesHint());

		connectionRequest.setSourceEditPart(null);
		connectionRequest.setTargetEditPart(sourceEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(connectionRequest);

		connectionRequest.setSourceEditPart(sourceEditPart);
		connectionRequest.setTargetEditPart(targetEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
		org.eclipse.gef.commands.Command creationAssociationCommand = targetEditPart.getCommand(connectionRequest);

		transactionalEditingDomain.getCommandStack().execute(GEFtoEMFCommandWrapper.wrap(creationAssociationCommand));
	}

	/**
	 * @param sourceClassEditPart
	 * @param targetInterfaceEditPart
	 */
	public void createInterfaceRealization(DiagramEditPart diagramEditPart, TransactionalEditingDomain transactionalEditingDomain, GraphicalEditPart sourceClassEditPart, GraphicalEditPart targetInterfaceEditPart) {
		IElementType type = UMLDIElementTypes.INTERFACE_REALIZATION_EDGE;

		CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type,
				diagramEditPart.getDiagramPreferencesHint());

		connectionRequest.setSourceEditPart(null);
		connectionRequest.setTargetEditPart(sourceClassEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		sourceClassEditPart.getCommand(connectionRequest);

		connectionRequest.setSourceEditPart(sourceClassEditPart);
		connectionRequest.setTargetEditPart(targetInterfaceEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
		org.eclipse.gef.commands.Command creationAssociationCommand = targetInterfaceEditPart.getCommand(connectionRequest);

		transactionalEditingDomain.getCommandStack().execute(GEFtoEMFCommandWrapper.wrap(creationAssociationCommand));
	}

	/**
	 * @param sourceEditPart
	 * @param targetEditPart
	 */
	public void createConnector(IHintedType connectionType, DiagramEditPart diagramEditPart, TransactionalEditingDomain transactionalEditingDomain, org.eclipse.gef.GraphicalEditPart sourceEditPart, org.eclipse.gef.GraphicalEditPart targetEditPart) {
		IElementType type = connectionType; // UMLDIElementTypes.INTERFACE_REALIZATION_EDGE;

		CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type,
				diagramEditPart.getDiagramPreferencesHint());

		connectionRequest.setSourceEditPart(null);
		connectionRequest.setTargetEditPart(sourceEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(connectionRequest);

		connectionRequest.setSourceEditPart(sourceEditPart);
		connectionRequest.setTargetEditPart(targetEditPart);
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
		org.eclipse.gef.commands.Command creationAssociationCommand = targetEditPart.getCommand(connectionRequest);

		transactionalEditingDomain.getCommandStack().execute(GEFtoEMFCommandWrapper.wrap(creationAssociationCommand));
	}

	//
	// GET SHAPE LOCATION
	//

	/**
	 * @param shape
	 * @return
	 */
	public Point getShapeLocation(Shape shape) {
		LayoutConstraint layoutConstraint = shape.getLayoutConstraint();
		int x = ((Integer) layoutConstraint.eGet(NotationPackage.eINSTANCE.getLocation_X()));
		int y = ((Integer) layoutConstraint.eGet(NotationPackage.eINSTANCE.getLocation_Y()));
		return new Point(x, y);
	}

	//
	// CREATE GRAPHICAL UML CLASS
	//

	public Class createGraphicalUmlClass(DiagramEditPart diagramEditPart, String className, Point classLocation) {
		// Prepare the request to create the (copied) class with the correct location
		CreateViewRequest requestToCreateAClass = umlClassCreationRequest(diagramEditPart, classLocation);

		// Create and execute the corresponding command

		org.eclipse.gef.commands.Command commandToCreateAClass = diagramEditPart
				.getCommand(requestToCreateAClass);
		commandToCreateAClass.execute();

		// Name the target class and insert in general mappings

		Class targetClass = fetchCreatedClass(commandToCreateAClass);

		setElementName(targetClass, className);

		return targetClass;
	}

	//
	// ASSOCIATED METHOD TO **CREATE GRAPHICAL UML CLASS**
	//

	private Class fetchCreatedClass(org.eclipse.gef.commands.Command commandToCreateAClass) {
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAClass);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				if (adapter.resolve() instanceof org.eclipse.uml2.uml.Class) {
					return org.eclipse.uml2.uml.Class.class.cast(adapter.resolve());
				}
			}
		}
		return null;
	}

	/**
	 * @param graphicSourceClass
	 * @return
	 */
	private CreateViewRequest umlClassCreationRequest(DiagramEditPart diagramEditPart, Point classLocation) {
		CreateViewRequest requestToCreateAClass = CreateViewRequestFactory.getCreateShapeRequest(
				UMLElementTypes.Class_Shape, diagramEditPart.getDiagramPreferencesHint());
		requestToCreateAClass.setLocation(classLocation);
		return requestToCreateAClass;
	}


	//
	// GET ATTRIBUTE COMPARTMENT OF A CLASSIFIER
	//

	public ClassAttributeCompartmentEditPart getAttributeCompartment(GraphicalEditPart fakeClassEditPart) {


		for (Object child : fakeClassEditPart.getChildren()) {
			if (child instanceof ClassAttributeCompartmentEditPart) {
				return ClassAttributeCompartmentEditPart.class.cast(child);
			}
		}
		return null;
	}

	//
	// GET NAME COMPARTMENT OF A CLASSIFIER
	//


	public ClassNameEditPart getNameCompartment(GraphicalEditPart classifierEditPart) {
		for (Object child : classifierEditPart.getChildren()) {
			if (child instanceof ClassNameEditPart) {
				return ClassNameEditPart.class.cast(child);
			}
		}
		return null;
	}

	//
	// GET OPERATION COMPARTMENT OF A CLASS
	//


	public ClassOperationCompartmentEditPart getOperationCompartment(GraphicalEditPart classifierEditPart) {
		for (Object child : classifierEditPart.getChildren()) {
			if (child instanceof ClassOperationCompartmentEditPart) {
				return ClassOperationCompartmentEditPart.class.cast(child);
			}
		}
		return null;
	}
	//
	// GET ATTRIBUTE EDIT PART OF A ATTRIBUTE IN A CLASS
	//

	public GraphicalEditPart getAttributeEditPart(GraphicalEditPart containerClassEdit, Property attribute) {
		GraphicalEditPart propertyEditPart = null;
		for (Object child : containerClassEdit.getChildren()) {
			if (child instanceof GraphicalEditPart) {
				GraphicalEditPart editPart = GraphicalEditPart.class.cast(child);
				if (editPart.resolveSemanticElement() == attribute) {
					propertyEditPart = editPart;
				}
			}
		}
		return propertyEditPart;
	}

	//
	// GET OPERATION EDIT PART OF A ATTRIBUTE IN A CLASS
	//

	public GraphicalEditPart getOperationEditPart(GraphicalEditPart classifierEditPart, Operation operation) {
		GraphicalEditPart propertyEditPart = null;

		for (Object child : getOperationCompartment(classifierEditPart).getChildren()) {
			if (child instanceof GraphicalEditPart) {
				GraphicalEditPart editPart = GraphicalEditPart.class.cast(child);
				if (editPart.resolveSemanticElement() == operation) {
					propertyEditPart = editPart;
				}
			}
		}
		return propertyEditPart;
	}

	//
	// CREATE GRAPHICAL UML INTERFACE
	//

	public Interface createGraphicalUmlInterface(DiagramEditPart diagramEditPart, String interfaceName, Point interfaceLocation) {
		// Prepare the request to create the (copied) interface with the correct location
		CreateViewRequest requestToCreateAnInterface = umlInterfaceCreationRequest(diagramEditPart, interfaceLocation);

		// Create and execute the corresponding command

		org.eclipse.gef.commands.Command commandToCreateAnInterface = diagramEditPart
				.getCommand(requestToCreateAnInterface);
		commandToCreateAnInterface.execute();

		// Name the target interface and insert in general mappings

		Interface targetInterface = fetchCreatedInterface(commandToCreateAnInterface);

		setElementName(targetInterface, interfaceName);

		return targetInterface;
	}

	//
	// ASSOCIATED METHOD TO **CREATE GRAPHICAL UML CLASS**
	//

	private Interface fetchCreatedInterface(org.eclipse.gef.commands.Command commandToCreateAnInterface) {
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAnInterface);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				if (adapter.resolve() instanceof org.eclipse.uml2.uml.Interface) {
					return org.eclipse.uml2.uml.Interface.class.cast(adapter.resolve());
				}
			}
		}
		return null;
	}

	/**
	 * @param graphicSourceClass
	 * @return
	 */
	private CreateViewRequest umlInterfaceCreationRequest(DiagramEditPart diagramEditPart, Point classLocation) {
		CreateViewRequest requestToCreateAnInterface = CreateViewRequestFactory.getCreateShapeRequest(
				UMLElementTypes.Interface_Shape, diagramEditPart.getDiagramPreferencesHint());
		requestToCreateAnInterface.setLocation(classLocation);
		return requestToCreateAnInterface;
	}

	//
	// SET NAME
	//


	public void setElementName(EObject element, String name) {
		createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__NAME, name),
				element).execute();
	}

	//
	// SET ELEMENT TYPE
	//

	public void setElementType(EObject element, Type type) {
		createSetCommand(new SetRequest(element, Literals.TYPED_ELEMENT__TYPE, type),
				element).execute();
	}

	//
	// SET ELEMENT VISIBILITY
	//

	public void setElementVisibility(EObject element, String visibility) {
		VisibilityKind visibilityKind = VisibilityKind.get(visibility);
		createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__VISIBILITY, visibilityKind),
				element).execute();

	}

	//
	// SET ELEMENT LIFE COLOR
	//

	public void setElementLineColor(EObject element, int color) {
		createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.LINE_STYLE__LINE_COLOR, color),
				element).execute();

	}

	//
	// SET ELEMENT LIFE WIDTH
	//

	public void setElementLineWidth(EObject element, int width) {
		createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.LINE_STYLE__LINE_WIDTH, width),
				element).execute();

	}

	//
	// SET ELEMENT LIFE WIDTH
	//

	public void setElementBackgroundColor(EObject element, int color) {
		createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.FILL_STYLE__FILL_COLOR, color),
				element).execute();

	}

	//
	// SET ELEMENT VISUAL VISIBILITY
	//

	public void setElementVisualVisibility(EObject element, boolean visible) {
		createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.VIEW__VISIBLE, visible),
				element).execute();

	}

	//
	// CREATE SET COMMAND (generic method)
	//

	public Command createSetCommand(SetRequest setRequest, EObject eObject) {
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(eObject);
		ICommand setCommand = commandProvider.getEditCommand(setRequest);
		return new GMFtoEMFCommandWrapper(setCommand);
	}

	//
	// CREATE ELEMENT
	//

	public Object createElement(
			DiagramEditPart diagramEditPart,
			GraphicalEditPart container,
			IElementType elementType, java.lang.Class expectedValueType) {

		CreateViewRequest requestToCreateAClass = CreateViewRequestFactory.getCreateShapeRequest(
				elementType, diagramEditPart.getDiagramPreferencesHint());

		org.eclipse.gef.commands.Command commandToCreateAClass = container
				.getCommand(requestToCreateAClass);
		commandToCreateAClass.execute();
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAClass);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				if (expectedValueType.isInstance(adapter.resolve())) {
					return expectedValueType.cast(adapter.resolve());
				}
			}
		}
		return null;
	}

	// CREATE CLASS ATTRIBUTE

	public Property createClassAttribute(DiagramEditPart diagramEditPart,
			GraphicalEditPart container) {
		return Property.class.cast(
				createElement(diagramEditPart, container, UMLElementTypes.Property_ClassAttributeLabel, Property.class));
	}

	// CREATE CLASS OPERATION

	public Operation createClassOperation(DiagramEditPart diagramEditPart,
			GraphicalEditPart container) {
		return Operation.class.cast(
				createElement(diagramEditPart, container, UMLElementTypes.Operation_ClassOperationLabel, Operation.class));
	}


	// CREATE INTERFACE ATTRIBUTE

	public Property createInterfaceAttribute(DiagramEditPart diagramEditPart,
			GraphicalEditPart container) {
		return Property.class.cast(
				createElement(diagramEditPart, container, UMLElementTypes.Property_InterfaceAttributeLabel, Property.class));
	}

	// CREATE INTERFACE OPERATION

	public Operation createInterfaceOperation(DiagramEditPart diagramEditPart,
			GraphicalEditPart container) {
		return Operation.class.cast(
				createElement(diagramEditPart, container, UMLElementTypes.Operation_InterfaceOperationLabel, Operation.class));
	}


	// CREATE PRIMITIVE TYPE

	public PrimitiveType createPrimitiveType(DiagramEditPart diagramEditPart) {
		return PrimitiveType.class.cast(
				createElement(diagramEditPart, diagramEditPart, UMLElementTypes.PrimitiveType_Shape, PrimitiveType.class));
	}

	//
	// GET EDIT PART
	//

	public org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart getEditPart(DiagramEditPart diagramEditPart, EObject element) {
		for (Object child : diagramEditPart.getChildren()) {
			EditPart currentEditPart = EditPart.class.cast(child);
			if (currentEditPart.getModel() instanceof Shape) {
				Shape shape = Shape.class.cast(currentEditPart.getModel());
				if (shape.getElement().equals(element)) {
					return org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart.class.cast(currentEditPart);
				}
			}

		}
		return null;
	}

}
