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
 * @author lepallec
 *
 */
public class LevelPerformed extends Level {

	private boolean done;
	private int earnedPoints;
	private int earnedGoldCoins;

	public LevelPerformed(Level level) {
		super(level.getLabel(), level.getGameClass(), level.getModelPath(), level.getDiagramName(), level.getDiagramToLoadName(), level.getStatement(), level.getVideoToShowUrl(), level.getIcon());
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(int earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public int getEarnedGoldCoins() {
		return earnedGoldCoins;
	}

	public void setEarnedGoldCoins(int earnedGoldCoins) {
		this.earnedGoldCoins = earnedGoldCoins;
	}

}
