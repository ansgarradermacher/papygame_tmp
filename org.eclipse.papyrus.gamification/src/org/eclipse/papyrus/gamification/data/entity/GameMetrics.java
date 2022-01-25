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

package org.eclipse.papyrus.gamification.data.entity;

/**
 * @author maximesavaryleblanc
 *
 */
public class GameMetrics {
	protected boolean isGameSuccess;
	protected int okMoves;
	protected int errors;
	protected int timeSpent;
	protected String icon;

	public boolean isGameSuccess() {
		return isGameSuccess;
	}

	public void setGameSuccess(boolean isGameSuccess) {
		this.isGameSuccess = isGameSuccess;
	}

	public int getOkMoves() {
		return okMoves;
	}

	public void setOkMoves(int okMoves) {
		this.okMoves = okMoves;
	}

	public int getErrors() {
		return errors;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public int getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


}
