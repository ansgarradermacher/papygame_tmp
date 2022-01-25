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

package org.eclipse.papyrus.gamification.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lepallec
 *
 */
public class SeriesPerformed extends Series {

	List<LevelPerformed> levelsPerformed;

	/**
	 * Constructor.
	 *
	 */
	public SeriesPerformed(Series series) {
		super(series.getName(), series.getSeriesGameId());
		super.levels.addAll(series.getLevels());
		this.levelsPerformed = new ArrayList<>();
	}

	public int getGoldCoinsTotal() {
		int counter = 0;
		for (LevelPerformed levelPerformed : levelsPerformed) {
			counter += levelPerformed.getEarnedGoldCoins();
		}
		return counter;
	}

	public int getPointsTotal() {
		int counter = 0;
		for (LevelPerformed levelPerformed : levelsPerformed) {
			counter += levelPerformed.getEarnedPoints();
		}
		return counter;
	}

	public void setLevelsPerformed(List<LevelPerformed> levelsPerformed) {
		this.levelsPerformed = levelsPerformed;
	}

	/**
	 * @return the levelsPerformed
	 */
	public List<LevelPerformed> getLevelsPerformed() {
		return levelsPerformed;
	}




}
