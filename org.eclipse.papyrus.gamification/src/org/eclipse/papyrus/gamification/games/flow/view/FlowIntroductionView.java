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

import org.eclipse.papyrus.gamification.games.framework.communication.OnCancelGameItf;
import org.eclipse.papyrus.gamification.games.framework.communication.OnPlayerReadyItf;
import org.eclipse.papyrus.gamification.games.framework.entity.LevelContext;
import org.eclipse.papyrus.gamification.view.game.GameIntroductionView;

/**
 * @author maximesavaryleblanc
 *
 */
public class FlowIntroductionView extends GameIntroductionView {

	/**
	 * Constructor.
	 *
	 * @param levelContext
	 * @param onPlayerReadyItf
	 */
	public FlowIntroductionView(LevelContext levelContext, OnPlayerReadyItf onPlayerReadyItf, OnCancelGameItf onCancelGameItf) {
		super(levelContext, onPlayerReadyItf, onCancelGameItf);
	}

	@Override
	public String getHtmlPath() {
		return "/html/games/flow/html/intro.html";
	}

}
