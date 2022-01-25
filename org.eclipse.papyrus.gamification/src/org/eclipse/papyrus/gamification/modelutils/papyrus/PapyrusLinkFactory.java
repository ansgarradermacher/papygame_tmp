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

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.commands.wrappers.GEFtoEMFCommandWrapper;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.GeneralizationEditPart;
import org.eclipse.papyrus.uml.diagram.common.editparts.UMLConnectionNodeEditPart;
import org.eclipse.papyrus.uml.service.types.element.UMLDIElementTypes;

/**
 * @author lepallec
 *
 */
public class PapyrusLinkFactory {

	final static PapyrusLinkFactory INSTANCE = new PapyrusLinkFactory();


	public static PapyrusLinkFactory getInstance() {
		return INSTANCE;
	}

	public UMLConnectionNodeEditPart createLink(PapyrusClassDiagram papyrusDiagram, IElementType type, Class<? extends UMLConnectionNodeEditPart> editPartClass, PapyrusNode sourceElement,
			PapyrusNode targetElement) {
		CreateConnectionViewRequest connectionRequest = CreateViewRequestFactory.getCreateConnectionRequest(type,
				papyrusDiagram.getDiagramEditPart().getDiagramPreferencesHint());

		connectionRequest.setSourceEditPart(null);
		connectionRequest.setTargetEditPart(sourceElement.getEditPart());
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_START);
		sourceElement.getEditPart().getCommand(connectionRequest);

		connectionRequest.setSourceEditPart(sourceElement.getEditPart());
		connectionRequest.setTargetEditPart(targetElement.getEditPart());
		connectionRequest.setType(RequestConstants.REQ_CONNECTION_END);
		org.eclipse.gef.commands.Command controlFlowCreationCommand = targetElement.getEditPart()
				.getCommand(connectionRequest);

		papyrusDiagram.getTransactionalEditingDomain().getCommandStack()
				.execute(GEFtoEMFCommandWrapper.wrap(controlFlowCreationCommand));

		for (Object connection : sourceElement.getDiagramEditPart().getConnections()) {
			if (editPartClass.isInstance(connection)) {
				UMLConnectionNodeEditPart editPart = (UMLConnectionNodeEditPart) connection;

				if (sourceElement.getEditPart().equals(((ConnectionEditPart) editPart).getSource())
						&& targetElement.getEditPart().equals(((ConnectionEditPart) editPart).getTarget())) {
					return editPart;
				}
			}
		}

		return null;
	}

	public PapyrusAssociation createAssociation(PapyrusClassDiagram papyrusDiagram, String associationName, PapyrusNode sourceElement,
			PapyrusNode targetElement) {
		/*
		 * if ((isDirected != null) && isDirected) {
		 * if ("none".equals(aggregationValue)) {
		 * return UmlRelationType.DIRECTED_ASSOCIATION;
		 * }
		 * if (("composite").equals(aggregationValue)) {
		 * return UmlRelationType.COMPOSITE_ASSOCIATION;
		 * }
		 * if (("shared").equals(aggregationValue)) {
		 * return UmlRelationType.SHARED_ASSOCIATION;
		 * }
		 * } else {
		 * if (relationship instanceof Association) {
		 * return UmlRelationType.UNDIRECTED_ASSOCIATION;
		 * }
		 * }
		 */
		// AssociationEditPart linkEditPart = AssociationEditPart.class.cast(createLink(papyrusDiagram, UMLDIElementTypes.ASSOCIATION_NON_DIRECTED_EDGE, AssociationEditPart.class, sourceElement, targetElement));
		System.out.println("Creating association NODE");

		AssociationEditPart linkEditPart = AssociationEditPart.class.cast(createLink(papyrusDiagram, UMLDIElementTypes.ASSOCIATION_COMPOSITE_DIRECTED_EDGE, AssociationEditPart.class, sourceElement, targetElement));
		System.out.println("Edit part is " + linkEditPart);

		PapyrusAssociation papyrusAssociation = papyrusDiagram.getPapyrusAssociation(linkEditPart);
		papyrusAssociation.setName(associationName);
		return papyrusAssociation;

	}

	public PapyrusGeneralization createGeneralization(PapyrusClassDiagram papyrusDiagram, PapyrusClassifier subclass,
			PapyrusClassifier superclass) {

		GeneralizationEditPart linkEditPart = GeneralizationEditPart.class.cast(createLink(papyrusDiagram, UMLDIElementTypes.GENERALIZATION_EDGE, GeneralizationEditPart.class, subclass, superclass));
		PapyrusGeneralization papyrusGeneralization = papyrusDiagram.getPapyrusGeneralization(linkEditPart);
		return papyrusGeneralization;

	}


}
