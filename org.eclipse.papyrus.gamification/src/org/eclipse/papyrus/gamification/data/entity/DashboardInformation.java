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

import java.util.List;

/**
 * @author maximesavaryleblanc
 *
 */
public class DashboardInformation {

	private PlayerProfile playerProfile;
	private List<Series> availableSeries;

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public void setPlayerProfile(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}

	public List<Series> getAvailableSeries() {
		return availableSeries;
	}

	public void setAvailableSeries(List<Series> availableSeries) {
		this.availableSeries = availableSeries;
	}


}
