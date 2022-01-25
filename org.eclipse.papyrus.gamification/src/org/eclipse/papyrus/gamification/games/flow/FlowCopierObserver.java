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

package org.eclipse.papyrus.gamification.games.flow;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public class FlowCopierObserver implements CopierObserver {

	private static final String PARAMETER_SUFFIX = "ZZZ";
	Flow flow;

	/**
	 * Constructor.
	 *
	 * @param hangman
	 * @param diagramEditPart2
	 */
	public FlowCopierObserver(Flow hangman, DiagramEditPart diagramEditPart) {
		this.flow = hangman;
	}

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver#canICopyElement(org.eclipse.emf.ecore.EObject)
	 *
	 * @param originalElement
	 */
	@Override
	public boolean canICopyElement(EObject originalElement) {
		Object name = originalElement.eGet(Literals.NAMED_ELEMENT__NAME);
		if (name instanceof String && String.class.cast(name).endsWith(PARAMETER_SUFFIX)) {
			return false;
		}
		return true;
	}

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.copiers.CopierObserver#elementHasBeenCopied(org.eclipse.emf.ecore.EObject)
	 *
	 * @param copy
	 */
	@Override
	public void elementHasBeenCopied(EObject copy) {

	}


}
