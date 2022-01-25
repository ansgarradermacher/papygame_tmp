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

package org.eclipse.papyrus.gamification.games.framework.exception;

/**
 * @author maximesavaryleblanc
 *
 */
public class GameLoadingException extends Exception {

	String className;

	public GameLoadingException(String className) {
		super();
		this.className = className;
	}

	@Override
	public String getMessage() {
		return "Could not load the game " + className + ". Please check the spelling, and the availability of the Game in this version of PapyGame";
	}


}
