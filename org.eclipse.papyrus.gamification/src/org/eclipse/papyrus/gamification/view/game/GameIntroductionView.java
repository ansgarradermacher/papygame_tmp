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

import org.eclipse.papyrus.gamification.games.framework.communication.OnCancelGameItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnPlayerReadyItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.swt.widgets.Display;

/**
 * @author maximesavaryleblanc
 *
 */
public class GameIntroductionView extends GameView implements OnPlayerReadyItf, OnCancelGameItf {

	LevelContext levelContext;
	OnPlayerReadyItf onPlayerReadyItf;
	OnCancelGameItf onCancelGameItf;

	public GameIntroductionView(LevelContext levelContext, OnPlayerReadyItf onPlayerReadyItf, OnCancelGameItf onCancelGameItf) {
		super();
		this.levelContext = levelContext;
		this.onPlayerReadyItf = onPlayerReadyItf;
		this.onCancelGameItf = onCancelGameItf;
	}

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSPlayerReady(browser, this);
		new JSCancelGame(browser, onCancelGameItf);
	}

	@Override
	public void clearJavascriptFunctions(Browser browser) {
		// TODO Auto-generated method stub
		super.clearJavascriptFunctions(browser);
	}

	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);
		initiateVideo(levelContext.getLevel().getVideoToShowUrl());
		callJSScript("setLevelLabel('" + levelContext.getLevel().getLabel().replace("'", "\'") + "')");
		callJSScript("setSeriesLabel('" + levelContext.getSeries().getName().replace("'", "\'") + "')");
		callJSScript("setLevelStatement(`" + levelContext.getLevel().getStatement().replace("'", "\'") + "`)");
	}


	@Override
	public void onVideoFinished() {
		callJSScript("activeReadyButton()");
	}



	@Override
	public void onCancelGame() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				onCancelGameItf.onCancelGame();
			}
		});
	}

	@Override
	public void onPlayerReady() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				onPlayerReadyItf.onPlayerReady();
			}
		});
	}

}
