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

package org.eclipse.papyrus.gamification.games.flow;

import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.games.flow.view.FlowGameOverView;
import org.eclipse.papyrus.gamification.games.flow.view.FlowGameSuccessView;
import org.eclipse.papyrus.gamification.games.flow.view.FlowIntroductionView;
import org.eclipse.papyrus.gamification.games.flow.view.FlowPlayView;
import org.eclipse.papyrus.gamification.games.framework.communication.OnCancelGameItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnPlayerReadyItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.games.framework.entity.Game;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.modelutils.PlayerModel;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.ClassDiagramCopier;
import org.eclipse.papyrus.gamification.view.game.GameFinishedView;
import org.eclipse.papyrus.gamification.view.game.GameIntroductionView;
import org.eclipse.papyrus.gamification.view.game.GamePlayView;
import org.eclipse.papyrus.gamification.view.game.JSPlayerReady;
import org.eclipse.swt.browser.Browser;

/**
 * @author lepallec
 *
 */
public class Flow extends Game {


	private JSPlayerReady playerReadyFunction;
	private int fakeAttributesNumber = 0;
	private LevelContext levelContext;

	/**
	 * @see org.eclipse.papyrus.gamification.games.framework.entity.Game#getName()
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return "Flow";
	}

	@Override
	public GameIntroductionView getIntroductionView(LevelContext levelContext, OnPlayerReadyItf onPlayerReadyItf, OnCancelGameItf onCancelGameItf) {
		return new FlowIntroductionView(levelContext, onPlayerReadyItf, onCancelGameItf);
	}

	@Override
	public GamePlayView getPlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf) {
		return new FlowPlayView(levelContext, papyrusContext, onGameEndedItf);
	}

	@Override
	public GameFinishedView getGameSucessView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore) {
		return new FlowGameSuccessView(levelContext, onResumeToDashboardItf, gameScore);
	}

	@Override
	public GameFinishedView getGameOverView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore) {
		return new FlowGameOverView(levelContext, onResumeToDashboardItf, gameScore);
	}


	/**
	 * @see org.eclipse.papyrus.gamification.games.framework.entity.Game#onGameReady(org.eclipse.papyrus.gamification.games.framework.entity.LevelContext)
	 *
	 * @param levelContext
	 */
	@Override
	public void onGameReady(LevelContext levelContext) {
		this.levelContext = levelContext;

	}

	@Override
	public void onHomePageLoaded(Browser browser) {
		System.out.println("onHomePageLoaded");
		browser.execute("setLevelLabel('" + levelContext.getLevel().getLabel() + "')");
		browser.execute("setLevelStatement('" + levelContext.getLevel().getStatement() + "')");
		browser.execute("activeReadyButton();");
	}


	public void incrementFakeAttributesNumber() {
		this.fakeAttributesNumber++;
	}

	/**
	 * @return the fakeAttributesNumber
	 */
	public int getFakeAttributesNumber() {
		return fakeAttributesNumber;
	}

	/**
	 * @see org.eclipse.papyrus.gamification.games.framework.entity.Game#getClassDiagramCopier(org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram, org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram)
	 *
	 * @param originalDiagram
	 * @param playerDiagram
	 * @return
	 */
	@Override
	public ClassDiagramCopier getClassDiagramCopier(PapyrusClassDiagram originalDiagram, PapyrusClassDiagram playerDiagram) {
		return new ClassDiagramCopier(originalDiagram, playerDiagram);
	}

	@Override
	public String getLevelPathToLoad(LevelContext levelContext) {
		System.out.println("Level is : " + levelContext.toString());
		if (levelContext.getLevel().getDiagramToLoadName() == null) {
			return null;
		}
		if (levelContext.getLevel().getDiagramToLoadName().isEmpty()) {
			return null;
		}
		return PlayerModel.getUserProjectUri().getPath().toString() + "/" + levelContext.getSeries().getName().replace(" ", "") + "/" + levelContext.getLevel().getDiagramToLoadName();
	}

	@Override
	public String getProjectName(LevelContext levelContext) {
		return levelContext.getSeries().getName().replace(" ", "") + "/" + levelContext.getLevel().getDiagramName();
	}

	@Override
	public boolean isResponseModelInternal() {
		return false;
	}

}
