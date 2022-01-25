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

package org.eclipse.papyrus.gamification.data.api.response;

/**
 * @author maximesavaryleblanc
 *
 */
public class LevelCompletedJson {

	int level;
	String levelLabel;
	int earnedGoldCoins;
	int earnedXP;
	int timeToComplete;
	int moveNumber;
	int errors;
	String logs;

	public int getLevel() {
		return level;
	}

	public String getLevelLabel() {
		return levelLabel;
	}

	public int getEarnedGoldCoins() {
		return earnedGoldCoins;
	}

	public int getEarnedXP() {
		return earnedXP;
	}

	public int getTimeToComplete() {
		return timeToComplete;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public int getErrors() {
		return errors;
	}

	public String getLogs() {
		return logs;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLevelLabel(String levelLabel) {
		this.levelLabel = levelLabel;
	}

	public void setEarnedGoldCoins(int earnedGoldCoins) {
		this.earnedGoldCoins = earnedGoldCoins;
	}

	public void setEarnedXP(int earnedXP) {
		this.earnedXP = earnedXP;
	}

	public void setTimeToComplete(int timeToComplete) {
		this.timeToComplete = timeToComplete;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	@Override
	public String toString() {
		return "LevelCompletedJson [level=" + level + ", levelLabel=" + levelLabel + ", earnedGoldCoins=" + earnedGoldCoins + ", earnedXP=" + earnedXP + ", timeToComplete=" + timeToComplete + ", moveNumber=" + moveNumber + ", errors=" + errors + ", logs="
				+ logs + "]";
	}


}