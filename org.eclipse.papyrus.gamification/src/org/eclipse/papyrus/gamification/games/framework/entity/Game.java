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

package org.eclipse.papyrus.gamification.games.framework.entity;

import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.games.framework.communication.OnCancelGameItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnPlayerReadyItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassDiagramCopier;
import org.eclipse.papyrus.gamification.view.game.GameFinishedView;
import org.eclipse.papyrus.gamification.view.game.GameIntroductionView;
import org.eclipse.papyrus.gamification.view.game.GamePlayView;
import org.eclipse.swt.browser.Browser;

/**
 * @author lepallec
 *
 */
public abstract class Game {


	public abstract String getName();

	// public abstract CopierObserver getCopierObserver(DiagramEditPart diagramEditPart);

	public abstract ClassDiagramCopier getClassDiagramCopier(PapyrusClassDiagram originalDiagram, PapyrusClassDiagram playerDiagram);

	/**
	 * @param level
	 */
	public abstract void onGameReady(LevelContext levelContext);

	/**
	 * @param browser
	 */
	public abstract void onHomePageLoaded(Browser browser);

	public GameIntroductionView getIntroductionView(LevelContext levelContext, OnPlayerReadyItf onPlayerReadyItf, OnCancelGameItf onCancelGameItf) {
		return new GameIntroductionView(levelContext, onPlayerReadyItf, onCancelGameItf);
	}

	public abstract GamePlayView getPlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf);

	public abstract GameFinishedView getGameSucessView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore);

	public abstract GameFinishedView getGameOverView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore);


	// PAth for response model to be loaded for comparison or clone
	public abstract String getLevelPathToLoad(LevelContext levelContext);

	// If the response model is located inside the plugin, return true, else false
	public abstract boolean isResponseModelInternal();


	// Project name to create for user to play
	public abstract String getProjectName(LevelContext levelContext);



}
