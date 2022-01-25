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

import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.UMLPackage.Literals;

/**
 * @author lepallec
 *
 */
public abstract class PapyrusTypedElement extends PapyrusNamedElement {

	PapyrusType innerPapyrusType = null;

	/**
	 * @see org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusNode#getSemanticElement()
	 *
	 * @return
	 */
	@Override
	public TypedElement getSemanticElement() {
		return TypedElement.class.cast(super.getSemanticElement());
	}

	public void setType(PapyrusType papyrusType) {
		innerPapyrusType = papyrusType;
		TypedElement typedElement = getSemanticElement();
		Type type = papyrusType.getSemanticElement();
		createSetCommand(new SetRequest(typedElement, Literals.TYPED_ELEMENT__TYPE, type),
				typedElement).execute();
	}

	public PapyrusType getType() {
		if (innerPapyrusType != null) {
			return innerPapyrusType;
		}
		TypedElement element = getSemanticElement();
		Type type = element.getType();
		return getPapyrusClassDiagram().getPapyrusType(type);
	}

}
