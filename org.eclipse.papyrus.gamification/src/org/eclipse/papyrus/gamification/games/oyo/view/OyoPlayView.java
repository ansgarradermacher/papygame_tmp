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

package org.eclipse.papyrus.gamification.games.oyo.view;

import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.games.oyo.DiagramComparator;
import org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition;
import org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition.TestMyPropositionInterface;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.game.GamePlayView;

/**
 * @author zaza Le Pallec
 *
 */
public class OyoPlayView extends GamePlayView implements TestMyPropositionInterface {

	private int propositionCounter = 0;

	/**
	 * Constructor.
	 *
	 * @param levelContext
	 * @param papyrusContext
	 * @param onGameEndedItf
	 */
	public OyoPlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf) {
		super(levelContext, papyrusContext, onGameEndedItf);
	}


	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSTestMyProposition(browser, this);
	}

	@Override
	public String getHtmlPath() {
		return "/html/games/oyo/html/game.html";
	}

	public void onGameEnded(boolean success, int goodMoves, int badMoves) {

		/*
		 * ActionRequestContent actionRequestContent = new ActionRequestContent("taskCompleted");
		 * actionRequestContent.setGameId(gameContext.getSeries().getSeriesGameId());
		 * actionRequestContent.setPlayerId(gameContext.getPlayerProfile().getPlayerId());
		 * ActionRequestCustomData arcd = new ActionRequestCustomData(goodMoves + badMoves, badMoves, gameContext.getLevel().getLabel());
		 * actionRequestContent.setData(arcd);
		 *
		 * levelPresenter.submitGame(actionRequestContent);
		 */

	}

	/**
	 * @see org.eclipse.papyrus.gamification.view.common.DisplayableView#onHtmlPageLoaded(org.eclipse.swt.browser.Browser)
	 *
	 * @param browser
	 */
	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);
		System.out.println("page loaded on oyo view");
	}

	/**
	 * @see org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition.TestMyPropositionInterface#userAsksToTest(int)
	 *
	 * @param numberOfPropositionsSubmitted
	 */
	@Override
	public void userAsksToTest() {

		PapyrusClassDiagram originalClassDiagram = papyrusContext.getOriginalDiagram();
		PapyrusClassDiagram playerClassDiagram = papyrusContext.getPlayerDiagram();

		propositionCounter++;

		DiagramComparator comparator = new DiagramComparator(originalClassDiagram, playerClassDiagram);
		if (!comparator.compareDiagram()) {
			// Time is retrieved through a callback, when we have it we continue the process
			getSpentTime();

		} else {
			callJSScript("handleBadProposition('Proposition #" + propositionCounter + "')");
		}

	}


	/**
	 * @see org.eclipse.papyrus.gamification.view.game.GamePlayView#proceedWithTime(int)
	 *
	 * @param seconds
	 */
	@Override
	public void proceedWithTime(Double seconds) {
		// TODO Auto-generated method stub
		GameMetrics gameMetrics = new GameMetrics();
		gameMetrics.setGameSuccess(true);
		gameMetrics.setErrors(propositionCounter - 1);
		gameMetrics.setOkMoves(propositionCounter);
		gameMetrics.setTimeSpent(seconds.intValue());
		System.out.println("Almost good");
		System.out.println("Retrieving time : " + seconds);

		endGame(gameMetrics);
	}



}
