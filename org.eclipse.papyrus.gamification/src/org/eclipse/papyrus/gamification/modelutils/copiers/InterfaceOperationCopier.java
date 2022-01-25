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

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.ResizeableListCompartmentEditPart;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPartCN;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * @author lepallec
 *
 */
public class InterfaceOperationCopier {

	InterfaceCopier interfaceCopier;
	GraphicalEditPart targetInterfaceEditPart;
	Interface sourceInterface;
	Interface targetInterface;
	ResizeableListCompartmentEditPart operationCompartment = null;



	/**
	 * Constructor.
	 *
	 * @param interfaceCopier
	 * @param sourceInterface
	 * @param targetInterface
	 */
	public InterfaceOperationCopier(InterfaceCopier interfaceCopier, Interface sourceInterface, Interface targetInterface) {
		this.interfaceCopier = interfaceCopier;
		this.sourceInterface = sourceInterface;
		this.targetInterface = targetInterface;
		setEditPartTargetInterface();
		setOperationCompartment();
	}

	/**
	 *
	 */
	private void setEditPartTargetInterface() {
		this.targetInterfaceEditPart = GraphicalEditPart.class.cast(getInterfaceCopier().getDiagramCopier().getEditPart(this.targetInterface));
	}

	/**
	 *
	 */
	private void setOperationCompartment() {

		for (Object child : this.targetInterfaceEditPart.getChildren()) {
			if (child instanceof InterfaceOperationCompartmentEditPart ||
					child instanceof InterfaceOperationCompartmentEditPartCN) {
				operationCompartment = ResizeableListCompartmentEditPart.class.cast(child);
			}
		}
	}

	/**
	 * @return
	 *
	 */
	public Operation copyOperation(org.eclipse.uml2.uml.Operation sourceOperation) {

		String operationName = sourceOperation.getName();

		PapyrusUtils utils = PapyrusUtils.getINSTANCE();

		Operation targetOperation = utils.createInterfaceOperation(getInterfaceCopier().getDiagramCopier().getTargetDiagram(), getOperationCompartment());

		utils.setElementName(targetOperation, operationName);
		utils.setElementVisibility(targetOperation, sourceOperation.getVisibility().getLiteral());

		getInterfaceCopier().getDiagramCopier().getTargetIndexedMapping().put(targetOperation, sourceOperation);


		copyParameters(sourceOperation, targetOperation);

		getInterfaceCopier().getDiagramCopier().elementHasBeenCopied(targetOperation);
		return targetOperation;
	}

	/**
	 * @param sourceOperation
	 * @param targetOperation
	 */
	private void copyParameters(org.eclipse.uml2.uml.Operation sourceOperation, Operation targetOperation) {
		for (Parameter parameter : sourceOperation.getOwnedParameters()) {

			copyParameter(targetOperation, parameter);

		}
	}

	/**
	 * @param targetOperation
	 * @param parameter
	 */
	private void copyParameter(Operation targetOperation, Parameter parameter) {

		Type parameterType = Type.class.cast(getInterfaceCopier().getDiagramCopier().getSourceIndexedMapping().get(parameter.getType()));

		Parameter targetParameter = UMLFactory.eINSTANCE.createParameter();

		targetParameter.setName(parameter.getName());
		targetParameter.setType(parameterType);
		// Multiplicity - Lower
		targetParameter.setLower(parameter.getLower());

		// Multiplicity - Upper
		targetParameter.setUpper(parameter.getUpper());

		// Direction
		targetParameter.setDirection(parameter.getDirection());

		// Ordered
		targetParameter.setIsOrdered(parameter.isOrdered());

		// Unique
		targetParameter.setIsUnique(parameter.isUnique());

		// Sequence - not found


		org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest moveRequest = new MoveRequest(targetOperation, targetParameter);
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(targetOperation);
		ICommand moveCommand = commandProvider.getEditCommand(moveRequest);
		GMFtoEMFCommandWrapper command = new GMFtoEMFCommandWrapper(moveCommand);

		command.execute();

	}

	/**
	 * @return the interfaceCopier
	 */
	public InterfaceCopier getInterfaceCopier() {
		return interfaceCopier;
	}

	/**
	 * @return the attributeCompartment
	 */
	public ResizeableListCompartmentEditPart getOperationCompartment() {
		return operationCompartment;
	}

}
