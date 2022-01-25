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

package org.eclipse.papyrus.gamification.view.dashboard;

import org.eclipse.papyrus.gamification.data.entity.PlayerProfile;

/**
 * @author maximesavaryleblanc
 *
 */
public interface DashboardContract {

	interface Presenter {
		void getPlayerProfile(String login);

		void registerView(DashboardContract.View view);

		void onShutdown();

		void getVideoIntroductionState(String playerId);
	}

	interface View {
		void displayPlayerProfile(PlayerProfile player);

		void showVideo();

		void skipVideo();

	}
}
