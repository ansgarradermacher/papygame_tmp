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
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.services.edit.service.ElementEditServiceUtils;
import org.eclipse.papyrus.infra.services.edit.service.IElementEditService;
import org.eclipse.uml2.uml.UMLPackage.Literals;

public abstract class PapyrusNode {

	protected Command createSetCommand(SetRequest setRequest, EObject eObject) {
		IElementEditService commandProvider = ElementEditServiceUtils.getCommandProvider(eObject);
		ICommand setCommand = commandProvider.getEditCommand(setRequest);
		return new GMFtoEMFCommandWrapper(setCommand);
	}

	protected void setElementName(EObject element, String name) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__NAME, name), element).execute();
		}
	}

	protected void setElementStyle(EObject element, List<String> styleNameList) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(element, org.eclipse.gmf.runtime.notation.NotationPackage.Literals.NAMED_STYLE__NAME, styleNameList),
					element).execute();
		}

	}

	abstract public DiagramEditPart getDiagramEditPart();

	public boolean isReadOnly() {
		return getTransactionalEditingDomain() == null;
	}

	public void locate(int x, int y) {
		if (!isReadOnly()) {
			getTransactionalEditingDomain().getCommandStack()
					.execute(new RecordingCommand(getTransactionalEditingDomain(), "locate") {

						@Override
						protected void doExecute() {
							Shape umlShape = getNotationalElement();
							LayoutConstraint bounds = umlShape.getLayoutConstraint();
							bounds.eSet(NotationPackage.eINSTANCE.getLocation_X(), x);
							bounds.eSet(NotationPackage.eINSTANCE.getLocation_Y(), y);
						}
					});
		}

	}

	public void resize(int w, int h) {
		if (!isReadOnly()) {

			getTransactionalEditingDomain().getCommandStack()
					.execute(new RecordingCommand(getTransactionalEditingDomain(), "resize") {

						@Override
						protected void doExecute() {
							Shape umlShape = getNotationalElement();
							LayoutConstraint bounds = umlShape.getLayoutConstraint();
							bounds.eSet(NotationPackage.eINSTANCE.getSize_Width(), w);
							bounds.eSet(NotationPackage.eINSTANCE.getSize_Height(), h);
						}
					});
		}

	}

	public int getX() {
		return (java.lang.Integer) getNotationalElement().getLayoutConstraint().eGet(NotationPackage.eINSTANCE.getLocation_X());
	}

	public int getY() {
		return (java.lang.Integer) getNotationalElement().getLayoutConstraint().eGet(NotationPackage.eINSTANCE.getLocation_Y());
	}

	public int getWidth() {
		return (java.lang.Integer) getNotationalElement().getLayoutConstraint().eGet(NotationPackage.eINSTANCE.getSize_Width());
	}

	public int getHeight() {
		return (java.lang.Integer) getNotationalElement().getLayoutConstraint().eGet(NotationPackage.eINSTANCE.getSize_Height());
	}

	public EObject getSemanticElement() {
		return getNotationalElement().getElement();
	}

	public abstract Shape getNotationalElement();

	public abstract EditPart getEditPart();

	public abstract TransactionalEditingDomain getTransactionalEditingDomain();

	public abstract PapyrusClassDiagram getPapyrusClassDiagram();


	public void setLineColor(int color) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(getNotationalElement(), org.eclipse.gmf.runtime.notation.NotationPackage.Literals.LINE_STYLE__LINE_COLOR, color),
					getNotationalElement()).execute();
		}

	}

	public void setLineWidth(int width) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(getNotationalElement(), org.eclipse.gmf.runtime.notation.NotationPackage.Literals.LINE_STYLE__LINE_WIDTH, width),
					getNotationalElement()).execute();
		}

	}

	public void setBackgroundColor(int color) {
		if (!isReadOnly()) {
			createSetCommand(new SetRequest(getNotationalElement(), org.eclipse.gmf.runtime.notation.NotationPackage.Literals.FILL_STYLE__FILL_COLOR, color),
					getNotationalElement()).execute();
		}

	}

	/**
	 * @see java.lang.Object#toString()
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "PapyrusNode [getSemanticElement()=" + getSemanticElement() + ", getEditPart()=" + getEditPart() + "]";
	}

}
