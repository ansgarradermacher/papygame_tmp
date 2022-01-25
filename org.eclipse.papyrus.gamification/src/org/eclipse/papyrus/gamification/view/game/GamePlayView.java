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

package org.eclipse.papyrus.gamification.view.game;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.data.entity.UmlDiagramSolution;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnModelChangedItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnSecondsReadyItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.modelutils.ModelChangesListenerAdapter;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.common.swt.JSOnSecondsReady;
import org.eclipse.uml2.uml.Model;

/**
 * @author maximesavaryleblanc
 *
 */
public abstract class GamePlayView extends GameView implements OnModelChangedItf, OnSecondsReadyItf {

	private OnGameEndedItf onGameEndedItf;
	protected LevelContext levelContext;
	protected PapyrusContext papyrusContext;
	protected Browser browser;

	public GamePlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf) {
		this.onGameEndedItf = onGameEndedItf;
		this.papyrusContext = papyrusContext;
		this.levelContext = levelContext;
		System.out.println("PapyContext is " + papyrusContext);
		System.out.println("LevelContext is " + levelContext);
	}


	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		System.out.println("Register JS functions is " + browser);

		super.registerJavaScriptFunctions(browser);
		new JSOnSecondsReady(browser, this);
		this.browser = browser;
	}


	@Override
	public void start() {
		super.start();
		listenActionsOnModel();
	}




	protected void endGame(GameMetrics gameMetrics) {
		callJSScript("waitForResults()");
		onGameEndedItf.onGameEnded(gameMetrics);
	}

	protected void endGame(UmlDiagramSolution umlDiagramSolution) {
		callJSScript("waitForResults()");
		onGameEndedItf.onGameEnded(umlDiagramSolution);
	}


	private void listenActionsOnModel() {
		Model modelToListen = null;
		if (this.papyrusContext == null) {
			// case of the FLOW game type - no diagram to compare/evaluate
			return;
		} else {

			EObject firstValue = papyrusContext
					.getPlayerDiagram().allNodes().iterator().next().getSemanticElement();
			while (firstValue != null) {
				if (firstValue instanceof Model) {
					modelToListen = Model.class.cast(firstValue);
				}
				firstValue = firstValue.eContainer();
			}

			modelToListen.eAdapters().add(new ModelChangesListenerAdapter(this));
		}
	}


	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);
		callJSScript("setLevelStatement(`" + levelContext.getLevel().getStatement().replace("'", "\'") + "`)");
	}


	@Override
	public void onModelChanged(Notification notification) {
		// Do nothing
	}

	protected void getSpentTime() {
		callJSScript("getSecondsSpent();");
	}

	public abstract void proceedWithTime(Double seconds);

	/**
	 * @see org.eclipse.papyrus.gamification.games.framework.communication.OnSecondsReadyItf#onSecondsReady(int)
	 *
	 * @param seconds
	 */
	@Override
	public void onSecondsReady(Double seconds) {
		// TODO Auto-generated method stub
		proceedWithTime(seconds);
	}
}
