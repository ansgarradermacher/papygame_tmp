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

import java.util.ArrayList;
import java.util.List;

/**
 * @author lepallec
 *
 */
public class PlayerProfile {

	String playerId;
	int points;
	int goldCoins;
	List<SeriesPerformed> seriesPerformed;
	boolean displayQuestionnaire;
	int invitations;

	/**
	 * Constructor.
	 *
	 * @param login
	 * @param pwd
	 * @param points
	 * @param goldCoins
	 */
	public PlayerProfile(String playerId) {
		super();
		this.playerId = playerId;
		this.seriesPerformed = new ArrayList<>();
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getGoldCoins() {
		return goldCoins;
	}

	public void setGoldCoins(int goldCoins) {
		this.goldCoins = goldCoins;
	}

	public boolean isDisplayQuestionnaire() {
		return displayQuestionnaire;
	}

	public void setDisplayQuestionnaire(boolean displayQuestionnaire) {
		this.displayQuestionnaire = displayQuestionnaire;
	}

	public String getPlayerId() {
		return playerId;
	}

	public List<SeriesPerformed> getSeriesPerformed() {
		return seriesPerformed;
	}

	public void addGoldCoins(int goldCoinsToAdd) {
		this.goldCoins += goldCoinsToAdd;
	}

	public void addXP(int xpToAdd) {
		this.points += xpToAdd;
	}

	public void computeQuestionnaireBoolean(boolean isQuestionnaireFilledInThisGame) {
		this.displayQuestionnaire = (displayQuestionnaire || !isQuestionnaireFilledInThisGame);
	}

	public int getInvitations() {
		return invitations;
	}

	public void setInvitations(int invitations) {
		this.invitations = invitations;
	}

}
