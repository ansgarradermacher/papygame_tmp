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

package org.eclipse.papyrus.gamification.games.flow.view;

import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.data.jsonmapper.UMLToJSONMapper;
import org.eclipse.papyrus.gamification.data.jsonmapper.UmlClassDiagram;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition;
import org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition.TestMyPropositionInterface;
import org.eclipse.papyrus.gamification.modelutils.ModelDisplayManager;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClass;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusClassDiagram;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.game.GamePlayView;
import org.eclipse.papyrus.uml.diagram.common.editparts.ClassEditPart;
import org.eclipse.uml2.uml.Model;

/**
 * @author zaza Le Pallec
 *
 */
public class FlowPlayView extends GamePlayView implements TestMyPropositionInterface {

	private int propositionCounter = 0;
	UmlClassDiagram userUmlClassDiagram;

	/**
	 * Constructor.
	 *
	 * @param levelContext
	 * @param papyrusContext
	 * @param onGameEndedItf
	 */
	public FlowPlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf) {
		super(levelContext, papyrusContext, onGameEndedItf);
	}


	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSTestMyProposition(browser, this);
	}

	@Override
	public String getHtmlPath() {
		return "/html/games/flow/html/game.html";
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
		System.out.println("page loaded on FLOW  view");
	}

	/**
	 * @see org.eclipse.papyrus.gamification.games.oyo.JSTestMyProposition.TestMyPropositionInterface#userAsksToTest(int)
	 *
	 * @param numberOfPropositionsSubmitted
	 */
	@Override
	public void userAsksToTest() {

		ModelDisplayManager.saveEditor();
		PapyrusClassDiagram playerClassDiagram = papyrusContext.getPlayerDiagram();

		// PapyrusClassDiagram originalClassDiagram = papyrusContext.getOriginalDiagram();

		// Should have one class to send
		if (playerClassDiagram.getClasses().length > 0) {
			PapyrusClass pc = playerClassDiagram.getClasses()[0];
			Model m = ((ClassEditPart) pc.getEditPart()).getUMLElement().getModel();
			userUmlClassDiagram = UMLToJSONMapper.map(m);
			getSpentTime();
		}

	}


	/**
	 * @see org.eclipse.papyrus.gamification.view.game.GamePlayView#proceedWithTime(int)
	 *
	 * @param seconds
	 */
	@Override
	public void proceedWithTime(Double seconds) {
		UmlDiagramSolution umlDiagramSolution = new UmlDiagramSolution();

		umlDiagramSolution.setUmlClassDiagram(userUmlClassDiagram);
		umlDiagramSolution.setTimeSpent(seconds.intValue());

		System.out.println("Almost good");
		System.out.println("Retrieving time : " + seconds);

		endGame(umlDiagramSolution);
	}



}
