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

import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusNamedElement;

/**
 * @author lepallec
 *
 */
public class Mask {

	private String mask = null;
	private String originName = null;
	private String finalName = null;

	final static String DONTCOPY = "x";
	final static String MASK_DELIMITER = "@";

	public Mask(String originName) {
		this.originName = originName;
		computeMaskAndFinalName();
	}

	public Mask(PapyrusNamedElement papyrusNamedElement) {
		this(papyrusNamedElement.getName());
	}

	private void computeMaskAndFinalName() {
		int indexMask = originName == null ? -1 : originName.indexOf(MASK_DELIMITER);
		if (indexMask != -1) {
			this.mask = originName.substring(indexMask);
			this.finalName = originName.substring(0, indexMask);

		} else {
			this.mask = null;
			this.finalName = originName;
		}
	}

	public String getFinalName() {
		return finalName;
	}

	public boolean isMaskDefined() {
		return mask != null;
	}

	protected boolean isFieldMentioned(String field) {
		return mask != null && mask.toUpperCase().indexOf(field.toUpperCase()) != -1;
	}

	public boolean dontCopy() {
		return this.isFieldMentioned(DONTCOPY);
	}

}
