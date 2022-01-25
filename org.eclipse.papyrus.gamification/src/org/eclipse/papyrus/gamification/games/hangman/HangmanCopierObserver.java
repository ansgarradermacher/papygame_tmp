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

package org.eclipse.papyrus.gamification.games.hangman;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver;
import org.eclipse.papyrus.gamification.modelutils.copiers.PapyrusUtils;
import org.eclipse.papyrus.infra.gmfdiag.css.CSSShapeImpl;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassNameEditPart;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;

/**
 * @author lepallec
 *
 */
public class HangmanCopierObserver implements CopierObserver {

	Class fakeClass;
	GraphicalEditPart fakeClassEditPart;
	DiagramEditPart diagramEditPart;
	private static final String PARAMETER_SUFFIX = "ZZZ";
	Hangman hangman;
	int minimumMoves = 0;


	/**
	 * @return the minimumMoves
	 */
	public int getMinimumMoves() {
		return minimumMoves;
	}

	/**
	 * Constructor.
	 *
	 * @param hangman
	 * @param diagramEditPart2
	 */
	public HangmanCopierObserver(Hangman hangman, DiagramEditPart diagramEditPart) {
		this.hangman = hangman;
		this.diagramEditPart = diagramEditPart;
		this.fakeClass = PapyrusUtils.getINSTANCE().createGraphicalUmlClass(diagramEditPart, "FakeClass", new Point(900, 100));

		this.fakeClassEditPart = GraphicalEditPart.class.cast(PapyrusUtils.getINSTANCE().getEditPart(diagramEditPart, fakeClass));



		CSSShapeImpl fakeShapeClass = CSSShapeImpl.class.cast(this.fakeClassEditPart.getModel());



		PapyrusUtils.getINSTANCE().setElementLineColor(fakeShapeClass, 16219144);
		PapyrusUtils.getINSTANCE().setElementLineWidth(fakeShapeClass, 2);
		PapyrusUtils.getINSTANCE().setElementBackgroundColor(fakeShapeClass, 16777215);

		ClassNameEditPart nameCompartment = PapyrusUtils.getINSTANCE().getNameCompartment(fakeClassEditPart);

		try {
			// TOTO PapyrusUtils.getINSTANCE().setElementVisualVisibility(EObject.class.cast(nameCompartment.getModel()), false);
		} catch (Exception ex) {
			System.out.println("didn't manage the doExecuteWithResult");
		}
	}

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver#canICopyElement(org.eclipse.emf.ecore.EObject)
	 *
	 * @param originalElement
	 */
	@Override
	public boolean canICopyElement(EObject originalElement) {
		return true;
	}

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver#elementHasBeenCopied(org.eclipse.emf.ecore.EObject)
	 *
	 * @param copy
	 */
	@Override
	public void elementHasBeenCopied(EObject copy) {
		if (copy instanceof Property) {
			Property newProperty = Property.class.cast(copy);
			Classifier containerClass = Classifier.class.cast(newProperty.eContainer());
			GraphicalEditPart containerClassEdit = GraphicalEditPart.class.cast(PapyrusUtils.getINSTANCE().getEditPart(diagramEditPart, containerClass));
			if (newProperty.getName().endsWith(PARAMETER_SUFFIX)) {
				PapyrusUtils.getINSTANCE().setElementName(newProperty, newProperty.getName().replace(PARAMETER_SUFFIX, ""));
				GraphicalEditPart targetPropertyEditPart = PapyrusUtils.getINSTANCE().getAttributeEditPart(containerClassEdit, newProperty);
				if (targetPropertyEditPart != null) {
					moveAttributeToFakeClass(targetPropertyEditPart);
					minimumMoves++;
				}
			}
		}
		if (copy instanceof Operation) {
			Operation newOperation = Operation.class.cast(copy);
			Classifier containerClass = Classifier.class.cast(newOperation.eContainer());
			GraphicalEditPart containerClassEdit = GraphicalEditPart.class.cast(PapyrusUtils.getINSTANCE().getEditPart(diagramEditPart, containerClass));
			if (newOperation.getName().endsWith(PARAMETER_SUFFIX)) {
				PapyrusUtils.getINSTANCE().setElementName(newOperation, newOperation.getName().replace(PARAMETER_SUFFIX, ""));
				GraphicalEditPart targetOperationEditPart = PapyrusUtils.getINSTANCE().getOperationEditPart(containerClassEdit, newOperation);
				if (targetOperationEditPart != null) {
					moveOperationToFakeClass(targetOperationEditPart);
					minimumMoves++;
				}
			}
		}
	}

	/**
	 * @param targetPropertyEditPart
	 */
	private void moveFeatureToFakeClass(GraphicalEditPart targetPropertyEditPart, GraphicalEditPart targetContainer) {
		ChangeBoundsRequest request2 = new ChangeBoundsRequest();
		request2.setEditParts(targetPropertyEditPart);
		request2.setLocation(new Point(1, 1));
		request2.setType(org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants.REQ_DROP);
		org.eclipse.gef.commands.Command command2 = targetContainer.getCommand(request2);
		command2.execute();
		// this.hangman.incrementFakeAttributesNumber();
	}

	/**
	 * @param targetPropertyEditPart
	 */
	private void moveAttributeToFakeClass(GraphicalEditPart targetPropertyEditPart) {
		moveFeatureToFakeClass(targetPropertyEditPart, PapyrusUtils.getINSTANCE().getAttributeCompartment(fakeClassEditPart));
	}

	/**
	 * @param targetOperationEditPart
	 */
	private void moveOperationToFakeClass(GraphicalEditPart targetOperationEditPart) {
		moveFeatureToFakeClass(targetOperationEditPart, PapyrusUtils.getINSTANCE().getOperationCompartment(fakeClassEditPart));
	}

}
