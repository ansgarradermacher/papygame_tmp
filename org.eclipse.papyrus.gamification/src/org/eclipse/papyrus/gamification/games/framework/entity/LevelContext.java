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

import org.eclipse.papyrus.gamification.data.entity.Level;
import org.eclipse.papyrus.gamification.data.entity.PlayerProfile;
import org.eclipse.papyrus.gamification.data.entity.Series;

/**
 * @author maximesavaryleblanc
 *
 */
public class LevelContext {
	private Level level;
	private PlayerProfile playerProfile;
	private Series series;

	public LevelContext(Level level, PlayerProfile playerProfile, Series series) {
		super();
		this.level = level;
		this.playerProfile = playerProfile;
		this.series = series;
	}

	public Level getLevel() {
		return level;
	}

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public Series getSeries() {
		return series;
	}

	@Override
	public String toString() {
		return "GameContext [level=" + level + ", playerProfile=" + playerProfile + ", series=" + series + "]";
	}


}
