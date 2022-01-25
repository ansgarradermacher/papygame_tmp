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

package org.eclipse.papyrus.gamification.modelutils.papyrus.copy;

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperationParameter;

/**
 * @author lepallec
 *
 */
public class OperationParameterMask extends Mask {

	final static String NAME = "n";
	final static String MULTIPLICITY = "m";
	final static String DIRECTION = "d";
	final static String ORDERED = "o";
	final static String UNIQUE = "u";
	final static String TYPE = "t";

	public OperationParameterMask(PapyrusOperationParameter papyrusOperationParameter) {
		super(papyrusOperationParameter.getName() != null ? papyrusOperationParameter.getName() : "");
	}

	public boolean isNameMentioned() {
		return this.isFieldMentioned(NAME);
	}

	public boolean isMultiplicityMentioned() {
		return this.isFieldMentioned(MULTIPLICITY);
	}

	public boolean isDirectionMentioned() {
		return this.isFieldMentioned(DIRECTION);
	}

	public boolean isOrderedMentioned() {
		return this.isFieldMentioned(ORDERED);
	}

	public boolean isUniqueMentioned() {
		return this.isFieldMentioned(UNIQUE);
	}

	public boolean isTypeMentioned() {
		return this.isFieldMentioned(TYPE);
	}
}
