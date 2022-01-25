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

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperation;

/**
 * @author lepallec
 *
 */
public class OperationMask extends Mask {


	final static String NAME = "n";

	public OperationMask(PapyrusOperation papyrusOperation) {
		super(papyrusOperation);
	}

	public boolean isNameMentioned() {
		return this.isFieldMentioned(NAME);
	}

}
