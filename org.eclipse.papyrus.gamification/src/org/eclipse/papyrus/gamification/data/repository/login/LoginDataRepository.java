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

package org.eclipse.papyrus.gamification.data.repository.login;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author maximesavaryleblanc
 *
 */
public class LoginDataRepository {

	private LoginRemoteDataSource loginRemoteDataSource;

	public LoginDataRepository(LoginRemoteDataSource loginRemoteDataSource) {
		this.loginRemoteDataSource = loginRemoteDataSource;
	}

	public Maybe<String> getPlayer(String username, String password) {
		return loginRemoteDataSource.getPlayer(username, password);
	}


	public Single<String> createAccount(String username, String password, String sponsor) {
		return loginRemoteDataSource.createAccount(username, password, sponsor);

	}

}
