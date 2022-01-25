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

package org.eclipse.papyrus.gamification.view.login;

/**
 * @author maximesavaryleblanc
 *
 */
public interface LoginContract {

	interface Presenter {
		void getPlayer(String login, String password);

		void createAccount(String login, String password, String sponsor);

		void registerView(LoginContract.View view);
	}

	interface View {
		void moveToDashboard(String playerId);

		void showError(Throwable throwable);

		void proposeAccountCreation();

	}
}
