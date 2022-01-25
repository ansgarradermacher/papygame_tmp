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

package org.eclipse.papyrus.gamification.games.framework;

import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.games.framework.communication.LevelContract;
import org.eclipse.papyrus.gamification.games.framework.communication.LevelPresenter;
import org.eclipse.papyrus.gamification.games.framework.communication.OnCancelGameItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnPlayerReadyItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.games.framework.entity.Game;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.games.framework.exception.GameAlreadyRunningException;
import org.eclipse.papyrus.gamification.games.framework.exception.GameLoadingException;
import org.eclipse.papyrus.gamification.modelutils.ModelDisplayManager;
import org.eclipse.papyrus.gamification.modelutils.ResponseModel;
import org.eclipse.papyrus.gamification.view.ViewManager;

/**
 * @author maximesavaryleblanc
 *
 */
public class LevelExecutor implements LevelContract.Executor, OnPlayerReadyItf, OnGameEndedItf, OnResumeToDashboardItf, OnCancelGameItf {

	private static LevelExecutor instance;
	private LevelPresenter levelPresenter;

	private LevelContext currentLevelContext;
	private Game currentGame;
	private PapyrusContext currentPapyrusContext;

	public static LevelExecutor getInstance() {
		if (instance == null) {
			instance = new LevelExecutor();
		}
		return instance;
	}

	private LevelExecutor() {
		this.levelPresenter = new LevelPresenter();
		levelPresenter.registerExecutor(this);
	}

	public void start(LevelContext levelContext) throws GameAlreadyRunningException, GameLoadingException {
		// Check if game already running
		if (currentLevelContext != null) {
			throw new GameAlreadyRunningException();
		}

		this.currentLevelContext = levelContext;

		// Retrieve Game to play
		this.currentGame = GameLoader.loadGame(levelContext.getLevel().getGameClass());

		// Instantiate GameIntroductionView for the LevelContext
		ViewManager.getInstance().displayView(currentGame.getIntroductionView(levelContext, this, this));

	}

	@Override
	public void onPlayerReady() {
		// Player clicked "I'm ready", we can start the game
		System.out.println("Player is now ready to play !");

		ResponseModel responseModel = null;

		// if (!this.currentGame.getName().equalsIgnoreCase("Flow")) {
		// games where UML diagram must be checked (OYO and HANGMAN)
		// Get ResponseModel for the current level: load model from path in LevelDescription

		System.out.println("Level path to load is : " + currentGame.getLevelPathToLoad(currentLevelContext));
		if (currentGame.getLevelPathToLoad(currentLevelContext) != null) {
			responseModel = levelPresenter.loadResponseModel(
					currentGame.getLevelPathToLoad(currentLevelContext),
					currentGame.isResponseModelInternal());
		}

		// }
		// Generate and open the diagram to play
		this.currentPapyrusContext = ModelDisplayManager.openAndGenerateLevelModel(
				responseModel,
				currentGame,
				currentLevelContext.getLevel(),
				currentGame.getProjectName(currentLevelContext));

		ViewManager.getInstance().displayView(currentGame.getPlayView(currentLevelContext, currentPapyrusContext, this));


	}

	@Override
	public void onGameEnded(GameMetrics gameMetrics) {
		// The Game view asked to end the game
		// We need to get the results of the game from GDF server.

		if (gameMetrics.isGameSuccess()) {
			levelPresenter.submitGame(gameMetrics, currentLevelContext);
		} else {
			GameScore gameScore = new GameScore(gameMetrics);
			gameScore.setLevelLabel(currentLevelContext.getLevel().getLabel());
			ViewManager.getInstance().displayView(currentGame.getGameOverView(currentLevelContext, this, gameScore));
		}

	}

	@Override
	public void onGameEnded(UmlDiagramSolution umlDiagramSolution) {
		// We need to get the results of the game from GDF server.
		levelPresenter.submitGame(umlDiagramSolution, currentLevelContext);
	}



	@Override
	public void onGameScoreReceived(GameScore gameScore) {
		System.out.println("Received submission results : " + gameScore.toString());

		if (gameScore.isGameSuccess()) {
			ViewManager.getInstance().displayView(currentGame.getGameSucessView(currentLevelContext, this, gameScore));
		} else {
			ViewManager.getInstance().displayView(currentGame.getGameOverView(currentLevelContext, this, gameScore));
		}
	}

	@Override
	public void onResumeToDashboard() {
		// TODO Auto-generated method stub
		System.out.println("Resume to dashboard now !");

		this.currentLevelContext = null;
		this.currentGame = null;
		this.currentPapyrusContext = null;

		ViewManager.getInstance().resumeToDashboard();

	}

	@Override
	public void onCancelGame() {
		// TODO Auto-generated method stub
		onResumeToDashboard();
	}





}
