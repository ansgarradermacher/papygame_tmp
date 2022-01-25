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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class DiagramCopier {

	TransactionalEditingDomain currentTransactionalEditingDomain;

	private DiagramEditPart targetDiagram;
	private Diagram sourceDiagram;
	private ClassCopier classCopier;
	private InterfaceCopier interfaceCopier;
	private PrimitiveTypeCopier primitiveTypeCopier;
	private AssociationCopier associationCopier;
	private InterfaceRealizationCopier interfaceRealizationCopier;

	/**
	 * @return the interfaceRealizationCopier
	 */
	public InterfaceRealizationCopier getInterfaceRealizationCopier() {
		return interfaceRealizationCopier;
	}

	private CopierObserver copierObserver;

	RecordingCommand postCommands;


	Map<EObject, EObject> sourceIndexedMapping = new HashMap<>();
	Map<EObject, EObject> targetIndexedMapping = new HashMap<>();

	/**
	 * Constructor.
	 *
	 * @param targetDiagram
	 * @param sourceDiagram
	 */
	public DiagramCopier(DiagramEditPart targetDiagram, Diagram sourceDiagram) {
		super();
		this.targetDiagram = targetDiagram;
		this.sourceDiagram = sourceDiagram;
		this.classCopier = new ClassCopier(this);
		this.interfaceCopier = new InterfaceCopier(this);
		this.primitiveTypeCopier = new PrimitiveTypeCopier(this);
		this.associationCopier = new AssociationCopier(this);
		this.interfaceRealizationCopier = new InterfaceRealizationCopier(this);
	}

	/**
	 * @return the sourceIndexedMapping
	 */
	public Map<EObject, EObject> getSourceIndexedMapping() {
		return sourceIndexedMapping;
	}

	/**
	 * @return the targetIndexedMapping
	 */
	public Map<EObject, EObject> getTargetIndexedMapping() {
		return targetIndexedMapping;
	}

	/**
	 * @return the targetDiagram
	 */
	public DiagramEditPart getTargetDiagram() {
		return targetDiagram;
	}

	/**
	 * @return the sourceDiagram
	 */
	public Diagram getSourceDiagram() {
		return sourceDiagram;
	}




	/**
	 * @return the currentTransactionalEditingDomain
	 */
	public TransactionalEditingDomain getCurrentTransactionalEditingDomain() {
		return currentTransactionalEditingDomain;
	}

	/**
	 * @param currentTransactionalEditingDomain
	 *            the currentTransactionalEditingDomain to set
	 */
	public void setCurrentTransactionalEditingDomain(TransactionalEditingDomain currentTransactionalEditingDomain) {
		this.currentTransactionalEditingDomain = currentTransactionalEditingDomain;
	}

	/**
	 * @return the classCopier
	 */
	public ClassCopier getClassCopier() {
		return classCopier;
	}



	/**
	 * @return the interfaceCopier
	 */
	public InterfaceCopier getInterfaceCopier() {
		return interfaceCopier;
	}

	/**
	 * @return the primitiveTypeCopier
	 */
	public PrimitiveTypeCopier getPrimitiveTypeCopier() {
		return primitiveTypeCopier;
	}

	/**
	 * @return the associationCopier
	 */
	public AssociationCopier getAssociationCopier() {
		return associationCopier;
	}

	//
	// STARTING POINT => COPY
	//

	public Map<EObject, EObject> copy(TransactionalEditingDomain transactionalEditingDomain) {

		this.setCurrentTransactionalEditingDomain(transactionalEditingDomain);



		RecordingCommand command = new RecordingCommand(transactionalEditingDomain) {

			@Override
			protected void doExecute() {
				copyPrimitiveTypes();
				copyClasses();
				copyInterfaces();
				copyAssociations();

			}
		};

		// GMFUnsafe.write(transactionalEditingDomain, command);
		transactionalEditingDomain.getCommandStack().execute(command);

		return targetIndexedMapping;
	}


	/**
	 *
	 */
	protected void copyPrimitiveTypes() {
		getPrimitiveTypeCopier().copy("String");
		getPrimitiveTypeCopier().copy("Boolean");
		getPrimitiveTypeCopier().copy("Real");
		getPrimitiveTypeCopier().copy("Integer");
	}

	//
	// COPY ALL ASSOCIATIONS
	//

	protected void copyAssociations() {
		for (Object originalConnector : getSourceDiagram().getEdges()) {
			if (originalConnector instanceof Connector) {
				Connector connector = Connector.class.cast(originalConnector);
				System.out.println("CONNECTOR ===== " + connector.getElement().getClass());
				System.out.println(connector.getSource());
				System.out.println(connector.getTarget());
				if (connector.getElement() instanceof Association) {
					if (getCopierObserver().canICopyElement(connector.getElement())) {
						getAssociationCopier().copy(connector);
					}
				}
				if (connector.getElement() instanceof InterfaceRealization) {
					if (getCopierObserver().canICopyElement(connector.getElement())) {
						getInterfaceRealizationCopier().copy(connector);
					}
				}
			}
		}
	}

	//
	// COPY ALL CLASSES
	//

	protected void copyClasses() {
		EcoreUtil.resolveAll(sourceDiagram);
		for (Object originalObject : sourceDiagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Class) {
					if (getCopierObserver().canICopyElement(shape.getElement())) {
						getClassCopier().copy(shape);
					}
				}

			}

		}
	}

	//
	// COPY ALL INTERFACES
	//

	protected void copyInterfaces() {
		EcoreUtil.resolveAll(sourceDiagram);
		for (Object originalObject : sourceDiagram.getChildren()) {
			if (originalObject instanceof Shape) {
				Shape shape = Shape.class.cast(originalObject);
				if (shape.getElement() instanceof org.eclipse.uml2.uml.Interface) {
					if (getCopierObserver().canICopyElement(shape.getElement())) {
						getInterfaceCopier().copy(shape);
					}
				}

			}

		}
	}

	//
	// CREATE OBJECT
	//

	public Object createEObject(org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart container, IElementType elementType, java.lang.Class expectedValueType) {
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

	public org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart createEObjectAndGetEditPart(GraphicalEditPart container, IElementType elementType,
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

	public Command createSetCommand(SetRequest setRequest, EObject eObject) {
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(eObject);
		ICommand setCommand = commandProvider.getEditCommand(setRequest);
		return new GMFtoEMFCommandWrapper(setCommand);
	}

	public void setElementName(EObject element, String name) {
		createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__NAME, name),
				element).execute();
	}

	/**
	 * @return the copierObserver
	 */
	public CopierObserver getCopierObserver() {
		return copierObserver;
	}

	/**
	 * @param copierObserver
	 *            the copierObserver to set
	 */
	public void setCopierObserver(CopierObserver copierObserver) {
		this.copierObserver = copierObserver;
	}

	public void elementHasBeenCopied(EObject element) {
		if (getCopierObserver() != null) {
			getCopierObserver().elementHasBeenCopied(element);
		}
	}
}
