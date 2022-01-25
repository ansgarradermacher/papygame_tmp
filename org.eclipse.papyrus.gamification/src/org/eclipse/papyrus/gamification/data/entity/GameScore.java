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
public class GameScore extends GameMetrics {

	private int earnedXP;
	private int earnedGoldCoins;
	private String levelLabel;

	public GameScore(GameMetrics gameMetrics) {
		this.errors = gameMetrics.getErrors();
		this.okMoves = gameMetrics.getOkMoves();
		this.timeSpent = gameMetrics.getTimeSpent();
		this.isGameSuccess = gameMetrics.isGameSuccess();
		this.icon = gameMetrics.getIcon();
	}

	public GameScore() {
	}

	public int getEarnedXP() {
		return earnedXP;
	}

	public void setEarnedXP(int earnedXP) {
		this.earnedXP = earnedXP;
	}

	public int getEarnedGoldCoins() {
		return earnedGoldCoins;
	}

	public void setEarnedGoldCoins(int earnedGoldCoins) {
		this.earnedGoldCoins = earnedGoldCoins;
	}

	@Override
	public String toString() {
		return "GameScore [earnedXP=" + earnedXP + ", earnedGoldCoins=" + earnedGoldCoins + ", isGameSuccess=" + isGameSuccess + ", okMoves=" + okMoves + ", errors=" + errors + ", timeSpent=" + timeSpent + "]";
	}

	public String getLevelLabel() {
		return levelLabel;
	}

	public void setLevelLabel(String levelLabel) {
		this.levelLabel = levelLabel;
	}





}
