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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public abstract class PapyrusNamedElement extends PapyrusNode {

	public String getName() {
		NamedElement namedElement = NamedElement.class.cast(getSemanticElement());
		return namedElement.getName();
	}

	public void setName(String name) {
		EObject element = getSemanticElement();
		createSetCommand(new SetRequest(element, Literals.NAMED_ELEMENT__NAME, name), element).execute();
	}

	public String getLabel() {
		NamedElement namedElement = NamedElement.class.cast(getSemanticElement());
		return namedElement.getLabel(false);
	}


}
