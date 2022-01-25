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

import org.eclipse.papyrus.gamification.data.entity.GameScore;
import org.eclipse.papyrus.gamification.games.framework.communication.OnResumeToDashboardItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.view.common.swt.Browser;
import org.eclipse.papyrus.gamification.view.game.GameFinishedView;

/**
 * @author maximesavaryleblanc
 *
 */
public class OyoGameSuccessView extends GameFinishedView {



	public OyoGameSuccessView(LevelContext levelContext, OnResumeToDashboardItf onResumeToDashboardItf, GameScore gameScore) {
		super(levelContext, onResumeToDashboardItf, gameScore);
	}

	@Override
	public void registerJavaScriptFunctions(Browser browser) {
		// TODO Auto-generated method stub
		super.registerJavaScriptFunctions(browser);
	}

	@Override
	public String getHtmlPath() {
		// TODO Auto-generated method stub
		return "/html/gamesuccess.html";
	}

	@Override
	public void onHtmlPageLoaded(Browser browser) {
		super.onHtmlPageLoaded(browser);

		// TODO set content
	}

}
