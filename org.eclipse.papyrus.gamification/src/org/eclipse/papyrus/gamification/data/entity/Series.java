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
public class Series {
	List<Level> levels;
	String name;
	String seriesGameId;

	/**
	 * Constructor.
	 *
	 */
	public Series(String name, String seriesGameId) {
		this.name = name;
		this.levels = new ArrayList<>();
		this.seriesGameId = seriesGameId;
	}

	/**
	 * @return the levels
	 */
	public List<Level> getLevels() {
		return levels;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getSeriesGameId() {
		return seriesGameId;
	}

}
