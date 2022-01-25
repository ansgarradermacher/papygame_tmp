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

package org.eclipse.papyrus.gamification.modelutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
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
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.commands.wrappers.GEFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class DiagramCopier {

	private static final String PARAMETER_SUFFIX = "ZZZ";
	private DiagramEditPart targetDiagram;
	private Diagram sourceDiagram;
	Map<org.eclipse.uml2.uml.Type, org.eclipse.uml2.uml.Type> sourceIndexedMapping = new HashMap<>();
	Map<EObject, EObject> targetIndexedMapping = new HashMap<>();
	private CopierObserver copierObserver;

	Class fakeClass;
	GraphicalEditPart fakeClassEditPart;

	public DiagramCopier(DiagramEditPart targetDiagram, Diagram sourceDiagram) {
		this.sourceDiagram = sourceDiagram;
		this.targetDiagram = targetDiagram;
	}

	public void setCopierObserver(CopierObserver copierObserver) {
		this.copierObserver = copierObserver;
	}

	public boolean doIMakeACopy(EObject object) {
		if (copierObserver != null) {
			return copierObserver.doIMakeACopy(object);
		} else {
			return true;
		}
	}

	public void fakeInit() {
		fakeClass = createFakeClass();
		fakeClassEditPart = getEditPart(fakeClass);

	}

	public Class createFakeClass() {

		CreateViewRequest requestToCreateAClass = CreateViewRequestFactory.getCreateShapeRequest(
				UMLElementTypes.Class_Shape, targetDiagram.getDiagramPreferencesHint());
		requestToCreateAClass.setLocation(new Point(100, 300));

		org.eclipse.gef.commands.Command commandToCreateAClass = targetDiagram
				.getCommand(requestToCreateAClass);
		commandToCreateAClass.execute();
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAClass);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				if (adapter.resolve() instanceof org.eclipse.uml2.uml.Class) {
					Class copyClass = org.eclipse.uml2.uml.Class.class.cast(adapter.resolve());
					setElementName(copyClass, "FakeClass");
					return copyClass;
				}
			}
		}
		return null;
	}

	//
	// STARTING POINT => COPY
	//

	public Map<EObject, EObject> copy(TransactionalEditingDomain transactionalEditingDomain) {


		RecordingCommand command = new RecordingCommand(transactionalEditingDomain) {

			@Override
			protected void doExecute() {
				TypeHelper typeHelper = new TypeHelper(sourceDiagram);
				sourceIndexedMapping.put(typeHelper.getPrimitiveType("String"), createPrimitiveType("String"));

				copyClasses(sourceDiagram);
				copyAssociations(sourceDiagram, transactionalEditingDomain);

			}
		};
		transactionalEditingDomain.getCommandStack().execute(command);

		return targetIndexedMapping;
	}

	private Command createSetCommand(SetRequest setRequest, EObject eObject) {
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(eObject);
		ICommand setCommand = commandProvider.getEditCommand(setRequest);
		return new GMFtoEMFCommandWrapper(setCommand);
	}

	private void setElementName(EObject element, String name) {
		createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__NAME, name),
				element).execute();
	}

	private void setElementType(EObject element, Type type) {
		createSetCommand(new SetRequest(element, Literals.TYPED_ELEMENT__TYPE, type),
				element).execute();
	}

	protected void copyAttributes(org.eclipse.uml2.uml.Class originalClass, org.eclipse.uml2.uml.Class targetClass, Map<Type, Type> originalCopyMap) {
		GraphicalEditPart targetClassEditPart = getEditPart(targetClass);
		EList<Property> properties = originalClass.getAllAttributes();
		for (Property property : properties) {
			copyAttribute(property, targetClassEditPart, originalCopyMap);
		}
	}

	//
	// COPY ATTRIBUTE
	//

	protected void copyAttribute(Property property, GraphicalEditPart targetClassEditPart, Map<Type, Type> originalCopyMap) {

		if (doIMakeACopy(property)) {
			copyAttributeWithoutChecking(property, targetClassEditPart, originalCopyMap);
		}

	}

	public void copyAttributeWithoutChecking(Property property, GraphicalEditPart targetClassEditPart, Map<Type, Type> originalCopyMap) {

		ClassAttributeCompartmentEditPart attributeCompartment = null;
		for (Object child : targetClassEditPart.getChildren()) {
			if (child instanceof ClassAttributeCompartmentEditPart) {
				attributeCompartment = ClassAttributeCompartmentEditPart.class.cast(child);
			}
		}
		if (attributeCompartment == null) {
			return;
		}
		Property newProperty = Property.class.cast(createEObject(attributeCompartment, UMLElementTypes.Property_ClassAttributeLabel, Property.class));
		setElementName(newProperty, property.getName());
		setElementType(newProperty, originalCopyMap.get(property.getType()));
		targetIndexedMapping.put(newProperty, property);

		ClassAttributeCompartmentEditPart fakeAttributeCompartment = null;
		for (Object child : fakeClassEditPart.getChildren()) {
			if (child instanceof ClassAttributeCompartmentEditPart) {
				fakeAttributeCompartment = ClassAttributeCompartmentEditPart.class.cast(child);
			}
		}
		if (fakeAttributeCompartment == null) {
			return;
		}

		if (newProperty.getName().endsWith(PARAMETER_SUFFIX)) {
			setElementName(newProperty, newProperty.getName().replace(PARAMETER_SUFFIX, ""));
			GraphicalEditPart propertyEditPart = null;
			for (Object child : attributeCompartment.getChildren()) {
				if (child instanceof GraphicalEditPart) {
					GraphicalEditPart editPart = GraphicalEditPart.class.cast(child);
					if (editPart.resolveSemanticElement() == newProperty) {
						propertyEditPart = editPart;
					}
				}
			}

			if (propertyEditPart != null) {
				ChangeBoundsRequest request2 = new ChangeBoundsRequest();
				request2.setEditParts(propertyEditPart);
				request2.setLocation(new Point(1, 1));
				request2.setType(org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants.REQ_DROP);
				org.eclipse.gef.commands.Command command2 = fakeAttributeCompartment.getCommand(request2);
				command2.execute();

			}
		}


	}

	//
	// COPY CLASS
	//

	protected void copyClass(Shape shape, org.eclipse.uml2.uml.Class originalClass) {

		if (doIMakeACopy(originalClass)) {
			copyClassWithoutChecking(shape, originalClass);
		}

	}

	public void copyClassWithoutChecking(Shape shape, org.eclipse.uml2.uml.Class originalClass) {

		CreateViewRequest requestToCreateAClass = CreateViewRequestFactory.getCreateShapeRequest(
				UMLElementTypes.Class_Shape, targetDiagram.getDiagramPreferencesHint());
		LayoutConstraint layoutConstraint = shape.getLayoutConstraint();
		double x = ((Integer) layoutConstraint.eGet(NotationPackage.eINSTANCE.getLocation_X())).doubleValue();
		double y = ((Integer) layoutConstraint.eGet(NotationPackage.eINSTANCE.getLocation_Y())).doubleValue();
		requestToCreateAClass.setLocation(new Point(x, y));

		org.eclipse.gef.commands.Command commandToCreateAClass = targetDiagram
				.getCommand(requestToCreateAClass);
		commandToCreateAClass.execute();
		Collection returnValues = DiagramCommandStack.getReturnValues(commandToCreateAClass);
		for (Object returnValue : returnValues) {
			if (returnValue instanceof CreateElementRequestAdapter) {
				CreateElementRequestAdapter adapter = CreateElementRequestAdapter.class.cast(returnValue);
				if (adapter.resolve() instanceof org.eclipse.uml2.uml.Class) {
					Class copyClass = org.eclipse.uml2.uml.Class.class.cast(adapter.resolve());
					setElementName(copyClass, originalClass.getName());
					sourceIndexedMapping.put(originalClass, copyClass);
					targetIndexedMapping.put(copyClass, originalClass);
					copyAttributes(originalClass, copyClass, sourceIndexedMapping);
				}
			}
		}
	}

	//
	// COPY OBJECT
	//

	protected Object createEObject(org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart container, IElementType elementType, java.lang.Class expectedValueType) {
		CreateViewRequest requestToCreateAClass = CreateViewRequestFactory.getCreateShapeRequest(
				elementType, targetDiagram.getDiagramPreferencesHint());

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

	//
	// CREATE OBJECT AND GET EDIT PART
	//

	protected org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart createEObjectAndGetEditPart(GraphicalEditPart container, IElementType elementType,
			java.lang.Class expectedValueType) {
		EObject newObject = EObject.class.cast(createEObject(container, elementType, expectedValueType));
		return getEditPart(newObject);
	}

	//
	// GET EDIT PART
	//

	public org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart getEditPart(EObject element) {
		for (Object child : targetDiagram.getChildren()) {
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

	//
	// COPY ASSOCIATION
	//

	protected void copyAssociation(TransactionalEditingDomain transactionalEditingDomain, Connector connector) {
		if (doIMakeACopy(connector)) {
			copyAssociationWithoutChecking(transactionalEditingDomain, connector);
		}
	}

	public void copyAssociationWithoutChecking(TransactionalEditingDomain transactionalEditingDomain, Connector connector) {

		Association originalAssociation = Association.class.cast(connector.getElement());
		EList<Property> memberEnds = originalAssociation.getMemberEnds();

		Property originalSource = originalAssociation.getMemberEnds().get(0);
		Property originalTarget = originalAssociation.getMemberEnds().get(1);

		Class source = Class.class.cast(sourceIndexedMapping.get(originalSource.getType()));
		Class target = Class.class.cast(sourceIndexedMapping.get(originalTarget.getType()));

		GraphicalEditPart sourceEditPart = getEditPart(source);
		GraphicalEditPart targetEditPart = getEditPart(target);

		IElementType type = UMLDIElementTypes.ASSOCIATION_NON_DIRECTED_EDGE;

		CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type,
				targetDiagram.getDiagramPreferencesHint());

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
	// COPY ALL CLASSES
	//

	protected void copyClasses(Diagram diagramLevel2) {
		for (Object originalObject : diagramLevel2.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Class) {
					org.eclipse.uml2.uml.Class originalClass = org.eclipse.uml2.uml.Class.class.cast(shape.getElement());
					copyClass(shape, originalClass);
				}

			}

		}
	}

	//
	// COPY ALL ASSOCIATIONS
	//

	protected void copyAssociations(Diagram diagramLevel2, TransactionalEditingDomain transactionalEditingDomain) {
		for (Object originalConnector : diagramLevel2.getEdges()) {
			if (originalConnector instanceof Connector) {
				Connector connector = Connector.class.cast(originalConnector);
				if (connector.getElement() instanceof Association) {
					copyAssociation(transactionalEditingDomain, connector);
				}
			}
		}
	}

	//
	// CREATE PRIMITIVE TYPE
	//


	protected EObject createPrimitiveType(TransactionalEditingDomain transaction, EObject container, String name) {
		CreateElementRequest request = new CreateElementRequest(transaction, container, UMLElementTypes.DataType_Shape);
		return null;
	}

	protected PrimitiveType createPrimitiveType(String name) {
		GraphicalEditPart dataTypeEditPart = createEObjectAndGetEditPart(targetDiagram, UMLElementTypes.PrimitiveType_Shape, DataType.class);
		Shape shape = Shape.class.cast(dataTypeEditPart.getModel());
		setElementName(shape.getElement(), name);
		try {
			shape.setVisible(false);
		} catch (Exception ex) {

		}
		return PrimitiveType.class.cast(shape.getElement());
	}
}
