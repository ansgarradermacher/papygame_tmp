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

/**
 * @author lepallec
 *
 */
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;

public abstract class PapyrusLink {

	protected Command createSetCommand(SetRequest setRequest, EObject eObject) {
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(eObject);
		ICommand setCommand = commandProvider.getEditCommand(setRequest);
		return new GMFtoEMFCommandWrapper(setCommand);
	}

	protected void setElementStyle(EObject element, List<String> styleNameList) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.NAMED_STYLE__NAME, styleNameList),
					element).execute();
		}

	}

	// ************************
	//
	// BASIC stuff
	//
	// ************************

	public DiagramEditPart getDiagramEditPart() {
		return getPapyrusClassDiagram().getDiagramEditPart();
	}

	public boolean isReadOnly() {
		return getTransactionalEditingDomain() == null;
	}

	public void setElementLineColor(int color) {
		if (!isReadOnly()) {

			createSetCommand(
					new SetRequest(getNotationalElement(),
							org.eclipse.gmf.runtime.notation.NotationPackage.Literals.LINE_STYLE__LINE_COLOR, color),
					getNotationalElement()).execute();
		}

	}


	public EObject getSemanticElement() {
		return getNotationalElement().getElement();
	}

	public abstract Connector getNotationalElement();

	public abstract EditPart getEditPart();

	public TransactionalEditingDomain getTransactionalEditingDomain() {
		return getPapyrusClassDiagram().getTransactionalEditingDomain();
	}

	public PapyrusNode getSource() {
		EObject source = getNotationalElement().getSource().getElement();
		return getPapyrusClassDiagram().getCorrespondingPapyrusNode(source);
	}

	public PapyrusNode getTarget() {
		EObject target = getNotationalElement().getTarget().getElement();
		return getPapyrusClassDiagram().getCorrespondingPapyrusNode(target);
	}

	public abstract PapyrusClassDiagram getPapyrusClassDiagram();
}
