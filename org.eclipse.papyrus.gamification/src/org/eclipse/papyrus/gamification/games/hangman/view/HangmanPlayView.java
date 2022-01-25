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

package org.eclipse.papyrus.gamification.games.hangman.view;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.gamification.data.entity.GameMetrics;
import org.eclipse.papyrus.gamification.games.framework.communication.LevelPresenter;
import org.eclipse.papyrus.gamification.games.framework.communication.OnGameEndedItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.games.framework.entity.PapyrusContext;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusAttribute;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusNode;
import org.eclipse.papyrus.gamification.modelutils.papyrus.PapyrusOperation;
import org.eclipse.papyrus.gamification.modelutils.papyrus.copy.Mask;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.game.GamePlayView;
import org.eclipse.uml2.uml.NamedElement;

/**
 * @author zaza Le Pallec
 *
 */
public class HangmanPlayView extends GamePlayView {

	private LevelPresenter levelPresenter;
	private int goodMoveCounter = 0;
	private int badMoveCounter = 0;
	private int fakeElementsNumber = 0;

	private boolean gameSuccess;

	public HangmanPlayView(LevelContext levelContext, PapyrusContext papyrusContext, OnGameEndedItf onGameEndedItf) {
		super(levelContext, papyrusContext, onGameEndedItf);
		this.levelPresenter = new LevelPresenter();
	}

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		// TODO Auto-generated method stub
		super.registerJavaScriptFunctions(browser);
	}


	@Override
	public String getHtmlPath() {
		return "/html/games/hangman/html/game.html";
	}



	@Override
	public void onModelChanged(Notification notification) {
		if (notification.getEventType() == Notification.ADD
				|| notification.getEventType() == Notification.ADD_MANY) {
			if (notification.getNewValue() instanceof EObject) {
				EObject newObject = EObject.class.cast(notification.getNewValue());
				PapyrusNode addedPapyrusNode = papyrusContext.getPlayerDiagram().getCorrespondingPapyrusNode(newObject);
				if (addedPapyrusNode instanceof PapyrusAttribute) {
					PapyrusAttribute playerAttribute = PapyrusAttribute.class.cast(addedPapyrusNode);
					PapyrusAttribute originalAttribute = PapyrusAttribute.class.cast(papyrusContext.getDiagramClassCopier().getCorrespondingOriginalElement(playerAttribute));

					String playerContainerName = NamedElement.class.cast(playerAttribute.getSemanticElement().eContainer()).getName();
					String originalContainerName = NamedElement.class.cast(originalAttribute.getSemanticElement().eContainer()).getName();

					playerContainerName = new Mask(playerContainerName).getFinalName();
					originalContainerName = new Mask(originalContainerName).getFinalName();

					if (!playerContainerName.equals(originalContainerName)) {
						handleBadMove(playerAttribute.getName());
						// this.papyrusContext.getTransactionalEditingDomain().getCommandStack().undo();
					} else {
						handleGoodMove(playerAttribute.getName());
					}
				} else if (addedPapyrusNode instanceof PapyrusOperation) {
					PapyrusOperation playerOperation = PapyrusOperation.class.cast(addedPapyrusNode);
					PapyrusOperation originalOperation = PapyrusOperation.class.cast(papyrusContext.getDiagramClassCopier().getCorrespondingOriginalElement(playerOperation));

					String playerContainerName = NamedElement.class.cast(playerOperation.getSemanticElement().eContainer()).getName();
					String originalContainerName = NamedElement.class.cast(originalOperation.getSemanticElement().eContainer()).getName();

					playerContainerName = new Mask(playerContainerName).getFinalName();
					originalContainerName = new Mask(originalContainerName).getFinalName();

					if (!playerContainerName.equals(originalContainerName)) {
						handleBadMove(playerOperation.getName());
						// this.papyrusContext.getTransactionalEditingDomain().getCommandStack().undo();
					} else {
						handleGoodMove(playerOperation.getName());
					}
				}


			}
		}
	}

	private void handleGoodMove(String elementName) {
		System.out.println("GOOD");
		goodMoveCounter++;

		if (goodMoveCounter < fakeElementsNumber) {
			// if (goodMoveCounter < 2) {
			callJSScript("handleGoodMove(" + goodMoveCounter + ",\"" + elementName + "\")");

		} else {
			retrieveGameMetrics(true);
		}
	}

	private void handleBadMove(String elementName) {
		System.out.println("BAD");
		badMoveCounter++;

		// In our version of the hangman, we only draw 1-head, 2-body, 3-arms and 4-legs
		// When legs are displayed, game is over.

		if (badMoveCounter < 4) {
			callJSScript("handleBadMove(" + badMoveCounter + ",\"" + elementName + "\")");
			if (badMoveCounter == 1) {
				callJSScript("drawHead()");
			}
			if (badMoveCounter == 2) {
				callJSScript("drawBody()");
			}
			if (badMoveCounter == 3) {
				callJSScript("drawArms()");
			}
			if (badMoveCounter == 4) {
				callJSScript("drawLegs()");
			}
		} else {
			retrieveGameMetrics(false);
		}
	}

	private void retrieveGameMetrics(boolean gameSuccess) {
		this.gameSuccess = gameSuccess;
		getSpentTime();
	}

	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);
	}

	public void setFakeElementsNumber(int fakeAttributeNumber) {
		this.fakeElementsNumber = fakeAttributeNumber;
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
		gameMetrics.setOkMoves(goodMoveCounter);
		gameMetrics.setErrors(badMoveCounter);
		gameMetrics.setGameSuccess(gameSuccess);
		gameMetrics.setTimeSpent(seconds.intValue());
		endGame(gameMetrics);
	}


}
