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

package org.eclipse.papyrus.gamification.data.repository.level;

import org.eclipse.papyrus.gamification.Preferences;
import org.eclipse.papyrus.gamification.data.api.GameManagerService;
import org.eclipse.papyrus.gamification.data.api.query.ActionRequestContent;
import org.eclipse.papyrus.gamification.data.api.query.ActionRequestCustomData;
import org.eclipse.papyrus.gamification.data.api.response.CustomDataJson;
import org.eclipse.papyrus.gamification.data.api.response.PlayerStatusJson;
import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author maximesavaryleblanc
 *
 */
public class LevelRemoteDataSource {

	GameManagerService gameManagerService;

	public LevelRemoteDataSource(GameManagerService gameManagerService) {
		this.gameManagerService = gameManagerService;
	}

	Completable submitGameResult(GameMetrics gameMetrics, LevelContext levelContext) {

		ActionRequestContent<ActionRequestCustomData> actionRequestContent = new ActionRequestContent<>(Preferences.SUBMIT_GAME_ACTION_ID);
		actionRequestContent.setPlayerId(levelContext.getPlayerProfile().getPlayerId());
		actionRequestContent.setGameId(levelContext.getSeries().getSeriesGameId());
		// actionRequestContent.setExecutionMoment(executionMoment);

		ActionRequestCustomData arcd = new ActionRequestCustomData();
		arcd.setMoves(gameMetrics.getOkMoves());
		arcd.setErrors(gameMetrics.getErrors());
		arcd.setLevelLabel(levelContext.getLevel().getLabel());
		arcd.setLogs("LOGS");
		arcd.setTimeSpent(gameMetrics.getTimeSpent());

		actionRequestContent.setData(arcd);

		System.out.println("Request to Submit game result remoteLevel");
		return gameManagerService.submitGameResult(actionRequestContent.getGameId(), actionRequestContent.getActionId(), actionRequestContent);
	}

	Completable submitGameResult(UmlDiagramSolution umlDiagramSolution, LevelContext levelContext) {

		ActionRequestContent<UmlDiagramSolution> actionRequestContent = new ActionRequestContent<>(Preferences.SUBMIT_GAME_ACTION_ID);
		actionRequestContent.setPlayerId(levelContext.getPlayerProfile().getPlayerId());
		actionRequestContent.setGameId(levelContext.getSeries().getSeriesGameId());
		// actionRequestContent.setExecutionMoment(executionMoment);

		actionRequestContent.setData(umlDiagramSolution);

		System.out.println("Request to Submit game result remoteLevel");
		return gameManagerService.submitGameResult(actionRequestContent.getGameId(), actionRequestContent.getActionId(), actionRequestContent);
	}

	Single<PlayerStatusJson> getPlayerStatus(String gameId, String playerId) {
		System.out.println("--- Retrieving remote Player Status");

		return gameManagerService.getPlayerStatus(gameId, playerId);
		/*
		 * .map(new Function<PlayerStatusJson, PlayerStatus>() {
		 *
		 * @Override
		 * public PlayerStatus apply(PlayerStatusJson jsonStatus) throws Exception {
		 * System.out.println("GET PLAYER STATUS ============");
		 * System.out.println(jsonStatus.toString());
		 * return new PlayerStatus(
		 * jsonStatus.getState().getPoints(),
		 * jsonStatus.getState().getGoldCoins(),
		 * 10);// jsonStatus.getCustomData().getIntLevel());
		 * }
		 * });
		 */

	}

	Completable setPlayerCustomData(String gameId, String playerId, CustomDataJson content) {
		return gameManagerService.setPlayerCustomData(gameId, playerId, content);
	}

}
