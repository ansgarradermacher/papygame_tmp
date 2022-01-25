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

import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;

import com.google.gson.Gson;

/**
 * @author maximesavaryleblanc
 *
 */
public class GameFinishedView extends GameView implements OnResumeToDashboardItf {

	protected LevelContext levelContext;
	protected OnResumeToDashboardItf onResumeToDashboardItf;
	protected GameScore gameScore;

	public GameFinishedView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore) {
		super();
		this.levelContext = levelContext;
		this.onResumeToDashboardItf = onResumeToDashboardItf;
		this.gameScore = gameScore;
	}

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		super.registerJavaScriptFunctions(browser);
		new JSResumeToDashboard(browser, onResumeToDashboardItf);
	}

	@Override
	public String getHtmlPath() {
		// TODO provide a default page
		return "";
	}

	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);
		callJSScript("displayResults(" + (new Gson()).toJson(gameScore) + ")");
	}

	@Override
	public void onResumeToDashboard() {
		onResumeToDashboardItf.onResumeToDashboard();
	}

}
